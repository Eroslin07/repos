package com.newtouch.uctp.module.business.service;

import com.newtouch.uctp.module.business.dal.dataobject.UctpBusinessFileDO;

/**
 * 车辆主表 Service 接口
 *
 * @author lc
 */
public interface UctpBusinessFileService {
    /**
     * 保存图片关联信息
     * @return
     */
    int insert(UctpBusinessFileDO fileDO);
}
