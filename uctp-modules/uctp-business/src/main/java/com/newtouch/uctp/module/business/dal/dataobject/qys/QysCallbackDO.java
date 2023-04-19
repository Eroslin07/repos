package com.newtouch.uctp.module.business.dal.dataobject.qys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newtouch.uctp.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

/**
 * 契约锁回调日志 DO
 *
 * @author 芋道源码
 */
@TableName("uctp_qys_callback")
//@KeySequence("uctp_qys_callback_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QysCallbackDO extends BaseDO {

    /**
     * 乐观锁
     */
    private Integer revision;
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 契约锁回调类型
     */
    private Integer type;
    /**
     * 契约锁回调内容
     */
    private String content;
    /**
     * 关联业务Id
     */
    private Long mainId;

}
