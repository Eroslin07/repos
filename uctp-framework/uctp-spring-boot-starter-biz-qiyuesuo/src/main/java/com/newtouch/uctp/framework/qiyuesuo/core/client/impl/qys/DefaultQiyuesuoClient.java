package com.newtouch.uctp.framework.qiyuesuo.core.client.impl.qys;

import cn.hutool.core.lang.Assert;
import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoCommonResult;
import com.newtouch.uctp.framework.qiyuesuo.core.client.impl.AbstractQiyuesuoClient;
import com.newtouch.uctp.framework.qiyuesuo.core.property.QiyuesuoChannelProperties;
import com.qiyuesuo.sdk.v2.bean.Contract;

public class DefaultQiyuesuoClient extends AbstractQiyuesuoClient {
    public DefaultQiyuesuoClient(QiyuesuoChannelProperties properties) {
        super(properties, new DefaultCodeMapping());
        Assert.notEmpty(properties.getApiKey(), "apiKey 不能为空");
        Assert.notEmpty(properties.getApiSecret(), "apiSecret 不能为空");
    }

    @Override
    protected void doInit() {

    }

    @Override
    protected QiyuesuoCommonResult<Contract> doDraft(Contract contract) throws Throwable {
        return null;
    }
}
