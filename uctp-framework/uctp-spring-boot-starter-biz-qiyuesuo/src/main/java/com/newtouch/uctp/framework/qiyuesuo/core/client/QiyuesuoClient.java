package com.newtouch.uctp.framework.qiyuesuo.core.client;

import com.qiyuesuo.sdk.v2.bean.Contract;
import com.qiyuesuo.sdk.v2.bean.Employee;
import com.qiyuesuo.sdk.v2.bean.Seal;
import com.qiyuesuo.sdk.v2.bean.TemplateParam;
import com.qiyuesuo.sdk.v2.request.*;
import com.qiyuesuo.sdk.v2.response.ContractPageResult;
import com.qiyuesuo.sdk.v2.response.DocumentAddResult;
import com.qiyuesuo.sdk.v2.response.SealListResult;

import java.io.FileOutputStream;
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
     * 公司静默签章
     * @param contractId 合同Id
     * @return
     */
    QiyuesuoCommonResult<Object> defaultCompanysign(Long contractId);
    /**
     * 公司静默签章
     * @param contractId 合同Id
     * @param documentId 合同文件Id
     * @param seaLId 印章Id
     * @param keywords 签章关键字
     * @param dateKeyword 签章日期关键字
     * @return
     */
    QiyuesuoCommonResult<Object> defaultCompanysign(Long contractId,
                                                    Long documentId,
                                                    Long seaLId,
                                                    List<String> keywords,
                                                    String dateKeyword);

    /**
     * 静默签章
     * @param request 契约锁需要的参数
     * @return
     */
    QiyuesuoCommonResult<Object> defaultCompanysign(ContractSignCompanyRequest request);

    /**
     * 撤回/作废合同
     * @param request 契约锁需要的参数
     * @return
     */
    QiyuesuoCommonResult<String> defaultContractInvalid(ContractInvalidRequest request);

    /**
     * “签署中”状态下撤回合同
     *
     * @param contractId 合同 ID
     * @param reason 撤回 / 作废原因
     * @return
     */
    QiyuesuoCommonResult<String> defaultContractInvalid(Long contractId, String reason);

    /**
     *  “已完成”状态下请求作废合同，同时发起方签署作废合同
     *
     * @param contractId 合同 ID
     * @param sealId 印章 ID，发起方签署作废文件时指定的印章，作废合同时使用。若发起方已签署，不传默认取发起方印章 ID
     * @param reason 撤回 / 作废原因
     * @return
     */
    QiyuesuoCommonResult<String> defaultContractInvalid(Long contractId, Long sealId, String reason);

    /**
     *  “草稿”状态下删除合同
     *
     * @param contractId 合同 ID
     * @return
     */
    QiyuesuoCommonResult<String> defaultContractInvalid(Long contractId);

    /**
     * 获取合同详情
     *
     * @param request 合同 ID
     * @return
     */
    QiyuesuoCommonResult<Contract> defaultContractDetail(ContractDetailRequest request);
    /**
     * 获取合同详情
     *
     * @param contractId 合同 ID
     * @return
     */
    QiyuesuoCommonResult<Contract> defaultContractDetail(Long contractId);

    /**
     * 获取合同签署页面
     *
     * @param request 契约锁需要参数
     * @return
     */
    QiyuesuoCommonResult<ContractPageResult> defaultdeContractPage(ContractPageRequest request);
    /**
     * 获取合同签署页面
     *
     * @param contractId 契约锁合同Id
     * @param contact 签署人电话
     * @return
     */
    QiyuesuoCommonResult<ContractPageResult> defaultdeContractPage(Long contractId, String contact);

    /**
     * 获取公司下的印章列表
     * @param request 契约锁需要参数
     * @return
     */
    QiyuesuoCommonResult<SealListResult> defaultSealList(SealListRequest request);

    /**
     * 获取公司下的印章列表
     * @param tenantName 查询条件：子公司名称，若传递了则查询子公司下的印章，默认为平台方主公司
     * @return
     */
    QiyuesuoCommonResult<SealListResult> defaultSealList(String tenantName);

    /**
     * 添加员工
     * @param request 契约锁需要的参数
     * @return
     */
    QiyuesuoCommonResult<Employee> defaultEmployeeCreate(EmployeeCreateRequest request);
    /**
     * 添加员工
     * @param name 员工名字
     * @param contact 电话
     * @return
     */
    QiyuesuoCommonResult<Employee> defaultEmployeeCreate(String name,String contact);
    /**
     * 移除员工
     * @param request 契约锁需要的参数
     * @return
     */
    QiyuesuoCommonResult<Employee> defaultEmployeeRemove(EmployeeRemoveRequest request);
    /**
     * 移除员工
     * @param name 联系类型：MOBILE（手机号），EMAIL（邮箱），EMPLOYEEID（员工 ID）
     * @param contact 联系方式
     *
     * @return
     */
    QiyuesuoCommonResult<Employee> defaultEmployeeRemove(String name,String contact);

    /**
     * 管理员工角色
     * @param request 契约锁需要的参数
     * @return
     */
    QiyuesuoCommonResult<Object> defaultRoleManage(RoleManagementRequest request);

    /**
     * 管理员工角色
     * @param contacts 已认证员工手机
     * @return
     */
    QiyuesuoCommonResult<Object> defaultRoleManage(List<String> contacts);

    /**
     * 调用此接口可以直接为公司生成印章，并指定印章使用者，
     * 不需要经过契约锁审核。 可以在云平台中，
     * 侧边栏 -》企业 -》印章管理 -》制作印章页面中，选择 "自动生成" 来查看生成效果。
     * @param request 已认证员工手机
     * @return
     */
    QiyuesuoCommonResult<Seal> defaultSealAutoCreate(SealAutoCreateRequest request);

    /**
     * 调用此接口可以直接为公司生成印章，并指定印章使用者，
     * 不需要经过契约锁审核。 可以在云平台中，
     * 侧边栏 -》企业 -》印章管理 -》制作印章页面中，选择 "自动生成" 来查看生成效果。
     * @param name 印章名称
     * @param enterpriseCode 企业信息编码；为 13 到 15 位的数字，生成公章时设置
     * @return
     */
    QiyuesuoCommonResult<Seal> defaultSealAutoCreate(String name,String enterpriseCode);

    /**
     * 合同文档下载
     *
     * @param fos 输出流
     * @param documentId 文档id
     * @return
     */
    QiyuesuoCommonResult<Boolean> defaultDocumentDownload(FileOutputStream fos, Long documentId);

    /**
     * 下载合同与附属文件
     *
     * @param fos 输出流
     * @param contractId 合同id
     * @param downloadItems 下载子项目，子项目可选项：CONTRACT（"合同原文"）、SIGNLOG（"签署日志"）、ATTACHMENT（"附件"）、NOTARY（"存证报告"）、ENDSIGN_ATTACHMENT（“强制结束附件”）;默认为合同文件与签署日志
     * @param needCompressForOneFile 当下载的文件为单份文件时，是否压缩；默认压缩
     * @return
     */
    QiyuesuoCommonResult<Boolean> defaultContractDownload(FileOutputStream fos,
                                                          Long contractId,
                                                          List<String> downloadItems,
                                                          Boolean needCompressForOneFile);

    /**
     * 签署作废合同
     *
     * @param request 契约锁需要参数
     * @return
     */
    QiyuesuoCommonResult<Contract> defaultContractInvalidSign(ContractSignInvalidRequest request);
    /**
     * 签署作废合同
     * @param contractId
     * @return
     */
    QiyuesuoCommonResult<Contract> defaultContractInvalidSign(Long contractId);

}
