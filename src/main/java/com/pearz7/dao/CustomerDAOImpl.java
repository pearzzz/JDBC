package com.pearz7.dao;

import com.pearz.bean.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**
 * @Description
 * @Author pearz
 * @Email zhaihonghao317@163.com
 * @Date 9:47 2021/12/7
 */
public class CustomerDAOImpl extends BaseDAO implements CustomerDAO {
    @Override
    public void insert(Connection conn, Customer customer) {
        String sql = "insert into customers(name,email,birth) value(?,?,?)";
        update(conn, sql, customer.getName(), customer.getEmail(), customer.getBirth());
    }

    @Override
    public void deleteById(Connection conn, int id) {
        String sql = "delete from customers where id=?";
        update(conn, sql, id);
    }

    @Override
    public void update(Connection conn, Customer customer) {
        String sql = "update customers set name=?,email=?,birth=? where id=?";
        update(conn, sql, customer.getName(), customer.getEmail(), customer.getBirth(), customer.getId());
    }

    @Override
    public Customer getCustomerById(Connection conn, int id) {
        String sql = "select id,name,email,birth from customers where id=?";
        Customer customer = getInstance(conn, Customer.class, sql, id);
        return customer;
    }

    @Override
    public List<Customer> getAll(Connection conn) {
        String sql = "select id,name,email,birth from customers";
        List<Customer> customers = getForList(conn, Customer.class, sql);
        return customers;
    }

    @Override
    public Long getCount(Connection conn) {
        String sql = "select count(*) from customers";
        return getValue(conn, sql);
    }

    @Override
    public Date getMaxBirth(Connection conn) {
        String sql = "select max(birth) from customers";
        return getValue(conn, sql);
    }
}
