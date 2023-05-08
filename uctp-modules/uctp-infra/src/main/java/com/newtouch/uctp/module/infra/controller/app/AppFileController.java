package com.newtouch.uctp.module.infra.controller.app;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.common.util.servlet.ServletUtils;
import com.newtouch.uctp.framework.operatelog.core.annotations.OperateLog;
import com.newtouch.uctp.module.infra.controller.admin.file.vo.file.FilePageReqVO;
import com.newtouch.uctp.module.infra.controller.admin.file.vo.file.FileRespVO;
import com.newtouch.uctp.module.infra.controller.admin.file.vo.file.FileUploadReqVO;
import com.newtouch.uctp.module.infra.convert.file.FileConvert;
import com.newtouch.uctp.module.infra.dal.dataobject.file.FileDO;
import com.newtouch.uctp.module.infra.service.file.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;

@Tag(name =  "管理后台 - 文件存储")
@RestController
@RequestMapping("/infra/file")
@Validated
@Slf4j
public class AppFileController {

    @Resource
    private FileService fileService;

    @PostMapping("/upload")
    @Operation(summary = "上传文件")
    @OperateLog(logArgs = false) // 上传文件，没有记录操作日志的必要
    public CommonResult<List> uploadFile(@RequestParam("file") MultipartFile[] files) throws Exception {
        System.out.println(files);
        ArrayList<Object> list = new ArrayList<>();
        for (MultipartFile file : files) {
            FileDO fileDO = fileService.createFileNew(file.getOriginalFilename(), "", IoUtil.readBytes(file.getInputStream()));
            list.add(fileDO);
        }
        return success(list);
    }

    @PostMapping("/uploadReport")
    @Operation(summary = "上传检测报告")
    @OperateLog(logArgs = false) // 上传文件，没有记录操作日志的必要
    public CommonResult<List> uploadReport(@RequestParam("file") MultipartFile[] files,@RequestParam("carId") Long carId) throws Exception {
        System.out.println(files);
        ArrayList<Object> list = new ArrayList<>();
        for (MultipartFile file : files) {
            FileDO fileDO = fileService.uploadReport(file.getOriginalFilename(), "", IoUtil.readBytes(file.getInputStream()),carId);
            list.add(fileDO);
        }
        return success(list);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除文件")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteFile(@RequestParam("id") Long id) throws Exception {
        fileService.deleteFile(id);
        return success(true);
    }

    @DeleteMapping("/deleteReport")
    @Operation(summary = "删除检测报告")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteReport(@RequestParam("id") Long id,@RequestParam("carId") Long carId) throws Exception {
        fileService.deleteReport(id,carId);
        return success(true);
    }

    @GetMapping("/{configId}/get/**")
    @PermitAll
    @Operation(summary = "下载文件")
    @Parameter(name = "configId", description = "配置编号",  required = true)
    public void getFileContent(HttpServletRequest request,
                               HttpServletResponse response,
                               @PathVariable("configId") Long configId) throws Exception {
        // 获取请求的路径
        String path = StrUtil.subAfter(request.getRequestURI(), "/get/", false);
        if (StrUtil.isEmpty(path)) {
            throw new IllegalArgumentException("结尾的 path 路径必须传递");
        }

        // 读取内容
        byte[] content = fileService.getFileContent(configId, path);
        if (content == null) {
            log.warn("[getFileContent][configId({}) path({}) 文件不存在]", configId, path);
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }
        ServletUtils.writeAttachment(response, path, content);
    }

    @GetMapping("/page")
    @Operation(summary = "获得文件分页")
    @PreAuthorize("@ss.hasPermission('infra:file:query')")
    public CommonResult<PageResult<FileRespVO>> getFilePage(@Valid FilePageReqVO pageVO) {
        PageResult<FileDO> pageResult = fileService.getFilePage(pageVO);
        return success(FileConvert.INSTANCE.convertPage(pageResult));
    }

}
