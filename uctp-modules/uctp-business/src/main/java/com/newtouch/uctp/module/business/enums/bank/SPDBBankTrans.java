package com.newtouch.uctp.module.business.enums.bank;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public enum SPDBBankTrans {

    TRAN_DATE_FORMAT("tranDate", (s) -> {
        return s.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }),
    TRAN_TIME_FORMAT("tranTime", (s) -> {
        return s.format(DateTimeFormatter.ofPattern("HHmmss"));
    }),
    TRAN_SEQ_NO("tranSeqNo", (s) -> {
        return new StringBuffer()
                .append("101")
                .append(LocalDateTimeUtil.format(s, DatePattern.PURE_DATETIME_MS_FORMATTER))
                .append(RandomUtil.randomNumbers(10)).toString();
    }),
    TRAN_ORDER_NO("ordrNo", (s) -> {
        // 银行APP转账订单号30位
        return new StringBuffer()
                .append("202").append(LocalDateTimeUtil.format(s, DatePattern.PURE_DATETIME_MS_FORMATTER))
                .append(RandomUtil.randomNumbers(10)).toString();
    }),
    POS_ORDER_NO("outOrderNo", (s) -> {
        return UUID.randomUUID().toString(true);
    });

    private String name;

    private Function<LocalDateTime, String> service;

    SPDBBankTrans(String name, Function<LocalDateTime, String> service) {
        this.name = name;
        this.service = service;
    }

    public String getName() {
        return name;
    }

    public Function<LocalDateTime, String> getService() {
        return service;
    }

    public String get(LocalDateTime dateTime) {
        return service.apply(dateTime);
    }

    /**
     * POS_ORDER_NO 使用
     *
     * @return
     */
    public String get() {
        return service.apply(null);
    }
}
