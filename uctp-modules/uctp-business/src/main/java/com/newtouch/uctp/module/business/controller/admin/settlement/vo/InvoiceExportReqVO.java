package com.newtouch.uctp.module.business.controller.admin.settlement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @ClassName InvoiceExportReqVO
 * @Author: zhang
 * @Date 2023/4/18.
 */

@Schema(description = "结算中心 - 发票管理")
@Data
public class InvoiceExportReqVO {
    @Schema(description = "商户", example = "张三二手车")
    private String merchant;
    @Schema(description = "发票类型", example = "1")
    private Integer type;
    @Schema(description = "发票状态", example = "1")
    private Integer status;
}
