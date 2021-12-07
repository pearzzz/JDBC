package com.pearz7.dao;

import com.pearz.bean.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**
 * @Description 此接口用于规范针对于customers表的常用操作
 * @author pearz
 * @Date 15:57 2021/12/7
**/
public interface CustomerDAO {

    //将customer对象添加到数据库中
    void insert(Connection conn, Customer customer);

    //根据id删除表中指定一条记录
    void deleteById(Connection conn, int id);

    //针对内存中的customer对象去修改数据表中指定的对象
    void update(Connection conn, Customer customer);

    //针对指定的id查询得到对应的Customer对象
    Customer getCustomerById(Connection conn, int id);

    //查询表中所有数据
    List<Customer> getAll(Connection conn);

    //返回数据表中的数据的条目数
    Long getCount(Connection conn);

    //返回数据表中最大的生日
    Date getMaxBirth(Connection conn);
}
