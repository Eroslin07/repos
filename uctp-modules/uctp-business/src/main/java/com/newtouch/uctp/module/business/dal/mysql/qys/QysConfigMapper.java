package com.newtouch.uctp.module.business.dal.mysql.qys;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.mybatis.core.mapper.BaseMapperX;
import com.newtouch.uctp.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.newtouch.uctp.framework.mybatis.core.query.QueryWrapperX;
import com.newtouch.uctp.module.business.controller.app.qys.vo.QysConfigPageReqVO;
import com.newtouch.uctp.module.business.dal.dataobject.qys.QysConfigDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 契约锁 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface QysConfigMapper extends BaseMapperX<QysConfigDO> {

    default PageResult<QysConfigDO> selectPage(QysConfigPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<QysConfigDO>()
                .eqIfPresent(QysConfigDO::getRevision, reqVO.getRevision())
                .eqIfPresent(QysConfigDO::getServerUrl, reqVO.getServerUrl())
                .eqIfPresent(QysConfigDO::getAccessKey, reqVO.getAccessKey())
                .eqIfPresent(QysConfigDO::getAccessSecret, reqVO.getAccessSecret())
                .eqIfPresent(QysConfigDO::getStatus, reqVO.getStatus())
                .likeIfPresent(QysConfigDO::getBusinessName, reqVO.getBusinessName())
                .eqIfPresent(QysConfigDO::getBusinessId, reqVO.getBusinessId())
                .betweenIfPresent(QysConfigDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(QysConfigDO::getId));
    }


    default List<QysConfigDO> selectListAuth(){
        return selectList(new QueryWrapperX<QysConfigDO>()
                .isNotNull("ACCESS_KEY")
                .isNotNull("ACCESS_SECRET"));
    }

    @InterceptorIgnore(tenantLine = "true")
    @Select("select * from uctp_qys_config where COMPANY_ID=#{companyId} and DELETED = 0")
    QysConfigDO selectByCompanyId(@Param("companyId") Long companyId);

}
