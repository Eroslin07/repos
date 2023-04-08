package com.newtouch.uctp.module.infra.api.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "RPC 服务 - 文件创建 Request DTO")
@Data
public class FileRespDTO {

    @Schema(description = "编号，数据库自增", example = "1")
    private Long id;
    @Schema(description = "配置编号", example = "1")
    private Long configId;
    @Schema(description = "原文件名")
    private String name;
    @Schema(description = "路径，即文件名", example = "xxx.jpg")
    private String path;
    @Schema(description = "访问地址")
    private String url;
    @Schema(description = "文件的 MIME 类型", example = "application/octet-stream")
    private String type;
    @Schema(description = "文件大小")
    private Integer size;
    @Schema(description = "业务类型文件", example = "1车辆图片 2行驶证 3登记证书 4卖家身份证 5买家身份证")
    private String fileType;

}