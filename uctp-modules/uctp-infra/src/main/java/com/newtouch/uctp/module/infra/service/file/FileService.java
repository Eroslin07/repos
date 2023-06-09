package com.newtouch.uctp.module.infra.service.file;

import java.util.List;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.infra.api.file.dto.FileDTO;
import com.newtouch.uctp.module.infra.controller.admin.file.vo.file.FilePageReqVO;
import com.newtouch.uctp.module.infra.dal.dataobject.file.FileDO;

/**
 * 文件 Service 接口
 *
 * @author 芋道源码
 */
public interface FileService {

    /**
     * 获得文件分页
     *
     * @param pageReqVO 分页查询
     * @return 文件分页
     */
    PageResult<FileDO> getFilePage(FilePageReqVO pageReqVO);

    /**
     * 保存文件，并返回文件的访问路径
     *
     * @param name 原文件名称
     * @param path 文件路径
     * @param content 文件内容
     * @return 文件路径
     */
    FileDTO createFile(String name, String path, byte[] content);

    /**
     * 保存文件new
     * @param name
     * @param path
     * @param content
     * @return
     */
    FileDO createFileNew(String name, String path, byte[] content);

    FileDO uploadReport(String name, String path, byte[] content,Long carId);

    /**
     * 删除文件
     *
     * @param id 编号
     */
    void deleteFile(Long id) throws Exception;

    /**
     * 删除文件
     *
     * @param id 编号
     */
    void deleteFileNew(Long id) throws Exception;

    void deleteReport(Long id,Long carId) throws Exception;

    /**
     * 获得文件内容
     *
     * @param configId 配置编号
     * @param path 文件路径
     * @return 文件内容
     */
    byte[] getFileContent(Long configId, String path) throws Exception;

    /**
     * 获取文件列表
     * @param ids
     * @return
     */
    List<FileDO> getFileList(List<Long> ids);

    FileDO getFileInfoById(Long fileId);
}
