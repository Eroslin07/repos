package com.newtouch.uctp.module.bpm.dal.mysql.definition;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.lang.Nullable;

import com.newtouch.uctp.framework.mybatis.core.mapper.BaseMapperX;
import com.newtouch.uctp.framework.mybatis.core.query.QueryWrapperX;
import com.newtouch.uctp.module.bpm.dal.dataobject.definition.BpmTaskAssignRuleDO;

@Mapper
public interface BpmTaskAssignRuleMapper extends BaseMapperX<BpmTaskAssignRuleDO> {

    default List<BpmTaskAssignRuleDO> selectListByProcessDefinitionId(String processDefinitionId,
                                                                      @Nullable String taskDefinitionKey) {
        return selectList(new QueryWrapperX<BpmTaskAssignRuleDO>()
                .eq("process_definition_id", processDefinitionId)
                .eqIfPresent("task_definition_key", taskDefinitionKey));
    }

    default List<BpmTaskAssignRuleDO> selectListByModelId(String modelId) {
        return selectList(new QueryWrapperX<BpmTaskAssignRuleDO>()
                .eq("model_id", modelId)
                .eq("process_definition_id", BpmTaskAssignRuleDO.PROCESS_DEFINITION_ID_NULL));
    }

    default BpmTaskAssignRuleDO selectListByModelIdAndTaskDefinitionKey(String modelId,
                                                                        String taskDefinitionKey) {
        return selectOne(new QueryWrapperX<BpmTaskAssignRuleDO>()
                .eq("model_id", modelId)
                .eq("process_definition_id", BpmTaskAssignRuleDO.PROCESS_DEFINITION_ID_NULL)
                .eq("task_definition_key", taskDefinitionKey));
    }

    List<String> getTaskAssignRulesByRoleCode(@Param("roleCode") String roleCode);
}
