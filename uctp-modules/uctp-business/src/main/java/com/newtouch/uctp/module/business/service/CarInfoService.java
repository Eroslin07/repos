package com.newtouch.uctp.module.business.service;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.*;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDO;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDetailsDO;

import javax.validation.Valid;
import java.math.BigDecimal;
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
    AppBpmCarInfoRespVO insertCarInfo(@Valid AppCarInfoCreateReqVO createReqVO);

    /**
     * 卖家信息
     * @param reqVO
     * @return
     */
    AppBpmCarInfoRespVO insertSellerInfo(@Valid AppSellerInfoReqVO reqVO);

    CarInfoDO selectCarInfoByID(String id);

    CarInfoDetailsDO seleCarInfoDetail(String id);

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
     * 收车删除车辆主表&明细表数据
     *
     * @param id 编号
     */
    void delCarInfoWithCollect(Long id);




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
    List<HomeCountVO> getCarCountGroupByStatus();

    /**
     * 获取APP首页卖车分页
     * @param pageVO
     * @return
     */
    PageResult<AppSellCarInfoPageRespVO> getSellCarInfoPage(AppSellCarInfoPageReqVO pageVO);

    /**
     * 获取我要卖车详情页面
     * @param id 车辆id
     * @return
     */
    AppSellCarInfoRespVO getSellCarInfo(Long id);

    Map getCarInfoByVIN(String vin);

    /**
     * 根据车辆id回显车辆信息
     * @param id
     * @return
     */
    AppBpmCarInfoRespVO getCarInfoByID(Long id);


    /**
     * 根据车辆id查询卡片信息
     * @param id
     * @return
     */
    AppCarInfoCardRespVO getCardByID(Long id);

    /**
     * 获取卖车详情的明细费用
     * @param id 车辆id
     * @return
     */
    AppCarInfoAmountRespVO getCarInfoAmount(Long id, BigDecimal sellAmount);

    /**
     * 保存卖车填写草稿信息
     * @param reqVO
     */
    AppBpmCarInfoRespVO saveSellCarInfo(AppSellCarInfoReqVO reqVO);

     AppCarInfoAndDetailVO getCarInfoAndDetail(String id);
    /**
     * 获得车辆部分明细信息
     *
     * @param carDCVo
     * @return 车辆部分明细信息
     */
    CarDetailRespVO getCarInfoAndDetails(CarDCVo carDCVo);

    /**
     * 获得车辆DC信息
     *
     * @param id
     * @return 车辆部分信息
     */


    PicResp getCarDCDetails(String id);


    /**
     * 获得车辆驾驶证，行驶证编号
     *
     * @param carID
     * @return 车辆驾驶证，行驶证编号
     */
    CarDCVo getCarDC(String carID);
    /**
     * 获得车辆在业务上传表中的id集合
     *
     * @param carID
     * @return 车辆在业务上传表中的id集合
     */
    List<CarDCVo> getCarIds(String carID);
    /**
     * 获得车辆驾驶证在业务上传表中的id集合
     *
     * @param carID
     * @return 车辆驾驶证在业务上传表中的id集合
     */
    List<CarDCVo> getDrivingLicenseIds(String carID);
    /**
     * 获得车辆行驶证在业务上传表中的id集合
     *
     * @param carID
     * @return 车辆行驶证在业务上传表中的id集合
     */
    List<CarDCVo> getCertificateIds(String carID);


    /**
     * 获得卖家/买家信息
     *
     * @param carID
     * @return PeopleVo
     */
    PeopleVo getPeopelInfo(String carID);

    /**
     * 删除卖车草稿数据
     * @param id 车辆主表id
     */
    void deleteSell(Long id);

    /**
     * 根据车辆ID获取流程所需的过户信息
     * @param carId 车辆ID
     * @return
     */
    CarTransferInfoVO getTransferInfo(Long carId, String procDefKey);

    CarInvoiceInfoVO getForwardInvoiceInfo(Long contractId);

    CarInvoiceInfoVO getReverseInvoiceInfo(Long contractId);

    void update(CarInfoDO carInfo);
}
