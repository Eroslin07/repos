package com.newtouch.uctp.module.business.service.bank.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.RandomUtil;
import com.newtouch.uctp.module.business.enums.bank.BankConstants;
import com.newtouch.uctp.module.business.service.bank.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Validated
public class TransactionServiceImpl implements TransactionService {


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
    public String unKnowClearing() {
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
