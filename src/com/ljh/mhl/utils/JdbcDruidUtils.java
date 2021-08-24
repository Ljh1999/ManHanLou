package com.ljh.mhl.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * 基于Druid连接池的工具类
 */
@SuppressWarnings("ALL")
public class JdbcDruidUtils {
    private static DataSource dataSource;

    static {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("src\\druid.properties"));
            dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //得到连接
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    //关闭连接：在数据库连接池技术中，close不是真的断掉连接，而是把使用的Connection放回连接池
   public static void close(ResultSet resultSet, Statement statement,Connection connection){
       try {
           if(resultSet != null){
               resultSet.close();
           }
           if(statement != null){
               statement.close();
           }
           if(connection != null){
               connection.close();
           }
       } catch (SQLException e) {
          throw new RuntimeException(e);
       }
   }
}
