package com.newtouch.uctp.module.business.service;

import com.newtouch.uctp.module.business.dal.dataobject.BusinessFileDO;
import com.newtouch.uctp.module.infra.api.file.dto.FileRespDTO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 车辆主表 Service 接口
 *
 * @author lc
 */
@Service
public interface BusinessFileService {
    /**
     * 保存图片关联信息
     * @return
     */
    int insert(BusinessFileDO fileDO);

    /**
     * 获取业务对应的文件
     * @param id 业务Id
     * @param type 类型
     * @return
     */
    List<FileRespDTO> getFileByMainId(Long id, String type);
}
