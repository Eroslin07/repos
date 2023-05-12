package com.newtouch.uctp.module.business.controller.app.account.vo;

import lombok.Data;

@Data
public class PosDevicesRespVO {

    private Long id;

    private String posName;

    private String authCode;

    private String deviceSn;
}
