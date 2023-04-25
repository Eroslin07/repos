package com.newtouch.uctp.module.business.controller.app.account.cash.vo;

import cn.hutool.core.date.DateUtil;
import com.newtouch.uctp.module.business.controller.app.account.vo.PresentStatusRecordRespVO;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantCashDO;
import com.newtouch.uctp.module.business.enums.AccountEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_PATTERN;


@Schema(description = "我的保证金页面详情 Resp VO")
@Data
public class CashDetailRespVO {

    @Schema(description = "主键标识")
    private Long id;

    @Schema(description = "支付渠道")
    private String payChannel;

    @Schema(description = "支付渠道名称")
    private String payChannelName;

    @Schema(description = "损益类型")
    private String profitLossType;

    @Schema(description = "损益类型名称")
    private String profitLossTypeName;

    @Schema(description = "支付流水")
    private String tranRecordNo;

    @Schema(description = "支付金额")
    private Long payAmount;

    @Schema(description = "交易类型")
    private String tradeType;

    @Schema(description = "交易类型名称")
    private String tradeTypeName;

    @Schema(description = "交易去向")
    private String tradeTo;

    @Schema(description = "交易去向名称")
    private String tradeToName;

    @Schema(description = "交易合同号")
    private String contractNo;

    @Schema(description = "账户余额")
    private Long accountBalance;

    @Schema(description = "提现状态")
    private String presentState;

    @Schema(description = "提现状态名称")
    private String presentStateName;

    @Schema(description = "乐观锁")
    private Integer revision;

    @Schema(description = "交易时间")
    private String createTime;

    @Schema(description = "到账银行卡")
    private String payeeBankAccount;

    private List<PresentStatusRecordRespVO> presentStatusRecords;

    public static CashDetailRespVO build(MerchantCashDO merchantCashDO) {
        CashDetailRespVO cashDetailRespVO = new CashDetailRespVO();
        if (merchantCashDO != null) {
            BeanUtils.copyProperties(merchantCashDO, cashDetailRespVO);
            cashDetailRespVO.setPayChannelName(AccountEnum.accountEnumMap.get(cashDetailRespVO.getPayChannel()));
            cashDetailRespVO.setProfitLossTypeName(AccountEnum.accountEnumMap.get(cashDetailRespVO.getProfitLossType()));
            cashDetailRespVO.setTradeTypeName(AccountEnum.accountEnumMap.get(cashDetailRespVO.getTradeType()));
            cashDetailRespVO.setTradeToName(AccountEnum.accountEnumMap.get(cashDetailRespVO.getTradeTo()));
            cashDetailRespVO.setPresentStateName(AccountEnum.accountEnumMap.get(cashDetailRespVO.getPresentState()));

            if (merchantCashDO.getCreateTime() != null) {
                cashDetailRespVO.setCreateTime(DateUtil.format(merchantCashDO.getCreateTime(), NORM_DATETIME_PATTERN));
            }
        }
        return cashDetailRespVO;

    }
}
