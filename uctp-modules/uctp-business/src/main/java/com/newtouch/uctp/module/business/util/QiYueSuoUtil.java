package com.newtouch.uctp.module.business.util;

import cn.hutool.core.util.ObjectUtil;
import com.qiyuesuo.sdk.v2.SdkClient;
import com.qiyuesuo.sdk.v2.bean.*;
import com.qiyuesuo.sdk.v2.http.StreamFile;
import com.qiyuesuo.sdk.v2.json.JSONUtils;
import com.qiyuesuo.sdk.v2.request.*;
import com.qiyuesuo.sdk.v2.response.*;
import com.qiyuesuo.sdk.v2.utils.CollectionUtils;
import com.qiyuesuo.sdk.v2.utils.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;

import static com.newtouch.uctp.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.newtouch.uctp.module.business.enums.ErrorCodeConstants.*;

/**
 * 负责调用契约锁相关的接口
 * https://open.qiyuesuo.com/document?id=2593801164428415723
 */
@Slf4j
public class QiYueSuoUtil {
    private SdkClient client = null;

    private QiYueSuoUtil() {

    }

    public QiYueSuoUtil(String url, String accessKey, String accessSecret) {
        this.initClient(url, accessKey, accessSecret);
    }

    public void initClient(String url, String accessKey, String accessSecret) {
        if (StringUtils.isBlank(url) || StringUtils.isBlank(accessKey) || StringUtils.isBlank(accessSecret)) {
            throw exception(QYS_INIT_PARAM_ERROR);
        }
        client = new SdkClient(url, accessKey, accessSecret);
        if (null == client) {
            throw exception(QYS_INIT_PARAM_ERROR);
        }

    }

    /**
     * 稿配置签字合同草稿
     *
     * @param subject
     * @param persoanlTenantName
     * @param persoanlContact
     * @return
     * @throws Exception
     */
    public SdkResponse<Contract> signDraft(String subject, String persoanlTenantName, String persoanlContact) throws Exception {
        // 合同基本参数
        Contract draftContract = new Contract();
        draftContract.setSubject(subject);
        draftContract.setCategory(new Category("签字合约"));
        draftContract.setBizId("");
        draftContract.setSend(false);
        // 个人
        Signatory signatory = new Signatory();
        signatory.setTenantName(persoanlTenantName);
        signatory.setTenantType("PERSONAL");
        signatory.setReceiver(new User(persoanlTenantName, persoanlContact, "MOBILE"));
        signatory.setSerialNo(1);
// 设置签署方
        draftContract.addSignatory(signatory);
// 创建合同
        ContractDraftRequest request = new ContractDraftRequest(draftContract);
        return this.draft(draftContract, Contract.class);
    }
    /**
     * 稿配置签章合同草稿
     * @param subject
     * @param platformTenantName
     * @param platformContact
     * @param persoanlTenantName
     * @param persoanlContact
     * @return
     * @throws Exception
     */
    public SdkResponse<Contract> sealDraft(String subject, String platformTenantName, String platformContact,
                                       String persoanlTenantName, String persoanlContact) throws Exception {
        return null;
    }
    /**
     * 稿配置签字签章合同草稿
     * @param subject
     * @param platformTenantName
     * @param platformContact
     * @param persoanlTenantName
     * @param persoanlContact
     * @return
     * @throws Exception
     */
    public SdkResponse<Contract> signSealDraft(String subject, String platformTenantName, String platformName,String platformContact,
                                       String persoanlTenantName, String persoanlContact) throws Exception {
        Contract draftContract = new Contract();
        draftContract.setCategory(new Category("签字签章"));
        draftContract.setSubject(subject);
        // 设置合同接收方
        // 个人签署方
        Signatory persoanlSignatory = new Signatory();
        persoanlSignatory.setTenantType("PERSONAL");
        persoanlSignatory.setTenantName(persoanlTenantName);
        persoanlSignatory.setSerialNo(1);
        persoanlSignatory.setReceiver(new User(persoanlTenantName,persoanlContact, "MOBILE"));
        // 公司接收方，设置签署流程：（1）合同审批，并指定审批人（2）公章签署（3）法人章签署
        Signatory platformSignatory = new Signatory();
        platformSignatory.setTenantType("COMPANY");
        platformSignatory.setTenantName(platformTenantName);
        platformSignatory.setSerialNo(2);
        platformSignatory.setReceiver(new User(platformName,platformContact, "MOBILE"));
        draftContract.addSignatory(persoanlSignatory);
        draftContract.addSignatory(platformSignatory);
        // 设置合同过期时间
        //draftContract.setExpireTime("2020-07-28 23:59:59");
        draftContract.setBizId("");
        draftContract.setSend(false); // 不发起合同
        String s = JSONUtils.toJson(draftContract);
        System.out.println(s);
        return draft(draftContract, Contract.class);
    }
    /**
     * 稿配置签字签章签章合同草稿
     * @param subject
     * @param platformTenantName
     * @param platformContact
     * @param persoanlTenantName
     * @param persoanlContact
     * @return
     * @throws Exception
     */
    public SdkResponse<Contract> signSealSealDraft(String subject, String platformTenantName, String platformContact,
                                       String persoanlTenantName, String persoanlContact) throws Exception {
        return null;
    }

