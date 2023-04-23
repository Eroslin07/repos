package com.newtouch.uctp.module.business.controller.app.account;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.web.core.util.WebFrameworkUtils;
import com.newtouch.uctp.module.business.controller.app.account.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
public class AccountProfitControllerTest {

    @Resource
    private AccountProfitController accountProfitController;

    @Resource
    private MockHttpServletRequest mockHttpServletRequest;

    private String accountNo = "55555555";

    @BeforeEach
    public void before() {
        // 模拟登录
        WebFrameworkUtils.setLoginUserId(mockHttpServletRequest, 211L);
    }

    @Test
    public void testSummary() {
        CommonResult<ProfitSummaryRespVO> r = accountProfitController.summary(accountNo);
        Assertions.assertNotNull(r);
    }

    @Test
    public void testCostList() {
        ProfitCostQueryReqVO query = new ProfitCostQueryReqVO();
        query.setAccountNo(accountNo);
        query.setQuarter("2023Q2");
        CommonResult<List<ProfitCostMonthRespVO>> r = accountProfitController.costList(query);
        Assertions.assertNotNull(r);
    }

    @Test
    public void testProfitPresent() {
        ProfitPresentReqVO req = new ProfitPresentReqVO();
        req.setAccountNo(accountNo);
        req.setAmount(1);
        req.setMerchantBankId("2");
        List<ProfitPresentInvoiceReqVO> invoiceFiles = new ArrayList<>();
        ProfitPresentInvoiceReqVO f1 = new ProfitPresentInvoiceReqVO();
        f1.setFileId("1");
        f1.setFileUrl("http://127.0.0.1/1.jpg");
        invoiceFiles.add(f1);
        ProfitPresentInvoiceReqVO f2 = new ProfitPresentInvoiceReqVO();
        f2.setFileId("2");
        f2.setFileUrl("http://127.0.0.1/2.jpg");
        invoiceFiles.add(f2);

        req.setInvoiceFiles(invoiceFiles);

        CommonResult<String> r = accountProfitController.profitPresent(req);
        Assertions.assertNotNull(r);
    }

    @Test
    public void testProfitDetail() {
        CommonResult<ProfitDetailRespVO> r = accountProfitController.profitDetail(accountNo, "1650142990934413313");
        Assertions.assertNotNull(r);
    }
}
