package com.pearz8.connectionpool;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.util.Properties;

/**
 * @Description
 * @Author pearz
 * @Email zhaihonghao317@163.com
 * @Date 11:01 2021/12/8
 */
public class DruidTest {

    @Test
    public void getConnection() throws Exception {
        Properties pros = new Properties();
        FileInputStream is = new FileInputStream(new File("src/main/resources/mapper/druid.properties"));
        pros.load(is);
        DataSource source = DruidDataSourceFactory.createDataSource(pros);

        Connection conn = source.getConnection();
        System.out.println(conn);
    }
}
