package com.newtouch.uctp.module.business.controller.app.account.vo;

import com.newtouch.uctp.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "资金管理 - 利润查询")
@Data
public class ProfitQueryReqVO extends PageParam {
    @Schema(description = "查询明细类型：1：利润明细 2：冻结明细 3：支出 4：收入 默认：1")
    private int type = 1;
}
