package com.newtouch.uctp.module.business.dal.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newtouch.uctp.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("uctp_car_info_details")
//@KeySequence("uctp_car_info_details_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarInfoDetailsDO extends BaseDO {
    /**
     * 乐观锁
     */
    private Integer revision;
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 车辆主表id
     */
    private String carId;
    /**
     * 里程数,保留2位小数
     */
    private BigDecimal mileage;
    /**
     * 是/非事故车(1是 0非)
     */
    private Integer accidentVehicle;
    /**
     * 是/非泡水车(1是 0非)
     */
    private Integer soakingCar;
    /**
     * 是/非火烧车(1是 0非)
     */
    private Integer burnCar;
    /**
     * 首次/初次登记日期
     */
    private LocalDateTime firstRegistDate;
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
}