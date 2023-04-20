package com.newtouch.uctp.module.business.service.qys;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.qys.vo.QysConfigCreateReqVO;
import com.newtouch.uctp.module.business.controller.app.qys.vo.QysConfigPageReqVO;
import com.newtouch.uctp.module.business.controller.app.qys.vo.QysConfigUpdateReqVO;
import com.newtouch.uctp.module.business.dal.dataobject.qys.QysConfigDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

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
    String certification(String signature, String timestamp, String content) throws Exception;
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
     * @param signature 签名
     * @param timestamp 时间戳
     * @param content 内容
     * @return
     */
    String verification(String ticket) throws Exception;
    /**
     * 电子签回调接口 ->8）平台登录地址：若选择单点登录集成方案，用于单点登录集成Ticket校验失败的重定向地址；
     * @param signature 签名
     * @param timestamp 时间戳
     * @param content 内容
     * @return
     */
    String login(String signature, String timestamp, String content) throws Exception;

    void test();

    /**
     * 发送契约锁合同
     * @param carId 车辆Id
     */
    void send(Long carId);
}
