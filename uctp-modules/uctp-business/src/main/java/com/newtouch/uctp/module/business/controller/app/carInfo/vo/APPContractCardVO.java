package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.newtouch.uctp.framework.tenant.core.db.TenantBaseDO;
import com.newtouch.uctp.module.business.dal.dataobject.ContractDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@ToString(callSuper = true)
public class APPContractCardVO {
    @Schema(description = "合同主表")
    private ContractDO contractDO;

    @Schema(description = "文件名")
    private String path;

    @Schema(description = "文件url")
    private String url;
}
