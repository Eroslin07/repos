package com.newtouch.uctp.module.business.controller.app.account;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.web.core.util.WebFrameworkUtils;
import com.newtouch.uctp.module.business.controller.app.account.vo.CashBackReqVO;
import com.newtouch.uctp.module.business.controller.app.account.vo.CashBackRespVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class AccountCashBackControllerTest {

    @Resource
    private AccountCashBackController accountCashBackController;

    @Resource
    private MockHttpServletRequest mockHttpServletRequest;

    private String accountNo = "55555555";

    @BeforeEach
    public void before() {
        // 模拟登录
        WebFrameworkUtils.setLoginUserId(mockHttpServletRequest, 211L);
    }

    @Test
    public void testList() {
        CashBackReqVO query = new CashBackReqVO();
        query.setAccountNo(accountNo);
        CommonResult<PageResult<CashBackRespVO>> r = accountCashBackController.list(query);
        Assertions.assertNotNull(r);
    }

    @Test
    public void testDetail() {
        CashBackReqVO query = new CashBackReqVO();
        query.setAccountNo(accountNo);
        CommonResult<CashBackRespVO> r = accountCashBackController.detail(accountNo, "122");
        Assertions.assertNull(r);
    }
}
