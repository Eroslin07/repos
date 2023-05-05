package com.newtouch.uctp.module.business.service.configuration;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.admin.configuration.vo.MakeInvoiceRespVO;
import com.newtouch.uctp.module.business.controller.admin.configuration.vo.MakeInvoiceUpdateReqVO;
import com.newtouch.uctp.module.business.controller.admin.contract.vo.*;

import java.util.List;

/**
 * @ClassName RecordService
 * @Author: zhang
 * @Date 2023/4/21.
 */
public interface MakeInvoiceService {
    /** 创建配置信息 */
    public void createMakeInvoice(MakeInvoiceRespVO reqVO);

    /** 编辑配置信息 */
    public void updateMakeInvoice(MakeInvoiceUpdateReqVO reqVO);

    /** 获取配置信息 */
    public MakeInvoiceRespVO getMakeInvoice(Long tenantId);
}
