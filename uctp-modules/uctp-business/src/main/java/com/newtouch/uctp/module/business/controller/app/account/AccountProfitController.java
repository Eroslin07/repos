package com.newtouch.uctp.module.business.controller.app.account;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.common.pojo.PageParam;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.account.vo.ProfitDetailRespVO;
import com.newtouch.uctp.module.business.controller.app.account.vo.ProfitPresentReqVO;
import com.newtouch.uctp.module.business.controller.app.account.vo.ProfitRespVO;
import com.newtouch.uctp.module.business.controller.app.account.vo.ProfitSummaryRespVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;

@Tag(name = "商户利润")
@RestController
@RequestMapping("/uctp/account/profit")
@Validated
public class AccountProfitController {

    @GetMapping("/summary")
    @Operation(summary = "查询利润概要信息")
    public CommonResult<ProfitSummaryRespVO> summary() {
        // todo 通过什么方式能获取到当前商户（用户）
        ProfitSummaryRespVO respVO = new ProfitSummaryRespVO();
        return success(respVO);
    }

    @PostMapping("/present")
    @Operation(summary = "利润提取")
    public CommonResult<String> profitPresent(@RequestBody ProfitPresentReqVO profitPresentReqVO) {
        // todo 通过什么方式能获取到当前商户（用户）
        return success("已提交申请");
    }

    @GetMapping("/list")
    @Operation(summary = "利润明细查询")
    public CommonResult<PageResult<List<ProfitRespVO>>> profitList(@Valid PageParam PageParam) {
        // todo 通过什么方式能获取到当前商户（用户）
        PageResult pr = new PageResult();
        pr.setTotal(0L);
        return success(pr);
    }

    @GetMapping("/detail")
    @Operation(summary = "利润详情")
    public CommonResult<ProfitDetailRespVO> profitDetail(Long profitId) {
        ProfitDetailRespVO respVO = new ProfitDetailRespVO();
        return success(respVO);
    }

}
