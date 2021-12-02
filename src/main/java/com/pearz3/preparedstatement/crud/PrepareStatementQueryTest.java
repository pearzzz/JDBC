package com.pearz3.preparedstatement.crud;

import com.pearz3.bean.Customer;
import com.pearz3.bean.Order;
import com.pearz3.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * @ClassName PrepareStatementQueryTest
 * @Description
 * @Author pearz
 * @Date 2021/12/2 21:41
 */
public class PrepareStatementQueryTest {

    @Test
    public void testGetInstance() {
        String sql = "select id,name,email,birth from customers where id=?";
        Customer customer = getInstance(Customer.class, sql, 8);
        System.out.println(customer);

        String sql1 = "select order_name orderName,order_date orderDate from `order` where order_id=?";
        Order order = getInstance(Order.class, sql1, 2);
        System.out.println(order);
    }

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
