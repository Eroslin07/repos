package com.newtouch.uctp.module.business.service.bank.impl;

import api.ApiResponse;
import api.SPDBSMEncryptor;
import com.newtouch.uctp.framework.common.exception.BankException;
import com.newtouch.uctp.module.business.dal.dataobject.TransactionLogDO;
import com.newtouch.uctp.module.business.enums.bank.BankConstants;
import com.newtouch.uctp.module.business.service.bank.BankAPIService;
import com.newtouch.uctp.module.business.service.bank.TransactionLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
@Slf4j
public class BankAPIServiceImpl implements BankAPIService {

    @Value("${uctp.spdb.client-id}")
    private String clientId;
    @Value("${uctp.spdb.secret}")
    private String secret;
    @Value("${uctp.spdb.privatekey}")
    private String privateKey;
    @Value("${uctp.spdb.spdbPublicKey}")
    private String spdbPublicKey;
    @Value("${uctp.spdb.host}")
    private String host;

    @Resource
    private TransactionLogService transactionLogService;

    @Override
    public String post(String apiPath, String requestBody) throws BankException {
        LocalDateTime now = LocalDateTime.now();
        ApiResponse apiResponse = null;
        try {
            apiResponse = SPDBSMEncryptor.requestApi(clientId, secret, privateKey, spdbPublicKey,
                    BankConstants.REQUEST_METHOD_POST, host.concat(apiPath),
                    requestBody, true, true);
            if (apiResponse.getCode() == BankConstants.BANK_STATUS_CODE_SUCCESS
                    && apiResponse.getVerify()) {
                return apiResponse.getDecryptBody();
            } else {
                throw new BankException(requestBody, StringUtils.isBlank(apiResponse.getDecryptBody())
                        ? apiResponse.getBody()
                        : apiResponse.getDecryptBody());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BankException(requestBody, apiResponse.getBody());
        } finally {
            transactionLogService.save(TransactionLogDO.builder()
                    .tranBeginTime(now)
                    .tranEndTime(LocalDateTime.now())
                    .tranRequest(requestBody)
                    .tranResponse(StringUtils.isBlank(apiResponse.getDecryptBody())
                            ? apiResponse.getBody()
                            : apiResponse.getDecryptBody()).build()
            );
        }
    }
}
