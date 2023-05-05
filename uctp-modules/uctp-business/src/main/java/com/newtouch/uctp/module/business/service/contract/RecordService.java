package com.newtouch.uctp.module.business.service.contract;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.admin.contract.vo.*;

import java.util.List;

/**
 * @ClassName RecordService
 * @Author: zhang
 * @Date 2023/4/21.
 */
public interface RecordService {
    /** 获取合同档案分页 */
    PageResult<RecordRespVO> getRecordPage(RecordReqVO pageVO);

    /** 导出合同模板 */
    List<RecordExport> exportRecord(RecordExportReqVO reqVO);
}