    /**
     * 配置合同草稿
     * 由于契约锁本身的参数较多，此方法只使用了最简单的参数来创建一份草稿合同
     * 后面的业务会调用契约锁的内部页面完成。
     * 如不满足业务，可以自行构建契约锁 Contract 对象
     *
     * @param subject            合同标题
     * @param platformTenantName 公司名称
     * @param platformContact    公司电话
     * @param persoanlTenantName 个人名称
     * @param persoanlContact    个人电话
     * @return
     * @throws Exception
     */
    public SdkResponse<Contract> draft(String subject, String platformTenantName, String platformContact,
                                       String persoanlTenantName,String persoanlContact) throws Exception {
        Contract draftContract = new Contract();
        draftContract.setCategory(new Category("通用签字签章"));
        draftContract.setSubject(subject);
        // 设置合同接收方
        // 个人签署方
        Signatory persoanlSignatory = new Signatory();
        persoanlSignatory.setTenantType("PERSONAL");
        persoanlSignatory.setTenantName(persoanlTenantName);
        persoanlSignatory.setSerialNo(1);
        persoanlSignatory.setReceiver(new User(persoanlContact, "MOBILE"));
        // 公司接收方，设置签署流程：（1）合同审批，并指定审批人（2）公章签署（3）法人章签署
        Signatory platformSignatory = new Signatory();
        platformSignatory.setTenantType("COMPANY");
        platformSignatory.setTenantName(platformTenantName);
        platformSignatory.setSerialNo(2);
        platformSignatory.setReceiver(new User(platformContact, "MOBILE"));
        // 合同公章签署流程
       /* Action sealAction = new Action("COMPANY", 1);
        sealAction.setSealId(2490828768980361630L);
        platformSignatory.addAction(sealAction);*/

        draftContract.addSignatory(persoanlSignatory);
        draftContract.addSignatory(platformSignatory);
        // 设置合同过期时间
        //draftContract.setExpireTime("2020-07-28 23:59:59");
        draftContract.setSend(false); // 不发起合同
        return draft(draftContract, Contract.class);

    }

    /**
     * 配置特殊三方合同草稿->武汉医疗的
     *
     * @param subject            合同标题
     * @param platformTenantName 三方公司名称
     * @param platformContact    三方公司电话
     * @param persoanlTenantName 个人名称
     * @param persoanlContact    个人电话
     * @param iCompanyName       发起方公司名称
     * @param iCompanyContact    发起方联系方式
     * @return
     * @throws Exception
     */
    public SdkResponse<Contract> specThreeDraft(String subject, String platformTenantName, String receiverName, String platformContact,
                                                String persoanlTenantName, String persoanlContact,
                                                String iCompanyName, String iCompanyContact) throws Exception {
        return specThreeDraft(subject, platformTenantName, receiverName, platformContact, persoanlTenantName, persoanlContact, iCompanyName, iCompanyContact, null, null);
    }

