package com.newtouch.uctp.module.business.api.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Schema(description = "RPC 服务 - 文件创建 Request DTO")
@Data
public class FileInsertReqDTO {

    @Schema(description = "图片url")
    private List<String> Url;

    @Schema(description = "图片类型")
    private String type;

    @Schema(description = "主表id")
    private Long mainId;

    @Schema(description = "主表id")
    private Long tenantId;
}
