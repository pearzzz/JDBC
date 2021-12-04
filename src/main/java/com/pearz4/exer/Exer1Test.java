package com.pearz4.exer;

import com.pearz3.preparedstatement.util.JDBCUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * @ClassName Exer1Test
 * @Description 练习题1：从控制台向数据库的表customers中插入一条数据
 * @Author pearz
 * @Email zhaihonghao317@163.com
 * @Date 2021/12/3 15:10
 */
public class Exer1Test {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入姓名：");
        String name = scanner.nextLine();
        System.out.println("请输入邮箱：");
        String email = scanner.nextLine();
        System.out.println("请输入生日：");
        String birthday = scanner.nextLine();

        String sql = "insert customers(name,email,birth) value(?,?,?)";

        int update = update(sql, name, email, birthday);
        if (update > 0) {
            System.out.println("插入成功");
        } else {
            System.out.println("插入失败");
        }
    }

    //通用的增删改操作
    public static int update(String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1.获取数据库的连接
            conn = JDBCUtils.getConnection();
            //2.预编译sql语句，返回PreparedStatement的实例
            ps = conn.prepareStatement(sql);
            //3.填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            //4.执行
            return ps.executeUpdate();
        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            //5.资源的关闭
            JDBCUtils.closeResource(conn, ps);
        }

        return 0;
    }
}
