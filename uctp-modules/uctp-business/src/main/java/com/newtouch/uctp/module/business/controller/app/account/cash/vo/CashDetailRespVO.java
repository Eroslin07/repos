package com.newtouch.uctp.module.business.controller.app.account.cash.vo;

import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantCashDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;


@Schema(description = "管理后台 - 车辆明细创建 Request VO")
@Data
public class CashDetailRespVO {

    @Schema(description = "支付渠道")
    private String payChannel;

    @Schema(description = "损益类型")
    private String profitLossType;

    @Schema(description = "支付流水")
    private String tradeRecordNo;

    @Schema(description = "支付金额")
    private Integer payAmount;

    @Schema(description = "交易类型")
    private String tradeType;

    @Schema(description = "账户余额")
    private Integer accountBalance;

    @Schema(description = "乐观锁")
    private Integer revision;

    public static CashDetailRespVO build(MerchantCashDO merchantCashDO) {
        CashDetailRespVO cashDetailRespVO = new CashDetailRespVO();
        if (merchantCashDO != null) {
            BeanUtils.copyProperties(merchantCashDO, cashDetailRespVO);
        }
        return cashDetailRespVO;

    }
}