    /**
     * 配置特殊三方合同草稿->武汉医疗的
     *
     * @param subject            合同标题
     * @param platformTenantName 三方公司名称
     * @param platformContact    三方公司电话
     * @param persoanlTenantName 个人名称
     * @param persoanlContact    个人电话
     * @param iName              发起方个人签字名称
     * @param iContact           发起方个人联系方式
     * @param iCompanyName       发起方公司名称
     * @param iCompanyContact    发起方联系方式
     * @return
     * @throws Exception
     */
    public SdkResponse<Contract> specThreeDraft(String subject, String platformTenantName, String receiverName, String platformContact,
                                                String persoanlTenantName, String persoanlContact,
                                                String iCompanyName,String iCompanyContact,String iName,String iContact) throws Exception {
        Contract draftContract = new Contract();
        String category = "特殊三方";
        draftContract.setSubject(subject);
        // 设置合同接收方
        // 个人签署方
        Signatory persoanlSignatory = new Signatory();
        persoanlSignatory.setTenantType("PERSONAL");
        persoanlSignatory.setTenantName(persoanlTenantName);
        persoanlSignatory.setSerialNo(1);
        persoanlSignatory.setReceiver(new User(persoanlContact, this.getContractType(persoanlContact)));
        draftContract.addSignatory(persoanlSignatory);
        // 公司接收方，设置签署流程：（1）合同审批，并指定审批人（2）公章签署（3）法人章签署
        Signatory platformSignatory = new Signatory();
        platformSignatory.setTenantType("COMPANY");
        platformSignatory.setTenantName(platformTenantName);
        platformSignatory.setSerialNo(2);
        platformSignatory.setReceiver(new User(receiverName, platformContact, this.getContractType(platformContact)));
        draftContract.addSignatory(platformSignatory);
        //发起方个人
        if (StringUtils.isNotBlank(iContact)) {
            category = "武汉医卫";
            Signatory initiator1 = new Signatory();
            initiator1.setTenantType("PERSONAL");
            initiator1.setTenantName(iName);
            initiator1.setSerialNo(3);
            initiator1.setReceiver(new User(iContact, this.getContractType(iContact)));
            draftContract.addSignatory(initiator1);
        }
        // 发起方
        Signatory initiator2 = new Signatory();
        initiator2.setTenantType("COMPANY");
        initiator2.setTenantName(iCompanyName);
        initiator2.setSerialNo(4);
        initiator2.setReceiver(new User(iCompanyContact, "MOBILE"));
        draftContract.addSignatory(initiator2);
        // 合同公章签署流程
        /*Action sealAction = new Action("COMPANY", 1);
        sealAction.setSealId(2490828768980361630L);
        platformSignatory.addAction(sealAction);*/

        // 设置合同过期时间
        //draftContract.setExpireTime("2020-07-28 23:59:59");
        draftContract.setCategory(new Category(category));
        draftContract.setSend(false); // 不发起合同
        return draft(draftContract, Contract.class);

    }

    public SdkResponse<String> withdrawOrVoidContract(Long contractId, String reason) throws Exception {
        return withdrawOrVoidContract(contractId, null, reason, Boolean.FALSE);
    }

    /**
     * 作废/撤回合同
     * @param contractId 合同Id
     * @param sealId 印章ID，发起方签署作废文件时指定的印章，作废合同时使用。若发起方已签署，不传默认取发起方印章ID
     * @param reason 撤回/作废原因
     * @param deleteDoc 作废完成后或撤回后是否删除合同文件，默认false
     * @return
     */
    public SdkResponse<String> withdrawOrVoidContract(Long contractId, Long sealId, String reason, boolean deleteDoc) throws Exception {
        //TODO 这里可以通过契约锁接口获取到当前合同的状态，然后判断是撤回还是作废合同
        ContractInvalidRequest request = new ContractInvalidRequest(contractId, sealId, reason, deleteDoc);
        return execute(request, String.class);
    }

    private String getContractType(String contract){
        if (contract.contains("@")) {
            return "EMAIL";
        }
        return "MOBILE";
    }

    public SdkResponse<Contract> contractDetail(Long contractId) throws Exception {
        return contractDetail(contractId,Contract.class);
    }

    /**
     * 获取合同详情
     * @param contractId 契约锁合同Id
     * @param clazz
     * @return
     * @throws Exception
     */
    public SdkResponse<Contract> contractDetail(Long contractId,Class<Contract> clazz) throws Exception {
        ContractDetailRequest request = new ContractDetailRequest(contractId);
        return execute(request, clazz);
    }


    public SdkResponse<Contract> draft(Contract draftContract, Class<Contract> clazz) throws Exception {
        ContractDraftRequest request = new ContractDraftRequest(draftContract);
        return execute(request, Contract.class);
    }

    public SdkResponse<DocumentAddResult> addDocumentByFile(Long contractId, File file, String title)
            throws Exception {
        return addDocumentByFile(contractId, file, title, "docx");
    }

    /**
     * 根据本地文件添加合同文档
     *
     * @param contractId 契约锁合同Id
     * @param file       本地文件
     * @param fileSuffix 文件后缀
     * @param title      文件名称
     * @return
     * @throws Exception
     */
    public SdkResponse<DocumentAddResult> addDocumentByFile(Long contractId, File file, String title, String fileSuffix)
            throws Exception {
        StreamFile streamFile = new StreamFile(new FileInputStream(file));
        DocumentAddByFileRequest request = new DocumentAddByFileRequest(contractId, streamFile, fileSuffix, title);
        return addDocumentByFile(request);
    }

