package com.newtouch.uctp.module.business.dal.mysql.qys;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.mybatis.core.mapper.BaseMapperX;
import com.newtouch.uctp.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.newtouch.uctp.module.business.controller.app.qys.vo.QysCallbackPageReqVO;
import com.newtouch.uctp.module.business.dal.dataobject.qys.QysCallbackDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 契约锁回调日志 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface QysCallbackMapper extends BaseMapperX<QysCallbackDO> {

    default PageResult<QysCallbackDO> selectPage(QysCallbackPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<QysCallbackDO>()
                .eqIfPresent(QysCallbackDO::getRevision, reqVO.getRevision())
                .eqIfPresent(QysCallbackDO::getType, reqVO.getType())
                .eqIfPresent(QysCallbackDO::getContent, reqVO.getContent())
                .eqIfPresent(QysCallbackDO::getMainId, reqVO.getMainId())
                .betweenIfPresent(QysCallbackDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(QysCallbackDO::getId));
    }

    /**
     * 获取 业务关联的回调数据，根据时间倒叙
     * @param mainId 业务Id
     * @return
     */
    default List<QysCallbackDO> getByMainIdAndType(Long mainId, Integer type){
        return selectList(new LambdaQueryWrapperX<QysCallbackDO>()
                .eq(QysCallbackDO::getMainId,mainId)
                .eq(QysCallbackDO::getType,type)
                .orderByDesc(QysCallbackDO::getCreateTime));
    }
}
