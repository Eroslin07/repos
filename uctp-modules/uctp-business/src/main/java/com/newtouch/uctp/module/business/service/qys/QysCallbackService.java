package com.newtouch.uctp.module.business.service.qys;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.qys.vo.QysCallbackCreateReqVO;
import com.newtouch.uctp.module.business.controller.app.qys.vo.QysCallbackPageReqVO;
import com.newtouch.uctp.module.business.controller.app.qys.vo.QysCallbackUpdateReqVO;
import com.newtouch.uctp.module.business.dal.dataobject.qys.QysCallbackDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * 契约锁回调日志 Service 接口
 *
 * @author 芋道源码
 */
public interface QysCallbackService {

    /**
     * 创建契约锁回调日志
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createQysCallback(@Valid QysCallbackCreateReqVO createReqVO);

    /**
     * 更新契约锁回调日志
     *
     * @param updateReqVO 更新信息
     */
    void updateQysCallback(@Valid QysCallbackUpdateReqVO updateReqVO);

    /**
     * 删除契约锁回调日志
     *
     * @param id 编号
     */
    void deleteQysCallback(Long id);

    /**
     * 获得契约锁回调日志
     *
     * @param id 编号
     * @return 契约锁回调日志
     */
    QysCallbackDO getQysCallback(Long id);

    /**
     * 获得契约锁回调日志列表
     *
     * @param ids 编号
     * @return 契约锁回调日志列表
     */
    List<QysCallbackDO> getQysCallbackList(Collection<Long> ids);

    /**
     * 获得契约锁回调日志分页
     *
     * @param pageReqVO 分页查询
     * @return 契约锁回调日志分页
     */
    PageResult<QysCallbackDO> getQysCallbackPage(QysCallbackPageReqVO pageReqVO);


}
