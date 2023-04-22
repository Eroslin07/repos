package com.newtouch.uctp.module.business.service.account;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.account.vo.ProfitDetailRespVO;
import com.newtouch.uctp.module.business.controller.app.account.vo.ProfitQueryReqVO;
import com.newtouch.uctp.module.business.controller.app.account.vo.ProfitRespVO;
import com.newtouch.uctp.module.business.controller.app.account.vo.ProfitSummaryRespVO;
import com.newtouch.uctp.module.business.dal.dataobject.profit.MerchantProfitDO;
import com.newtouch.uctp.module.business.service.account.dto.CostDTO;
import com.newtouch.uctp.module.business.service.account.dto.TaxDTO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@Slf4j
public class AccountProfitServiceTest {

    @Resource
    private AccountProfitService accountProfitService;

    @Resource
    private RedissonClient redissonClient;

    private String accountNo = "55555555";
    private String contractNo = "22001";

    @Test
    public void testRecorded() {
        Integer vehicleReceiptAmount = 201;
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
        t.setRate(BigDecimal.valueOf(0.1));
        taxes.add(t);

        List<MerchantProfitDO> list = accountProfitService.recorded(accountNo, contractNo, vehicleReceiptAmount, carSalesAmount, costs, taxes);
        Assertions.assertEquals(6, list.size());
    }

    @Test
    public void testSummary() {
        ProfitSummaryRespVO summary = accountProfitService.summary(accountNo);
        Assertions.assertNotNull(summary);
    }

    @Test
    public void testProfitList() {
        ProfitQueryReqVO profitQueryReqVO = new ProfitQueryReqVO();
        profitQueryReqVO.setPageNo(1);
        profitQueryReqVO.setPageSize(10);

        PageResult<ProfitRespVO> r = accountProfitService.profitList(accountNo, profitQueryReqVO);
        Assertions.assertNotNull(r);
    }

    @Test
    public void testProfitListForIncome() {
        ProfitQueryReqVO profitQueryReqVO = new ProfitQueryReqVO();
        profitQueryReqVO.setPageNo(1);
        profitQueryReqVO.setPageSize(10);
        profitQueryReqVO.setType(4);

        PageResult<ProfitRespVO> r = accountProfitService.profitList(accountNo, profitQueryReqVO);
        Assertions.assertNotNull(r);
    }

    @Test
    public void testProfitListForFreeze() {
        ProfitQueryReqVO profitQueryReqVO = new ProfitQueryReqVO();
        profitQueryReqVO.setPageNo(1);
        profitQueryReqVO.setPageSize(10);
        profitQueryReqVO.setType(2);

        PageResult<ProfitRespVO> r = accountProfitService.profitList(accountNo, profitQueryReqVO);
        Assertions.assertNotNull(r);
    }

    @Test
    public void testProfitDetail() {
        ProfitDetailRespVO pd = accountProfitService.profitDetail(accountNo, 1647850179174305793L);
        Assertions.assertNotNull(pd);
    }

    @Test
    public void testProfitPresent() {
        Long id = accountProfitService.profitPresent(accountNo, 1L, 5, null);
        Assertions.assertNotNull(id);
    }

    @Test
    public void testAuditProfitPressentReject() throws InterruptedException {
        accountProfitService.auditProfitPressent(1647853199840743426L, ProfitPressentAuditOpinion.AUDIT_PROCESSING);
        Thread.sleep(10);
        accountProfitService.auditProfitPressent(1647853199840743426L, ProfitPressentAuditOpinion.AUDIT_REJECT);
    }

    @Test
    public void testAuditProfitPressentApproved() throws InterruptedException {
        accountProfitService.auditProfitPressent(1647855387535155202L, ProfitPressentAuditOpinion.AUDIT_PROCESSING);
        Thread.sleep(10);
        accountProfitService.auditProfitPressent(1647855387535155202L, ProfitPressentAuditOpinion.AUDIT_APPROVED);
    }

    @Test
    public void testLock() throws InterruptedException {
        RLock lock = redissonClient.getLock("UCTP:ACCOUNT:PROFIT:RECORDED:" + contractNo);
        try {
            // 锁10秒
            //lock.lock(10, TimeUnit.SECONDS);
            // 等待1s，由看门狗控制超时
            boolean locked = lock.tryLock(1, TimeUnit.SECONDS);
            log.info("locked: {}", locked);
            new Thread(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    RLock lock2 = redissonClient.getLock("UCTP:ACCOUNT:PROFIT:RECORDED2:" + contractNo);
                    boolean locked2 = lock2.tryLock(1, TimeUnit.SECONDS);
                    log.info("locked2: {}", locked2);
                }
            }).start();
            if (locked) {
                log.info("start");
                // 业务处理30秒
                Thread.sleep(3 * 1000);

            }

        } finally {
            log.info("finally");
            // 被当前线程锁定，则需要解锁
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
