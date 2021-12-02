package com.pearz3.preparedstatement.crud;

import com.pearz3.util.JDBCUtils;
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
    public void testCommonUpdate() {
        //增
//        String sql = "insert user(name,password,address,phone) value(?,?,?,?)";
//        update(sql, "pearz", "123456", "Xian", "12306");

        //删
//        String sql = "delete from customers where id=?";
//        update(sql, 4);

        //改
        String sql = "update `order` set order_name=? where order_id=?";
        update(sql, "MM", 4);

    }

    //通用的增删改操作
    public void update(String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1.获取数据库的连接
            conn = JDBCUtils.getConnection();
            //2.预编译sql语句，返回PreparedStatement的实例
            ps = conn.prepareStatement(sql);
            //3.填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            //4.执行
            ps.execute();
        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            //5.资源的关闭
            JDBCUtils.closeResource(conn, ps);
        }
    }

    //修改customers表的一条记录
    @Test
    public void testUpdate() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1.获取数据库的连接
            conn = JDBCUtils.getConnection();
            //2.预编译sql语句，返回PreparedStatement的实例
            String sql = "update customers set name = ? where id = ?";
            ps = conn.prepareStatement(sql);
            //3.填充占位符
            ps.setObject(1, "redz");
            ps.setObject(2, 4);
            //4.执行
            ps.execute();
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            //5.资源的关闭
            JDBCUtils.closeResource(conn, ps);
        }

    }

    //在customers中插入一条语句
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
