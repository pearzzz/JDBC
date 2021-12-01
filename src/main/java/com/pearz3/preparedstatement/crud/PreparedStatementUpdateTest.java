package com.pearz3.preparedstatement.crud;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class PreparedStatementUpdateTest {

    @Test
    public void testInsret() {

        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1.读取配置文件
            File file = new File("src/main/java/jdbc.properties");
            FileInputStream is = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(is);

            String driver = properties.getProperty("driver");
            String url = properties.getProperty("url");
            String user = properties.getProperty("user");
            String password = properties.getProperty("password");

            //2.加载驱动
            Class.forName(driver);

            //3.获取连接
            conn = DriverManager.getConnection(url, user, password);

            //4.预编译sql语句，返回PrepareStatement的实例
            String sql = "insert customers(name,email,birth)value(?,?,?)";
            ps = conn.prepareStatement(sql);

            //5.填充占位符
            ps.setString(1, "pearz");
            ps.setString(2, "pearz@gmail.com");
            SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
            Date date = sdf.parse("1998-04-13");
            ps.setDate(3, new java.sql.Date(date.getTime()));

            //6.执行操作
            ps.executeUpdate();
        } catch (IOException | ClassNotFoundException | SQLException | ParseException e) {
            e.printStackTrace();
        } finally {
            //7.资源的关闭
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
