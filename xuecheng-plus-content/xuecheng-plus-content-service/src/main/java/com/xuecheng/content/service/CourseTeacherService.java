package com.xuecheng.content.service;

import com.xuecheng.content.model.po.CourseTeacher;

import java.util.List;

/**
 * 教师信息管理服务
 */
public interface CourseTeacherService {
    /**
     * 查询教师
     * @param courseId
     * @return
     */
    List<CourseTeacher> list(Integer courseId);

    /**
     * 新增教师
     * @param CourseTeacher
     * @return
     */
    CourseTeacher createOrUpdateCourseTeacher(CourseTeacher CourseTeacher);

    /**
     * 删除教师
     * @param courseId
     * @param teacherId
     */
    void deleteCourseTeacher(Long courseId, Long teacherId);

}
