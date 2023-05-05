package com.newtouch.uctp.module.business.service.settlement;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.mybatis.core.util.MyBatisUtils;
import com.newtouch.uctp.module.business.controller.admin.settlement.vo.InvoiceExcelVO;
import com.newtouch.uctp.module.business.controller.admin.settlement.vo.InvoiceExportReqVO;
import com.newtouch.uctp.module.business.controller.admin.settlement.vo.InvoiceReqVO;
import com.newtouch.uctp.module.business.controller.admin.settlement.vo.InvoiceRespVO;
import com.newtouch.uctp.module.business.dal.mysql.settlement.InvoiceMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * /**
 *
 * @author zhang
 * @date 2023/4/17 14:14
 */
@Slf4j
@Service
public class InvoiceServiceImpl implements InvoiceService {
    @Resource
    private InvoiceMapper invoiceMapper;

    @Override
    public PageResult<InvoiceRespVO> getInvoicePage(InvoiceReqVO pageVO) {
        Page<InvoiceRespVO> page = MyBatisUtils.buildPage(pageVO);
        invoiceMapper.getInvoice(page, pageVO);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public List<InvoiceExcelVO> getInvoice(InvoiceExportReqVO pageVO) {
        return invoiceMapper.getInvoiceList(pageVO);
    }
}
