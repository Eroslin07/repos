package com.newtouch.uctp.module.business.controller.app.account.cash.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantCashDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

import static com.newtouch.uctp.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;
import static com.newtouch.uctp.framework.common.util.date.DateUtils.TIME_ZONE_DEFAULT;


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

    @Schema(description = "交易时间")
    @JsonProperty("create_time")
    @JsonFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND, timezone = TIME_ZONE_DEFAULT)
    private LocalDateTime createTime;

    public static CashDetailRespVO build(MerchantCashDO merchantCashDO) {
        CashDetailRespVO cashDetailRespVO = new CashDetailRespVO();
        if (merchantCashDO != null) {
            BeanUtils.copyProperties(merchantCashDO, cashDetailRespVO);
        }
        return cashDetailRespVO;

    }
}
