package com.newtouch.uctp.module.business.controller.app.account;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.web.core.util.WebFrameworkUtils;
import com.newtouch.uctp.module.business.controller.app.account.vo.ProfitSummaryRespVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.annotation.Resource;

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
}
