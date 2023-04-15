package com.newtouch.uctp.module.business.controller.app.invoices;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.business.controller.app.notice.vo.NoticeVO;
import com.newtouch.uctp.module.business.dal.dataobject.InvoiceDetailsListDO;
import com.newtouch.uctp.module.business.dal.dataobject.NoticeInfoDO;
import com.newtouch.uctp.module.business.dal.dataobject.OutputInvoiceUsedCarIssueDO;
import com.newtouch.uctp.module.business.dal.dataobject.VatInvoiceDO;
import com.newtouch.uctp.module.business.service.InvoicesService;
import com.newtouch.uctp.module.business.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;

@RestController
@RequestMapping("/uctp/car-invoices")
@Validated
public class AppInvoicesController {


    @Resource
    private InvoicesService invoicesService;


    //private static final Logger log = LoggerFactory.getLogger(AppNoticenfoController.class);

    @GetMapping ("/vatInvoices")
    @Operation(summary = "获得增值发票信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<VatInvoiceDO> getNotices(@RequestParam("id") Long id) {


        VatInvoiceDO vatInvoiceDO = invoicesService.getVatInvoiceDO(Long.valueOf(id));

        return success(vatInvoiceDO);
    }


    @GetMapping ("/outputInvoiceUsedCarIssue")
    @Operation(summary = "获得正反统一发票信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<OutputInvoiceUsedCarIssueDO> getNotices2(@RequestParam("id") Long id) {


        OutputInvoiceUsedCarIssueDO outputInvoiceUsedCarIssue = invoicesService.getOutputInvoiceUsedCarIssue(id);

        return success(outputInvoiceUsedCarIssue);
    }






}
