package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.mapper.CourseTeacherMapper;
import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class CourseTeacherServiceImpl implements CourseTeacherService {
    @Autowired
    private CourseTeacherMapper courseTeacherMapper;


    @Override
    public List<CourseTeacher> list(Integer courseId) {
        //查询教师by课程id
        LambdaQueryWrapper<CourseTeacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper = queryWrapper.eq(CourseTeacher::getCourseId, courseId);
        List<CourseTeacher> courseTeachers = courseTeacherMapper.selectList(queryWrapper);
        return courseTeachers;
    }

    @Override
    public CourseTeacher createOrUpdateCourseTeacher(CourseTeacher courseTeacher) {
        //判断是否存在教师，没有就新增，有就修改
        if (courseTeacher.getId() == null) {
            //新增
            CourseTeacher newCourseTeacher = new CourseTeacher();
            BeanUtils.copyProperties(courseTeacher, newCourseTeacher);
            newCourseTeacher.setCreateDate(LocalDateTime.now());
            int insert = courseTeacherMapper.insert(newCourseTeacher);
            if(insert <= 0){
                XueChengPlusException.cast("新增教师失败");
            }
            return newCourseTeacher;
        }
        else {
            //修改
            //查询教师
            CourseTeacher oldCourseTeacher = courseTeacherMapper.selectById(courseTeacher.getId());
            if(oldCourseTeacher == null){
                XueChengPlusException.cast("没有查询到教师，不可修改");
            }
            BeanUtils.copyProperties(courseTeacher, oldCourseTeacher);
            int update = courseTeacherMapper.updateById(oldCourseTeacher);
            if(update <= 0){
                XueChengPlusException.cast("更新教师信息失败");
            }
            return oldCourseTeacher;
        }
    }

    @Override
    public void deleteCourseTeacher(Long courseId, Long teacherId) {
        LambdaQueryWrapper<CourseTeacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper = queryWrapper.eq(CourseTeacher::getCourseId, courseId).eq(CourseTeacher::getId, teacherId);
        int delete = courseTeacherMapper.delete(queryWrapper);
        if(delete <= 0){
            XueChengPlusException.cast("删除失败，课程id或者教师id出错");
        }
    }
}
