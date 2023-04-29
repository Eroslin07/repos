package com.newtouch.uctp.module.business.controller.admin.contract.vo;
import com.newtouch.uctp.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @ClassName TempReqVO
 * @Author: zhang
 * @Date 2023/4/20.
 */
@Schema(description = "合同管理 - 合同模板分页项 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TempReqVO extends PageParam {
    @Schema(description = "模板名称")
    private String name;

    @Schema(description = "操作人")
    private String createdBy;



}
