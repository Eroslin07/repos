package com.newtouch.uctp.framework.qiyuesuo.core.client;

import com.qiyuesuo.sdk.v2.http.StreamFile;
import com.qiyuesuo.sdk.v2.request.*;
import com.qiyuesuo.sdk.v2.response.*;

import java.util.List;

/**
 * 用于对接各契约锁SAAS模式
 *
 * @author lc
 * @since 2021/1/25 14:14
 */
public interface QiyuesuoSaasClient {

    /**
     * 获得渠道编号
     *
     * @return 渠道编号
     */
    Long getId();

    /**
     * 公司认证
     *
     * @param request 契约锁接口接受的参数
     * @return
     */
    QiyuesuoCommonResult<SaaSCompanyAuthPageResult> saasCompanyAuthPageUrl(SaasCompanyAuthPageUrlRequest request);

    /**
     * 公司认证
     * @param companyName 待认证公司名称
     * @param applicantInfo 认证提交人信息（申请者姓名name， 联系方式contact，联系方式类型contactType：MOBILE、EMAIL），企业认证通过后，认证提交 人会自动成为该企业的系统管理员(例：{"name":"aaa","contact": "15100000000","contactType": "MOBILE"})
     * @param legalPerson 待认证公司法人姓名
     * @param registerNo 待认证公司注册号
     * @param license 营业执照（图片）
     * @return
     */
    QiyuesuoCommonResult<SaaSCompanyAuthPageResult> saasCompanyAuthPageUrl(String companyName,String applicantInfo, String legalPerson, String registerNo, StreamFile license);
    /**
     * 公司认证
     * @param companyName 待认证公司名称
     * @param applicantInfo 认证提交人信息（申请者姓名name， 联系方式contact，联系方式类型contactType：MOBILE、EMAIL），企业认证通过后，认证提交 人会自动成为该企业的系统管理员(例：{"name":"aaa","contact": "15100000000","contactType": "MOBILE"})
     * @return
     */
    QiyuesuoCommonResult<SaaSCompanyAuthPageResult> saasCompanyAuthPageUrl(String companyName,String applicantInfo);

    /**
     * 调用此接口来获取个人认证页面链接，链接只能打开一次，链接有效期5分钟，页面会话有效期15分钟。
     *
     * @param request 契约锁接口接受的参数
     * @return
     */
    QiyuesuoCommonResult<SaaSUserAuthPageResult> saasUserAuthPage(SaaSUserAuthPageRequest request);

    /**
     * 调用此接口来获取个人认证页面链接，链接只能打开一次，链接有效期5分钟，页面会话有效期15分钟。
     *
     * @param contact 操作人联系方式
     * @param contactType 联系类型：MOBILE（手机号），EMAIL（邮箱）
     * @return
     */
    QiyuesuoCommonResult<SaaSUserAuthPageResult> saasUserAuthPage(String contact,
                                                              String contactType);

    /**
     * 调用此接口来获取个人认证页面链接，链接只能打开一次，链接有效期5分钟，页面会话有效期15分钟。
     *
     * @param contact 操作人联系方式
     * @return
     */
    QiyuesuoCommonResult<SaaSUserAuthPageResult> saasUserAuthPage(String contact);

    /**
     * 调用此接口，获取已认证企业的授权页面链接，企业在页面上授权后，在SAAS平台上可正常使用契约锁电子签相关功能。
     *
     * @param request 契约锁接口接受的参数
     * @return
     */
    QiyuesuoCommonResult<SaaSPrivilegeUrlResult> saasPrivilegeUrl(SaasPrivilegeUrlRequest request);
    /**
     * 调用此接口，获取已认证企业的授权页面链接，企业在页面上授权后，在SAAS平台上可正常使用契约锁电子签相关功能。
     *
     * @param companyId 企业Id
     * @param contact 操作人电话
     * @param privilegeModules 指定授权模块，指定的模块在授权页面默认勾选；可行值如下：SEAL（印章管理）、TEMPLATE（模板管理）、CONTRACT（合同管理）、COMPANY_EMPLOYEE（企业与成员）、ROLE_PERMISSION（角色与权限）、BASE_INFO（基本信息）、FILE_STATISTICS（文件统计）、CATEGORY（业务分类）、FEE（费用中心）
     * @return
     */
    QiyuesuoCommonResult<SaaSPrivilegeUrlResult> saasPrivilegeUrl(Long companyId,
                                                              String contact,
                                                                 List<String> privilegeModules);

    /**
     * 企业一键签章授权
     *
     * @param sealAdminContract 印章管理员电话
     * @param companyId 待授权印章自动签署的公司id
     * @param authDeadline 授权截止日期，格式yyyy-MM-dd
     * @param remark 授权使用范围，500字以内
     * @return
     */
    QiyuesuoCommonResult<SaaSSealSignAuthUrlResult> saasSealSignAuthUrl(String sealAdminContract, Long companyId, String authDeadline, String remark);
    /**
     * 企业一键签章授权
     *
     * @param request 契约锁需要的参数
     * @return
     */
    QiyuesuoCommonResult<SaaSSealSignAuthUrlResult> saasSealSignAuthUrl(SaaSSealSignAuthUrlRequest request);

    /**
     * 查询个人认证结果
     * @param request 契约锁需要的参数
     * @return
     */
    QiyuesuoCommonResult<SaaSUserAuthResult> saasUserAuthResult(SaaSUserAuthResultRequest request);
    /**
     * 查询个人认证结果
     * @param contact 认证用户联系方式
     * @return
     */
    QiyuesuoCommonResult<SaaSUserAuthResult> saasUserAuthResult(String contact);
}
