package com.newtouch.uctp.module.business.enums.bank;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public enum BankTransFormat {

    TRANS_DATE_FORMAT("yyyyMMdd", (s) -> {
        return s.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }),
    TRANS_TIME_FORMAT("HHmmss", (s) -> {
        return s.format(DateTimeFormatter.ofPattern("HHmmss"));
    });

    private String name;

    private Function<LocalDateTime, String> service;

    BankTransFormat(String name, Function<LocalDateTime, String> service) {
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
}
