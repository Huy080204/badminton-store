package com.mgr.api.controller;

import com.mgr.api.constant.MgrConstant;
import com.mgr.api.dto.ApiResponse;
import com.mgr.api.dto.ErrorCode;
import com.mgr.api.dto.ResponseListDto;
import com.mgr.api.dto.user.UserDto;
import com.mgr.api.exception.BadRequestException;
import com.mgr.api.exception.NotFoundException;
import com.mgr.api.form.user.CreateUserForm;

import com.mgr.api.form.user.UpdateUserForm;
import com.mgr.api.mapper.UserMapper;
import com.mgr.api.model.Account;
import com.mgr.api.model.User;

import com.mgr.api.model.criteria.UserCriteria;
import com.mgr.api.repository.AccountRepository;

import com.mgr.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/user")
public class UserController extends ABasicController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiResponse<String> create(@Valid @RequestBody CreateUserForm createUserForm, BindingResult bindingResult) {
        ApiResponse<String> apiMessageDto = new ApiResponse<>();

        // Kiểm tra username tồn tại
        if (accountRepository.findFirstByUsername(createUserForm.getUsername()).isPresent()) {
            throw new BadRequestException("Username is existed!", ErrorCode.ACCOUNT_ERROR_USERNAME_EXISTED);
        }

        //Check both email and phone
        // 2. Kiểm tra Email (Dùng Boolean existsByEmail)
        if (createUserForm.getEmail() != null && accountRepository.existsByEmail(createUserForm.getEmail())) {
            throw new BadRequestException("Email is existed!", ErrorCode.ACCOUNT_ERROR_EMAIL_EXISTED);
        }

        if (createUserForm.getPhone() != null && accountRepository.existsByPhone(createUserForm.getPhone())) {
            throw new BadRequestException("Phone is existed!", ErrorCode.ACCOUNT_ERROR_PHONE_EXISTED);
        }

        // 1. Tạo Account (kind = 2 cho người dùng)
        Account account = new Account();
        account.setUsername(createUserForm.getUsername());
        account.setPassword(passwordEncoder.encode(createUserForm.getPassword()));
        account.setFullName(createUserForm.getFullName());
        account.setEmail(createUserForm.getEmail());
        account.setKind(2); // Thiết lập kind = 2
        account.setStatus(MgrConstant.STATUS_ACTIVE);
        accountRepository.save(account);

        // 2. Tạo User (Lấy ID từ Account nhờ @MapsId)
        User user = new User();
        user.setAccount(account);
        user.setGender(createUserForm.getGender());
        user.setDateOfBirth(createUserForm.getDateOfBirth());
        user.setStatus(MgrConstant.STATUS_ACTIVE);
        userRepository.save(user);

        apiMessageDto.setMessage("Register user success.");
        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER_U')")
    @Transactional
    public ApiResponse<String> update(@Valid @RequestBody UpdateUserForm updateUserForm, BindingResult bindingResult) {
        ApiResponse<String> apiMessageDto = new ApiResponse<>();
        User user = userRepository.findById(updateUserForm.getId())
                .orElseThrow(() -> new NotFoundException("User not found!", ErrorCode.USER_ERROR_NOT_FOUND));

        // Cập nhật thông tin Account đi kèm
        Account account = user.getAccount();
        account.setFullName(updateUserForm.getFullName());
        if (updateUserForm.getPassword() != null) {
            account.setPassword(passwordEncoder.encode(updateUserForm.getPassword()));
        }

        // Cập nhật thông tin riêng của User
        user.setGender(updateUserForm.getGender());
        user.setDateOfBirth(updateUserForm.getDateOfBirth());

        userRepository.save(user);
        apiMessageDto.setMessage("Update user success.");
        return apiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER_V')")
    public ApiResponse<UserDto> get(@PathVariable("id") Long id) {
        ApiResponse<UserDto> apiMessageDto = new ApiResponse<>();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found!", ErrorCode.USER_ERROR_NOT_FOUND));

        apiMessageDto.setData(userMapper.fromEntityToDto(user));
        apiMessageDto.setMessage("Get user success.");
        return apiMessageDto;
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER_L')")
    public ApiResponse<ResponseListDto<List<UserDto>>> list(UserCriteria userCriteria, Pageable pageable) {
        // Sửa kiểu Generic của ApiResponse để chứa một danh sách trong ResponseListDto
        ApiResponse<ResponseListDto<List<UserDto>>> apiMessageDto = new ApiResponse<>();

        // Lấy dữ liệu phân trang từ repository
        Page<User> page = userRepository.findAll(userCriteria.getSpecification(), pageable);

        // userMapper.fromEntityListToDtoList trả về List<UserDto>
        // Do đó T trong ResponseListDto phải là List<UserDto>
        ResponseListDto<List<UserDto>> responseListDto = new ResponseListDto<>(
                userMapper.fromEntityListToDtoList(page.getContent()),
                page.getTotalElements(),
                page.getTotalPages()
        );

        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("List user success.");
        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER_D')")
    @Transactional
    public ApiResponse<Void> delete(@PathVariable("id") Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found!", ErrorCode.USER_ERROR_NOT_FOUND));

        // Xóa cả Account và User (ID trùng nhau)
        userRepository.delete(user);
        accountRepository.deleteById(id);

        ApiResponse<Void> response = new ApiResponse<>();
        response.setMessage("Delete user success");
        return response;
    }
}