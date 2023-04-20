package com.newtouch.uctp.framework.qiyuesuo.core.client;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.lang.Assert;
import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.qiyuesuo.core.enums.QiyuesuoFrameworkErrorCodeConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 契约锁的 CommonResult 拓展类
 *
 * 考虑到不同的平台，返回的 code 和 msg 是不同的，所以统一额外返回 {@link #apiCode} 和 {@link #apiMsg} 字段
 *
 * 另外，一些短信平台（例如说阿里云、腾讯云）会返回一个请求编号，用于排查请求失败的问题，我们设置到 {@link #apiRequestId} 字段
 *
 * @author lc
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class QiyuesuoCommonResult<T> extends CommonResult<T> {
    /**
     * API 返回错误码
     *
     * 由于第三方的错误码可能是字符串，所以使用 String 类型
     */
    private String apiCode;
    /**
     * API 返回提示
     */
    private String apiMsg;


    public static <T> QiyuesuoCommonResult<T> build(String apiCode, String apiMsg,
                                               T data, QiyuesuoCodeMapping codeMapping) {
        Assert.notNull(codeMapping, "参数 codeMapping 不能为空");
        QiyuesuoCommonResult<T> result = new QiyuesuoCommonResult<T>().setApiCode(apiCode).setApiMsg(apiMsg);
        result.setData(data);
        // 翻译错误码
//        if (codeMapping != null) {
//            ErrorCode errorCode = codeMapping.apply(apiCode);
//            if (errorCode == null) {
//                errorCode = QiyuesuoFrameworkErrorCodeConstants.QIYUESUO_UNKNOWN;
//            }
//            result.setCode(errorCode.getCode()).setMsg(errorCode.getMsg());
//        }
        result.setCode(Integer.valueOf(apiCode)).setMsg(apiMsg);
        return result;
    }

    public static <T> QiyuesuoCommonResult<T> error(Throwable ex) {
        QiyuesuoCommonResult<T> result = new QiyuesuoCommonResult<>();
        result.setCode(QiyuesuoFrameworkErrorCodeConstants.EXCEPTION.getCode());
        result.setMsg(ExceptionUtil.getRootCauseMessage(ex));
        return result;
    }
}
