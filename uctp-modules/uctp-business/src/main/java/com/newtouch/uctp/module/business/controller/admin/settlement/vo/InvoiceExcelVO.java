package com.newtouch.uctp.module.business.controller.admin.settlement.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.newtouch.uctp.framework.excel.core.annotations.DictFormat;
import com.newtouch.uctp.framework.excel.core.convert.DictConvert;
import com.newtouch.uctp.module.system.enums.DictTypeConstants;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName IncoiceExcelVO
 * @Author: zhang
 * @Date 2023/4/18.
 */

@Data
public class InvoiceExcelVO {
    @ExcelProperty("序号")
    private Long id;

    @ExcelProperty(value = "发票类型")
    private String typeName;

    @ExcelProperty("商户")
    private String merchant;

    @ExcelProperty("合同名称")
    private String name;

    @ExcelProperty("合同金额")
    private String amount;

    @ExcelProperty("税额")
    private String tax;

    @ExcelProperty(value = "发票状态")
    private String statusName;

    @ExcelProperty("操作人")
    private String founder;

    @ExcelProperty("创建时间")
    private LocalDateTime creationTime;
}
