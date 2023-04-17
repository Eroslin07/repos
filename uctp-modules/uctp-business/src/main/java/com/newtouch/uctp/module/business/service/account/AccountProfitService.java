package com.newtouch.uctp.module.business.service.account;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.account.vo.ProfitDetailRespVO;
import com.newtouch.uctp.module.business.controller.app.account.vo.ProfitQueryReqVO;
import com.newtouch.uctp.module.business.controller.app.account.vo.ProfitRespVO;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantAccountDO;
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

    /**
     * 获取账户信息
     * @param accountNo
     * @return
     */
    MerchantAccountDO queryByAccountNo(String accountNo);

    /**
     * 利润提现
     * @param accountNo 商户账号
     * @param merchantBankId 商户银行卡ID
     * @param amount 提取金额
     * @param invoiceIds 发票ID（支持多个）
     * @return 利润提现记录ID（后续市场审核通过后通过此ID通知银行转账）
     */
    Long profitPresent(String accountNo, Long merchantBankId, Integer amount, List<String> invoiceIds);

    /**
     * 利润分页查询
     * @param accountNo 商户账号
     * @param query 查询对接
     * @return 分页的利润列表
     */
    PageResult<ProfitRespVO> profitList(String accountNo, ProfitQueryReqVO query);

    /**
     * 查询利润明细
     * @param accountNo 商户账号
     * @param profitId 利润记录ID
     * @return 利润明细，提现交易会返回提现状态记录清单
     */
    ProfitDetailRespVO profitDetail(String accountNo, Long profitId);

    /**
     * 审核利润提现
     * @param id 利润ID
     * @param auditOpinion 审核意见
     */
    void auditProfitPressent(Long id, ProfitPressentAuditOpinion auditOpinion);
}