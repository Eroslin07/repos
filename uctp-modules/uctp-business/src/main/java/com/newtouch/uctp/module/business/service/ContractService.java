package com.newtouch.uctp.module.business.service;


import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppContractarVO;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.CarDCVo;
import com.newtouch.uctp.module.business.dal.dataobject.ContractDO;

import java.util.List;

/**
 * 合同 Service 接口
 *
 * @author hr
 */

public interface ContractService {

    /**
     * 获得车辆合同主表信息
     *
     * @param carID
     * @return 车辆合同主表信息
     */
    List<AppContractarVO> getContractInfo(String carID);
    /**
     * 作废合同
     *
     * @param carDCVo
     *
     */
    String updateContractStatas(CarDCVo carDCVo);

    /**
     * 获得合同在业务上传表中的id集合（查询url使用）
     *
     * @param contractID
     * @return 合同在业务上传表中的id集合
     */
    List<CarDCVo> getContractIds(String contractID);

    List<ContractDO> getContractListByCarId(Long id);

    /**
     * 通过Id获取合同
     * @param id
     * @return
     */
    ContractDO getById(Long id);
}
