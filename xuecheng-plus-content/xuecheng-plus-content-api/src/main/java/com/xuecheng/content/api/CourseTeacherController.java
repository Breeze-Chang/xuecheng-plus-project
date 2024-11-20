package com.xuecheng.content.api;

import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(value = "教师信息管理接口",tags = "教师信息管理接口")
public class CourseTeacherController {
    @Autowired
    private CourseTeacherService courseTeacherService;

    @ApiOperation("查询教师接口")
    @GetMapping("/courseTeacher/list/{courseId}")
    public List<CourseTeacher> courseTeacherList(@PathVariable("courseId") Integer courseId) {
        return courseTeacherService.list(courseId);
    }

    @ApiOperation("添加/修改教师")
    @PostMapping("/courseTeacher")
    public CourseTeacher createOrUpdateCourseTeacher(@RequestBody CourseTeacher courseTeacher) {
        return courseTeacherService.createOrUpdateCourseTeacher(courseTeacher);
    }

    @ApiOperation("删除教师")
    @DeleteMapping("/courseTeacher/course/{courseId}/{teacherId}")
    public void deleteCourseTeacher(@PathVariable("courseId") Long courseId,@PathVariable("teacherId") Long teacherId) {
        courseTeacherService.deleteCourseTeacher(courseId,teacherId);
    }

}
