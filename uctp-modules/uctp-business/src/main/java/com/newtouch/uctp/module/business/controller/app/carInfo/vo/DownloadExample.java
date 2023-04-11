package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
* 下载 example
*/
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DownloadExample {


    @Schema(description = "远程url")
    private String url;

    @Schema(description = "文件名称")
    private String name;

    @Schema(description = "类型(如：pdf,word,png,jpg等等)")
    private String type;

}
