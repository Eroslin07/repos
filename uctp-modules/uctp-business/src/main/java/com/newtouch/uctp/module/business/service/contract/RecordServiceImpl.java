package com.newtouch.uctp.module.business.service.contract;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.mybatis.core.util.MyBatisUtils;
import com.newtouch.uctp.module.business.controller.admin.contract.vo.RecordExport;
import com.newtouch.uctp.module.business.controller.admin.contract.vo.RecordExportReqVO;
import com.newtouch.uctp.module.business.controller.admin.contract.vo.RecordReqVO;
import com.newtouch.uctp.module.business.controller.admin.contract.vo.RecordRespVO;
import com.newtouch.uctp.module.business.dal.mysql.contract.RecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName RecordServiceImpl
 * @Author: zhang
 * @Date 2023/4/21.
 */
@Slf4j
@Transactional(readOnly = false)
@Service
public class RecordServiceImpl implements RecordService{
    @Resource
    private RecordMapper recordMapper;

    @Override
    public PageResult<RecordRespVO> getRecordPage(RecordReqVO pageVO) {
        Page<RecordRespVO> page = MyBatisUtils.buildPage(pageVO);
        recordMapper.getRecordPage(page, pageVO);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public List<RecordExport> exportRecord(RecordExportReqVO reqVO) {
        List<RecordExport> recordExports = recordMapper.recordList(reqVO);
        return recordExports;
    }
}