    public SdkResponse<DocumentAddResult> addDocumentByFile(DocumentAddByFileRequest request)
            throws Exception {
        return execute(request, DocumentAddResult.class);
    }

    /**
     * 发送合同
     *
     * @param contractId 契约锁合同Id
     * @return
     * @throws Exception
     */
    public SdkResponse send(Long contractId) throws Exception {
        return send(contractId, Arrays.asList());
    }

    /**
     * 发送合同
     *
     * @param contractId 契约锁合同Id
     * @param stampers   指定签署位置对象
     * @return
     * @throws Exception
     */
    public SdkResponse send(Long contractId, List<Stamper> stampers) throws Exception {
        ContractSendRequest request = new ContractSendRequest(contractId, stampers);
        return execute(request, Object.class);
    }

    /**
     * 生成合同浏览页面
     *
     * @param contractId
     * @return
     * @throws Exception
     */
    public SdkResponse<ContractPageResult> contractPageUrl(Long contractId) throws Exception {
        ContractViewPageRequest request = new ContractViewPageRequest(contractId);
        return execute(request, ContractPageResult.class);
    }


    /**
     * 生成签署链接
     *
     * @param contractId      契约锁合同Id
     * @param signUserContact 签署人电话
     * @return
     * @throws Exception
     */
    public SdkResponse<ContractPageResult> gerenateSignUrl(Long contractId, String signUserContact) throws Exception {
        User signUser = new User(signUserContact, "MOBILE");
        return gerenateSignUrl(contractId, signUser);
    }

    public SdkResponse<ContractPageResult> gerenateSignUrl(Long contractId, User signUser) throws Exception {
        ContractPageRequest request = new ContractPageRequest(contractId, signUser, null);
        return execute(request, ContractPageResult.class);
    }

    public void contractDownLoad(Long contractId, String outPath) throws Exception {
        contractDownLoad(contractId, outPath, "CONTRACT","SIGNLOG","ATTACHMENT","NOTARY");
    }
    public SdkResponse<Object> invalid(Long contractId,String reason) throws Exception {
        return this.invalid(contractId, reason, Boolean.FALSE);
    }

    /**
     * 作废/撤回合同
     * @param contractId 契约锁合同Id
     * @param reason  原因
     * @param deleteDoc 是否删除合同文件
     * @return
     * @throws Exception
     */
    public SdkResponse<Object> invalid(Long contractId,String reason,Boolean deleteDoc) throws Exception {
        if (null == contractId) {
            throw exception(QYS_CONTRACTID_NOT_EXISTS);
        }
        ContractInvalidRequest request = new ContractInvalidRequest(contractId, null, reason
                , null == deleteDoc ? Boolean.FALSE : Boolean.TRUE);
        return this.execute(request, Object.class);
    }

    /**
     * 下载契约锁存证报告
     *
     * @param contractId 契约锁合同id
     * @param outPath    输出路径
     * @param items  子项目可选项：CONTRACT（"合同原文"）、SIGNLOG（"签署日志"）、ATTACHMENT（"附件"）、NOTARY（"存证报告"）、ENDSIGN_ATTACHMENT（“强制结束附件”），各子项以逗号（","）相隔，如：CONTRACT,SIGNLOG，默认为合同文件与签署日志
     * @throws Exception
     */
    public void contractDownLoad(Long contractId, String outPath,String... items) throws Exception {
        List<String> itemList = Arrays.asList(items);
        FileOutputStream outputStream = new FileOutputStream(outPath);
        contractDownLoad(contractId, outputStream, itemList);
    }

    public void contractDownLoad(Long contractId, FileOutputStream outputStream,List<String> items) throws Exception {
        ContractDownloadRequest request = new ContractDownloadRequest(contractId);
        if (CollectionUtils.isNotEmpty(items)) {
            request.setDownloadItems(items);
        }
        downLoad(request, outputStream);
    }

    public SdkResponse<CategoryDetailResult> getCategoryDetail(Long categoryId,String tenantName) throws Exception {
        return this.getCategoryDetail(categoryId, null, tenantName);
    }
    public SdkResponse<CategoryDetailResult> getCategoryDetail(String categoryName,String tenantName) throws Exception {
        return this.getCategoryDetail(null, categoryName, tenantName);
    }

