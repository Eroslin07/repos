package com.newtouch.uctp.framework.qiyuesuo.core.client;

import com.qiyuesuo.sdk.v2.http.StreamFile;
import com.qiyuesuo.sdk.v2.request.SaaSUserAuthPageRequest;
import com.qiyuesuo.sdk.v2.request.SaasCompanyAuthPageUrlRequest;
import com.qiyuesuo.sdk.v2.request.SaasPrivilegeUrlRequest;
import com.qiyuesuo.sdk.v2.response.SaaSCompanyAuthPageResult;
import com.qiyuesuo.sdk.v2.response.SaaSPrivilegeUrlResult;
import com.qiyuesuo.sdk.v2.response.SaaSUserAuthPageResult;

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
    QiyuesuoCommonResult<SaaSCompanyAuthPageResult> companyAuthPageUrl(SaasCompanyAuthPageUrlRequest request);

    /**
     * 公司认证
     * @param companyName 待认证公司名称
     * @param legalPerson 待认证公司法人姓名
     * @param registerNo 待认证公司注册号
     * @param license 营业执照（图片）
     * @param applicantInfo 认证提交人信息（申请者姓名name， 联系方式contact，联系方式类型contactType：MOBILE、EMAIL），企业认证通过后，认证提交 人会自动成为该企业的系统管理员(例：{"name":"aaa","contact": "15100000000","contactType": "MOBILE"})
     * @return
     */
    QiyuesuoCommonResult<SaaSCompanyAuthPageResult> companyAuthPageUrl(String companyName, String legalPerson, String registerNo, StreamFile license,String applicantInfo);

    /**
     * 调用此接口来获取个人认证页面链接，链接只能打开一次，链接有效期5分钟，页面会话有效期15分钟。
     *
     * @param request 契约锁接口接受的参数
     * @return
     */
    QiyuesuoCommonResult<SaaSUserAuthPageResult> userAuthPage(SaaSUserAuthPageRequest request);

    /**
     * 调用此接口来获取个人认证页面链接，链接只能打开一次，链接有效期5分钟，页面会话有效期15分钟。
     *
     * @param contact 操作人联系方式
     * @param contactType 联系类型：MOBILE（手机号），EMAIL（邮箱）
     * @return
     */
    QiyuesuoCommonResult<SaaSUserAuthPageResult> userAuthPage(String contact,
                                                              String contactType);

    /**
     * 调用此接口，获取已认证企业的授权页面链接，企业在页面上授权后，在SAAS平台上可正常使用契约锁电子签相关功能。
     *
     * @param request 契约锁接口接受的参数
     * @return
     */
    QiyuesuoCommonResult<SaaSPrivilegeUrlResult> privilegeUrl(SaasPrivilegeUrlRequest request);
    /**
     * 调用此接口，获取已认证企业的授权页面链接，企业在页面上授权后，在SAAS平台上可正常使用契约锁电子签相关功能。
     *
     * @param companyId 企业Id
     * @param contact 操作人联系方式
     * @param contactType 联系类型：MOBILE（手机号），EMAIL（邮箱）
     * @return
     */
    QiyuesuoCommonResult<SaaSPrivilegeUrlResult> privilegeUrl(Long companyId,
                                                              String contact,
                                                              String contactType);
}
