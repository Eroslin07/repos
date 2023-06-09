package com.newtouch.uctp.module.bpm.dal.dataobject.car;

import lombok.*;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.newtouch.uctp.framework.tenant.core.db.TenantBaseDO;

@TableName(value = "uctp_car_info_details",autoResultMap = true)
//@KeySequence("uctp_car_info_details_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarInfoDetailsDO extends TenantBaseDO {
    /**
     * 乐观锁
     */
    private Integer revision;
    /**
     * id
     */
    @JsonSerialize(using= ToStringSerializer.class)
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 车辆主表id
     */
    @JsonSerialize(using= ToStringSerializer.class)
    private Long carId;
    /**
     * 里程数,保留2位小数
     */
    private BigDecimal mileage;

    /**
     * 运营性质
     */
    private String natureOfOperat;

    /**
     * 颜色
     */
    private String colour;

    /**
     * 首次/初次登记日期
     */
    private String firstRegistDate;
    /**
     * 行驶证编号
     */
    private String drivingLicense;
    /**
     * 登记证书编号
     */
    private String certificateNo;
    /**
     * 能源类型(1燃油 2新能源 3混动)
     */
    private Integer energyType;
    /**
     * 是否第三方代收
     */
    private String collection;
    /**
     * 卖家名字
     */
    private String sellerName;
    /**
     * 卖家身份证
     */
    private String sellerIdCard;
    /**
     * 卖家电话
     */
    private String sellerTel;

    /**
     * 卖家地址
     */
    private String sellerAdder;

    /**
     * 收车时付款方式
     */
    private String payType;

    /**
     * 卖家银行开户行
     */
    private String bankName;

    /**
     * 卖家银行卡号
     */
    private String bankCard;

    /**
     * 第三方卖家信息
     */
    private String thirdSellerName;

    /**
     * 第三方银行卡号
     */
    private String thirdBankCard;

    /**
     * 收车时收款方式
     */
    private String remitType;
    /**
     * 买家电话
     */
    private String buyerTel;
    /**
     * 买家身份证
     */
    private String buyerIdCard;
    /**
     * 买家姓名
     */
    private String buyerName;
    /**
     * 买家地址
     */
    private String buyerAdder;
    /**
     * 转入地车辆管理所名称（收车）
     */
    private String transManageName;
    /**
     * 转入地车辆管理所名称（卖车）
     */
    private String sellTransManageName;
    /**
     * 车辆手续及备件（收车）
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private ProceduresAndSpareParts proceduresAndSpareParts;
    /**
     * 车辆手续及备件（卖车）
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private ProceduresAndSpareSell proceduresAndSpareSell;
    /**
     * 其他费用及约定
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private FeesAndCommitments feesAndCommitments;
    /**
     * 车况相关
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private VehicleProblem vehicleProblem;
}
