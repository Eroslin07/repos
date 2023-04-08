package com.newtouch.uctp.module.business.service.impl;


import com.newtouch.uctp.module.business.dal.dataobject.UctpBusinessFileDO;
import com.newtouch.uctp.module.business.dal.mysql.UctpBusinessFileMapper;
import com.newtouch.uctp.module.business.service.UctpBusinessFileService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
/**
 * 图片中间表 Service 实现类
 *
 * @author qjj
 */
@Service
@Validated
public class UctpBusinessFileServiceImpl implements UctpBusinessFileService {

    @Resource
    private UctpBusinessFileMapper businessFileMapper;

    @Override
    public int insert(UctpBusinessFileDO fileDO) {
        return businessFileMapper.insert(fileDO);
    }
}
