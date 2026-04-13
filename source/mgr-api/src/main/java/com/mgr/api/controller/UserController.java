package com.mgr.api.controller;

import com.mgr.api.dto.ApiMessageDto;
import com.mgr.api.dto.ErrorCode;
import com.mgr.api.dto.user.UserDto;
import com.mgr.api.exception.BadRequestException;
import com.mgr.api.exception.NotFoundException;
import com.mgr.api.form.user.CreateUserForm;
import com.mgr.api.form.user.UpdateUserForm;
import com.mgr.api.mapper.UserMapper;
import com.mgr.api.model.Group;
import com.mgr.api.model.User;
import com.mgr.api.repository.GroupRepository;
import com.mgr.api.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class UserController extends ABasicController{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserMapper userMapper;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('U_C')")
    public ApiMessageDto<String> create(@Valid @RequestBody CreateUserForm createUserForm, BindingResult bindingResult) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        User existingUser = userRepository.findExistingUser(
                createUserForm.getUsername(),
                createUserForm.getEmail(),
                createUserForm.getPhone()
        );
        if (existingUser != null) {
            if (StringUtils.equals(existingUser.getAccount().getUsername(), createUserForm.getUsername())) {
                throw new BadRequestException("Username already exists!", ErrorCode.USER_ERROR_USERNAME_EXISTED);
            }
            if (StringUtils.equals(existingUser.getAccount().getEmail(), createUserForm.getEmail())) {
                throw new BadRequestException("Email already exists!", ErrorCode.USER_ERROR_EMAIL_EXISTED);
            }
            if (StringUtils.equals(existingUser.getAccount().getPhone(), createUserForm.getPhone())) {
                throw new BadRequestException("Phone already exists!", ErrorCode.USER_ERROR_PHONE_EXISTED);
            }
        }
        //Set default usser kind and user group
        User user = userMapper.fromCreateUserFormToEntity(createUserForm);
//        user.getAccount().setGroup(new Group("User", "Group of User",2,false,null));
//        user.getAccount().setKind(2);
        userRepository.save(user);

        apiMessageDto.setMessage("Create user success.");
        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('U_U')")
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateUserForm updateUserForm, BindingResult bindingResult) {
        if(!isSuperAdmin() && !updateUserForm.getId().equals(getCurrentUser())){
            throw new BadRequestException("You don't have permission to update user", ErrorCode.USER_ERROR_PERMISSION);
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        User user = userRepository.findById(updateUserForm.getId()).orElse(null);
        if (user == null) {
            throw new NotFoundException("User not found!", ErrorCode.USER_ERROR_NOT_FOUND);
        }
        user.getAccount().setFullName(updateUserForm.getFullName());
        user.getAccount().setPhone(updateUserForm.getPhone());
        user.setGender(updateUserForm.getGender());
        if (StringUtils.isNoneBlank(updateUserForm.getAvatarPath())) {
            user.getAccount().setAvatarPath(updateUserForm.getAvatarPath());
        }
        userRepository.save(user);
        apiMessageDto.setMessage("Update user success.");
        return apiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('U_V')")
    public ApiMessageDto<UserDto> get(@PathVariable("id") Long id) {
        ApiMessageDto<UserDto> apiMessageDto = new ApiMessageDto<>();
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new NotFoundException("User not found!", ErrorCode.USER_ERROR_NOT_FOUND);
        }
        apiMessageDto.setData(userMapper.fromUserEntityToDto(user));
        apiMessageDto.setMessage("Get user success.");
        return apiMessageDto;
    }

    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<UserDto> profile() {
        ApiMessageDto<UserDto> apiMessageDto = new ApiMessageDto<>();
        User user = userRepository.findById(getCurrentUser()).orElse(null);
        if (user == null) {
            throw new NotFoundException("User not found!", ErrorCode.USER_ERROR_NOT_FOUND);
        }
        apiMessageDto.setData(userMapper.fromUserEntityToDto(user));
        apiMessageDto.setMessage("Get user success.");
        return apiMessageDto;
    }
}
