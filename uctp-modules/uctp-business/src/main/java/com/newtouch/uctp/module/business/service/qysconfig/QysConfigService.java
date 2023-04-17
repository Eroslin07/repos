package com.newtouch.uctp.module.business.service.qysconfig;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.qiyuesuo.vo.QysConfigCreateReqVO;
import com.newtouch.uctp.module.business.controller.app.qiyuesuo.vo.QysConfigPageReqVO;
import com.newtouch.uctp.module.business.controller.app.qiyuesuo.vo.QysConfigUpdateReqVO;
import com.newtouch.uctp.module.business.dal.dataobject.qysconfig.QysConfigDO;

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
     * 电子签回调接口
     * @param signature 签名
     * @param timestamp 时间戳
     * @param content 内容
     * @return
     */
    String callback(String signature, String timestamp, String content);
}
