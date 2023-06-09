package com.newtouch.uctp.module.system.dal.mysql.user;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.mybatis.core.mapper.BaseMapperX;
import com.newtouch.uctp.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.newtouch.uctp.module.system.controller.admin.user.vo.user.UserExportReqVO;
import com.newtouch.uctp.module.system.controller.admin.user.vo.user.UserPageReqVO;
import com.newtouch.uctp.module.system.dal.dataobject.user.AdminUserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Collection;
import java.util.List;

@Mapper
public interface AdminUserMapper extends BaseMapperX<AdminUserDO> {

    default AdminUserDO selectByUsername(String username) {
        return selectOne(AdminUserDO::getUsername, username);
    }

    default AdminUserDO selectByEmail(String email) {
        return selectOne(AdminUserDO::getEmail, email);
    }

    default AdminUserDO selectByMobile(String mobile) {
        return selectOne(AdminUserDO::getMobile, mobile);
    }

    default List<AdminUserDO> selectByMobil(String mobile) {
        return selectList(new LambdaQueryWrapperX<AdminUserDO>()
                .eqIfPresent(AdminUserDO::getMobile, mobile));
    }

    default AdminUserDO selectByMobileAndStatus(String mobile) {
        return selectOne(AdminUserDO::getMobile, mobile);
    }

    default List<AdminUserDO> selectIsExist(String mobile, Integer status){
        return selectList(new LambdaQueryWrapperX<AdminUserDO>()
                .likeIfPresent(AdminUserDO::getMobile,mobile)
                .eqIfPresent(AdminUserDO::getStatus, status));
    }

    default PageResult<AdminUserDO> selectPage(UserPageReqVO reqVO, Collection<Long> deptIds) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AdminUserDO>()
                .likeIfPresent(AdminUserDO::getUsername, reqVO.getUsername())
                .likeIfPresent(AdminUserDO::getMobile, reqVO.getMobile())
                .eqIfPresent(AdminUserDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(AdminUserDO::getCreateTime, reqVO.getCreateTime())
                .inIfPresent(AdminUserDO::getDeptId, deptIds)
                .orderByDesc(AdminUserDO::getId));
    }

    default List<AdminUserDO> selectList(UserExportReqVO reqVO, Collection<Long> deptIds) {
        return selectList(new LambdaQueryWrapperX<AdminUserDO>()
                .likeIfPresent(AdminUserDO::getUsername, reqVO.getUsername())
                .likeIfPresent(AdminUserDO::getMobile, reqVO.getMobile())
                .eqIfPresent(AdminUserDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(AdminUserDO::getCreateTime, reqVO.getCreateTime())
                .inIfPresent(AdminUserDO::getDeptId, deptIds));
    }

    default List<AdminUserDO> selectListByNickname(String nickname) {
        return selectList(new LambdaQueryWrapperX<AdminUserDO>().like(AdminUserDO::getNickname, nickname));
    }

    default List<AdminUserDO> selectListByStatus(Integer status) {
        return selectList(AdminUserDO::getStatus, status);
    }

    default List<AdminUserDO> selectListByDeptIds(Collection<Long> deptIds) {
        return selectList(AdminUserDO::getDeptId, deptIds);
    }

    @Select("select *\n" +
            "from system_users a inner join uctp_user_ext b on a.id = b.USER_ID\n" +
            "where a.dept_id = #{deptId} and b.STAFF_TYPE = 1 and a.deleted = 0")
    AdminUserDO getMasterUser(@Param("deptId") Long deptId);
}
