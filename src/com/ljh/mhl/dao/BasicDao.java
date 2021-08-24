package com.ljh.mhl.dao;


import com.ljh.mhl.utils.JdbcDruidUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@SuppressWarnings("ALL")
public class BasicDao<T> {
    private QueryRunner queryRunner = new QueryRunner();

    //开发通用的DML方法，返回受影响的行数
    public int dmlUpdate(String sql,Object... parms){
        Connection connection = null;
        try {
            connection = JdbcDruidUtils.getConnection();
            return queryRunner.update(connection, sql, parms);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            JdbcDruidUtils.close(null,null,connection);
        }
    }
    //查询多条记录
    public  List<T> queryMultiply(String sql,Class<T> clszz,Object... parms){
        Connection connection = null;
        try {
            connection = JdbcDruidUtils.getConnection();
            return  queryRunner.query(connection, sql, new BeanListHandler<T>(clszz), parms);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            JdbcDruidUtils.close(null,null,connection);
        }
    }
    //查询单行记录
    public  T querySingle(String sql,Class<T> clszz,Object... parms){
        Connection connection = null;
        try {
            connection = JdbcDruidUtils.getConnection();
            return  queryRunner.query(connection, sql, new BeanHandler<T>(clszz), parms);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            JdbcDruidUtils.close(null,null,connection);
        }
    }
    

    //查询单行单列记录
    public Object queryScalar(String sql,Object... parms){
        Connection connection = null;
        try {
            connection = JdbcDruidUtils.getConnection();
            return  queryRunner.query(connection, sql, new ScalarHandler<>(), parms);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            JdbcDruidUtils.close(null,null,connection);
        }
    }
}
