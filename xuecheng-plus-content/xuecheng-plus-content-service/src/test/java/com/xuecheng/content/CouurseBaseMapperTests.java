package com.xuecheng.content;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CouurseBaseMapperTests {

    @Autowired
    private CourseBaseMapper courseBaseMapper;

    @Test
    public void testCourseBaseMapper() {
        CourseBase courseBase = courseBaseMapper.selectById(18);
        Assertions.assertNotNull(courseBase);
        //查询条件
        QueryCourseParamsDto courseParamsDto = new QueryCourseParamsDto();
        courseParamsDto.setCourseName("java");

        //拼装查询条件
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();
        //根据名称模糊查询
        queryWrapper.like(StringUtils.isNotEmpty(courseParamsDto.getCourseName()),CourseBase::getName,courseParamsDto.getCourseName());
        //根据审核状态查询
        queryWrapper.eq(StringUtils.isNotEmpty(courseParamsDto.getAuditStatus()),CourseBase::getAuditStatus,courseParamsDto.getAuditStatus());
        //创建page分页参数
        PageParams pageParams = new PageParams(1L,2L);
        Page<CourseBase> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        Page<CourseBase> pageResult = courseBaseMapper.selectPage(page, queryWrapper);
        PageResult<CourseBase> courseBasePageResult = new PageResult<CourseBase>(pageResult.getRecords(),pageResult.getTotal(),pageParams.getPageNo(),pageParams.getPageSize());
        System.out.println(courseBasePageResult);
    }
}
