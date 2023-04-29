package com.newtouch.uctp.module.business.service.contract;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.mybatis.core.util.MyBatisUtils;
import com.newtouch.uctp.module.business.controller.admin.contract.vo.*;
import com.newtouch.uctp.module.business.dal.mysql.contract.TempMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName TempServiceImpl
 * @Author: zhang
 * @Date 2023/4/20.
 */
@Slf4j
@Transactional(readOnly = false)
@Service
public class TempServiceImpl implements TempService {
    @Resource
    private TempMapper tempMapper;

    @Override
    public PageResult<TempRespVO> getTempPage(TempReqVO pageVO) {
        Page<TempRespVO> page = MyBatisUtils.buildPage(pageVO);
        tempMapper.getTempPage(page, pageVO);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public void updateTemp(TempUpdateReqVO reqVO) {
        tempMapper.updateTemp(reqVO);
    }

    @Override
    public TempRespVO getTemp(Long id) {
        return tempMapper.getTemp(id);
    }

    @Override
    public void deleteTemp(Long id) {
        tempMapper.deleteTemp(id);
    }

    @Override
    public List<TempExcelVO> exportTemp(TempExportReqVO reqVO) {
        return tempMapper.tempList(reqVO);
    }
}
