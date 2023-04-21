package com.newtouch.uctp.module.bpm.dal.mysql.form;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newtouch.uctp.framework.mybatis.core.mapper.BaseMapperX;
import com.newtouch.uctp.module.bpm.controller.admin.task.vo.task.BpmTaskDoneReqVO;
import com.newtouch.uctp.module.bpm.controller.admin.task.vo.task.BpmTaskDoneRespVO;
import com.newtouch.uctp.module.bpm.controller.admin.task.vo.task.BpmTaskTodoReqVO;
import com.newtouch.uctp.module.bpm.controller.admin.task.vo.task.BpmTaskTodoRespVO;
import com.newtouch.uctp.module.bpm.dal.dataobject.form.BpmFormMainDO;

/**
 * @author helong
 * @date 2023/4/8 16:08
 */
@Mapper
public interface BpmFormMainMapper extends BaseMapperX<BpmFormMainDO> {

    /**
     * 物理删除流程业务主表数据
     * @param tableName
     * @param businessKey
     * @return
     */
    @DeleteProvider(type = WorkFlowSqlProvider.class, method = "deleteWorkFlowMain")
    int deleteWorkFlowMain(String tableName, Long businessKey);
    /**
     * 物理删除流程业务子表数据
     * @param tableName
     * @param businessKey
     * @return
     */
    @DeleteProvider(type = WorkFlowSqlProvider.class, method = "deleteWorkFlowDetail")
    int deleteWorkFlowDetail(String tableName, Long businessKey);


    Page<BpmTaskTodoRespVO> getBpmTaskTodo(Page<BpmTaskTodoRespVO> page, @Param("todoReqVO") BpmTaskTodoReqVO todoReqVO, @Param("loginUserId")Long loginUserId);

    Page<BpmTaskDoneRespVO> getBpmTaskDone(Page<BpmTaskDoneRespVO> page, @Param("doneReqVO") BpmTaskDoneReqVO doneReqVO, @Param("loginUserId")Long loginUserId);
}
