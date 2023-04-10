package com.newtouch.uctp.module.business.controller.app.carInfo;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppMyCostVO;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.CostExample;
import com.newtouch.uctp.module.business.domain.app.NoticeInfoDO;
import com.newtouch.uctp.module.business.service.CostService;
import com.newtouch.uctp.module.business.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.util.List;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;

@RestController
@RequestMapping("/uctp/car-notice")
@Validated
public class AppNoticenfoController {

    @Resource
    private NoticeService noticeService;


    private static final Logger log = LoggerFactory.getLogger(AppNoticenfoController.class);

    @PostMapping("/getNotices")
    @Operation(summary = "获得消息信息")
    public CommonResult<List<NoticeInfoDO>> getNotices() {
        List<NoticeInfoDO> pageResult = noticeService.getNotices();
        return success(pageResult);
    }

}
