package com.newtouch.uctp.module.business.dal.dataobject.profit;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newtouch.uctp.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.time.LocalDateTime;

@TableName("uctp_merchant_profit_invoice")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantProfitInvoiceDO extends BaseDO {

    @TableId
    private Long id; // 主键标识
    private Long profitId; // 利润id
    private Long fileId; // 文件id
    private String fileUrl; // 文件URL
    private LocalDateTime uploadTime; // 上传时间
}
