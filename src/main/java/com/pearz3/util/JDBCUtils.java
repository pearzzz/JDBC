package com.pearz3.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCUtils {
    public static Connection getConnection() throws IOException, ClassNotFoundException, SQLException {
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
        return conn;
    }

    public static void closeResource(Connection conn, Statement statement) {
        try {
            if (statement != null)
                statement.close();
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
