package com.newtouch.uctp.module.business.controller.app.carInfo;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppCarInfoAndDetailVO;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppMyCostVO;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.CostExample;
import com.newtouch.uctp.module.business.service.CostService;
import com.newtouch.uctp.module.business.util.DownLoadUtils;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.io.IOUtils;
import org.apache.dubbo.common.utils.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.*;
import java.net.URL;
import java.util.List;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;

@RestController
@RequestMapping("/uctp/car-cost")
@Validated
public class AppCostInfoController {

    @Resource
    private CostService costService;


    private static final Logger log = LoggerFactory.getLogger(AppCostInfoController.class);

    @PostMapping("/getMyCosts")
    @Operation(summary = "获得我的费用信息")
    //public CommonResult<AppMyCostVO> getMyCosts(@PathVariable String brand,@PathVariable String year,@PathVariable String mon) {
    public CommonResult<List<AppMyCostVO>> getMyCosts(@RequestBody  CostExample example) {
       // CostExample example=new CostExample();
        //example.setBrand(brand);
        //example.setYear(year);
        //example.setMon(mon);
        List<AppMyCostVO> pageResult = costService.getMyCosts(example);
        return success(pageResult);
    }


    @RequestMapping(value = "/preview")
    public void showPdf(HttpServletResponse response,@PathVariable String filePaths) {
        try {
            //String filePath = this.getClass().getClassLoader().getResource("../../static/pdf/readme.pdf").getPath();
            String filePath = "/Users/huangr/newtouch/测试.pdf";
            File file = new File(filePath);
            FileInputStream fileInputStream = new FileInputStream(file);
            response.setHeader("Content-Type", "application/pdf");
            OutputStream outputStream = response.getOutputStream();
            IOUtils.write(IOUtils.toByteArray(fileInputStream), outputStream);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    @ResponseBody
    @RequestMapping("/preview2")
    public void preview(HttpServletResponse response, boolean flag) {
        //String filePath = this.getClass().getClassLoader().getResource("../../static/pdf/readme.pdf").getPath();
        String filePath = "/Users/huangr/newtouch/测试.pdf";
        System.out.println("filePath:" + filePath);
        File f = new File(filePath);
        if (StringUtils.isBlank(filePath)) {
            log.info("文件不存在！");
            return;
        }
        BufferedInputStream br = null;
        OutputStream out = null;
        try {
            br = new BufferedInputStream(new FileInputStream(f));
            byte[] bs = new byte[1024];
            int len = 0;
            response.reset(); // 非常重要
            if (flag) {
                // 在线打开方式
                URL u = new URL("file:///" + filePath);
                String contentType = u.openConnection().getContentType();
                response.setContentType(contentType);
                response.setHeader("Content-Disposition", "inline;filename="
                        + "操作手册V1.0.pdf");
            } else {
                // 纯下载方式
                //response.setContentType("application/x-msdownload");
                response.setContentType("application/pdf;charset=utf-8");
                response.setHeader("Content-Disposition", "attachment;filename="
                        + "操作手册V1.0.pdf");
            }
            out = response.getOutputStream();
            while ((len = br.read(bs)) > 0) {
                out.write(bs, 0, len);
            }
            out.flush();
            out.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            log.info("pdf处理文件异常" + e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
