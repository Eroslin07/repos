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
     * 批量保存图片关联信息，适用于数据量不大的插入
     * @return
     */
    void insertBatch(List<BusinessFileDO> fileDOList);
    /**
     * 获取业务对应的文件
     * @param mainId 业务Id
     * @return
     */
    List<FileRespDTO> getDTOByMainId(Long mainId);
    /**
     * 获取业务对应的文件
     * @param mainId 业务Id
     * @param fileType 类型
     * @return
     */
    List<FileRespDTO> getDTOByMainIdAndType(Long mainId, String fileType);
    /**
     * 获取业务对应的文件
     * @param mainId 业务Id
     * @return
     */
    List<BusinessFileDO> getByMainId(Long mainId);
    /**
     * 获取业务对应的文件
     * @param mainId 业务Id
     * @param fileType 类型
     * @return
     */
    List<BusinessFileDO> getByMainIdAndType(Long mainId, String fileType);

    /**
     * 删除业务对应的文件
     * @param mainId 业务id
     * @param fileType 类型
     */
    int deleteByMainIdAndType(Long mainId, String fileType);

}
