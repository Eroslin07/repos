package com.newtouch.uctp.module.bpm.util.core;

import cn.hutool.core.util.StrUtil;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.support.JdbcUtils;

/**
 * 参考实现类：{@link ColumnMapRowMapper}
 * @author helong
 * @date 2022/12/8 14:34
 */
public class ColumnCamelCaseMapRowMapper extends ColumnMapRowMapper {

    /**
     * 是否开启转换为驼峰风格
     */
    protected Boolean isToCamelCase = Boolean.TRUE;

    public ColumnCamelCaseMapRowMapper() {
    }

    public ColumnCamelCaseMapRowMapper(Boolean isToCamelCase) {
        this.isToCamelCase = (isToCamelCase == null ? Boolean.TRUE : isToCamelCase);
    }

    @Override
    public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        Map<String, Object> mapOfColumnValues = createColumnMap(columnCount);
        for (int i = 1; i <= columnCount; i++) {
            String column = JdbcUtils.lookupColumnName(rsmd, i);
            if (this.isToCamelCase) {
                mapOfColumnValues.putIfAbsent(StrUtil.toCamelCase(getColumnKey(column).toLowerCase()), getColumnValue(rs, i));
            }
            else {
                mapOfColumnValues.putIfAbsent(getColumnKey(column), getColumnValue(rs, i));
            }
        }
        return mapOfColumnValues;
    }
}
