package com.pearz4.exer;

import com.pearz3.preparedstatement.util.JDBCUtils;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.Scanner;

/**
 * @ClassName Exer2Test
 * @Description
 * @Author pearz
 * @Email zhaihonghao317@163.com
 * @Date 2021/12/3 16:52
 */
public class Exer2Test {

    //问题3：删除指定的学生信息
    @Test
    public void testDelete() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入要删除学生的准考证号：");
        String examCard = scanner.next();

        String sql = "delete from examstudent where examCard=?";

        int delete = update(sql, examCard);
        if (delete > 0) {
            System.out.println("删除成功");
        } else {
            System.out.println("查无此人，请重新输入");
        }
    }

    //问题2：根据身份证号IDCard或准考证号ExamCard查询
    @Test
    public void testGetGrade() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请你选择查询方式，输入数字：1身份证号，2准考证号");
        int num = scanner.nextInt();
        if (num == 1) {
            System.out.println("请输入身份证号：");
            String idCard = scanner.next();
            String sql = "select FlowID flowID,Type type,IDCard idCard,ExamCard examCard,StudentName name," +
                    "Location location,Grade grade from examstudent where IDCard=?";
            Student student = getInstance(Student.class, sql, idCard);
            if (student != null) {
                System.out.println(student);
            } else {
                System.out.println("不存在身份证号为" + idCard + "的考生");
            }
        } else if(num == 2) {
            System.out.println("请输入准考证号：");
            String examCard = scanner.next();
            String sql = "select FlowID flowID,Type type,IDCard idCard,ExamCard examCard,StudentName name," +
                    "Location location,Grade grade from examstudent where ExamCard=?";
            Student student = getInstance(Student.class, sql, examCard);
            if (student != null) {
                System.out.println(student);
            } else {
                System.out.println("不存在准考证号为" + examCard + "的考生");
            }
        } else {
            System.out.println("输入错误");
        }
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

    //问题1：向examstudent表中添加一条记录
    @Test
    public void testInsert() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("四级/六级：");
        int type = scanner.nextInt();
        System.out.println("身份证号：");
        String idCard = scanner.next();
        System.out.println("准考证号：");
        String examCard = scanner.next();
        System.out.println("姓名：");
        String name = scanner.next();
        System.out.println("城市：");
        String location = scanner.next();
        System.out.println("成绩：");
        int grade = scanner.nextInt();

        String sql = "insert examstudent(type,idcard,examcard,studentname,location,grade) value(?,?,?,?,?,?)";

        int update = update(sql, type, idCard, examCard, name, location, grade);

        if (update > 0) {
            System.out.println("插入成功");
        } else {
            System.out.println("插入失败");
        }
    }

    //通用的增删改操作
    public int update(String sql, Object... args) {
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
