package com.newtouch.uctp.module.business.service.bank.response;

import lombok.Data;

/**
 * 场景转账链接生成交易响应报文
 */
@Data
public class TechAddressesResponse {

    /**
     * 返回信息
     */
    private String statusMsg;
    /**
     * 返回状态码
     */
    private String statusCode;
    /**
     * 交易流水号
     */
    private String transNo;
    /**
     * ios跳转的地址
     */
    private String addressCls;
    /**
     * android跳转的地址1
     */
    private String addressCls1;
    /**
     * H5跳转APP的地址
     */
    private String address;
}
