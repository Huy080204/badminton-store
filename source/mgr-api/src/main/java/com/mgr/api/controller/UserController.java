package com.mgr.api.controller;

import com.mgr.api.constant.MgrConstant;
import com.mgr.api.dto.ApiMessageDto;
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
import com.mgr.api.model.Group;
import com.mgr.api.model.User;

import com.mgr.api.model.criteria.UserCriteria;
import com.mgr.api.repository.AccountRepository;

import com.mgr.api.repository.GroupRepository;
import com.mgr.api.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.mgr.api.constant.MgrConstant.USER_KIND_USER;

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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USR_C')")
    public ApiMessageDto<Void> create(@Valid @RequestBody CreateUserForm createUserForm, BindingResult bindingResult) {
        if(!isSuperAdmin()){
            throw new BadRequestException("You don't have permission to create user", ErrorCode.USER_ERROR_PERMISSION_CREATE);
        }
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
        //Set default user kind and user group
        User user = userMapper.fromCreateUserFormToEntity(createUserForm);
        String rawPassword = createUserForm.getPassword();
        if (rawPassword != null && !rawPassword.isEmpty()) {
            String encodedPassword = passwordEncoder.encode(rawPassword);
            user.getAccount().setPassword(encodedPassword);
        }
        user.getAccount().setKind(USER_KIND_USER);
        Group group = groupRepository.findById(createUserForm.getGroupId())
                .orElseThrow(()-> new NotFoundException("Group Not Found", ErrorCode.GROUP_ERROR_NOT_FOUND));
        if(!group.getKind().equals(USER_KIND_USER)){
            throw new BadRequestException("Invalid group kind for this operation", ErrorCode.GROUP_ERROR_INVALID_KIND);
        }
        user.getAccount().setGroup(group);
        user.getAccount().setKind(USER_KIND_USER);
        userRepository.save(user);
        return makeSuccessResponse("Create success");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USR_U')")
    public ApiMessageDto<Void> update(@Valid @RequestBody UpdateUserForm updateUserForm, BindingResult bindingResult) {
        if(!isSuperAdmin()){
            throw new BadRequestException("You don't have permission to get user", ErrorCode.USER_ERROR_PERMISSION_UPDATE);
        }
        User user = userRepository.findById(updateUserForm.getId())
                .orElseThrow(() -> new NotFoundException("User not found!", ErrorCode.USER_ERROR_NOT_FOUND));
        userMapper.mappingUpdateFormToEntity( updateUserForm, user);
        userRepository.save(user);
        return makeSuccessResponse("Update success");
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USR_V')")
    public ApiMessageDto<UserDto> get(@PathVariable("id") Long id) {
        if(!isSuperAdmin()){
            throw new BadRequestException("You don't have permission to get user", ErrorCode.USER_ERROR_PERMISSION_GET);
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found!", ErrorCode.USER_ERROR_NOT_FOUND));
        return makeSuccessResponse( userMapper.fromUserEntityToDto(user), "Get success");
    }

    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USR_V')")
    public ApiMessageDto<UserDto> profile() {
        User user = userRepository.findById(getCurrentUser())
                .orElseThrow(() -> new NotFoundException("User not found!", ErrorCode.USER_ERROR_NOT_FOUND));
        return makeSuccessResponse( userMapper.fromUserEntityToDto(user), "Get success");
    }

    //update profile
    @PutMapping(value = "/update-profile", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USR_U')")
    public ApiMessageDto<Void> updateProfile(@Valid @RequestBody UpdateUserForm updateUserForm, BindingResult bindingResult) {
        User user = userRepository.findById(getCurrentUser())
                .orElseThrow(() -> new NotFoundException("User not found!", ErrorCode.USER_ERROR_NOT_FOUND));

        userMapper.mappingUpdateFormToEntity( updateUserForm, user);
        userRepository.save(user);
        return makeSuccessResponse("Update success");
    }
}
