package com.newland.spring.request.log.jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by garfield on 2018/1/19.
 */
@Component
public class DbConnect {

    private static DruidDataSource dataSourceLog =null;

    private static Connection connection = null;

    private static Statement statement = null;


    private final static Logger logger = LoggerFactory.getLogger(DbConnect.class);

    public String db;

    @Value("${jdbc.url:no_value_found}")
    private String dbUrl;

    @Value("${jdbc.user:no_value_found}")
    private String dbUser;

    @Value("${jdbc.password:no_value_found}")
    private String dbPassword;

    @Value("${jdbc.driverClass:no_value_found}")
    private String dbDriver;

    @Value("${jdbc.validationQuery:no_value_found}")
    private String dbValidation;

    /**
     * 构造函数完成数据库的连接和连接对象的生成
     */
    public DbConnect(){

    }

    public void GetDbConnectLog() {
        try{

            if(dataSourceLog ==null){

                dataSourceLog =new DruidDataSource();

                //设置连接参数
                dataSourceLog.setUrl(dbUrl);
                dataSourceLog.setDriverClassName(dbDriver);
                dataSourceLog.setUsername(dbUser);
                dataSourceLog.setPassword(dbPassword);
                //配置初始化大小、最小、最大
                dataSourceLog.setInitialSize(1);
                dataSourceLog.setMinIdle(1);
                dataSourceLog.setMaxActive(20);
                //连接泄漏监测
                dataSourceLog.setRemoveAbandoned(true);
                dataSourceLog.setRemoveAbandonedTimeout(30);
                //配置获取连接等待超时的时间
                dataSourceLog.setMaxWait(20000);
                //配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
                dataSourceLog.setTimeBetweenEvictionRunsMillis(20000);
                //防止过期
                dataSourceLog.setValidationQuery(dbValidation);
                dataSourceLog.setTestWhileIdle(true);
                dataSourceLog.setTestOnBorrow(true);

            }

        }catch(Exception e){
            logger.error("connect db error:",e);
        }
    }

    public Connection getConnect(String str){


        this.db=str;
        for (int i = 0; i < 3; i++) {
            try {
                if(db.equals("LOG")){
                    GetDbConnectLog();
                    connection= dataSourceLog.getConnection();
                    return connection;
                }
            } catch (SQLException e) {
                logger.error("get connect error:",e);
                logger.error("set dataSourceLog null and retry");
                dataSourceLog = null;
            }
        }
        return connection;
    }

    public Statement getStatement() throws SQLException {
        return connection.createStatement();
    }

    public void destroy() throws SQLException {
        if(statement != null){
            statement.close();
        }
//        if(connection != null){
//            connection.close();
//        }
//        if(dataSourceLog != null){
//            dataSourceLog.close();
//            dataSourceLog = null;
//        }

    }
}
