package com.newtouch.uctp.cloud;

import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoCommonResult;
import com.newtouch.uctp.framework.qiyuesuo.core.client.impl.qys.DefaultQiyuesuoClient;
import com.newtouch.uctp.framework.qiyuesuo.core.enums.QiyuesuoChannelEnum;
import com.newtouch.uctp.framework.qiyuesuo.core.property.QiyuesuoChannelProperties;
import com.qiyuesuo.sdk.v2.bean.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = UctpBusinessApplicationTests.class)
class UctpBusinessApplicationTests {

    private static DefaultQiyuesuoClient client;
    @BeforeAll
    public static void before() {
        // 创建配置类
//        APP Token （服务商接入令牌） yzFRkW26cb
//        APP Secret （服务商接入秘钥） eiTI3RA2yaMBpwmCAZPiXmzewEZstT
//        Secret            cJmnww1NgTUJwjvE
        QiyuesuoChannelProperties properties = new QiyuesuoChannelProperties();
        properties.setId(2L);
        properties.setCode(QiyuesuoChannelEnum.DEFAULT.getCode());
//        ("https://openapi.qiyuesuo.cn","q4xKsNcFI8","qKPK101VGyLsnSqFoLzSCu3JGiMAVO")
        properties.setAccessKey("q4xKsNcFI8");
        properties.setAccessSecret("qKPK101VGyLsnSqFoLzSCu3JGiMAVO");
        properties.setServerUrl("https://openapi.qiyuesuo.cn");
        client = new DefaultQiyuesuoClient(properties);
        client.init();
    }

	@Test
	void contextLoads() {
	}

    @Test
    void sendContract(){
//        QiyuesuoClient client = qiyuesuoClientFactory.getQiyuesuoClient(2L);
        Contract draftContract = new Contract();
        draftContract.setSubject("三方-二手车");
        // 设置合同接收方
        // 甲方个人签署方
        Signatory persoanlSignatory = new Signatory();
        persoanlSignatory.setTenantType("PERSONAL");
        persoanlSignatory.setTenantName("罗聪");
        persoanlSignatory.setReceiver(new User("17396202169", "MOBILE"));
        draftContract.addSignatory(persoanlSignatory);
        // 乙方平台
        Signatory platformSignatory = new Signatory();
        platformSignatory.setTenantType("COMPANY");
        platformSignatory.setTenantName("成都新致云服测试公司");
        platformSignatory.setReceiver(new User( "13708206115", "MOBILE"));
        draftContract.addSignatory(platformSignatory);
        //丙方
        Signatory initiator2 = new Signatory();
        initiator2.setTenantType("COMPANY");
        initiator2.setTenantName("平头哥二手车");
        initiator2.setReceiver(new User("17311271898", "MOBILE"));
        draftContract.addSignatory(initiator2);

        //模板参数
        draftContract.addTemplateParam(new TemplateParam("甲方","罗聪"));
        draftContract.addTemplateParam(new TemplateParam("乙方","新致"));
        draftContract.addTemplateParam(new TemplateParam("丙方","平头哥二手车"));
        draftContract.addTemplateParam(new TemplateParam("选择1","☑"));
        draftContract.addTemplateParam(new TemplateParam("选择2","☑"));
        draftContract.addTemplateParam(new TemplateParam("选择3","□"));
        draftContract.addTemplateParam(new TemplateParam("选择4","□"));

        draftContract.setCategory(new Category(3083237961123238073L));//业务分类配置
        draftContract.setSend(false); // 发起合同
        //2,签字时是丙方是否会自动签章
        QiyuesuoCommonResult<Contract> result = client.defaultDraftSend(draftContract);
        System.out.println(result.getData());
    }

}
