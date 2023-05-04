package com.newtouch.uctp.module.infra.api.file;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.infra.api.file.dto.FileCreateReqDTO;
import com.newtouch.uctp.module.infra.api.file.dto.FileDTO;
import com.newtouch.uctp.module.infra.api.file.dto.FileRespDTO;
import com.newtouch.uctp.module.infra.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = ApiConstants.NAME) // TODO 芋艿：fallbackFactory =
@Tag(name =  "RPC 服务 - 文件")
public interface FileApi {

    String PREFIX = ApiConstants.PREFIX + "/file";

    /**
     * 保存文件，并返回文件的访问路径
     *
     * @param content 文件内容
     * @return 文件路径
     */
    default String createFile(byte[] content) {
        return createFile(null, null, content);
    }

    /**
     * 保存文件，并返回文件的访问路径
     *
     * @param path 文件路径
     * @param content 文件内容
     * @return 文件路径
     */
    default String createFile(String path, byte[] content) {
        return createFile(null, path, content);
    }

    /**
     * 保存文件，并返回文件的访问路径
     *
     * @param name 原文件名称
     * @param path 文件路径
     * @param content 文件内容
     * @return 文件路径
     */
    default String createFile(@RequestParam("name") String name,
                              @RequestParam("path") String path,
                              @RequestParam("content") byte[] content) {
        return createFile(new FileCreateReqDTO().setName(name).setPath(path).setContent(content)).getCheckedData().getUrl();
    }

    @PostMapping(PREFIX + "/create")
    @Operation(summary = "保存文件，并返回文件的访问路径")
    CommonResult<FileDTO> createFile(@Valid @RequestBody FileCreateReqDTO createReqDTO);



    @PostMapping(PREFIX + "/list")
    @Operation(summary = "获取文件列表")
    CommonResult<List<FileRespDTO>> fileList(@Valid @RequestParam(value = "ids") List<Long> ids);


    @PostMapping(PREFIX + "/createNew")
    @Operation(summary = "保存文件，并返回文件实体")
    CommonResult<FileDTO>  createFileNew(@Valid @RequestBody FileCreateReqDTO createReqDTO);




}