package com.newtouch.uctp.module.system.service.user;

import cn.hutool.core.collection.CollUtil;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.common.util.collection.CollectionUtils;
import com.newtouch.uctp.module.system.controller.admin.user.vo.profile.UserProfileUpdatePasswordReqVO;
import com.newtouch.uctp.module.system.controller.admin.user.vo.profile.UserProfileUpdateReqVO;
import com.newtouch.uctp.module.system.controller.admin.user.vo.user.*;
import com.newtouch.uctp.module.system.dal.dataobject.user.AdminUserDO;
import com.newtouch.uctp.module.system.dal.dataobject.user.UserExtDO;
import org.apache.ibatis.annotations.Param;

import javax.validation.Valid;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 后台用户 Service 接口
 *
 * @author 芋道源码
 */
public interface UserExtService {



    /**
     * 新增用户
     */
    int insertUser(UserExtDO user);

    int deleteByUserId(Long userId);

    List<UserExtDO> selectByIDCard(String IDCard);

    List<UserExtDO> selectByUserId(Long IDCard);

    String getAccountNo(@Param("deptId") Long deptId);
}
