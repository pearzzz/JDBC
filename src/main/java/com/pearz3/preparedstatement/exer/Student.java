package com.pearz3.preparedstatement.exer;

/**
 * @ClassName Student
 * @Description
 * @Author pearz
 * @Email zhaihonghao317@163.com
 * @Date 14:20 2021/12/4
 */
public class Student {
    private int flowID;
    private int type;
    private String idCard;
    private String examCard;
    private String name;
    private String location;
    private int grade;

    public Student() {
    }

    public Student(int flowID, int type, String idCard, String examCard, String name, String location, int grade) {
        this.flowID = flowID;
        this.type = type;
        this.idCard = idCard;
        this.examCard = examCard;
        this.name = name;
        this.location = location;
        this.grade = grade;
    }

    public int getFlowID() {
        return flowID;
    }

    public void setFlowID(int flowID) {
        this.flowID = flowID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getExamCard() {
        return examCard;
    }

    public void setExamCard(String examCard) {
        this.examCard = examCard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "==========查询结果==========" +
                "\n流水号：" + flowID +
                "\n四级/六级：" + type +
                "\n身份证号：" + idCard +
                "\n准考证号：" + examCard +
                "\n学生姓名：" + name +
                "\n区域：" + location +
                "\n成绩：" + grade;
    }
}
