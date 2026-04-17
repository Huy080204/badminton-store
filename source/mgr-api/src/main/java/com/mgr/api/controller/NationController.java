package com.mgr.api.controller;

import com.mgr.api.dto.ApiMessageDto;
import com.mgr.api.dto.ApiResponse;
import com.mgr.api.dto.ResponseListDto;
import com.mgr.api.dto.nation.NationDto;
import com.mgr.api.exception.BadRequestException;
import com.mgr.api.exception.NotFoundException;
import com.mgr.api.form.nation.CreateNationForm;
import com.mgr.api.form.nation.UpdateNationForm;
import com.mgr.api.mapper.NationMapper;
import com.mgr.api.model.Nation;
import com.mgr.api.model.criteria.NationCriteria;
import com.mgr.api.repository.NationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/nation")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class NationController extends ABasicController{
    @Autowired
    private NationRepository nationRepository;

    @Autowired
    private NationMapper nationMapper;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('NAT_C')")
    public ApiMessageDto<Void> create(@Valid @RequestBody CreateNationForm form, BindingResult bindingResult){
        Nation parent = null;
        //Khi form truyền lên kind là 1(tỉnh) tức đã ở nation cao nhất vì vậy thì sẽ không có parentId của nation cha.
        if(form.getKind() == 1){
            if(form.getParentId() != null){
                throw new BadRequestException("Tỉnh cấp 1 nên không có Parent ID");
            }
        }
        else {
            if(form.getParentId() == null){
                throw new BadRequestException("Cấp bậc này bắt buộc có thông tin parentId của nation cha");
            }
            parent = nationRepository.findById(form.getParentId())
                    .orElseThrow(()-> new NotFoundException("Không tìm thấy parentId"));
            // Cấp con phải bằng cấp cha +1
            if(form.getKind() != parent.getKind() +1 ){
                throw new BadRequestException("Cấp bậc không logic với đơn vị cha đã chọn");
            }
        }

        // 2. Kiểm tra trùng tên trong cùng một khu vực, vi du 1 tinh se khong co 2 huyen cung ten
        boolean isDuplicate = nationRepository.existsByNameAndParent(form.getName(), parent);
        if (isDuplicate) {
            throw new BadRequestException("Tên đơn vị này đã tồn tại trong khu vực này");
        }

        Nation nation = nationMapper.fromCreateFormToEntity(form);
        nation.setParent(parent);
        nationRepository.save(nation);

        return  makeSuccessResponse("Create Success");
    }

    @PostMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('NAT_U')")
    public ApiMessageDto<Void> update(@PathVariable Long id, @Valid @RequestBody UpdateNationForm form, BindingResult bindingResult){
        Nation nation = nationRepository.findById(form.getId())
                .orElseThrow(()-> new NotFoundException("Khong tim thay ban ghi"));
        //Parent khong duoc la chinh no - chong lap
        if( form.getParentId()!=null && form.getParentId().equals(id)){
            throw new BadRequestException("Khong chon chinh no lam don vi cha");
        }

        //Logic tuong tu create
        Nation parent = null;
        String trimmedName = form.getName().trim();
        if (nation.getKind() == 1) {
            // Nếu hiện tại là Tỉnh, không được phép chuyển thành con của ai khác
            if (form.getParentId() != null) {
                throw new BadRequestException("Đơn vị cấp 1 (Tỉnh) không thể có đơn vị cha");
            }
        } else {
            // Nếu là Huyện/Xã, bắt buộc phải có parentId mới
            if (form.getParentId() == null) {
                throw new BadRequestException("Cấp bậc hiện tại bắt buộc phải có đơn vị cha");
            }

            parent = nationRepository.findById(form.getParentId())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn vị cha mới"));

            if (nation.getKind() != parent.getKind() + 1) {
                throw new BadRequestException("Đơn vị cha mới không phù hợp với cấp bậc hiện tại");
            }
        }

        //Kiem tra trung ten trong cung khu vuc (loai tru ID hien tai)
        if (nationRepository.existsByNameAndParentAndIdNot(trimmedName, parent, id)) {
            throw new BadRequestException("Tên đơn vị này đã tồn tại trong khu vực được chọn");
        }
        //Do chi can setName, nen em khong dung mapper
        nation.setName(trimmedName);
        nation.setParent(parent);
        nationRepository.save(nation);
        return makeSuccessResponse("Update Success");
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('NAT_V')")
    public ApiMessageDto<NationDto> get(@PathVariable("id") Long id) {
        Nation nation = nationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nation not found"));
        return makeSuccessResponse(nationMapper.fromEntityToNationDto(nation), "Get nation success");
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('NAT_L')")
    public ApiResponse<ResponseListDto<NationDto>> listNation(NationCriteria nationCriteria, Pageable pageable) {
        ApiResponse<ResponseListDto<NationDto>> apiResponse = new ApiResponse<>();
        Page<Nation> page = nationRepository.findAll(nationCriteria.getSpecification(), pageable);
        ResponseListDto<NationDto> responseListDto = new ResponseListDto(
                nationMapper.fromEntityToNationDtoList(page.getContent()),
                page.getTotalElements(),
                page.getTotalPages()
        );
        apiResponse.setData(responseListDto);
        apiResponse.setMessage("List nation success.");
        return apiResponse;
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('NAT_D')")
    public ApiMessageDto<Void> delete(@PathVariable("id") Long id) {
        Nation nation = nationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nation not found"));
        if (nationRepository.existsByParent(nation)) {
            throw new BadRequestException("Nation has children, cannot delete");
        }
        nationRepository.delete(nation);
        return makeSuccessResponse("Delete Success");
    }



}
