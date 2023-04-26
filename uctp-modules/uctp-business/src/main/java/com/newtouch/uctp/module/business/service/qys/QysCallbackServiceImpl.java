package com.newtouch.uctp.module.business.service.qys;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.qys.vo.QysCallbackCreateReqVO;
import com.newtouch.uctp.module.business.controller.app.qys.vo.QysCallbackPageReqVO;
import com.newtouch.uctp.module.business.controller.app.qys.vo.QysCallbackUpdateReqVO;
import com.newtouch.uctp.module.business.convert.qys.QysCallbackConvert;
import com.newtouch.uctp.module.business.dal.dataobject.dept.DeptDO;
import com.newtouch.uctp.module.business.dal.dataobject.qys.QysCallbackDO;
import com.newtouch.uctp.module.business.dal.mysql.qys.QysCallbackMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static com.newtouch.uctp.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.newtouch.uctp.module.business.enums.ErrorCodeConstants.QYS_CALLBACK_NOT_EXISTS;

/**
 * 契约锁回调日志 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class QysCallbackServiceImpl implements QysCallbackService {

    @Resource
    private QysCallbackMapper qysCallbackMapper;

    @Override
    public Long createQysCallback(QysCallbackCreateReqVO createReqVO) {
        // 插入
        QysCallbackDO qysCallback = QysCallbackConvert.INSTANCE.convert(createReqVO);
        qysCallbackMapper.insert(qysCallback);
        // 返回
        return qysCallback.getId();
    }

    @Override
    public void updateQysCallback(QysCallbackUpdateReqVO updateReqVO) {
        // 校验存在
        validateQysCallbackExists(updateReqVO.getId());
        // 更新
        QysCallbackDO updateObj = QysCallbackConvert.INSTANCE.convert(updateReqVO);
        qysCallbackMapper.updateById(updateObj);
    }

    @Override
    public void deleteQysCallback(Long id) {
        // 校验存在
        validateQysCallbackExists(id);
        // 删除
        qysCallbackMapper.deleteById(id);
    }

    private void validateQysCallbackExists(Long id) {
        if (qysCallbackMapper.selectById(id) == null) {
            throw exception(QYS_CALLBACK_NOT_EXISTS);
        }
    }

    @Override
    public QysCallbackDO getQysCallback(Long id) {
        return qysCallbackMapper.selectById(id);
    }

    @Override
    public List<QysCallbackDO> getQysCallbackList(Collection<Long> ids) {
        return qysCallbackMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<QysCallbackDO> getQysCallbackPage(QysCallbackPageReqVO pageReqVO) {
        return qysCallbackMapper.selectPage(pageReqVO);
    }

    @Override
    public List<QysCallbackDO> getByMainIdAndType(Long mainId, Integer type) {
        return qysCallbackMapper.getByMainIdAndType(mainId,type);
//        QysCallbackDO qysCallback = qysCallbackDOS.get(0);
//        QysCallBackType callBackType = QysCallBackType.toType(qysCallback.getType());
//        switch (callBackType){
//            case COMPANY_AUTH:
//                return JSON.parseObject(qysCallback.getContent(), CompanyAuthDTO.class);
//            case AUTH_RETURN:
//                //TODO 后面的对象后面有空在加
//                return null;
//            case PRIVATIZED:
//                return null;
//            case CONTRACT_STATUS:
//                return null;
//            case SIGNATURE:
//                return null;
//            case PERSONAL_AUTH:
//                return null;
//            case PERSONAL_SIGNATURE_AUTH:
//                return null;
//            default:
//                return null;
//
//        }

    }

    @Override
    public void saveDO(String content, Integer type,Long deptId) {
        QysCallbackDO qysCallbackDO = new QysCallbackDO();
        qysCallbackDO.setContent(content);
        //目前根据saas文档来的
        qysCallbackDO.setType(type);
        if (ObjectUtil.isNotNull(deptId)) {
            qysCallbackDO.setMainId(deptId);
        }
        qysCallbackMapper.insert(qysCallbackDO);
    }


}
