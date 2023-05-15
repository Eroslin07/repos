package com.newtouch.uctp.module.business.enums;

/**
 * 契约锁常量
 */
public class QysConstants {

    public static final String SERVER_URL = "https://openapi.qiyuesuo.cn"; // 测试环境域名为https://openapi.qiyuesuo.cn，生产环境域名为https://openapi.qiyuesuo.com
    public static final String AGENT_TOKEN = "yzFRkW26cb"; // 接入令牌，用于调用企业认证、企业授权等接口
    public static final String AGENT_SECRET = "eiTI3RA2yaMBpwmCAZPiXmzewEZstT"; //接入秘钥，用于调用企业认证、企业授权等接口
    public static final String SECRET = "cJmnww1NgTUJwjvE"; //用于接收回调消息（企业认证、企业授权、合同状态回调）的签名校验
    public static final Long PLATFORM_ID = 1L; //平台方id,用于发起合同
    public static final Long MARKET_ID = 8L; //市场方id
}
