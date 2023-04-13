package com.newtouch.uctp.module.business.controller.app.notice;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.business.controller.app.notice.vo.NoticeVO;
import com.newtouch.uctp.module.business.dal.dataobject.NoticeInfoDO;
import com.newtouch.uctp.module.business.service.NoticeService;
import com.newtouch.uctp.module.business.util.ListUtil;
import io.swagger.v3.oas.annotations.Operation;
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
public class AppNoticenfoController {

    @Resource
    private NoticeService noticeService;


    //private static final Logger log = LoggerFactory.getLogger(AppNoticenfoController.class);

    @GetMapping ("/getNotices")
    @Operation(summary = "获得消息信息")
    public CommonResult<List<NoticeInfoDO>> getNotices() {
        List<NoticeInfoDO> pageResult = noticeService.getNotices();
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
        Map<String,List<String> >map = ListUtil.groupList(list);

        for (int i = 0; i < map.size(); i++) {
            String ids="";
            for (int a= 0; a < map.get("keyName"+i).size(); a++) {
                ids+="'"+map.get("keyName"+i).get(a)+"',";
            }
            ids+="'#'";
            noticeService.updateAllNoticeStatus(ids);
        }

    }



}