    /**
     * 获取契约锁业务分类详情
     * @param categoryId  契约锁业务分类Id
     * @param categoryName  契约锁业务名称
     * @param tenantName  子公司名称
     * @return
     * @throws Exception
     */
    public SdkResponse<CategoryDetailResult> getCategoryDetail(Long categoryId,String categoryName,String tenantName) throws Exception {
        CategoryDetailRequest request = null;
        if (null != categoryId) {
            request = new CategoryDetailRequest(categoryId);
        }
        if (StringUtils.isNotBlank(categoryName)) {
            request = new CategoryDetailRequest(categoryName);
        }
        if (null == request) {
            throw exception(QYS_BUSINESS_TYPE_NOT_EXISTS);
        }
        if (StringUtils.isNotBlank(tenantName)) {
            request.setTenantName(tenantName);
        }
        return this.execute(request, CategoryDetailResult.class);
    }

    /**
     * 单点登录模板授权页面
     * @param companyId 公司id
     * @param companyName 公司名称
     * @param contact 单点用户联系方式
     * @param contactType 联系方式类型：MOBILE 或 EMAIL
     * @return
     */
    private SdkResponse<OpenSSOPrivilegeUrlResult> openSSOPrivilegeUrl(Long companyId,String companyName,String contact,String contactType) throws Exception {
        Company company = null;
        if (ObjectUtil.isNotNull(companyId)) {
            company = new Company(companyId);
        }
        if (StringUtils.isNotBlank(companyName)) {
            company = new Company(companyName);
        }
        User user = new User(contact, contactType);
        OpenSSOPrivilegeUrlRequest request = new OpenSSOPrivilegeUrlRequest(company, user);
        request.setSuccessUrl("www.baidu.com");
        return this.execute(request, OpenSSOPrivilegeUrlResult.class);
    }

    private <T extends SdkRequest> void downLoad(T request, FileOutputStream outputStream) {
        if (null == outputStream) {
            throw exception(QYS_FILE_URL_NOT_NULL);
        }
        client.download(request, outputStream);
        IOUtils.safeClose(outputStream);
    }

    private <T extends SdkRequest> SdkResponse execute(T request, Class clazz) throws Exception {
        String errMsg = "%s,请求契约锁服务器失败，失败原因：%s";
        String name = request.getClass().getSimpleName();
        String response = null;
        try {
            if (null == client) {
                throw exception(QYS_CLIENT_INIT_PARAM_ERROR);
            }
            response = client.service(request);
        } catch (Exception e) {
            String err = String.format(errMsg, name, e.getMessage());
            log.error(err, e);
            e.printStackTrace();
//            throw new unknowexception(err);
        }
        return JSONUtils.toQysResponse(response, clazz);
//        if (!sdkResponse.getCode().equals(0)) {
//            //此处抛出错误，应该去官网查看其对应错误详情
//            String codeMsg = String.format("%s,请求契约锁服务器成功，返回错误状态码原因：%s", name, sdkResponse.getMessage());
//            log.error(codeMsg);
////            throw new UnKnowException(codeMsg);
//        }
    }


    public static void main(String[] args) {
//        q4xKsNcFI8
//        qKPK101VGyLsnSqFoLzSCu3JGiMAVO
        QiYueSuoUtil qiYueSuoUtil = new QiYueSuoUtil("https://openapi.qiyuesuo.cn","q4xKsNcFI8","qKPK101VGyLsnSqFoLzSCu3JGiMAVO");
        try {
//            tset1(qiYueSuoUtil);

            test2(qiYueSuoUtil);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void tset1(QiYueSuoUtil qiYueSuoUtil) throws Exception {
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
        SdkResponse<Contract> draft = qiYueSuoUtil.draft(draftContract, Contract.class);
        if (!draft.getCode().equals(0)) {
            System.out.println("发起合同："+draft.getMessage());
        }
        Contract result = draft.getResult();
        System.out.println(JSONUtils.toJson(result));
        //2,签字时是丙方是否会自动签章
        qiYueSuoUtil.send(result.getId());
    }

    private static void test2(QiYueSuoUtil qiYueSuoUtil) throws Exception {
        //2,单点登陆签署页面
        SdkResponse<OpenSSOPrivilegeUrlResult> response = qiYueSuoUtil
                .openSSOPrivilegeUrl(null,
                        "平头哥二手车", "17311271898", "MOBILE");
        if (!response.getCode().equals(0)) {
            System.out.println("单点登录："+response.getMessage());
        }
        OpenSSOPrivilegeUrlResult result = response.getResult();
        System.out.println(result.getPageUrl());
    }
}
