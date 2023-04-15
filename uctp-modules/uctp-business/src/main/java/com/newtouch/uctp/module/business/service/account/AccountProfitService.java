package com.newtouch.uctp.module.business.service.account;

import com.newtouch.uctp.module.business.dal.dataobject.profit.MerchantProfitDO;
import com.newtouch.uctp.module.business.service.account.dto.CostDTO;
import com.newtouch.uctp.module.business.service.account.dto.TaxDTO;

import java.util.List;

/**
 * 利润服务接口
 *
 * @author zhangjun
 */
public interface AccountProfitService {

    /**
     * 利润划入
     * @param accountNo 商户号
     * @param contractNo 交易合同号
     * @param vehicleReceiptAmount 收车金额（单位为：分）
     * @param carSalesAmount 卖车金额（单位为：分）
     * @param costs 费用清单
     * @param taxes 税率清单
     * @return 利润明细结果
     */
    List<MerchantProfitDO> recorded(String accountNo,
                                    String contractNo,
                                    Integer vehicleReceiptAmount,
                                    Integer carSalesAmount,
                                    List<CostDTO> costs,
                                    List<TaxDTO> taxes);

}
