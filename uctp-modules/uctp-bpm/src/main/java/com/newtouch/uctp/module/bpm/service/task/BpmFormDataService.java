package com.newtouch.uctp.module.bpm.service.task;

import java.util.Map;

/**
 * @author helong
 * @date 2023/4/8 16:39
 */
public interface BpmFormDataService {
    /**
     * 流程表单数据保存
     * @param businessKey     业务ID
     * @param formDataObject    表单数据对象
     * @return
     */
    Long saveDataObject(Long businessKey, Map<String, Object> formDataObject);

    /**
     * 获取流程表单传递的主信息
     * @param formDataObject
     * @return
     */
    Map<String, Object> getObjectMainEntityMap(Map<String, Object> formDataObject);
    /**
     * 获取流程表单传递的主信息实体别名
     * @param formDataObject
     * @return
     */
    String getObjectMainEntityAlias(Map<String, Object> formDataObject);


}
