package com.pearz.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.commons.dbutils.DbUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * @author pearz
 */
public class JDBCUtils {

    private static DataSource source;
    static {
        try {
            Properties pros = new Properties();
            FileInputStream is = new FileInputStream(new File("src/main/resources/mapper/druid.properties"));
            pros.load(is);
            source = DruidDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
    public static Connection getConnection() throws SQLException {
        Connection conn = source.getConnection();
        return conn;
    }

    public static void closeResource(Connection conn, Statement statement) {
        DbUtils.closeQuietly(conn);
        DbUtils.closeQuietly(statement);
    }

    public static void closeResource(Connection conn, Statement statement, ResultSet rs) {
        DbUtils.closeQuietly(conn);
        DbUtils.closeQuietly(statement);
        DbUtils.closeQuietly(rs);
    }
}
