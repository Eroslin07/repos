package com.newtouch.uctp.module.business.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.business.dal.dataobject.BusinessFileDO;
import com.newtouch.uctp.module.business.dal.mysql.BusinessFileMapper;
import com.newtouch.uctp.module.business.service.BusinessFileService;
import com.newtouch.uctp.module.infra.api.file.FileApi;
import com.newtouch.uctp.module.infra.api.file.dto.FileRespDTO;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.newtouch.uctp.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.newtouch.uctp.module.infra.enums.ErrorCodeConstants.FILE_API_IS_FAIL;

/**
 * 图片中间表 Service 实现类
 *
 * @author qjj
 */
@Service
@Validated
public class BusinessFileServiceImpl implements BusinessFileService {

    @Resource
    private BusinessFileMapper businessFileMapper;
    @Resource
    private FileApi fileApi;


    @Override
    public int insert(BusinessFileDO fileDO) {
        return businessFileMapper.insert(fileDO);
    }

    @Override
    public List<FileRespDTO> getFileByMainId(Long mainId, String type) {
        List<BusinessFileDO> list = businessFileMapper.getFileByMainId(mainId, type);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        List<Long> ids = list.stream().map(item -> item.getId()).collect(Collectors.toList());
        CommonResult<List<FileRespDTO>> res = fileApi.fileList(ids);
        if (!res.getCode().equals(0)) {
            throw exception(FILE_API_IS_FAIL);
        }
        Map<Long, String> map = list.stream().collect(Collectors.toMap(BusinessFileDO::getId, BusinessFileDO::getFileType));
        List<FileRespDTO> dataList = res.getData();
        dataList.forEach(item->{
            if (map.containsKey(item.getId())) {
                item.setFileType(map.get(item.getId()));
            }
        });
        return dataList;
    }

}
