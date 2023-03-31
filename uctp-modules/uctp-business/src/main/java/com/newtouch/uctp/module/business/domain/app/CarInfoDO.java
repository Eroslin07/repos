package com.newtouch.uctp.module.business.domain.app;

import lombok.*;
import java.util.*;
import com.baomidou.mybatisplus.annotation.*;
import com.newtouch.uctp.framework.mybatis.core.dataobject.BaseDO;

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
public class CarInfoDO extends BaseDO {

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
    private String style;
    /**
     * 发动机编号
     */
    private String engineNum;
    /**
     * 收车金额
     */
    private String vehicleReceiptAmount;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 乐观锁
     */
    private Integer revision;
    /**
     * 车辆主表id
     */
    @TableId
    private Long id;
    /**
     * 汽车明细id
     */
    private Long carDetailId;
    /**
     * 商户id
     */
    private Long businessId;
    /**
     * 更新时间
     */
    private Date updatedTime;
    /**
     * 售卖状态(0未出售 1出售中 2已出售)
     */
    private Integer salesStatus;
    /**
     * 检测状态(0未检测 1已检测)
     */
    private Integer checkStatus;

}
