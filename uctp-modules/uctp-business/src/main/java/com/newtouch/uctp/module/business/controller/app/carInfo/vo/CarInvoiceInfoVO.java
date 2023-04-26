package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

import com.google.common.collect.Lists;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDO;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDetailsDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 流程正反向发票
 * @author qjj
 * @date 2023/4/25 11:00
 */
@Schema(description = "用户 APP - 流程车辆发票 VO")
@Data
@ToString(callSuper = true)
public class CarInvoiceInfoVO implements Serializable {
    @Schema(description = "卖车方姓名")
    private String sellerName;
    @Schema(description = "买方姓名")
    private String buyerName;
    @Schema(description = "发票信息")
    private CarInvoiceDetailVO carInvoiceDetailVO;
    @Schema(description = "收车/卖车合同号，流程生成标题时使用")
    private String contractCode;
    @Schema(description = "收车/卖车合同及合同附件简要信息")
    private List<ContractApprovalShowVO> contractList = Lists.newArrayList();

}
