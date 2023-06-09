package com.newtouch.uctp.module.system.convert.auth;

import com.newtouch.uctp.module.business.api.qys.dto.EmployeeCreateOrRemoveDTO;
import com.newtouch.uctp.module.system.api.sms.dto.code.SmsCodeSendReqDTO;
import com.newtouch.uctp.module.system.api.sms.dto.code.SmsCodeUseReqDTO;
import com.newtouch.uctp.module.system.api.social.dto.SocialUserBindReqDTO;
import com.newtouch.uctp.module.system.controller.admin.auth.vo.*;
import com.newtouch.uctp.module.system.dal.dataobject.dept.DeptDO;
import com.newtouch.uctp.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import com.newtouch.uctp.module.system.dal.dataobject.permission.MenuDO;
import com.newtouch.uctp.module.system.dal.dataobject.permission.RoleDO;
import com.newtouch.uctp.module.system.dal.dataobject.tenant.TenantDO;
import com.newtouch.uctp.module.system.dal.dataobject.user.AdminUserDO;
import com.newtouch.uctp.module.system.dal.dataobject.user.UserExtDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.newtouch.uctp.framework.common.util.collection.CollectionUtils.convertSet;
import static com.newtouch.uctp.framework.common.util.collection.CollectionUtils.filterList;
import static com.newtouch.uctp.module.system.dal.dataobject.permission.MenuDO.ID_ROOT;

@Mapper
public interface AuthConvert {

    AuthConvert INSTANCE = Mappers.getMapper(AuthConvert.class);

    AuthLoginRespVO convert(OAuth2AccessTokenDO bean);

    default AuthPermissionInfoRespVO convert(AdminUserDO user, List<RoleDO> roleList, List<MenuDO> menuList) {
        return AuthPermissionInfoRespVO.builder()
            .user(AuthPermissionInfoRespVO.UserVO.builder().id(user.getId()).nickname(user.getNickname()).tenantId(user.getTenantId()).deptId(user.getDeptId()).avatar(user.getAvatar()).build())
            .roles(convertSet(roleList, RoleDO::getCode))
            .permissions(convertSet(menuList, MenuDO::getPermission))
            .build();
    }

    default AuthPermissionInfoRespVO convert(AdminUserDO user, UserExtDO userExt,DeptDO dept, TenantDO tenant,String accountNo) {
        return AuthPermissionInfoRespVO.builder()
                .user(AuthPermissionInfoRespVO.UserVO.builder().id(user.getId()).staffType(userExt.getStaffType())
                        .nickname(user.getNickname()).tenantId(user.getTenantId()).deptId(user.getDeptId())
                        .deptName(dept.getName()).tenantName(tenant.getName()).avatar(user.getAvatar())
                        .registerType(userExt.getStatus()).accountNo(accountNo).paymentType(dept.getPaymentType()).build())
                .build();
    }

    AuthMenuRespVO convertTreeNode(MenuDO menu);

    /**
     * 将菜单列表，构建成菜单树
     *
     * @param menuList 菜单列表
     * @return 菜单树
     */
    default List<AuthMenuRespVO> buildMenuTree(List<MenuDO> menuList) {
        // 排序，保证菜单的有序性
        menuList.sort(Comparator.comparing(MenuDO::getSort));
        // 构建菜单树
        // 使用 LinkedHashMap 的原因，是为了排序 。实际也可以用 Stream API ，就是太丑了。
        Map<Long, AuthMenuRespVO> treeNodeMap = new LinkedHashMap<>();
        menuList.forEach(menu -> treeNodeMap.put(menu.getId(), AuthConvert.INSTANCE.convertTreeNode(menu)));
        // 处理父子关系
        treeNodeMap.values().stream().filter(node -> !node.getParentId().equals(ID_ROOT)).forEach(childNode -> {
            // 获得父节点
            AuthMenuRespVO parentNode = treeNodeMap.get(childNode.getParentId());
            if (parentNode == null) {
                LoggerFactory.getLogger(getClass()).error("[buildRouterTree][resource({}) 找不到父资源({})]",
                    childNode.getId(), childNode.getParentId());
                return;
            }
            // 将自己添加到父节点中
            if (parentNode.getChildren() == null) {
                parentNode.setChildren(new ArrayList<>());
            }
            parentNode.getChildren().add(childNode);
        });
        // 获得到所有的根节点
        return filterList(treeNodeMap.values(), node -> ID_ROOT.equals(node.getParentId()));
    }

    SocialUserBindReqDTO convert(Long userId, Integer userType, AuthSocialBindLoginReqVO reqVO);

    SmsCodeSendReqDTO convert(AuthSmsSendReqVO reqVO);

    SmsCodeUseReqDTO convert(AuthSmsLoginReqVO reqVO, Integer scene, String usedIp);


    EmployeeCreateOrRemoveDTO convertDTO(AdminUserDO user,Boolean isRole);
}
