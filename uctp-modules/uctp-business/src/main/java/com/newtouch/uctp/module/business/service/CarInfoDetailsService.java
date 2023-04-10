package com.newtouch.uctp.module.business.service;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.carinfodetails.vo.CarInfoDetailsCreateReqVO;
import com.newtouch.uctp.module.business.controller.app.carinfodetails.vo.CarInfoDetailsPageReqVO;
import com.newtouch.uctp.module.business.controller.app.carinfodetails.vo.CarInfoDetailsUpdateReqVO;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDetailsDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * 车辆明细 Service 接口
 *
 * @author lc
 */
public interface CarInfoDetailsService {
    /**
     * 创建车辆明细
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCarInfoDetails(@Valid CarInfoDetailsCreateReqVO createReqVO);

    /**
     * 更新车辆明细
     *
     * @param updateReqVO 更新信息
     */
    void updateCarInfoDetails(@Valid CarInfoDetailsUpdateReqVO updateReqVO);

    /**
     * 删除车辆明细
     *
     * @param id 编号
     */
    void deleteCarInfoDetails(Long id);

    /**
     * 获得车辆明细
     *
     * @param id 编号
     * @return 车辆明细
     */
    CarInfoDetailsDO getCarInfoDetails(Long id);

    /**
     * 获得车辆明细列表
     *
     * @param ids 编号
     * @return 车辆明细列表
     */
    List<CarInfoDetailsDO> getCarInfoDetailsList(Collection<String> ids);

    /**
     * 获得车辆明细分页
     *
     * @param pageReqVO 分页查询
     * @return 车辆明细分页
     */
    PageResult<CarInfoDetailsDO> getCarInfoDetailsPage(CarInfoDetailsPageReqVO pageReqVO);

    int insertCarInfoDetail(CarInfoDetailsDO detailsDO);

    void updateCarInfoDetail(CarInfoDetailsDO detailsDO);

    /**
     * 根据车辆主表id获取唯一的车辆明细表
     * @param carId 车辆表Id
     * @return
     */
    CarInfoDetailsDO getCarInfoDetailsByCarId(Long carId);
}
