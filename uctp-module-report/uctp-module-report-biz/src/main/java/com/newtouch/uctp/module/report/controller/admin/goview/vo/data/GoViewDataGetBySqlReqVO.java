package com.newtouch.uctp.module.report.controller.admin.goview.vo.data;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

//@Schema(description = "管理后台 - GoView 使用 SQL 查询数据 Request VO")
@Data
public class GoViewDataGetBySqlReqVO {

//    @Schema(description = "SQL 语句", required = true, example = "SELECT * FROM user")
    @NotEmpty(message = "SQL 语句不能为空")
    private String sql;

}
