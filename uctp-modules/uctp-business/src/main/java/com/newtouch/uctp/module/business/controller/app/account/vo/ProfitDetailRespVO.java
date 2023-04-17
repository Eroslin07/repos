package com.newtouch.uctp.module.business.controller.app.account.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "资金管理 - 利润明细")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfitDetailRespVO extends ProfitRespVO {
    @Schema(description = "交易去向")
    private String tradeTo;
    @Schema(description = "交易去向中文名称")
    private String tradeToText;
    @Schema(description = "交易合同号")
    private String contractNo;
    @Schema(description = "提现状态记录（交易类型为提现时返回该值）")
    private List<PresentStatusRecordRespVO> presentStatusRecords;
}
