package com.newtouch.uctp.module.bpm.service.task;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newtouch.uctp.framework.common.exception.ServiceException;
import com.newtouch.uctp.framework.mybatis.core.mapper.BaseMapperX;
import com.newtouch.uctp.module.bpm.annotation.WfEntity;
import com.newtouch.uctp.module.bpm.dal.mysql.form.BpmFormMainMapper;

/**
 * @author helong
 * @date 2023/4/8 16:43
 */
@Service
@Validated
@Slf4j
public class BpmFormDataServiceImpl implements BpmFormDataService {
    private final Map<String, Class<?>> workFlowEntityClassMap = new HashMap<String, Class<?>>();

    @Value("${mybatis-plus.type-aliases-package}")
    private String typeAliasesPackage;
    @Resource
    private BpmFormMainMapper bpmFormMainMapper;

    @PostConstruct
    public void init() {
        log.info("---------------------- [开始扫描：工作流表单数据存储相关实体] ----------------------");
        // 1.解析实体类集合中带@WfEntity注解的类
        Set<Class<?>> entityClassSet = ClassUtil.scanPackageByAnnotation(typeAliasesPackage, WfEntity.class);
        if (ObjectUtil.isNotEmpty(entityClassSet)) {
            for (Class<?> entityClass:entityClassSet) {
                WfEntity wfEntity = entityClass.getAnnotation(WfEntity.class);
                String alias = wfEntity.alias();
                this.workFlowEntityClassMap.put(alias, entityClass);
                log.info(alias + " : " + entityClass);
            }
        }
        log.info("---------------------- [结束扫描：工作流表单数据存储相关实体] ----------------------");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveDataObject(Long businessKey, Map<String, Object> formDataObject) {
        if (ObjectUtil.isNull(businessKey)) {
            throw new ServiceException(1009003005, "流程业务ID不能为空");
        }

        String workFlowMainEntityAlias = this.getObjectMainEntityAlias(formDataObject);
        if (!StringUtils.hasText(workFlowMainEntityAlias)) {
            throw new ServiceException(1009003006, "流程业务主信息无法保存");
        }

        for (Map.Entry<String, Object> entry:formDataObject.entrySet()) {
            String entityAlias = StringUtils.trimAllWhitespace(entry.getKey());
            Class<?> entityClass = this.workFlowEntityClassMap.get(entityAlias);
            Object entityObjectValue = entry.getValue();
            if (ObjectUtil.equals(workFlowMainEntityAlias, entityAlias)) {
                this.bpmFormMainMapper.deleteWorkFlowMain(this.getEntityTableName(entityAlias), businessKey);
                Map<String, Object> entityObjectMap = (Map<String, Object>) entityObjectValue;
                Map<String, Object> newEntityMap = this.getEntityMap(entityObjectMap, entityClass);
                newEntityMap.put("id", businessKey);
                Object entity = BeanUtil.toBean(newEntityMap, entityClass);
                this.getBaseMapperX(entityAlias).insert(entity);
            } else if (entityObjectValue instanceof Map) {
                this.bpmFormMainMapper.deleteWorkFlowDetail(this.getEntityTableName(entityAlias), businessKey);
                Map<String, Object> entityObjectMap = (Map<String, Object>) entityObjectValue;
                Map<String, Object> newEntityMap = this.getEntityMap(entityObjectMap, entityClass);
                newEntityMap.put("businessId", businessKey);
                Object entity = BeanUtil.toBean(newEntityMap, entityClass);
                this.getBaseMapperX(entityAlias).insert(entity);
            } else if (entityObjectValue instanceof ArrayList) {
                this.bpmFormMainMapper.deleteWorkFlowDetail(this.getEntityTableName(entityAlias), businessKey);
                ArrayList entityArrayList = (ArrayList) entityObjectValue;
                for (int i = 0; i < entityArrayList.size(); i++) {
                    Map<String, Object> entityObjectMap = (Map<String, Object>) entityArrayList.get(i);
                    Map<String, Object> newEntityMap = this.getEntityMap(entityObjectMap, entityClass);
                    newEntityMap.put("businessId", businessKey);
                    Object entity = BeanUtil.toBean(newEntityMap, entityClass);
                    this.getBaseMapperX(entityAlias).insert(entity);
                }
            } else {
                throw new ServiceException(1009003007, "流程业务表单信息传递格式有误，暂不支持");
            }
        }

        return businessKey;
    }

    public Map<String, Object> getObjectMainEntityMap(Map<String, Object> formDataObject) {
        String workFlowMainEntityAlias = this.getObjectMainEntityAlias(formDataObject);
        if (!StringUtils.hasText(workFlowMainEntityAlias)) {
            throw new ServiceException(1009003006, "流程业务主信息无法保存");
        }

        return (Map<String, Object>) formDataObject.get(workFlowMainEntityAlias);
    }

    public String getObjectMainEntityAlias(Map<String, Object> formDataObject) {
        for (String alias : formDataObject.keySet()) {
            Class<?> entityClass = this.workFlowEntityClassMap.get(alias);
            WfEntity wfEntity = entityClass.getAnnotation(WfEntity.class);
            if (wfEntity.isMainEntity()) {
                return StringUtils.trimAllWhitespace(alias);
            }
        }

        return null;
    }

    public String getEntityTableName(String entityAlias) {
        Class<?> entityClass = this.workFlowEntityClassMap.get(entityAlias);
        TableName tableNameClass = entityClass.getAnnotation(TableName.class);

        return tableNameClass.value();
    }

    public BaseMapperX getBaseMapperX(String entityAlias) {
        Class<?> entityClass = this.workFlowEntityClassMap.get(entityAlias);
        WfEntity wfEntity = entityClass.getAnnotation(WfEntity.class);
        Class mapperClass = wfEntity.mapperClass();

        return (BaseMapperX) SpringUtil.getBean(mapperClass);
    }

    private Map<String, Field> getEntityFields(Class<?> entityClass)
    {
        Map<String, Field> entityFields = new HashMap<>();
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field:fields)
        {
            String fieldName = field.getName();
            entityFields.put(fieldName.toLowerCase(), field);
        }

        return entityFields;
    }

    private Map<String, Object> getEntityMap(Map<String, Object> originalEntityMap, Class<?> entityClass) {
        Map<String, Object> newEntityMap = new HashMap<String, Object>();
        Map<String, Field> entityFields = this.getEntityFields(entityClass);
        for (Map.Entry<String, Object> entry:originalEntityMap.entrySet())
        {
            Field field = entityFields.get(entry.getKey().toLowerCase());
            if (Objects.isNull(field))
            {
                continue;
            }
            newEntityMap.put(field.getName(), entry.getValue());
        }

        return newEntityMap;
    }
}
