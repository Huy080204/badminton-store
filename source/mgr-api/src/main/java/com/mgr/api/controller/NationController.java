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

import com.mgr.api.constant.MgrConstant;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;

import static com.mgr.api.constant.MgrConstant.NATION_KIND_PROVINCE;

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
        //When the form is passed, the kind is 1 (province), meaning it's already in the highest nation, so there will be no parent ID of the parent nation.
        if(form.getKind() == 1){
            if(form.getParentId() != null){
                throw new BadRequestException("Province is highest level, it haven't parent");
            }
        }
        else {
            if(form.getParentId() == null){
                throw new BadRequestException("This kind must have father parent");
            }
            parent = nationRepository.findById(form.getParentId())
                    .orElseThrow(()-> new NotFoundException("ParentId Not Found"));
            // The child's rank must be equal to the father's rank + 1.
            if(form.getKind() != parent.getKind() +1 ){
                throw new BadRequestException("Kind not match selected father parent");
            }
        }

        // 2. Check for duplicate names within the same area; for example, a province cannot have two districts with the same name.
        boolean isDuplicate = nationRepository.existsByNameAndParent(form.getName(), parent);
        if (isDuplicate) {
            throw new BadRequestException("Parent Name have existed");
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
                .orElseThrow(()-> new NotFoundException("Resource Not Found"));
        //The parent shouldn't be themselves - a contradiction.
        if( form.getParentId()!=null && form.getParentId().equals(id)){
            throw new BadRequestException("Do not bury itself as the parent unit");
        }

        //Same logic create
        Nation parent = null;
        String trimmedName = form.getName().trim();
        if (nation.getKind().equals(NATION_KIND_PROVINCE)) {
            // If it is currently a Province, it is not permitted to transfer ownership to someone else's child.
            if (form.getParentId() != null) {
                throw new BadRequestException("A first-level unit (province) cannot have a parent unit.");
            }
        } else {
            // If it's a District/Commune, a new parentId is required.
            if (form.getParentId() == null) {
                throw new BadRequestException("The current rank requires a parent unit.");
            }

            parent = nationRepository.findById(form.getParentId())
                    .orElseThrow(() -> new NotFoundException("No new parent unit found."));

            if (nation.getKind() != parent.getKind() + 1) {
                throw new BadRequestException("The new father's unit is not compatible with the current rank.");
            }
        }

        //Check for duplicate names within the same area (excluding current IDs).
        if (!trimmedName.equalsIgnoreCase(nation.getName()) || (parent != null && !parent.equals(nation.getParent())) || (parent == null && nation.getParent() != null)) {
            if (nationRepository.existsByNameAndParentAndIdNot(trimmedName, parent, id)) {
                throw new BadRequestException("This unit's name already exists in the selected area.");
            }
        }
        //Since I only need to set the name, I don't need to use a mapper.
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
    @Transactional
    public ApiMessageDto<Void> delete(@PathVariable("id") Long id) {
        Nation nation = nationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nation not found"));
        hardDeleteRecursive(nation);
        return makeSuccessResponse("Delete Success");
    }

    private void hardDeleteRecursive(Nation nation){
        List<Nation> children = nationRepository.findByParent(nation);
        if(children != null && !children.isEmpty()){
            for(Nation child : children){
                hardDeleteRecursive(child);
            }
        }
        nationRepository.hardDeleteById(nation.getId());
    }
}
