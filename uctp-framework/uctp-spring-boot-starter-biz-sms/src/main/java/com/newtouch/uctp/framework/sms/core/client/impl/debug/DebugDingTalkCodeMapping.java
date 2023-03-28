package com.newtouch.uctp.framework.sms.core.client.impl.debug;

import com.newtouch.uctp.framework.common.exception.ErrorCode;
import com.newtouch.uctp.framework.common.exception.enums.GlobalErrorCodeConstants;
import com.newtouch.uctp.framework.sms.core.client.SmsCodeMapping;
import com.newtouch.uctp.framework.sms.core.enums.SmsFrameworkErrorCodeConstants;

import java.util.Objects;

/**
 * 钉钉的 SmsCodeMapping 实现类
 *
 * @author 芋道源码
 */
public class DebugDingTalkCodeMapping implements SmsCodeMapping {

    @Override
    public ErrorCode apply(String apiCode) {
        return Objects.equals(apiCode, "0") ? GlobalErrorCodeConstants.SUCCESS : SmsFrameworkErrorCodeConstants.SMS_UNKNOWN;
    }

}
