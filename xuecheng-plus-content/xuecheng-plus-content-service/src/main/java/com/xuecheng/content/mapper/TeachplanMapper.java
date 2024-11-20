package com.xuecheng.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 课程计划 Mapper 接口
 * </p>
 *
 * @author itcast
 */
public interface TeachplanMapper extends BaseMapper<Teachplan> {


    public List<TeachplanDto> selectTreeNodes(Long courseId);

    @Select("select max(orderby) as count from xuecheng_content.teachplan where parentid =#{parentId} and course_id = #{courseId}")
    Integer getMaxOrderby(@Param("parentId") Long parentid,@Param("courseId") Long courseId);
}
