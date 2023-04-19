package com.newtouch.uctp.framework.qiyuesuo;

import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoCommonResult;
import com.newtouch.uctp.framework.qiyuesuo.core.client.impl.qys.DefaultQiyuesuoClient;
import com.newtouch.uctp.framework.qiyuesuo.core.client.impl.saas.SaasQiyuesuoSaasClient;
import com.newtouch.uctp.framework.qiyuesuo.core.enums.QiyuesuoChannelEnum;
import com.newtouch.uctp.framework.qiyuesuo.core.property.QiyuesuoChannelProperties;
import com.qiyuesuo.sdk.v2.bean.*;
import com.qiyuesuo.sdk.v2.http.StreamFile;
import com.qiyuesuo.sdk.v2.request.SaasCompanyAuthPageUrlRequest;
import com.qiyuesuo.sdk.v2.response.SaaSCompanyAuthPageResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest(classes = UctpSpringBootStarterBizQiyuesuoApplicationTests.class)
class UctpSpringBootStarterBizQiyuesuoApplicationTests {
    private static DefaultQiyuesuoClient client;
    private static SaasQiyuesuoSaasClient saasClient;

    @BeforeAll
    public static void before() {
        // 创建配置类
//        APP Token （服务商接入令牌） yzFRkW26cb
//        APP Secret （服务商接入秘钥） eiTI3RA2yaMBpwmCAZPiXmzewEZstT
//        Secret            cJmnww1NgTUJwjvE
        QiyuesuoChannelProperties properties = new QiyuesuoChannelProperties();
        properties.setId(1L);
        properties.setCode(QiyuesuoChannelEnum.DEFAULT.getCode());
        properties.setAccessKey("yzFRkW26cb");
        properties.setAccessSecret("eiTI3RA2yaMBpwmCAZPiXmzewEZstT");
        properties.setServerUrl("https://openapi.qiyuesuo.cn");
        saasClient = new SaasQiyuesuoSaasClient(properties);
        saasClient.init();
    }
    @Test
    public void testDraft() {
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
        QiyuesuoCommonResult<Contract> draft = client.defaultSend(draftContract);
        Contract data = draft.getData();
    }
    @Test
    public void testCompanyAuthPageUrl() throws FileNotFoundException {
        SaasCompanyAuthPageUrlRequest urlRequest = new SaasCompanyAuthPageUrlRequest();
        urlRequest.setCompanyName("平头哥测试公司");
//        urlRequest.setRegisterNo("53453135123123123");
        urlRequest.setLegalPerson("平头哥");
        urlRequest.setApplicantInfo("{\"name\":\"平头哥\",\"contact\": \"17396202169\",\"contactType\": \"MOBILE\"}");

        urlRequest.setLicense(new StreamFile("营业执照", new FileInputStream("d:/R-C.jpg")));

        QiyuesuoCommonResult<SaaSCompanyAuthPageResult> result = saasClient.saasCompanyAuthPageUrl(urlRequest);
        System.out.println(result);
    }

}
