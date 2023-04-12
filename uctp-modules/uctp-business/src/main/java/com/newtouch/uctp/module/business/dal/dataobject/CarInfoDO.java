package com.newtouch.uctp.module.business.dal.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newtouch.uctp.framework.tenant.core.db.TenantBaseDO;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 车辆主表 DO
 *
 * @author lc
 */
@TableName("uctp_car_info")
//@KeySequence("uctp_car_info_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarInfoDO extends TenantBaseDO {
    /**
     * 车辆主表id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 车架号
     */
    private String vin;
    /**
     * 车辆品牌
     */
    private String brand;
    /**
     * 车辆年份
     */
    private String year;
    /**
     * 车辆型号
     */
    private String model;

    /**
     * 车牌号
     */
    private String plateNum;

    /**
     * 发动机编号
     */
    private String engineNum;
    /**
     * 收车金额
     */
    private BigDecimal vehicleReceiptAmount;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 乐观锁
     */
    private Integer revision;
    /**
     * 售卖状态
     */
    private Integer salesStatus;
    /**
     * 检测状态
     */
    private Integer checkStatus;
    /**
     * 商户id
     */
    private Long businessId;
    /**
     * 收车时间
     */
    private LocalDateTime pickUpTime;
    /**
     * 售卖时间
     */
    private LocalDateTime salesTime;
    /**
     * 卖车方式
     */
    private Integer paymentType;
    /**
     * 卖车金额
     */
    private BigDecimal sellAmount;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 卖车方式
     */
    private Integer sellType;
    /**
     * 使用年限至
     */
    private LocalDateTime scrapDate;
    /**
     * 年检签证有效期至
     */
    private LocalDateTime annualInspectionDate;
    /**
     * 保险险种
     */
    private String insurance;
    /**
     * 保险期至
     */
    private String insuranceEndData;
    /**
     * 其他
     */
    private String other;
}
