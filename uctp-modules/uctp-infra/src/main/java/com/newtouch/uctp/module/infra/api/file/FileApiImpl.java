package com.newtouch.uctp.module.infra.api.file;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.infra.api.file.dto.FileCreateReqDTO;
import com.newtouch.uctp.module.infra.api.file.dto.FileDTO;
import com.newtouch.uctp.module.infra.api.file.dto.FileRespDTO;
import com.newtouch.uctp.module.infra.convert.file.FileConvert;
import com.newtouch.uctp.module.infra.dal.dataobject.file.FileDO;
import com.newtouch.uctp.module.infra.service.file.FileService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;
import static com.newtouch.uctp.module.system.enums.ApiConstants.VERSION;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@DubboService(version = VERSION) // 提供 Dubbo RPC 接口，给 Dubbo Consumer 调用
@Validated
public class FileApiImpl implements FileApi {

    @Resource
    private FileService fileService;

    @Override
    public CommonResult<FileDTO> createFile(FileCreateReqDTO createReqDTO) {
        return success(fileService.createFile(createReqDTO.getName(), createReqDTO.getPath(),
                createReqDTO.getContent()));
    }

    @Override
    public CommonResult<List<FileRespDTO>> fileList(List<Long> ids) {
        List<FileDO> fileList = fileService.getFileList(ids);
        return success(FileConvert.INSTANCE.convertList(fileList));
    }

    @Override
    public CommonResult<FileDTO> createFileNew(FileCreateReqDTO createReqDTO) {
        FileDTO fileDTO=new FileDTO();
        FileDO fileNew = fileService.createFileNew(createReqDTO.getName(), createReqDTO.getPath(), createReqDTO.getContent());
        fileDTO.setConfigId(fileNew.getConfigId());
        fileDTO.setId(fileNew.getId());
        fileDTO.setCreateTime(fileNew.getCreateTime());
        fileDTO.setCreator(fileNew.getCreator());
        fileDTO.setDeleted(fileNew.getDeleted());
        fileDTO.setName(fileNew.getName());
        fileDTO.setPath(fileNew.getPath());
        fileDTO.setSize(fileNew.getSize());
        fileDTO.setPath(fileNew.getPath());
        fileDTO.setType(fileNew.getType());
        fileDTO.setUpdater(fileNew.getUpdater());
        fileDTO.setUpdateTime(fileNew.getUpdateTime());
        fileDTO.setUrl(fileNew.getUrl());

        return success(fileDTO);
    }

    @Override
    public CommonResult<String> deleteFileNew(@Valid Long id) {
        String result="失败";
        try {
            fileService.deleteFileNew(id);
            result="成功";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return success(result);
    }


}
