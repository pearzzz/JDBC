package com.pearz3.preparedstatement.crud;

import com.pearz.bean.Customer;
import com.pearz.bean.Order;
import com.pearz3.preparedstatement.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName PrepareStatementQueryTest
 * @Description
 * @Author pearz
 * @Date 2021/12/2 21:41
 */
public class PrepareStatementQueryTest {

    @Test
    public void testGetForList() {
        String sql = "select id,name,email from customers where id<=?";
        List<Customer> customers = getForList(Customer.class, sql, 6);
        customers.forEach(System.out::println);

        String sql1 = "select order_name orderName,order_date orderDate from `order` where order_id<?";
        List<Order> orders = getForList(Order.class, sql1, 3);
        orders.forEach(System.out::println);
    }

    public <T> List<T> getForList(Class<T> clazz, String sql, Object ...args) {
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

            ArrayList<T> list = new ArrayList<T>();
            while (rs.next()) {
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
                list.add(t);
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //7.关闭资源
            JDBCUtils.closeResource(conn, ps, rs);
        }

        return null;
    }

    @Test
    public void testGetInstance() {
        String sql = "select id,name,email,birth from customers where id=?";
        Customer customer = getInstance(Customer.class, sql, 8);
        System.out.println(customer);

        String sql1 = "select order_name orderName,order_date orderDate from `order` where order_id=?";
        Order order = getInstance(Order.class, sql1, 2);
        System.out.println(order);
    }

    //通用的查询操作，用于返回数据表中的一条数据
    public <T> T getInstance(Class<T> clazz, String sql, Object ...args) {
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
            JDBCUtils.closeResource(conn, ps, rs);
        }

        return null;
    }
}
