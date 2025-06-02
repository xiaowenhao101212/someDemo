package com.xquant.example.appservice;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
public class DruidTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void testDruid() throws SQLException {
        System.out.println("数据源：" + dataSource.getClass());
        Connection connection = dataSource.getConnection();
        System.out.println("连接：" + connection);
        System.out.println("连接池：" + ((DruidDataSource) dataSource).getStatData());
        connection.close();
    }
}
