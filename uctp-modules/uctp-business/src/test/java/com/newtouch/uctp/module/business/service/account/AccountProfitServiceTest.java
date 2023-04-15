package com.newtouch.uctp.module.business.service.account;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.account.vo.ProfitQueryReqVO;
import com.newtouch.uctp.module.business.controller.app.account.vo.ProfitRespVO;
import com.newtouch.uctp.module.business.dal.dataobject.profit.MerchantProfitDO;
import com.newtouch.uctp.module.business.enums.AccountConstants;
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

    @Test
    public void testRecorded() {
        String accountNo = "11111111";
        String contractNo = "1001";
        Integer vehicleReceiptAmount = 100;
        Integer carSalesAmount = 130;
        List<CostDTO> costs = new ArrayList<>();
        CostDTO c = new CostDTO();
        c.setAmount(1);
        c.setType("FEE_1");
        costs.add(c);

        List<TaxDTO> taxes = new ArrayList<>();
        TaxDTO t = new TaxDTO();
        t.setType("增值税");
        t.setRate(BigDecimal.valueOf(0.2));
        taxes.add(t);

        List<MerchantProfitDO> list = accountProfitService.recorded(accountNo, contractNo, vehicleReceiptAmount, carSalesAmount, costs, taxes);
        Assertions.assertEquals(4, list.size());
    }

    @Test
    public void testProfitList() {
        ProfitQueryReqVO profitQueryReqVO = new ProfitQueryReqVO();
        profitQueryReqVO.setPageNo(1);
        profitQueryReqVO.setPageSize(10);

        PageResult<ProfitRespVO> r = accountProfitService.profitList("1", profitQueryReqVO);
        Assertions.assertNotNull(r);
    }
}
