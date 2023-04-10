package com.newtouch.uctp.module.bpm.util;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.annotation.Resource;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.newtouch.uctp.module.bpm.util.core.ColumnCamelCaseMapRowMapper;

/**
 * @author helong
 * @date 2022/12/8 1:22
 */
@Component
@Transactional(readOnly = true)
public class JdbcTemplateUtils {

    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return namedParameterJdbcTemplate;
    }

    /**
     * 获取数据库连接对象
     * @return
     */
    public Connection getJdbcConnection() {
        return this.jdbcTemplate.execute(new ConnectionCallback<Connection>() {
            @Override
            public Connection doInConnection(Connection con) throws SQLException, DataAccessException {
                return con;
            }
        });
    }

    /**
     * 查询返回单个值
     * @param sql            查询脚本语句（使用“问号”作为条件占位符）
     * @param requiredType   返回值的类型
     * @param args           查询参数值
     * @return
     * @param <T>
     */
    public <T> T queryForSingleObject(String sql, Class<T> requiredType, @Nullable Object... args) {
        List<T> results = this.jdbcTemplate.query(sql, new SingleColumnRowMapper<>(requiredType), args);
        return nullableSingleResult(results);
    }

    /**
     * 查询返回单个值
     * @param sql            查询脚本语句（使用“具名参数”作为条件占位符）
     * @param paramObject    查询参数对象（支持传递实体类对象、Map键值对象）
     * @param requiredType   返回值的类型
     * @return
     * @param <T>
     */
    public <T> T queryForSingleObject(String sql, Object paramObject, Class<T> requiredType) {
        List<T> results = (List<T>) this.query(sql, paramObject, new SingleColumnRowMapper<>(requiredType));
        return nullableSingleResult(results);
    }

    /**
     * 查询返回单个实体类对象
     * @param sql            查询脚本语句（使用“问号”作为条件占位符）
     * @param requiredType   查询转换的实体类
     * @param args           查询参数值
     * @return
     * @param <T>
     */
    public <T> T queryForEntity(String sql, Class<T> requiredType, @Nullable Object... args) {
        List<T> results = this.queryForEntityList(sql, requiredType, args);
        return nullableSingleResult(results);
    }

    /**
     * 查询返回单个实体类对象
     * @param sql             查询脚本语句（使用“具名参数”作为条件占位符）
     * @param paramObject     查询参数对象（支持传递实体类对象、Map键值对象）
     * @param requiredType    查询转换的实体类
     * @return
     * @param <T>
     */
    public <T> T queryForEntity(String sql, Object paramObject, Class<T> requiredType) {
        List<T> results = this.queryForEntityList(sql, paramObject, requiredType);
        return nullableSingleResult(results);
    }

    /**
     * 查询返回单个Map对象
     * @param sql             查询脚本语句（使用“问号”作为条件占位符）
     * @param isToCamelCase   查询结果是否转换为驼峰风格
     * @param args            查询参数值
     * @return
     */
    public Map<String, Object> queryForMap(String sql, boolean isToCamelCase, @Nullable Object... args) {
        List<Map<String, Object>> results = this.jdbcTemplate.query(sql, new ColumnCamelCaseMapRowMapper(isToCamelCase), args);
        return nullableSingleResult(results);
    }

    /**
     * 查询返回单个Map对象
     * @param sql             查询脚本语句（使用“具名参数”作为条件占位符）
     * @param paramObject     查询参数对象（支持传递实体类对象、Map键值对象）
     * @param isToCamelCase   查询结果是否转换为驼峰风格
     * @return
     */
    public Map<String, Object> queryForMap(String sql, Object paramObject, boolean isToCamelCase) {
        List<Map<String, Object>> results = this.queryForMapList(sql, paramObject, isToCamelCase);
        return nullableSingleResult(results);
    }

    /**
     * 查询返回实体类对象集合
     * @param sql              查询脚本语句（使用“问号”作为条件占位符）
     * @param elementType      查询转换的实体类
     * @param args             查询参数值
     * @return
     * @param <T>
     */
    public <T> List<T> queryForEntityList(String sql, Class<T> elementType, @Nullable Object... args) {
        return this.jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(elementType), args);
    }

    /**
     * 查询返回实体类对象集合
     * @param sql               查询脚本语句（使用“具名参数”作为条件占位符）
     * @param paramObject       查询参数对象（支持传递实体类对象、Map键值对象）
     * @param elementType       查询转换的实体类
     * @return
     * @param <T>
     */
    public <T> List<T> queryForEntityList(String sql, Object paramObject, Class<T> elementType) {
        return (List<T>) this.query(sql, paramObject, new BeanPropertyRowMapper<>(elementType));
    }

    /**
     * 查询返回Map对象集合
     * @param sql             查询脚本语句（使用“问号”作为条件占位符）
     * @param isToCamelCase   查询结果是否转换为驼峰风格
     * @param args            查询参数值
     * @return
     */
    public List<Map<String, Object>> queryForMapList(String sql, boolean isToCamelCase, @Nullable Object... args) {
        List<Map<String, Object>> results = this.jdbcTemplate.query(sql, new ColumnCamelCaseMapRowMapper(isToCamelCase), args);
        return results;
    }

    /**
     * 查询返回Map对象集合
     * @param sql              查询脚本语句（使用“具名参数”作为条件占位符）
     * @param paramObject      查询参数对象（支持传递实体类对象、Map键值对象）
     * @param isToCamelCase    查询结果是否转换为驼峰风格
     * @return
     */
    public List<Map<String, Object>> queryForMapList(String sql, Object paramObject, boolean isToCamelCase) {
        return (List<Map<String, Object>>) this.query(sql, paramObject, new ColumnCamelCaseMapRowMapper(isToCamelCase));
    }

    /**
     * 使用“具名参数”方式查询的通用查询方法
     * @param sql              查询脚本语句（使用“具名参数”作为条件占位符）
     * @param paramObject      查询参数对象（支持传递实体类对象、Map键值对象）
     * @param rowMapper        查询结果转换映射器
     * @return
     * @param <T>
     */
    public <T> T query(String sql, Object paramObject, RowMapper<T> rowMapper) {
        PreparedStatementCreator psc = this.getPreparedStatementCreator(sql, paramObject);
        return (T) this.jdbcTemplate.query(psc, new RowMapperResultSetExtractor<>(rowMapper));
    }

    /**
     * 通用插入数据操作方法
     * @param sql               脚本语句（使用“问号”作为条件占位符）
     * @param args              参数值
     * @return
     */
    @Transactional
    public int insert(String sql, @Nullable Object... args) {
        return this.update(sql,args);
    }

    /**
     * 通用插入数据操作方法
     * @param sql               脚本语句（使用“具名参数”作为条件占位符）
     * @param paramObject       参数对象（支持传递实体类对象、Map键值对象）
     * @return
     */
    @Transactional
    public int insertObject(String sql, Object paramObject) {
        return this.updateObject(sql,paramObject);
    }

    /**
     * 通用修改操作方法
     * @param sql               脚本语句（使用“问号”作为条件占位符）
     * @param args              参数值
     * @return
     */
    @Transactional
    public int update(String sql, @Nullable Object... args) {
        return this.jdbcTemplate.update(sql, args);
    }

    /**
     * 通用修改操作方法
     * @param sql               脚本语句（使用“具名参数”作为条件占位符）
     * @param paramObject       参数对象（支持传递实体类对象、Map键值对象）
     * @return
     */
    @Transactional
    public int updateObject(String sql, Object paramObject) {
        PreparedStatementCreator psc = this.getPreparedStatementCreator(sql, paramObject);
        return this.jdbcTemplate.update(psc);
    }

    /**
     * 通用删除操作方法
     * @param sql               脚本语句（使用“问号”作为条件占位符）
     * @param args              参数值
     * @return
     */
    @Transactional
    public int delete(String sql, @Nullable Object... args) {
        return this.update(sql,args);
    }

    /**
     * 通用删除操作方法
     * @param sql               脚本语句（使用“具名参数”作为条件占位符）
     * @param paramObject       参数对象（支持传递实体类对象、Map键值对象）
     * @return
     */
    @Transactional
    public int deleteObject(String sql, Object paramObject) {
        return this.updateObject(sql,paramObject);
    }

    /**
     * 根据数据源获取数据库表的所有字段信息
     * @param tableNamePattern   表名
     * @return
     */
    public List<Map<String, Object>> queryTableColumns(String tableNamePattern) {
        List<Map<String, Object>> results = new ArrayList<>();

        ResultSet rs = null;
        try {
            Connection con = DataSourceUtils.getConnection(this.getJdbcTemplate().getDataSource());
            DatabaseMetaData databaseMetaData = con.getMetaData();
            String driverName = databaseMetaData.getDriverName();
            if (driverName.indexOf("MySQL") >= 0) {
                rs = databaseMetaData.getColumns(con.getCatalog(), con.getSchema(), tableNamePattern, null);
            }
            // TODO: 待测试支持更多数据库驱动
            else if (driverName.indexOf("Oracle") >= 0) {

            }

            int rowNum = 0;
            while (rs.next()) {
                results.add(new ColumnCamelCaseMapRowMapper().mapRow(rs, rowNum++));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            JdbcUtils.closeResultSet(rs);
        }

        return results;
    }

    protected PreparedStatementCreator getPreparedStatementCreator(String sql, Object paramObject) {
        try {
            SqlParameterSource paramSource = (paramObject instanceof Map) ? new MapSqlParameterSource((Map<String, ?>) paramObject) : new BeanPropertySqlParameterSource(paramObject);

            return (PreparedStatementCreator) MethodUtils.invokeMethod(this.namedParameterJdbcTemplate, true,
                    "getPreparedStatementCreator",
                    new Object[]{sql, paramSource, null},
                    new Class<?>[]{String.class, SqlParameterSource.class, Consumer.class});
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Nullable
    private static <T> T nullableSingleResult(@Nullable Collection<T> results) throws IncorrectResultSizeDataAccessException {
        if (CollectionUtils.isEmpty(results)) {
            return null;
        }
        if (results.size() > 1) {
            throw new IncorrectResultSizeDataAccessException(1, results.size());
        }
        return results.iterator().next();
    }

}
