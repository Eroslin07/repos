package com.newtouch.uctp.module.business.service;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.*;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
    Long createCarInfo(@Valid AppCarInfoCreateReqVO createReqVO);


    /**
     * 收车车辆新增
     * @param createReqVO
     * @return
     */
    String insertCarInfo(@Valid AppCarInfoCreateReqVO createReqVO);

    /**
     * 卖家信息
     * @param reqVO
     * @return
     */
    String insertSellerInfo(@Valid AppSellerInfoReqVO reqVO);

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
    void deleteCarInfo(Long id);

    /**
     * 获得车辆主表
     *
     * @param id 编号
     * @return 车辆主表
     */
    CarInfoDO getCarInfo(Long id);

    /**
     * 获得车辆主表列表
     *
     * @param ids 编号
     * @return 车辆主表列表
     */
    List<CarInfoDO> getCarInfoList(Collection<Long> ids);

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

    /**
     * 获取APP首页车辆分类统计
     * @return
     */
    List<Map<String, Object>> getCarCountGroupByStatus();

    /**
     * 获取APP首页卖车分页
     * @param pageVO
     * @return
     */
    PageResult<AppSellCarInfoRespVO> getSellCarInfoPage(AppSellCarInfoPageReqVO pageVO);
}
