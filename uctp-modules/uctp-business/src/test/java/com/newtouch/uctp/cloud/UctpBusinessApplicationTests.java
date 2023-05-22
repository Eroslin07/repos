package com.newtouch.uctp.cloud;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.newtouch.uctp.framework.qiyuesuo.core.client.impl.qys.DefaultQiyuesuoClient;
import com.newtouch.uctp.framework.qiyuesuo.core.enums.QiyuesuoChannelEnum;
import com.newtouch.uctp.framework.qiyuesuo.core.property.QiyuesuoChannelProperties;
import com.qiyuesuo.sdk.v2.SdkClient;
import com.qiyuesuo.sdk.v2.bean.*;
import com.qiyuesuo.sdk.v2.json.JSONUtils;
import com.qiyuesuo.sdk.v2.request.ContractPageRequest;
import com.qiyuesuo.sdk.v2.request.EmployeeListRequest;
import com.qiyuesuo.sdk.v2.response.ContractPageResult;
import com.qiyuesuo.sdk.v2.response.DocumentAddResult;
import com.qiyuesuo.sdk.v2.response.EmployeeListResult;
import com.qiyuesuo.sdk.v2.response.SdkResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest(classes =UctpBusinessApplicationTests.class )
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
        properties.setAccessKey("9oFYG0lOfG");
        properties.setAccessSecret("PcUsF4Xkmq9JBFjqVlKsnnXWYmRBHr");
        properties.setServerUrl("https://openapi.qiyuesuo.cn");
        client = new DefaultQiyuesuoClient(properties);
        client.init();
    }

	@Test
	void contextLoads() {
        String serverUrl = "https://openapi.qiyuesuo.cn";
//        String accessKey = "8aScPl53pm";
//        String accessSecret = "AI4nTFk5jA48JkmztTZUSnIbyKJGSK";
        String accessKey = "q4xKsNcFI8";
        String accessSecret = "qKPK101VGyLsnSqFoLzSCu3JGiMAVO";
        SdkClient sdkClient = new SdkClient(serverUrl, accessKey, accessSecret);
// 合同页面
        ContractPageRequest request = new ContractPageRequest(3092059562803470607l,
                new User("15328756760", "MOBILE"), "");
        String response = sdkClient.service(request);
        SdkResponse<ContractPageResult> responseObj = JSONUtils.toQysResponse(response, ContractPageResult.class);
        if(responseObj.getCode() == 0) {
            ContractPageResult result = responseObj.getResult();
            System.out.println("合同页面地址为:{}"+result.getPageUrl());
        } else {
            System.out.println(JSONUtil.toJsonStr(responseObj));
        }
    }

    @Test
    void sendContract(){
//        QiyuesuoClient client = qiyuesuoClientFactory.getQiyuesuoClient(2L);
        Contract draftContract = new Contract();
        draftContract.setSubject("两方-二手车-收车委托合同666");
        // 设置合同接收方
        // 甲方个人签署方
//        Signatory persoanlSignatory = new Signatory();
//        persoanlSignatory.setTenantType("PERSONAL");
//        persoanlSignatory.setTenantName("阿卡丽");
//        persoanlSignatory.setReceiver(new User("15196636618", "MOBILE"));
//        draftContract.addSignatory(persoanlSignatory);
        // 乙方平台
        Signatory platformSignatory = new Signatory();
        platformSignatory.setTenantType("COMPANY");
        platformSignatory.setTenantName("广东光耀汽车公司");
        platformSignatory.setReceiver(new User( "17380123816", "MOBILE"));
//        platformSignatory.setSerialNo(1);
        draftContract.addSignatory(platformSignatory);
        //丙方
        Signatory initiator2 = new Signatory();
        initiator2.setTenantType("COMPANY");
        initiator2.setTenantName("山西车友通汽车公司");
        initiator2.setReceiver(new User("15196636618", "MOBILE"));
//        initiator2.setSerialNo(2);
        draftContract.addSignatory(initiator2);

        //模板参数
//        draftContract.addTemplateParam(new TemplateParam("甲方","罗聪"));
//        draftContract.addTemplateParam(new TemplateParam("乙方","新致"));
//        draftContract.addTemplateParam(new TemplateParam("丙方","平头哥二手车"));
//        draftContract.addTemplateParam(new TemplateParam("选择1","☑"));
//        draftContract.addTemplateParam(new TemplateParam("选择2","☑"));
//        draftContract.addTemplateParam(new TemplateParam("选择3","□"));
//        draftContract.addTemplateParam(new TemplateParam("选择4","□"));
        draftContract.setCategory(new Category(3078145859615985671L));//业务分类配置
        draftContract.setSend(false); // 发起合同
//        draftContract.setCreator(new User("17380123816","MOBILE"));
        Contract contract = client.defaultDraftSend(draftContract).getCheckedData();
        System.out.println(contract.getId());
        //2,签字时是丙方是否会自动签章
//        QiyuesuoCommonResult<Contract> result = client.defaultDraftSend(draftContract);
//        System.out.println(JSONUtil.toJsonStr(result.getData()));
//        Contract contract = result.getData();
        ArrayList<TemplateParam> params = ListUtil.toList(new TemplateParam("甲方", "罗聪"),
                new TemplateParam("乙方", "新致"),
                new TemplateParam("丙方", "平头哥二手车"));
        DocumentAddResult result = client.defaultDocumentAddByTemplate(contract.getId(), 3089853271330792185L, params, "二手车收购协议").getCheckedData();
        System.out.println(result.getDocumentId());
//        System.out.println(JSONUtil.toJsonStr(docRes.getData()));
        client.defaultContractSend(contract.getId()).getCheckedData();
//        System.out.println(result1.getData());
    }


    @Test
    void http(){
        String url = UrlBuilder.create()
                .setScheme("https")
                .setHost("dwz.cn")
                .addPath("//api/v3/short-urls")
                .build();
        Map<String, String> map = MapUtil.builder("LongUrl", "https://expose.qiyuesuo.cn/enterpriseAuth/index?token=dUpRZTJOR2Z1NTN1MWhQbURwV2tFQ1NtWEM2cE5TanAzMzRscDhxVjhzOXJ1ck8yaDhVcmQ2L1kzNjloZFFNVQ==").put("TermOfValidity", "1-year").build();
        List<Map<String, String>> list = ListUtil.of(map);
        String result2 = HttpRequest.post(url)
                .header(Header.CONTENT_TYPE, "application/json; charset=UTF-8")//头信息，多个头信息多次调用此方法即可
                .header("Dwz-Token","a03c080908fccf5a2a21c125abf4ded6")
                .body(JSONUtil.toJsonStr(list))
                .timeout(20000)//超时，毫秒
                .execute().body();
        System.out.println(result2);
    }
    @Test
    void contractDetail(){
        Contract checkedData = client.defaultContractDetail(3091645118122296125L).getCheckedData();
        System.out.println(JSONUtil.toJsonStr(checkedData));

    }

    @Test
    void contractSend(){
        Object checkedData = client.defaultContractSend(3092074193567683165L).getCheckedData();
        System.out.println(JSONUtil.toJsonStr(checkedData));
    }

    @Test
    void sealAutocreate(){
        Seal seal = client.defaultSealAutoCreate("公章", "12345678902547").getCheckedData();
        System.out.println(JSONUtil.toJsonStr(seal));
    }

    @Test
    void contractPage(){
        ContractPageResult pageResult = client.defaultdeContractPage(3098148948951507300L, "15328756760").getCheckedData();
        System.out.println(pageResult.getPageUrl());
    }

    @Test
    void contractDownload() throws FileNotFoundException {
        File file = FileUtil.file("D:/usr/contract3.pdf");
        FileOutputStream fos = new FileOutputStream(file);
        client.defaultDocumentDownload(fos, 3095639330459231123L);
//        client.defaultContractDownload(fos, 3095639328445965188L, ListUtil.of("CONTRACT"), Boolean.FALSE).getCheckedData();

    }
    @Test
    void emp() throws FileNotFoundException {
        SdkClient sdkClient = new SdkClient("https://openapi.qiyuesuo.cn", "62bZtaGknb", "wWiTSWW6VmwllVlBVqwQJssvLNupeS");
        EmployeeListRequest request = new EmployeeListRequest();
        String response = sdkClient.service(request);
        SdkResponse<EmployeeListResult> responseObj = JSONUtils.toQysResponse(response, EmployeeListResult.class);
        List<String> mobiles = responseObj.getResult().getList().stream().map(Employee::getMobile).collect(Collectors.toList());
        if (mobiles.contains("17396202169")) {
            System.out.println("牛逼");
        }else {
            System.out.println("哦哦");
        }
    }

}
