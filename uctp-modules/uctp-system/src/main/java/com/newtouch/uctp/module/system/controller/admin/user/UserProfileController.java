package com.newtouch.uctp.module.system.controller.admin.user;

import cn.hutool.core.collection.CollUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.newtouch.uctp.framework.common.enums.UserTypeEnum;
import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.datapermission.core.annotation.DataPermission;
import com.newtouch.uctp.module.system.controller.admin.user.vo.profile.UserProfileRespVO;
import com.newtouch.uctp.module.system.controller.admin.user.vo.profile.UserProfileUpdatePasswordReqVO;
import com.newtouch.uctp.module.system.controller.admin.user.vo.profile.UserProfileUpdateReqVO;
import com.newtouch.uctp.module.system.convert.user.UserConvert;
import com.newtouch.uctp.module.system.dal.dataobject.dept.DeptDO;
import com.newtouch.uctp.module.system.dal.dataobject.dept.PostDO;
import com.newtouch.uctp.module.system.dal.dataobject.permission.RoleDO;
import com.newtouch.uctp.module.system.dal.dataobject.social.SocialUserDO;
import com.newtouch.uctp.module.system.dal.dataobject.user.AdminUserDO;
import com.newtouch.uctp.module.system.service.dept.DeptService;
import com.newtouch.uctp.module.system.service.dept.PostService;
import com.newtouch.uctp.module.system.service.permission.PermissionService;
import com.newtouch.uctp.module.system.service.permission.RoleService;
import com.newtouch.uctp.module.system.service.social.SocialUserService;
import com.newtouch.uctp.module.system.service.user.AdminUserService;

import static com.newtouch.uctp.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;
import static com.newtouch.uctp.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.newtouch.uctp.module.infra.enums.ErrorCodeConstants.FILE_IS_EMPTY;

@Tag(name =  "管理后台 - 用户个人中心")
@RestController
@RequestMapping("/system/user/profile")
@Validated
@Slf4j
public class UserProfileController {

    @Resource
    private AdminUserService userService;
    @Resource
    private DeptService deptService;
    @Resource
    private PostService postService;
    @Resource
    private PermissionService permissionService;
    @Resource
    private RoleService roleService;
    @Resource
    private SocialUserService socialService;

    @GetMapping("/get")
    @Operation(summary = "获得登录用户信息")
    @DataPermission(enable = false) // 关闭数据权限，避免只查看自己时，查询不到部门。
    public CommonResult<UserProfileRespVO> profile() {
        // 获得用户基本信息
        AdminUserDO user = userService.getUser(getLoginUserId());
        UserProfileRespVO resp = UserConvert.INSTANCE.convert03(user);
        // 获得用户角色
        List<RoleDO> userRoles = roleService.getRoleListFromCache(permissionService.getUserRoleIdListByUserId(user.getId()));
        resp.setRoles(UserConvert.INSTANCE.convertList(userRoles));
        // 获得部门信息
        if (user.getDeptId() != null) {
            DeptDO dept = deptService.getDept(user.getDeptId());
            resp.setDept(UserConvert.INSTANCE.convert02(dept));
        }
        // 获得岗位信息
        if (CollUtil.isNotEmpty(user.getPostIds())) {
            List<PostDO> posts = postService.getPostList(user.getPostIds());
            resp.setPosts(UserConvert.INSTANCE.convertList02(posts));
        }
        // 获得社交用户信息
        List<SocialUserDO> socialUsers = socialService.getSocialUserList(user.getId(), UserTypeEnum.ADMIN.getValue());
        resp.setSocialUsers(UserConvert.INSTANCE.convertList03(socialUsers));
        return success(resp);
    }

    @PutMapping("/update")
    @Operation(summary = "修改用户个人信息")
    public CommonResult<Boolean> updateUserProfile(@Valid @RequestBody UserProfileUpdateReqVO reqVO) {
        userService.updateUserProfile(getLoginUserId(), reqVO);
        return success(true);
    }

    @PutMapping("/update-password")
    @Operation(summary = "修改用户个人密码")
    public CommonResult<Boolean> updateUserProfilePassword(@Valid @RequestBody UserProfileUpdatePasswordReqVO reqVO) {
        userService.updateUserPassword(getLoginUserId(), reqVO);
        return success(true);
    }

    @PostMapping("/update-avatar")
    @Operation(summary = "上传用户个人头像")
    public CommonResult<String> updateUserAvatar(@RequestParam("avatarFile") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw exception(FILE_IS_EMPTY);
        }
        String avatar = userService.updateUserAvatar(getLoginUserId(), file.getInputStream());
        return success(avatar);
    }

}