package com.newtouch.uctp.module.business.service.contract;


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

    /**
     * 合同作废，包含契约锁合同作废
     * @param id
     */
    void contractInvalid(Long id,String reason);

    /**
     * 获取收车草稿合同
     * @param carId 车辆id
     * @param contractType 合同类型
     * @return
     */
    ContractDO getCollectDraft(Long carId, Integer contractType);

    /**
     * 更新数据
     * @param contractDO
     */
    void update(ContractDO contractDO);

    /**
     * 通过 契约锁合同id 找到合同
     * @param contractId
     * @return
     */
    ContractDO getByContractId(Long contractId);

    /**
     * 根据类型生成合同
     * @param type
     * @return
     */
    String GenerateCode(Integer type);

    void draft(Long carId);

    /**
     * 下载合同文档
     *
     * @param documentId 合同文档id
     */
    void documentDownload(Long documentId,String fileName);

    /**
     * 保存合同
     *
     * @param contractId
     * @param carId
     * @param contractName
     * @param deptId
     * @param code
     */
    Integer qysSave(Long contractId,Long carId,String contractName,
                    Long deptId,String code,Integer contractType);
}
