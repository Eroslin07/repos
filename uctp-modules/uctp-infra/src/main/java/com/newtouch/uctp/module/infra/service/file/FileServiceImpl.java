package com.newtouch.uctp.module.infra.service.file;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.common.util.io.FileUtils;
import com.newtouch.uctp.framework.file.core.client.FileClient;
import com.newtouch.uctp.framework.file.core.utils.FileTypeUtils;
import com.newtouch.uctp.module.infra.api.file.dto.FileDTO;
import com.newtouch.uctp.module.infra.controller.admin.file.vo.file.FilePageReqVO;
import com.newtouch.uctp.module.infra.dal.dataobject.file.BusinessFileDO;
import com.newtouch.uctp.module.infra.dal.dataobject.file.FileDO;
import com.newtouch.uctp.module.infra.dal.mysql.file.BusinessFileMapper;
import com.newtouch.uctp.module.infra.dal.mysql.file.FileMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

import static com.newtouch.uctp.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.newtouch.uctp.module.infra.enums.ErrorCodeConstants.FILE_NOT_EXISTS;

/**
 * 文件 Service 实现类
 *
 * @author 芋道源码
 */
@Service
public class FileServiceImpl implements FileService {

    @Resource
    private FileConfigService fileConfigService;

    @Resource
    private FileMapper fileMapper;

    @Resource
    private BusinessFileMapper businessFileMapper;

    @Override
    public PageResult<FileDO> getFilePage(FilePageReqVO pageReqVO) {
        return fileMapper.selectPage(pageReqVO);
    }

    @Override
    @SneakyThrows
    public FileDTO createFile(String name, String path, byte[] content) {
        // 计算默认的 path 名
        String type = FileTypeUtils.getMineType(content, name);
        if (StrUtil.isEmpty(path)) {
            path = FileUtils.generatePath(content, name);
        }
        // 如果 name 为空，则使用 path 填充
        if (StrUtil.isEmpty(name)) {
            name = path;
        }

        // 上传到文件存储器
        FileClient client = fileConfigService.getMasterFileClient();
        Assert.notNull(client, "客户端(master) 不能为空");
        String url = client.upload(content, path, type);

        // 保存到数据库
        FileDO file = new FileDO();
        FileDTO fileDTO = new FileDTO();
        file.setConfigId(client.getId());
        file.setName(name);
        file.setPath(path);
        file.setUrl(url);
        file.setType(type);
        file.setSize(content.length);
        fileMapper.insert(file);
        fileDTO.setConfigId(client.getId());
        fileDTO.setName(name);
        fileDTO.setPath(path);
        fileDTO.setUrl(url);
        fileDTO.setType(type);
        fileDTO.setSize(content.length);
        fileDTO.setId(file.getId());
        return fileDTO;
    }


    @Override
    @SneakyThrows
    public FileDO createFileNew(String name, String path, byte[] content) {
        // 计算默认的 path 名
        String type = FileTypeUtils.getMineType(content, name);
        if (StrUtil.isEmpty(path)) {
            path = FileUtils.generatePath(content, name);
        }
        // 如果 name 为空，则使用 path 填充
        if (StrUtil.isEmpty(name)) {
            name = path;
        }

        // 上传到文件存储器
        FileClient client = fileConfigService.getMasterFileClient();
        Assert.notNull(client, "客户端(master) 不能为空");
        String url = client.upload(content, path, type);

        // 保存到数据库
        FileDO file = new FileDO();
        file.setConfigId(client.getId());
        file.setName(name);
        file.setPath(path);
        file.setUrl(url);
        file.setType(type);
        file.setSize(content.length);
        fileMapper.insert(file);
        return file;
    }


    @Override
    @SneakyThrows
    public FileDO uploadReport(String name, String path, byte[] content,Long carId) {
        // 计算默认的 path 名
        String type = FileTypeUtils.getMineType(content, name);
        if (StrUtil.isEmpty(path)) {
            path = FileUtils.generatePath(content, name);
        }
        // 如果 name 为空，则使用 path 填充
        if (StrUtil.isEmpty(name)) {
            name = path;
        }

        // 上传到文件存储器
        FileClient client = fileConfigService.getMasterFileClient();
        Assert.notNull(client, "客户端(master) 不能为空");
        String url = client.upload(content, path, type);

        // 保存到数据库
        FileDO file = new FileDO();
        file.setConfigId(client.getId());
        file.setName(name);
        file.setPath(path);
        file.setUrl(url);
        file.setType(type);
        file.setSize(content.length);
        fileMapper.insert(file);

        BusinessFileDO businessFileDO = new BusinessFileDO();
        businessFileDO.setId(file.getId());
        businessFileDO.setMainId(carId);
        businessFileDO.setFileType("14");
        businessFileMapper.insert(businessFileDO);
        fileMapper.updateCarInfoStatus(2,23,231,carId);
        return file;
    }


    @Override
    public void deleteFile(Long id) throws Exception {
        // 校验存在
        FileDO file = validateFileExists(id);

        // 从文件存储器中删除
        FileClient client = fileConfigService.getFileClient(file.getConfigId());
        Assert.notNull(client, "客户端({}) 不能为空", file.getConfigId());
        client.delete(file.getPath());

        // 删除记录
        fileMapper.deleteById(id);
    }

    private FileDO validateFileExists(Long id) {
        FileDO fileDO = fileMapper.selectById(id);
        if (fileDO == null) {
            throw exception(FILE_NOT_EXISTS);
        }
        return fileDO;
    }

    @Override
    public byte[] getFileContent(Long configId, String path) throws Exception {
        FileClient client = fileConfigService.getFileClient(configId);
        Assert.notNull(client, "客户端({}) 不能为空", configId);
        return client.getContent(path);
    }

    @Override
    public List<FileDO> getFileList(List<Long> ids) {
        return fileMapper.selectList("id", ids);
    }

}
