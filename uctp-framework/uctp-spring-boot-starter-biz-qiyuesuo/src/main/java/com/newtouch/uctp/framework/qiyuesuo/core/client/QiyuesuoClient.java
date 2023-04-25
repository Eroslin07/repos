package com.newtouch.uctp.framework.qiyuesuo.core.client;

import com.qiyuesuo.sdk.v2.bean.Contract;
import com.qiyuesuo.sdk.v2.bean.TemplateParam;
import com.qiyuesuo.sdk.v2.request.ContractSignCompanyRequest;
import com.qiyuesuo.sdk.v2.request.DocumentAddByTemplateRequest;
import com.qiyuesuo.sdk.v2.response.DocumentAddResult;

import java.util.List;

/**
 * 用于对接各契约锁平台的 SDK，实现合同发送等功能
 *
 * @author lc
 * @since 2021/1/25 14:14
 */
public interface QiyuesuoClient {

    /**
     * 获得渠道编号
     *
     * @return 渠道编号
     */
    Long getId();

    /**
     * 发起合同草稿
     * @param draftContract 契约锁接口接受的参数
     * @return
     */
    QiyuesuoCommonResult<Contract> defaultDraftSend(Contract draftContract);

    /**
     * 添加合同模板
     * @param request 契约锁接口接受的参数
     * @return
     */
    QiyuesuoCommonResult<DocumentAddResult> defaultDocumentAddByTemplate(DocumentAddByTemplateRequest request);
    /**
     * 添加合同模板
     * @param contractId 合同 ID
     * @param templateId 模板 ID
     * @param params  模板参数
     * @param title  模板名称
     * @return
     */
    QiyuesuoCommonResult<DocumentAddResult> defaultDocumentAddByTemplate(Long contractId,
                                                                         Long templateId,
                                                                         List<TemplateParam> params,
                                                                         String title);

    /**
     * 发起合同
     * @param contractId 合同 ID
     * @return
     */
    QiyuesuoCommonResult<Object> defaultContractSend(Long contractId);

    /**
     * 企业签字
     * @param contractId 合同Id
     * @return
     */
    QiyuesuoCommonResult<Object> defaultCompanysign(Long contractId);

    /**
     * 企业签字
     * @param request 契约锁需要的参数
     * @return
     */
    QiyuesuoCommonResult<Object> defaultCompanysign(ContractSignCompanyRequest request);
}
