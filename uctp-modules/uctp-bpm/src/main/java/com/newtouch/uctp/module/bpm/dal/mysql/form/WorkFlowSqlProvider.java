package com.newtouch.uctp.module.bpm.dal.mysql.form;

import org.apache.ibatis.jdbc.SQL;

/**
 * @author helong
 * @date 2023/4/10 17:08
 */
public class WorkFlowSqlProvider {

    public String deleteWorkFlowMain(String tableName) {
        return new SQL() {{
                DELETE_FROM(tableName);
                WHERE("id = #{arg1}");
        }}.toString();
    }

    public String deleteWorkFlowDetail(String tableName) {
        return new SQL() {{
            DELETE_FROM(tableName);
                WHERE("business_id = #{arg1}");
        }}.toString();
    }
}
