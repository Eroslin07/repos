package com.newtouch.uctp.module.business.service.bank;

import com.newtouch.uctp.framework.common.exception.BankException;

public interface BankAPIService {

    String post(String api, String requestBody) throws BankException;
}
