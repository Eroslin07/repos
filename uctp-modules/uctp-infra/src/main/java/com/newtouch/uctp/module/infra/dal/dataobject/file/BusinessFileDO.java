package com.newtouch.uctp.module.infra.dal.dataobject.file;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newtouch.uctp.framework.tenant.core.db.TenantBaseDO;
import lombok.*;

/**
 * 图片中间表 DO
 *
 * @author qjj
 */
@TableName("uctp_business_file")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessFileDO extends TenantBaseDO {
    /**
     * id(infra_file的id)
     */
    @TableId(type = IdType.INPUT)
    private Long id;

    /**
     * 业务id
     */
    private Long mainId;

    /**
     * 文件类型（1车辆图片 2行驶证 3登记证书 4卖家身份证 5买家身份证  6银行卡 7发票）
     */
    private String fileType;

}
