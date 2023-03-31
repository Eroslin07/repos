package com.newtouch.uctp.module.business.service;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppCarInfoPageReqVO;
import com.newtouch.uctp.module.business.domain.app.CarInfoDO;

/**
 * 车辆主表 Service 接口
 *
 * @author lc
 */
public interface CarInfoService {

    /**
     * 获得车辆主表分页
     *
     * @param pageReqVO 分页查询
     * @return 车辆主表分页
     */
    PageResult<CarInfoDO> getCarInfoPage(AppCarInfoPageReqVO pageReqVO);

}
