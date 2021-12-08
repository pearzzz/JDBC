package com.pearz1.connection;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionTest {


    /**
     * 方式一
    **/
    @Test
    public void testConnection1() throws SQLException {
        Driver driver = new com.mysql.jdbc.Driver();

        //jdbc:mysql:协议
        //localhost:ip地址
        //3306:端口号
        //test:数据库名
        String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8";

        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "root");

        Connection conn = driver.connect(url, properties);

        System.out.println(conn);
    }

    /**
     * 方式二
    **/
    @Test
    public void testConnection2() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        //使用反射获取Driver实现类对象
        Class aClass = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) aClass.newInstance();

        String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8";

        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "root");

        Connection conn = driver.connect(url, properties);

        System.out.println(conn);
    }

    /**
     * 方式三
    **/
    @Test
    public void testConnection3() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

        Class aClass = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) aClass.newInstance();
        DriverManager.registerDriver(driver);

        String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8";
        String user = "root";
        String password = "root";
        Connection conn = DriverManager.getConnection(url, user, password);

        System.out.println(conn);
    }

    /**
     * 方式四
    **/
    @Test
    public void testConnection4() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

        /*
        在mysql的Driver类中，声明了如下操作：
            static {
            try {
                DriverManager.registerDriver(new Driver());
            } catch (SQLException var1) {
                throw new RuntimeException("Can't register driver!");
            }
        }
         */
        Class.forName("com.mysql.jdbc.Driver");
//        Driver driver = (Driver) aClass.newInstance();
//        DriverManager.registerDriver(driver);

        String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8";
        String user = "root";
        String password = "root";
        Connection conn = DriverManager.getConnection(url, user, password);

        System.out.println(conn);
    }

    /**
     * 方式五（最终版）：将数据库连接需要的4个基本信息声明在配置文件中，通过读取配置文件的方式，获取连接
    **/
    @Test
    public void getConnection5() throws IOException, ClassNotFoundException, SQLException {

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
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println(conn);
    }

}
