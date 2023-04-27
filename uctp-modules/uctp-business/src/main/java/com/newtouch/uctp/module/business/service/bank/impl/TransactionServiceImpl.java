package com.newtouch.uctp.module.business.service.bank.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.newtouch.uctp.module.business.enums.bank.BankConstants;
import com.newtouch.uctp.module.business.enums.bank.BankSubAccountType;
import com.newtouch.uctp.module.business.enums.bank.ClearingType;
import com.newtouch.uctp.module.business.enums.bank.ResponseStatusCode;
import com.newtouch.uctp.module.business.service.account.AccountProfitService;
import com.newtouch.uctp.module.business.service.bank.TransactionService;
import com.newtouch.uctp.module.business.service.bank.request.InnerTransferRequest;
import com.newtouch.uctp.module.business.service.bank.request.NominalAccountRequest;
import com.newtouch.uctp.module.business.service.bank.request.UnKnowClearingRequest;
import com.newtouch.uctp.module.business.service.bank.response.InnerTransferResponse;
import com.newtouch.uctp.module.business.service.bank.response.UnKnowClearingResponse;
import com.newtouch.uctp.module.business.util.bank.SPDBSMSignature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
    public String nominalAccountGenerate(NominalAccountRequest nominalAccountRequest) {
        return null;
    }


    @Override
    public String outGold() {
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW) // 新建事务，避免调用方的事务回滚影响交易日志记录
    public InnerTransferResponse innerTransfer(String accountNo,
                                               String contractNo,
                                               BankSubAccountType outSubAccountType,
                                               BankSubAccountType inSubAccountType,
                                               Long tranAmount,
                                               String remark) {
        if (StringUtils.isBlank(accountNo)) {
            throw new IllegalArgumentException("账户号不能为空");
        }
        if (StringUtils.isBlank(contractNo)) {
            throw new IllegalArgumentException("合同号不能为空");
        }
        if (outSubAccountType == null) {
            throw new IllegalArgumentException("转出子账户类型不能为空");
        }
        if (inSubAccountType == null) {
            throw new IllegalArgumentException("转入子账户类型不能为空");
        }
        if (tranAmount == null) {
            throw new IllegalArgumentException("交易金额不能为空");
        }
        if (tranAmount.compareTo(0L) <= 0) {
            throw new IllegalArgumentException("交易金额必需大于0");
        }

        LocalDateTime now = LocalDateTime.now();

        String outSubAccountNo = null; // TODO: 根据转出账户类型查询对应子账户号
        String outSubAccountNname = null; // TODO: 根据转出账户类型查询对应子账户号
        String inSubAccountNo = null; // TODO: 根据转入账户类型查询对应子账户号
        String inSubAccountName = null; // TODO: 根据转入账户类型查询对应子账户号
        String mainAccountNo = null; // TODO: 主账户号

        InnerTransferRequest request = new InnerTransferRequest();
        request.setAreaCode(BankConstants.AREA_CODE);
        request.setTranSeqNo(this.generaTranNo());
        request.setSettleAcctNo(mainAccountNo);
        request.setPayeeAcctNo(outSubAccountNo);
        request.setPayeeName(outSubAccountNname);
        request.setPayeeAcctNo(inSubAccountNo);
        request.setPayeeName(inSubAccountName);
        // 传入的是分，需要转为元
        String tranAmt = BigDecimal.valueOf(tranAmount).divide(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_UP).toString();
        request.setTranAmt(tranAmt);
        request.setRemark(remark);

        String requestMessage = JSONObject.toJSONString(request);
        log.info("合同：{}的银行请求报文是：{}", contractNo, requestMessage);

        String responseMessage = null;
        // 调用银行接口
        try {
            responseMessage = SPDBSMSignature.call(HttpMethod.POST.name(), BankConstants.UNKNOWN_CLEARINGS_API, requestMessage);
            log.info("合同：{}的银行响应报文是：{}", contractNo, responseMessage);
            if (responseMessage == null) {
                throw new RuntimeException("银行响应报文为空，交易失败");
            }

            InnerTransferResponse response = JSONObject.parseObject(responseMessage, InnerTransferResponse.class);
            return response;
        } catch (Exception e) {
            log.error("调用银行接口失败", e);
            throw new RuntimeException("调用银行接口失败", e);
        } finally {
            // TODO 记录交易日志
        }
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
