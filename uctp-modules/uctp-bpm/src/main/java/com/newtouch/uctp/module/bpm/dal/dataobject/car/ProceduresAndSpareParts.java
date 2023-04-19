package com.newtouch.uctp.module.bpm.dal.dataobject.car;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;

/**
 * 车辆手续及备件
 */
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProceduresAndSpareParts implements Serializable {
    /**
     * 是否有行驶证正、副本
     */
    @Schema(description = "是否有行驶证正、副本")
    private Boolean drivingLicense;
    /**
     * 是否有购车发票
     */
    @Schema(description = "是否有购车发票")
    private Boolean carInvoice;
    /**
     * 是否有机动车登记证
     */
    @Schema(description = "是否有机动车登记证")
    private Boolean registrationCertificate;
    /**
     * 是否有购置税完税凭证
     */
    @Schema(description = "是否有购置税完税凭证")
    private Boolean purchaseTax;
    /**
     * 是否有备胎
     */
    @Schema(description = "是否有备胎")
    private Boolean spareTire;
    /**
     * 是否有车船使用税完税凭证
     */
    @Schema(description = "是否有车船使用税完税凭证")
    private Boolean carShipTax;
    /**
     * 是否有交强险保单
     */
    @Schema(description = "是否有交强险保单")
    private Boolean heavyTrafficInsurance;
    /**
     * 是否有商业险保单
     */
    @Schema(description = "是否有商业险保单")
    private Boolean commercialInsurance;
    /**
     * 是否有千斤顶
     */
    @Schema(description = "是否有千斤顶")
    private Boolean jack;
    /**
     * 是否有说明书
     */
    @Schema(description = "是否有说明书")
    private Boolean specification;
    /**
     * 钥匙数量
     */
    @Schema(description = "钥匙数量")
    private Integer vehicleKey;
    /**
     * 其他
     */
    @Schema(description = "其他")
    private String accidentVehicle;
}
