package com.pearz9.dbutils;

import com.pearz.bean.Customer;
import com.pearz.util.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.*;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @Description commons-dbutils是Apache提供的一个开源JDBC工具类库,封装了针对于数据库的增删改查操作
 * @Author pearz
 * @Email zhaihonghao317@163.com
 * @Date 14:47 2021/12/8
 */
public class QueryRunnerTest {

    /**
     * 测试插入
     **/
    @Test
    public void testInsert() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "insert into customers(name,email,birth)value(?,?,?)";
            QueryRunner queryRunner = new QueryRunner();
            int updateCount = queryRunner.update(conn, sql, "redz", "redz@163.com", "1998-3-17");
            System.out.println("共影响了" + updateCount + "行数据");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    //测试查询

    /**
     * BeanHander:是ResultSetHandler接口的实现类，用于封装表中的一条记录
     **/
    @Test
    public void testQuery() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select name,email,birth from customers where id=?";
            BeanHandler<Customer> handler = new BeanHandler<Customer>(Customer.class);
            QueryRunner queryRunner = new QueryRunner();
            Customer customer = queryRunner.query(conn, sql, handler, 8);
            System.out.println(customer);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    /**
     * BeanListHandler:是ResultSetHandler接口的实现类，用于封装表中的多条记录构成的集合
     **/
    @Test
    public void testQuery1() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select name,email,birth from customers where id<?";
            BeanListHandler<Customer> handler = new BeanListHandler<Customer>(Customer.class);
            QueryRunner queryRunner = new QueryRunner();
            List<Customer> customers = queryRunner.query(conn, sql, handler, 8);
            customers.forEach(System.out::println);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    /**
     * MapHander:是ResultSetHandler接口的实现类，用于封装表中的一条记录
     * 将字段及相应字段的值作为map中的key和value
     **/
    @Test
    public void testQuery2() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select name,email,birth from customers where id=?";
            MapHandler handler = new MapHandler();
            QueryRunner queryRunner = new QueryRunner();
            Map<String, Object> query = queryRunner.query(conn, sql, handler, 8);
            System.out.println(query);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    /**
     * MapListHander:是ResultSetHandler接口的实现类，用于封装表中的多条记录构成的集合
     * 将字段及相应字段的值作为map中的key和value，将这些map添加到list中
     **/
    @Test
    public void testQuery3() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select id,name,email,birth from customers where id<?";
            MapListHandler handler = new MapListHandler();
            QueryRunner queryRunner = new QueryRunner();
            List<Map<String, Object>> query = queryRunner.query(conn, sql, handler, 8);
            query.forEach(System.out::println);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    /**
     * ScalarHandler：用于查询特殊值
    **/
    @Test
    public void testQuery4() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select count(*) from customers";
            ScalarHandler handler = new ScalarHandler();
            QueryRunner queryRunner = new QueryRunner();
            Long count = (Long) queryRunner.query(conn, sql, handler);
            System.out.println(count);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    /**
     * 自定义ResultSetHandler实现类
    **/
    @Test
    public void testQuery5() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select id,name,email,birth from customers where id=?";
            ResultSetHandler<Customer> handler = new ResultSetHandler<Customer>() {
                @Override
                public Customer handle(ResultSet rs) throws SQLException {
                    if (rs.next()) {
                        int id = rs.getInt("id");
                        String name = rs.getString("name");
                        String email = rs.getString("email");
                        Date birth = rs.getDate("birth");

                        Customer customer = new Customer(id, name, email, birth);

                        return customer;
                    }
                    return null;
                }
            };
            QueryRunner queryRunner = new QueryRunner();
            Object query = queryRunner.query(conn, sql, handler, 8);
            System.out.println(query);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }
}
