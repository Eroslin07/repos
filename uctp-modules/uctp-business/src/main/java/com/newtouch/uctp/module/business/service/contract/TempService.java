package com.newtouch.uctp.module.business.service.contract;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.admin.contract.vo.*;



import java.util.List;

/**
 * @ClassName TempService
 * @Author: zhang
 * @Date 2023/4/20.
 */
public interface TempService {
    /** 获取合同模板分页 */
    PageResult<TempRespVO> getTempPage(TempReqVO pageVO);

    /** 修改合同模板 */
    public void updateTemp(TempUpdateReqVO reqVO);

    /** 获取合同模板 */
    public TempRespVO getTemp(Long id);

    /** 刪除合同模板 */
    public void deleteTemp(Long id);

    /** 导出合同模板 */
    List<TempExcelVO> exportTemp(TempExportReqVO reqVO);
}
