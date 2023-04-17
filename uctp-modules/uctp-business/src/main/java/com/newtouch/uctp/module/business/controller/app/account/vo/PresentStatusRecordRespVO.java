package com.newtouch.uctp.module.business.controller.app.account.vo;

import cn.hutool.core.date.DateUtil;
import com.newtouch.uctp.module.business.dal.dataobject.account.PresentStatusRecordDO;
import com.newtouch.uctp.module.business.enums.AccountEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_PATTERN;

@Schema(description = "资金管理 - 提现状态记录")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PresentStatusRecordRespVO {
    @Schema(description = "标识")
    private Long presentNo;

    @Schema(description = "状态变更日期，格式：yyyy-MM-dd")
    private String occurredTime;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "状态名称")
    private String statusName;

    public static PresentStatusRecordRespVO build(PresentStatusRecordDO presentStatusRecordDO) {
        return PresentStatusRecordRespVO.builder()
                .presentNo(presentStatusRecordDO.getPresentNo())
                .occurredTime(DateUtil.format(presentStatusRecordDO.getOccurredTime(), NORM_DATETIME_PATTERN))
                .status(presentStatusRecordDO.getStatus())
                .statusName(AccountEnum.getName(presentStatusRecordDO.getStatus()))
                .build();
    }
}
