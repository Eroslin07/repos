package com.newtouch.uctp.module.business.service.settlement;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.admin.settlement.vo.InvoiceExcelVO;
import com.newtouch.uctp.module.business.controller.admin.settlement.vo.InvoiceExportReqVO;
import com.newtouch.uctp.module.business.controller.admin.settlement.vo.InvoiceReqVO;
import com.newtouch.uctp.module.business.controller.admin.settlement.vo.InvoiceRespVO;
import com.newtouch.uctp.module.business.dal.dataobject.SettlementInvoiceDO;

import java.util.List;

/**
 /**
 * @author zhang
 * @date 2023/4/17 14:07
 */
public interface InvoiceService {
    PageResult<InvoiceRespVO> getInvoicePage(InvoiceReqVO pageVO);

    List<InvoiceExcelVO> getInvoice(InvoiceExportReqVO pageVO);

}
