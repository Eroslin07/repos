package com.newtouch.uctp.module.business.controller.app.account.cash.vo;

import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.newtouch.uctp.module.business.controller.app.account.vo.PresentStatusRecordRespVO;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantCashDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.List;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_PATTERN;


@Schema(description = "我的保证金页面详情 Resp VO")
@Data
public class CashDetailRespVO {

    @Schema(description = "主键标识")
    private Long id;

    @Schema(description = "支付渠道")
    private String payChannel;

    @Schema(description = "损益类型")
    private String profitLossType;

    @Schema(description = "支付流水")
    private String tranRecordNo;

    @Schema(description = "支付金额")
    private Integer payAmount;

    @Schema(description = "交易类型")
    private String tradeType;

    @Schema(description = "交易去向")
    private String tradeTo;

    @Schema(description = "交易合同号")
    private String contractNo;

    @Schema(description = "账户余额")
    private Integer accountBalance;

    @Schema(description = "提现状态")
    private String presentState;

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
            if (merchantCashDO.getCreateTime() != null) {
                cashDetailRespVO.setCreateTime(DateUtil.format(merchantCashDO.getCreateTime(), NORM_DATETIME_PATTERN));
            }
        }
        return cashDetailRespVO;

    }
}
