package com.newtouch.uctp.module.business.dal.mysql.contract;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newtouch.uctp.module.business.controller.admin.contract.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName RecordMapper
 * @Author: zhang
 * @Date 2023/4/21.
 */
@Mapper
public interface RecordMapper {
    Page<RecordRespVO> getRecordPage(Page<RecordRespVO> page, @Param("reqVO") RecordReqVO pageVO);

    List<RecordExport> recordList(@Param("reqVO") RecordExportReqVO reqVO);
}
