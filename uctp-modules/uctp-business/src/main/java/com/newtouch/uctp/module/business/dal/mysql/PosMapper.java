package com.newtouch.uctp.module.business.dal.mysql;

import com.newtouch.uctp.framework.mybatis.core.mapper.BaseMapperX;
import com.newtouch.uctp.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.newtouch.uctp.module.business.controller.app.pos.vo.PosRespVO;
import com.newtouch.uctp.module.business.dal.dataobject.PosDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * POS机主表 Mapper
 *
 * @author qjj
 */
@Mapper
public interface PosMapper extends BaseMapperX<PosDO> {


    default List<PosDO> selectByBusinessId(Long businessId) {
        return selectList(new LambdaQueryWrapperX<PosDO>()
                .eqIfPresent(PosDO::getBusinessId,businessId));
    }

    default List<PosDO> selectByBusinessIdAndStatus(Long businessId) {
        return selectList(new LambdaQueryWrapperX<PosDO>()
                .eqIfPresent(PosDO::getBusinessId,businessId)
                .eqIfPresent(PosDO::getStatus,0));
    }

    default List<PosDO> selectByPosId(String posId) {
        return selectList(new LambdaQueryWrapperX<PosDO>()
                .eqIfPresent(PosDO::getPosId, posId));
    }
}
