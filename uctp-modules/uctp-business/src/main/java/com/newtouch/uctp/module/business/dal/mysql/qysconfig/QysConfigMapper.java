package com.newtouch.uctp.module.business.dal.mysql.qysconfig;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.mybatis.core.mapper.BaseMapperX;
import com.newtouch.uctp.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.newtouch.uctp.module.business.controller.app.qiyuesuo.vo.QysConfigPageReqVO;
import com.newtouch.uctp.module.business.dal.dataobject.qysconfig.QysConfigDO;
import org.apache.ibatis.annotations.Mapper;

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


}
