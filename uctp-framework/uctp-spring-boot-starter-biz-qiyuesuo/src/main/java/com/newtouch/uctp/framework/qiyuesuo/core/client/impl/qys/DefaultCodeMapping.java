package com.newtouch.uctp.framework.qiyuesuo.core.client.impl.qys;

import com.newtouch.uctp.framework.common.exception.ErrorCode;
import com.newtouch.uctp.framework.common.exception.enums.GlobalErrorCodeConstants;
import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoCodeMapping;
import com.newtouch.uctp.framework.qiyuesuo.core.enums.QiyuesuoFrameworkErrorCodeConstants;

/**
 * 阿里云的 SmsCodeMapping 实现类
 *
 * 参见 https://help.aliyun.com/document_detail/101346.htm 文档
 *
 * @author 芋道源码
 */
public class DefaultCodeMapping implements QiyuesuoCodeMapping {

    @Override
    public ErrorCode apply(String apiCode) {
        switch (apiCode) {
            case "OK": return GlobalErrorCodeConstants.SUCCESS;
            default:
                return QiyuesuoFrameworkErrorCodeConstants.QIYUESUO_UNKNOWN;
        }
    }

}
