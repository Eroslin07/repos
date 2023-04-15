package com.newtouch.uctp.module.business.dal.mysql;


import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppCarInvoiceVo;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.CarDCVo;
import com.newtouch.uctp.module.business.controller.app.myCost.vo.AppMyCostVO;
import com.newtouch.uctp.module.business.controller.app.myCost.vo.CostExample;
import com.newtouch.uctp.module.business.dal.dataobject.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 发票 Mapper
 */
@Mapper
public interface InvoicesMapper {


    List<AppCarInvoiceVo> getInvoicesInfo(String carID);

    List<CarDCVo> getInvoiceIds(String id);

    VatInvoiceDO getVatInvoiceDO(Long id);

    List<InvoiceDetailsListDO> getVatInvoiceDetailsList(Long id);

    OutputInvoiceUsedCarIssueDO getOutputInvoiceUsedCarIssue(Long id);

    CarTransactionInvoiceDataDO getCarTransactionInvoiceData(Long id);

    InvoiceVehicleInfoDO getInvoiceVehicleInfo(Long id);


}
