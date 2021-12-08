package com.pearz7.dao;

import com.pearz.bean.Customer;
import com.pearz.util.OldJDBCUtils;
import junit.framework.TestCase;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CustomerDAOImplTest extends TestCase {

    private final CustomerDAOImpl dao = new CustomerDAOImpl();

    public void testInsert()  {
        Connection conn = null;
        try {
            conn = OldJDBCUtils.getConnection();
            SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
            Date date = sdf.parse("1998-04-13");
            Customer customer = new Customer(1, "pearz", "pearz@163.com", date);
            dao.insert(conn, customer);
            System.out.println("插入成功");
        } catch (IOException | SQLException | ParseException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            OldJDBCUtils.closeResource(conn, null);
        }
    }

    public void testDeleteById() {
        Connection conn = null;
        try {
            conn = OldJDBCUtils.getConnection();
            dao.deleteById(conn, 19);
            System.out.println("删除成功");
        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            OldJDBCUtils.closeResource(conn, null);
        }
    }

    public void testUpdate() {
        Connection conn = null;
        try {
            conn = OldJDBCUtils.getConnection();
            SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
            Date date = sdf.parse("1998-04-13");
            Customer customer = new Customer(21, "pearz", "pearz@gmail.com", date);
            dao.update(conn, customer);
            System.out.println("更新成功");
        } catch (IOException | SQLException | ParseException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            OldJDBCUtils.closeResource(conn, null);
        }
    }

    public void testGetCustomerById() {
        Connection conn = null;
        try {
            conn = OldJDBCUtils.getConnection();
            Customer customer = dao.getCustomerById(conn, 10);
            System.out.println(customer);
        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            OldJDBCUtils.closeResource(conn, null);
        }
    }

    public void testGetAll() {
        Connection conn = null;
        try {
            conn = OldJDBCUtils.getConnection();
            List<Customer> customers = dao.getAll(conn);
            customers.forEach(System.out::println);
        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            OldJDBCUtils.closeResource(conn, null);
        }
    }

    public void testGetCount() {
        Connection conn = null;
        try {
            conn = OldJDBCUtils.getConnection();
            Long count = dao.getCount(conn);
            System.out.println(count);
        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            OldJDBCUtils.closeResource(conn, null);
        }
    }

    public void testGetMaxBirth() {
        Connection conn = null;
        try {
            conn = OldJDBCUtils.getConnection();
            java.sql.Date maxBirth = dao.getMaxBirth(conn);
            System.out.println(maxBirth);
        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            OldJDBCUtils.closeResource(conn, null);
        }
    }
}