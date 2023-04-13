package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

import com.newtouch.uctp.module.infra.api.file.dto.FileRespDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 简单文件VO
 */
@Data
@ToString(callSuper = true)
public class AppSimpleFileVO implements Serializable {
    @Schema(description = "系统文件主表id")
    private Long id;
    @Schema(description = "系统文件path")
    private String path;
    @Schema(description = "系统文件url")
    private String url;

    public AppSimpleFileVO(FileRespDTO dto) {
        this.id = dto.getId();
        this.path = dto.getPath();
        this.url = dto.getUrl();
    }
}
