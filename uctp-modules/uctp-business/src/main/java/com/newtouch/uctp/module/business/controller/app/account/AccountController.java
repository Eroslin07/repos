package com.newtouch.uctp.module.business.controller.app.account;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.common.pojo.PageParam;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.account.vo.ProfitDetailRespVO;
import com.newtouch.uctp.module.business.controller.app.account.vo.ProfitPresentReqVO;
import com.newtouch.uctp.module.business.controller.app.account.vo.ProfitRespVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;

@Tag(name = "商户账户")
@RestController
@RequestMapping("/uctp/account")
@Validated
public class AccountController {

    @PostMapping("/open")
    @Operation(summary = "商户账户开户")
    public CommonResult<String> accountOpen() {
        return success("OK");
    }

    @PostMapping("/profit/present")
    @Operation(summary = "利润提取")
    public CommonResult<String> profitPresent(@RequestBody ProfitPresentReqVO profitPresentReqVO) {
        // todo 通过什么方式能获取到当前商户（用户）
        return success("已提交申请");
    }

    @GetMapping("/profit/list")
    @Operation(summary = "利润明细查询")
    public CommonResult<PageResult<List<ProfitRespVO>>> profitList(@Valid PageParam PageParam) {
        // todo 通过什么方式能获取到当前商户（用户）
        PageResult pr = new PageResult();
        pr.setTotal(0L);
        return success(pr);
    }

    @GetMapping("/profit/detail")
    @Operation(summary = "利润详情")
    public CommonResult<ProfitDetailRespVO> profitDetail(Long profitId) {
        ProfitDetailRespVO respVO = new ProfitDetailRespVO();
        return success(respVO);
    }


}
