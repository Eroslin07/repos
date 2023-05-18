package com.newtouch.uctp.module.business.service.qys;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.contact.vo.QYSContractVO;
import com.newtouch.uctp.module.business.controller.app.qys.vo.QysConfigCreateReqVO;
import com.newtouch.uctp.module.business.controller.app.qys.vo.QysConfigPageReqVO;
import com.newtouch.uctp.module.business.controller.app.qys.vo.QysConfigUpdateReqVO;
import com.newtouch.uctp.module.business.dal.dataobject.qys.QysConfigDO;
import com.newtouch.uctp.module.business.mq.message.UserAuthMessage;
import com.newtouch.uctp.module.system.api.user.dto.AddAccountDTO;

import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 契约锁 Service 接口
 *
 * @author 芋道源码
 */
public interface QysConfigService {
    /**
     * 初始化契约锁客户端
     */
    void initLocalCache();
    /**
     * 创建契约锁
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createQysConfig(@Valid QysConfigCreateReqVO createReqVO);

    /**
     * 更新契约锁
     *
     * @param updateReqVO 更新信息
     */
    void updateQysConfig(@Valid QysConfigUpdateReqVO updateReqVO);

    /**
     * 删除契约锁
     *
     * @param id 编号
     */
    void deleteQysConfig(Long id);

    /**
     * 获得契约锁
     *
     * @param id 编号
     * @return 契约锁
     */
    QysConfigDO getQysConfig(Long id);

    /**
     * 获得契约锁列表
     *
     * @param ids 编号
     * @return 契约锁列表
     */
    List<QysConfigDO> getQysConfigList(Collection<Long> ids);

    /**
     * 获得契约锁分页
     *
     * @param pageReqVO 分页查询
     * @return 契约锁分页
     */
    PageResult<QysConfigDO> getQysConfigPage(QysConfigPageReqVO pageReqVO);

    /**
     * 电子签回调接口->企业认证回调地址：获取企业认证的进度和结果
     * @param signature 签名
     * @param timestamp 时间戳
     * @param content 内容
     * @return
     */
    String callbackCertification(String signature, String timestamp, String content) throws Exception;
    /**
     * 电子签回调接口->6）合同状态回调地址：跟踪企业签约文件的签署状态和进度；
     * @param signature 签名
     * @param timestamp 时间戳
     * @param content 内容
     * @return
     */
    String status(String signature, String timestamp, String content) throws Exception;
    /**
     * 电子签回调接口->7）CAS验证地址：若选择单点登录集成方案，用于单点登录集成的Ticket校验；
     * @param ticket 加密内容
     * @return
     */
    Map<String, Object> verification(String ticket);
    /**
     * 电子签回调接口 ->8）平台登录地址：若选择单点登录集成方案，用于单点登录集成Ticket校验失败的重定向地址；
     * @param signature 签名
     * @param timestamp 时间戳
     * @param content 内容
     * @return
     */
    String login(String signature, String timestamp, String content) ;

    void test(Long id,Integer type) throws Exception;

    /**
     * 发送契约锁合同
     * @param contractId 合同Id
     * @param hasReserve 是否预占
     */
    String send(Long contractId,Boolean hasReserve);
    /**
     * 发送契约锁合同
     * @param carId 车辆Id
     */
    List<QYSContractVO> ContractEcho(Long carId, String type);
    /**
     * 获取收车合同/委托合同
     * @param carId 车辆Id
     */
    List<QYSContractVO> FindQYSContract(Long carId);

    /**
     * 获取到契约锁单点登录地址
     * @param pageType 车辆Id
     * @param contractId 合同Id
     * @return
     */
    String getSsoUrl(String pageType,Long contractId);

    /**
     * 企业认证
     * @param userId 注册用户id
     * @return
     */
    void companyAuth(Long userId) throws FileNotFoundException;
    /**
     * 个人认证
     * @param userId 注册用户id
     * @return
     */
    void userAuth(Long userId);

    /**
     * 用户认证消息
     * @param message 用户认证消息
     * @return
     */
    void userAuthResult(UserAuthMessage message);
    /**
     * 企业授权
     *
     * @param signature 签名
     * @param timestamp 时间戳
     * @param content   内容
     * @return
     */
    String callBackPrivilege(String signature, String timestamp, String content) throws Exception;
    /**
     * 印章授权静默签
     *
     * @param signature 签名
     * @param timestamp 时间戳
     * @param content   内容
     * @return
     */
    String callBackSealSignAuth(String signature, String timestamp, String content) throws Exception;


    /**
     *  通过部门Id获取契约锁配置
     * @param businessId 业务表id（部门表Id）
     * @return
     */
    QysConfigDO getByDeptId(Long businessId);

    /**
     * 静默签章
     * @param contractId  契约锁合同id
     */
    void companySign(Long contractId);

    /**
     * 下载合同文档
     * @param documentId 合同文档id
     */
    byte[] documentDownload(Long documentId) throws Throwable;

    /**
     * 收车公允价值合理时，处理保证金预占和收车委托合同静默签章
     * @param contractId
     */
    void companyGyhlSign(Long contractId);

    /**
     * 个人认证
     *
     * @param signature
     * @param timestamp
     * @param content
     * @return
     */
    String callbackCertificationPerson(String signature, String timestamp, String content) throws Exception;

    /**
     * 契约锁员工删除
     */
    void employeeRemove(Long deptId,String mobile,String userName);

    /**
     * 契约锁员工创建
     */
    void employeeCreate(Long deptId,String mobile,String userName,Boolean isRole);
    /**
     * 契约锁员工删除
     */
    void employeeRemove(QysConfigDO configDO,String mobile,String userName);

    /**
     * 契约锁员工创建
     */
    void employeeCreate(QysConfigDO configDO,String mobile,String userName,Boolean isRole);


    Map addAccount(@Valid AddAccountDTO reqVO);

    int deleteAccount(Long id);

    /**
     * 公司签署作废合同
     *
     * @param contractId 契约锁合同id
     */
    void companyContractInvalidSign(Long contractId);

}
