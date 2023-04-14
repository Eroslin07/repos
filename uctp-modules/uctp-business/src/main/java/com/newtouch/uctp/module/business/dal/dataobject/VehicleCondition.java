package com.newtouch.uctp.module.business.dal.dataobject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;

/**
 * 车况相关
 */
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleCondition implements Serializable {
    //--------------------租金相关-------------------------------
    @Schema(description = "里程表未被蓄意改动")
    private Boolean conditionA;
    @Schema(description = "事故车")
    private Boolean conditionB;
    @Schema(description = "泡水车")
    private Boolean conditionC;
    @Schema(description = "火烧车")
    private Boolean conditionD;

}
