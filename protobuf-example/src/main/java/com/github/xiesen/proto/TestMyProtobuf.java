package com.github.xiesen.proto;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;

import java.util.Arrays;

/**
 * @author xiesen
 * @title: TestMyProtobuf
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/7/8 9:23
 */
public class TestMyProtobuf {
    public static void main(String[] args) {
        // 生成课程1对象
        com.github.xiesen.proto.dto.CourseProto.course.Builder courseBuilder1 = com.github.xiesen.proto.dto.CourseProto.course.newBuilder();
        courseBuilder1.setName("Java");
        courseBuilder1.setScore(99);
        com.github.xiesen.proto.dto.CourseProto.course course1 = courseBuilder1.build();

        // 生成课程2对象
        com.github.xiesen.proto.dto.CourseProto.course.Builder courseBuilder2 = com.github.xiesen.proto.dto.CourseProto.course.newBuilder();
        courseBuilder2.setName("Python");
        courseBuilder2.setScore(98);
        com.github.xiesen.proto.dto.CourseProto.course course2 = courseBuilder2.build();

        // 生成学生对象
        com.github.xiesen.proto.dto.StudentProto.student.Builder studentBuilder = com.github.xiesen.proto.dto.StudentProto.student.newBuilder();
        studentBuilder.setName("Lucy");
        studentBuilder.setAge(23);
        studentBuilder.addCourse(0, course1);
        studentBuilder.addCourse(1, course2);
        com.github.xiesen.proto.dto.StudentProto.student student = studentBuilder.build();

        // proto对象
        System.out.println("The student object is: \n" + student);
        // 序列化
        byte[] studentByte = student.toByteArray();
        System.out.println("The student after encode is:\n" + Arrays.toString(studentByte));
        try {
            // 反序列化
            com.github.xiesen.proto.dto.StudentProto.student newStudent = com.github.xiesen.proto.dto.StudentProto.student.parseFrom(studentByte);
            System.out.println("The student after decode is:\n" + newStudent);
            // 转换json
            System.out.println("The student json format is:\n" + JsonFormat.printer().print(student));
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

    }
}
