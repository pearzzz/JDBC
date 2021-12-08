package com.pearz7.dao;

import com.pearz.util.OldJDBCUtils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName BaseDAO
 * @Description 封装了针对于数据表的通用操作
 * @Author pearz
 * @Email zhaihonghao317@163.com
 * @Date 16:28 2021/12/4
 */
public abstract class BaseDAO {
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

    /**
     * 通用的查询操作，用于返回数据表中的一条数据（version 2.0，考虑事务）
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
     * 通用的查询操作，用于返回数据表中的多条数据（version 2.0，考虑事务）
    **/
    public <T> List<T> getForList(Connection conn, Class<T> clazz, String sql, Object ...args) {
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
            OldJDBCUtils.closeResource(null, ps, rs);
        }

        return null;
    }

    /**
     * 用于查询特殊值的通用的方法
    **/
    public <E> E getValue(Connection conn, String sql, Object ...args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            rs = ps.executeQuery();
            if (rs.next()) {
                return (E) rs.getObject(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            OldJDBCUtils.closeResource(null, ps, rs);
        }
        return null;
    }
}
