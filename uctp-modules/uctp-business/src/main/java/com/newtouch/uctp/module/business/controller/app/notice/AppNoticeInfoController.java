package com.newtouch.uctp.module.business.controller.app.notice;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.bpm.controller.admin.form.vo.BpmFormMainVO;
import com.newtouch.uctp.module.business.controller.app.notice.vo.NoticeReqVO;
import com.newtouch.uctp.module.business.controller.app.notice.vo.NoticeVO;
import com.newtouch.uctp.module.business.dal.dataobject.NoticeInfoDO;
import com.newtouch.uctp.module.business.service.NoticeService;
import com.newtouch.uctp.module.business.util.ListUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;

@RestController
@RequestMapping("/uctp/car-notice")
@Validated
public class AppNoticeInfoController {

    @Resource
    private NoticeService noticeService;


    //private static final Logger log = LoggerFactory.getLogger(AppNoticenfoController.class);

    @GetMapping ("/getNotices")
    @Operation(summary = "获得消息信息")
    @Parameter(name = "businessID", description = "商户ID", required = true, example = "1234")
    public CommonResult<List<NoticeInfoDO>> getNotices(@RequestParam("businessID") String businessID) {
        List<NoticeInfoDO> pageResult = noticeService.getNotices(businessID);
        return success(pageResult);
    }

    @PostMapping ("/updateNoticeStatus")
    @Operation(summary = "单个更新消息状态")
    public void updateNoticeStatus(@RequestBody NoticeVO noticeVO) {
        noticeService.updateNoticeStatus(noticeVO);
    }

    @PostMapping ("/updateBatchNoticeStatus")
    @Operation(summary = "批量更新消息状态")
    public void updateAllNoticeStatus(@RequestBody List<String> list) {

        noticeService.updateAllNoticeStatus(list);

    }




    @PostMapping ("/deleteBatchNoticeStatus")
    @Operation(summary = "批量删除消息/目前只考虑单个删除")
    public void deleteBatchNoticeStatus(@RequestBody List<String> list) {

        noticeService.deleteAllNoticeStatus(list);


    }

    @PostMapping("/saveNotice")
    @Operation(summary = "保存消息")
    public CommonResult saveNotice(@RequestBody Map<String,String> infoVO){
        String result = noticeService.saveNotice(infoVO);
        return success(result);
    }
    @PostMapping("/saveTaskNotice")
    @Operation(summary = "保存消息")
    public CommonResult saveTaskNotice(@RequestParam String type,@RequestParam String contentType,@RequestParam String reason,@RequestBody BpmFormMainVO infoVO){
        String result = noticeService.saveTaskNotice(type,contentType,reason,infoVO);
        return success(result);
    }

    @GetMapping ("/getUnreadNoticeCount")
    @Operation(summary = "获取未读消息条数")
    @Parameter(name = "businessID", description = "商户ID", required = true, example = "1234")
    public CommonResult getUnreadNoticeCount(@RequestParam String businessID){
        int count = noticeService.getUnreadNoticeCount(businessID);
        return success(count);
    }

}
