package com.newtouch.uctp.module.business.service;


import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppCarInvoiceVo;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.CarDCVo;
import com.newtouch.uctp.module.business.dal.dataobject.*;
import org.springframework.stereotype.Service;


import java.util.List;

/**
 * 车辆发票 Service 接口
 *
 * @author hr
 */

public interface InvoicesService {

    /**
     * 获得车辆发票信息
     *
     * @param carID
     * @return 车辆发票信息
     */
    List<AppCarInvoiceVo> getInvoicesInfo(String carID);


    /**
     * 获得发票在业务上传表中的id集合（查询url使用）
     *
     * @param id
     * @return 发票在业务上传表中的id集合
     */
    List<CarDCVo> getInvoiceIds(String id);
    /**
     * 获得增值税发票信息
     *
     * @param id
     * @return 增值税发票实体
     */
    VatInvoiceDO getVatInvoiceDO(Long id);
    /**
     * 获得增值税发票商品明细节点
     *
     * @param id
     * @return 商品明细实体list
     */
    List<InvoiceDetailsListDO> getVatInvoiceDetailsList(Long id);

    /**
     * 获得正反统一发票
     *
     * @param id
     * @return 正反统一发票
     */
    OutputInvoiceUsedCarIssueDO getOutputInvoiceUsedCarIssue(Long id);
    /**
     * 正反发票中间信息
     *
     * @param id
     * @return 正反发票中间信息
     */
    CarTransactionInvoiceDataDO getCarTransactionInvoiceData(Long id);
    /**
     * 获得二手车明细节点
     *
     * @param id
     * @return 二手车明细
     */
    InvoiceVehicleInfoDO getInvoiceVehicleInfo(Long id);
}
