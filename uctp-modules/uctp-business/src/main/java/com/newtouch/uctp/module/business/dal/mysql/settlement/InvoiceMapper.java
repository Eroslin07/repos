package com.newtouch.uctp.module.business.dal.mysql.settlement;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newtouch.uctp.module.business.controller.admin.settlement.vo.InvoiceExcelVO;
import com.newtouch.uctp.module.business.controller.admin.settlement.vo.InvoiceExportReqVO;
import com.newtouch.uctp.module.business.controller.admin.settlement.vo.InvoiceReqVO;
import com.newtouch.uctp.module.business.controller.admin.settlement.vo.InvoiceRespVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 /**
 * @author zhang
 * @date 2023/4/17 14:19
 */
@Mapper
public interface InvoiceMapper {
    Page<InvoiceRespVO> getInvoice(Page<InvoiceRespVO> page, @Param("invoiceReqVO") InvoiceReqVO invoiceReqVO);

    List<InvoiceExcelVO> getInvoiceList(@Param("invoiceReqVO") InvoiceExportReqVO invoiceReqVO);
}
