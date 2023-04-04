package com.newtouch.uctp.module.business.dal.mysql;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.mybatis.core.mapper.BaseMapperX;
import com.newtouch.uctp.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppCarInfoPageReqVO;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppHomeCarInfoPageReqVO;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppHomeCarInfoRespVO;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * 车辆主表 Mapper
 *
 * @author lc
 */
@Mapper
public interface CarInfoMapper extends BaseMapperX<CarInfoDO> {

    default PageResult<CarInfoDO> selectPage(AppCarInfoPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CarInfoDO>()
                .likeIfPresent(CarInfoDO::getVin, reqVO.getVin())
                .likeIfPresent(CarInfoDO::getBrand, reqVO.getBrand())
                .eqIfPresent(CarInfoDO::getSalesStatus, reqVO.getSalesStatus())
                .eqIfPresent(CarInfoDO::getCheckStatus, reqVO.getCheckStatus())
                .orderByDesc(CarInfoDO::getCreateTime));
    }


    Page<AppHomeCarInfoRespVO> selectAppHomePage(@Param("pg") Page<AppHomeCarInfoRespVO> page,
                                                 @Param("pageVO") AppHomeCarInfoPageReqVO pageVO,
                                                 @Param("tenantId") String tenantId);
}