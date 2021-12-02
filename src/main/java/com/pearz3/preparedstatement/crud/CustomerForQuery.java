package com.pearz3.preparedstatement.crud;

import com.pearz3.bean.Customer;
import com.pearz3.util.JDBCUtils;
import org.junit.Test;

import java.io.IOException;
import java.lang.management.PlatformLoggingMXBean;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.Date;

public class CustomerForQuery {

    @Test
    public void testQueryForCustomers() {
        String sql = "select name,email from customers where id = ?";
        Customer customer = queryForCustomers(sql, 2);
        System.out.println(customer);
    }

    /**
     * @Description 针对customers表的通用的查询操作
     * @author pearz
     * @Date 19:26 2021/12/2
     **/
    public Customer queryForCustomers(String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //1.获取数据库连接
            conn = JDBCUtils.getConnection();

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

                Customer customer = new Customer();

                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);
                    String columnName = rsmd.getColumnName(i + 1);

                    //6.通过反射，给对象相应的属性赋值
                    Field field = Customer.class.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(customer, columnValue);
                }
                return customer;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //7.关闭资源
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;
    }

    @Test
    public void testQuery1() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            conn = JDBCUtils.getConnection();

            String sql = "select id,name,email,birth from customers where id=?";
            ps = conn.prepareStatement(sql);
            ps.setObject(1, 1);

            resultSet = ps.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                Date date = resultSet.getDate(4);

                Customer customer = new Customer(id, name, email, date);

                System.out.println(customer);
            }
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, resultSet);
        }
    }
}
