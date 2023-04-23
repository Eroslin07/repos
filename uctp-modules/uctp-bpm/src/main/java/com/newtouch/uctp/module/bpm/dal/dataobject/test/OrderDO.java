package com.newtouch.uctp.module.bpm.dal.dataobject.test;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @author helong
 * @date 2023/4/23 13:48
 */
@TableName(value = "order_tbl", autoResultMap = true)
@Data
@ToString(callSuper = true)
public class OrderDO {
    @TableId
    private Integer id;

    private String userId;

    private String commodityCode;

    private Integer count;

    private BigDecimal money;

}
