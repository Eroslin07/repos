package com.newtouch.uctp.module.bpm.service.car.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

import com.newtouch.uctp.module.infra.api.file.dto.FileRespDTO;

/**
 * @author helong
 * @date 2023/4/21 17:23
 */
@Data
@ToString(callSuper = true)
public class AppSimpleFileDTO implements Serializable {
    @Schema(description = "系统文件主表id")
    private Long id;
    @Schema(description = "系统文件path")
    private String path;
    @Schema(description = "系统文件url")
    private String url;

    public AppSimpleFileDTO(FileRespDTO dto) {
        this.id = dto.getId();
        this.path = dto.getPath();
        this.url = dto.getUrl();
    }
}
