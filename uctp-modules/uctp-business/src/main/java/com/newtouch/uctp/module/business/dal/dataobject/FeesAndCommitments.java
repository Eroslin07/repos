package com.newtouch.uctp.module.business.dal.dataobject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
/**
 * 其他费用及约定
 */
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeesAndCommitments implements Serializable {
    //--------------------租金相关-------------------------------
    @Schema(description = "A-轿车200元/天,B-商务车400元/天,C-豪车1000元/天,D-乙方（平台）无需承担")
    private String vehicle;
//    /**
//     * 轿车200元/天
//     */
//    @Schema(description = "轿车200元/天")
//    private Boolean vehicleA;
//    /**
//     * 商务车400 元/天
//     */
//    @Schema(description = "商务车400 元/天")
//    private Boolean vehicleB;
//    /**
//     * 豪车1000元/天
//     */
//    @Schema(description = "豪车1000元/天")
//    private Boolean vehicleC;
//    /**
//     * 乙方（平台）无需承担
//     */
//    @Schema(description = "乙方（平台）无需承担")
//    private Boolean vehicleD;
//--------------------交易过户费------------------------------
    @Schema(description = "A-销售车辆首次交易过户费(乙方（平台）过户甲方（买方）指定过户人),B-销售车辆首次交易过户费(乙方（平台）过户甲方（买方）指定过户人,C-甲方（买方）无需承担")
    private String transfer;
//    /**
//     * 销售车辆首次交易过户费(乙方（平台）过户甲方（买方）指定过户人)
//     */
//    @Schema(description = "销售车辆首次交易过户费(乙方（平台）过户甲方（买方）指定过户人)")
//    private Boolean transferA;
//    /**
//     * 销售车辆二次交易过户费(该车辆现户主过户丙方（车商）指定过户人);
//     */
//    @Schema(description = "销售车辆二次交易过户费(该车辆现户主过户丙方（车商）指定过户人)")
//    private Boolean transferB;
//    /**
//     * 甲方（买方）无需承担。
//     */
//    @Schema(description = "甲方（买方）无需承担")
//    private Boolean transferC;
    //--------------------车辆折损费用------------------------------
    @Schema(description = "A-依据本协议第二条车辆价款的5%支付车辆折损费用,B-依据本协议第二条车辆价款的5%支付车辆折损费用")
    private String loss;
//    /**
//     * 依据本协议第二条车辆价款的5%支付车辆折损费用。
//     */
//    @Schema(description = "依据本协议第二条车辆价款的5%支付车辆折损费用")
//    private Boolean lossA;
//    /**
//     * 依据本协议第二条车辆价款的5%支付车辆折损费用。
//     */
//    @Schema(description = "依据本协议第二条车辆价款的5%支付车辆折损费用")
//    private Boolean lossB;
    //--------------------第三方检测费用------------------------------
    @Schema(description = "A-全车检测费用,B-乙方（平台）无需承担")
    private String testing;
//    /**
//     * 全车检测费用。
//     */
//    @Schema(description = "全车检测费用")
//    private Boolean testingA;
//    /**
//     * 乙方（平台）无需承担。
//     */
//    @Schema(description = "乙方（平台）无需承担")
//    private Boolean testingB;

}
