package com.newtouch.uctp.module.business.service.bank.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.newtouch.uctp.module.business.enums.bank.BankConstants;
import com.newtouch.uctp.module.business.enums.bank.ClearingType;
import com.newtouch.uctp.module.business.enums.bank.ResponseStatusCode;
import com.newtouch.uctp.module.business.service.account.AccountProfitService;
import com.newtouch.uctp.module.business.service.bank.TransactionService;
import com.newtouch.uctp.module.business.service.bank.request.UnKnowClearingRequest;
import com.newtouch.uctp.module.business.service.bank.response.UnKnowClearingResponse;
import com.newtouch.uctp.module.business.util.bank.SPDBSMSignature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Validated
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    @Resource
    private AccountProfitService accountProfitService;

    @Override
    public void noticePaymentResult() {

    }

    @Override
    public String orderPayment(String contractNo) {
        return null;
    }

    @Override
    public String orderPayStatus(String contractNo) {
        return null;
    }

    @Override
    public String nominalAccountGenerate() {
        return null;
    }

    @Override
    public String outGold() {
        return null;
    }

    @Override
    public String innerTransfer() {
        return null;
    }

    @Override
    public String unKnowClearing(String contractNo) {
        LocalDateTime now = LocalDateTime.now();

        UnKnowClearingRequest request = new UnKnowClearingRequest();
        request.setTranDate(now.format(DateTimeFormatter.ofPattern(BankConstants.tranDateFormat)));
        request.setTranTime(now.format(DateTimeFormatter.ofPattern(BankConstants.tranTimeFormat)));
        request.setAreaCode(BankConstants.AREA_CODE);
        request.setSettleAcctNo(BankConstants.ACCT_NO);
        request.setChannelSeqNo(this.generaTranNo());

        request.setYlkTranSeqNo(null); // 还不确定该值从哪来
        request.setOrgTranSeqNo(null);

        boolean transExists = true; // TODO 需要判断该笔交易是否真实存在
        if (transExists) { // 存在该笔交易，则按子账号入金
            log.info("存在交易：{}，按子账号入金", contractNo);
            request.setClrgTp(ClearingType.DEPOSITS_BY_SUB_ACCOUNT.getCode());
            request.setClrgRsltDsc(ClearingType.DEPOSITS_BY_SUB_ACCOUNT.getValue());
        } else { // 不存在该笔交易，则原路退回
            log.warn("不存在交易：{}，原路退回", contractNo);
            request.setClrgTp(ClearingType.ORIGINAL_WAY_BACK.getCode());
            request.setClrgRsltDsc(ClearingType.ORIGINAL_WAY_BACK.getValue());
        }
        request.setRsrvFld(null);
        request.setRsrvFld1(null);
        request.setRsrvFld2(null);

        String requestMessage = JSONObject.toJSONString(request);
        log.info("交易：{}的银行请求报文是：{}", contractNo, requestMessage);

        String responseMessage = null;
        // 调用银行接口
        try {
            responseMessage = SPDBSMSignature.call(HttpMethod.POST.name(), BankConstants.UNKNOWN_CLEARINGS_API, requestMessage);
            log.info("交易：{}的银行响应报文是：{}", contractNo, responseMessage);
            if (responseMessage == null) {
                throw new RuntimeException("银行响应报文为空，交易失败");
            }

            UnKnowClearingResponse response = JSONObject.parseObject(responseMessage, UnKnowClearingResponse.class);

            if (ResponseStatusCode.TRAN_SUCCESS.getCode().equals(response.getStatusCode())) {
                // 交易成功

            }

        } catch (Exception e) {
            log.error("调用银行接口失败", e);

        } finally {
            // TODO 记录交易日志
        }

        return null;
    }

    /**
     * 交易流水号生成
     *
     * @return
     */
    private String generaTranNo() {
        StringBuffer tranNo = new StringBuffer();
        // 三位固定值后续根据业务可以做区分
        tranNo.append("101");
        // 时间戳毫秒单位
        tranNo.append(LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.PURE_DATETIME_MS_FORMATTER));
        // 十位随机字符
        tranNo.append(RandomUtil.randomNumbers(10));
        return tranNo.toString();
    }

    public static void main(String[] args) {
        System.out.println(LocalDate.now().format(DateTimeFormatter.ofPattern(BankConstants.tranDateFormat)));
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern(BankConstants.tranTimeFormat)));
    }
}
