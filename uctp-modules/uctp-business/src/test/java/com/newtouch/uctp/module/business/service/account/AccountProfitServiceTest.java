package com.newtouch.uctp.module.business.service.account;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.account.vo.ProfitDetailRespVO;
import com.newtouch.uctp.module.business.controller.app.account.vo.ProfitQueryReqVO;
import com.newtouch.uctp.module.business.controller.app.account.vo.ProfitRespVO;
import com.newtouch.uctp.module.business.dal.dataobject.profit.MerchantProfitDO;
import com.newtouch.uctp.module.business.service.account.dto.CostDTO;
import com.newtouch.uctp.module.business.service.account.dto.TaxDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class AccountProfitServiceTest {

    @Resource
    private AccountProfitService accountProfitService;

    public void openTest() {

    }

    @Test
    public void testRecorded() {
        String accountNo = "11111111";
        String contractNo = "1006";
        Integer vehicleReceiptAmount = 100;
        Integer carSalesAmount = 200;
        List<CostDTO> costs = new ArrayList<>();
        CostDTO c = new CostDTO();
        c.setAmount(1);
        c.setType("FEE_1");
        costs.add(c);
        CostDTO c2 = new CostDTO();
        c2.setAmount(2);
        c2.setType("FEE_2");
        c2.setPromptPayment(true);
        c2.setBankNo("622000000");
        c2.setBankName("张三");
        costs.add(c2);

        List<TaxDTO> taxes = new ArrayList<>();
        TaxDTO t = new TaxDTO();
        t.setType("增值税");
        t.setRate(BigDecimal.valueOf(0.2));
        taxes.add(t);

        List<MerchantProfitDO> list = accountProfitService.recorded(accountNo, contractNo, vehicleReceiptAmount, carSalesAmount, costs, taxes);
        Assertions.assertEquals(5, list.size());
    }

    @Test
    public void testProfitList() {
        ProfitQueryReqVO profitQueryReqVO = new ProfitQueryReqVO();
        profitQueryReqVO.setPageNo(1);
        profitQueryReqVO.setPageSize(10);

        PageResult<ProfitRespVO> r = accountProfitService.profitList("11111111", profitQueryReqVO);
        Assertions.assertNotNull(r);
    }

    @Test
    public void testProfitDetail() {
        ProfitDetailRespVO pd = accountProfitService.profitDetail("11111111", 1647527558918410241L);
        Assertions.assertNotNull(pd);
    }

    @Test
    public void testProfitPresent() {
        Long id = accountProfitService.profitPresent("11111111", 1L, 5, null);
        Assertions.assertNotNull(id);
    }

    @Test
    public void testAuditProfitPressentReject() throws InterruptedException {
        accountProfitService.auditProfitPressent(1647533421515194369L, ProfitPressentAuditOpinion.AUDIT_PROCESSING);
        Thread.sleep(10);
        accountProfitService.auditProfitPressent(1647533421515194369L, ProfitPressentAuditOpinion.AUDIT_REJECT);
    }

    @Test
    public void testAuditProfitPressentApproved() throws InterruptedException {
        accountProfitService.auditProfitPressent(1647578548698865666L, ProfitPressentAuditOpinion.AUDIT_PROCESSING);
        Thread.sleep(10);
        accountProfitService.auditProfitPressent(1647578548698865666L, ProfitPressentAuditOpinion.AUDIT_APPROVED);
    }
}
