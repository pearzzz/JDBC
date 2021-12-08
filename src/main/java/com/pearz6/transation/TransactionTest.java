package com.pearz6.transation;

import com.pearz.bean.User;
import com.pearz.util.OldJDBCUtils;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;

/**
 * @Description
 * @Author pearz
 * @Email zhaihonghao317@163.com
 * @Date 18:47 2021/12/6
 */
public class TransactionTest {

    @Test
    public void testTransactionSelect() throws SQLException, IOException, ClassNotFoundException {
        Connection conn = OldJDBCUtils.getConnection();
        //获取当前连接的隔离级别
        System.out.println(conn.getTransactionIsolation());
        //设置当前连接的隔离级别
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        //取消自动提交数据
        conn.setAutoCommit(false);

        String sql = "select user,password,balance from user_table where user = ?";
        User user = getInstance(conn, User.class, sql, "CC");
        System.out.println(user);
    }

    /**
     * 通用的查询操作，用于返回数据表中的一条数据（version 2.0，考虑事务） conn不能每次都获取,否则就不是一个事务了
    **/
    public <T> T getInstance(Connection conn, Class<T> clazz, String sql, Object ...args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //2.预编译sql语句，得到PreparedStatement对象
            ps = conn.prepareStatement(sql);

            //3.填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            //4.执行executeQuery()，得到结果集ResultSet
            rs = ps.executeQuery();


            if (rs.next()) {
                //5.得到结果集的元数据ResultSetMetaData
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();

                T t = clazz.newInstance();

                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //6.通过反射，给对象相应的属性赋值
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //7.关闭资源
            OldJDBCUtils.closeResource(null, ps, rs);
        }

        return null;
    }

    /**
     * 模拟转账：AA向BB转100元
    **/
    @Test
    public void  testUpdateWithTx() {
        Connection conn = null;
        try {
            conn = OldJDBCUtils.getConnection();
            //1.取消数据的自动提交
            conn.setAutoCommit(false);
            String sql = "update user_table set balance = balance - 100 where user = ?";
            update(conn, sql, "AA");

            //模拟网络异常
//            int num = 10 / 0;

            String sql1 = "update user_table set balance = balance + 100 where user = ?";
            update(conn, sql1, "BB");
            System.out.println("转账成功");

            //2.提交数据
            conn.commit();
        } catch (IOException | SQLException | ClassNotFoundException e) {
            //3.回滚数据
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            //修改气味自动提交数据
            //主要针对于使用数据库连接池使用
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            OldJDBCUtils.closeResource(conn, null);
        }
    }

    //********************考虑数据库事务********************
    /**
     * 通用的增删改操作---version 2.0
    **/
    public int update(Connection conn, String sql, Object... args) {
        PreparedStatement ps = null;
        try {
            //2.预编译sql语句，返回PreparedStatement的实例
            ps = conn.prepareStatement(sql);
            //3.填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            //4.执行
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //5.资源的关闭
            OldJDBCUtils.closeResource(null, ps);
        }
        return 0;
    }

    //********************未考虑数据库事务********************
    /**
     * 通用的增删改操作---version 1.0
    **/
    public int update(String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1.获取数据库的连接
            conn = OldJDBCUtils.getConnection();
            //2.预编译sql语句，返回PreparedStatement的实例
            ps = conn.prepareStatement(sql);
            //3.填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            //4.执行
            ps.executeUpdate();
        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            //5.资源的关闭
            OldJDBCUtils.closeResource(conn, ps);
        }
        return 0;
    }
}
