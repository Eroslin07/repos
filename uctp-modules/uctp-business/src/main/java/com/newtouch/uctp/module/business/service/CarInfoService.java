package com.newtouch.uctp.module.business.service;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.*;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * 车辆主表 Service 接口
 *
 * @author lc
 */
public interface CarInfoService {
    /**
     * 创建车辆主表
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createCarInfo(@Valid AppCarInfoCreateReqVO createReqVO);

    /**
     * 更新车辆主表
     *
     * @param updateReqVO 更新信息
     */
    void updateCarInfo(@Valid AppCarInfoUpdateReqVO updateReqVO);

    /**
     * 删除车辆主表
     *
     * @param id 编号
     */
    void deleteCarInfo(String id);

    /**
     * 获得车辆主表
     *
     * @param id 编号
     * @return 车辆主表
     */
    CarInfoDO getCarInfo(String id);

    /**
     * 获得车辆主表列表
     *
     * @param ids 编号
     * @return 车辆主表列表
     */
    List<CarInfoDO> getCarInfoList(Collection<String> ids);

    /**
     * 获得车辆主表分页
     *
     * @param pageReqVO 分页查询
     * @return 车辆主表分页
     */
    PageResult<CarInfoDO> getCarInfoPage(AppCarInfoPageReqVO pageReqVO);

    /**
     * 获取APP首页分页查询
     * @param pageVO
     * @return
     */
    PageResult<AppHomeCarInfoRespVO> getHomeCarInfoPage(AppHomeCarInfoPageReqVO pageVO);
}
