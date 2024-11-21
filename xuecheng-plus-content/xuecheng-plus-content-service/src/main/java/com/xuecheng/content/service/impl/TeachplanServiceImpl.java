package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.mapper.TeachplanMediaMapper;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import com.xuecheng.content.service.TeachplanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeachplanServiceImpl implements TeachplanService {
    @Autowired
    private TeachplanMapper teachplanMapper;
    @Autowired
    private TeachplanMediaMapper teachplanMediaMapper;

    @Override
    public List<TeachplanDto> findTeachplanTree(Long courseId) {
        List<TeachplanDto> teachplanDtos = teachplanMapper.selectTreeNodes(courseId);
        return teachplanDtos;
    }

    @Override
    public void saveTeachplan(SaveTeachplanDto saveTeachplanDto) {
        //根据id判断是新增还是修改
        Long teachplanId = saveTeachplanDto.getId();
        if(teachplanId == null){
            Teachplan teachplan = new Teachplan();
            BeanUtils.copyProperties(saveTeachplanDto, teachplan);
            Long parentId = saveTeachplanDto.getParentid();
            Long courseId = saveTeachplanDto.getCourseId();
            int count = getMaxTeachplanOrder(parentId, courseId);
            teachplan.setOrderby(count+1);
            teachplanMapper.insert(teachplan);
        }else {
            Teachplan teachplan = teachplanMapper.selectById(teachplanId);
            BeanUtils.copyProperties(saveTeachplanDto, teachplan);
            teachplanMapper.updateById(teachplan);
        }
    }

    @Transactional
    @Override
    public void deleteTeachplan(Long teachplanId) {
        //查询课程计划
        Teachplan teachplan = teachplanMapper.selectById(teachplanId);
        if(teachplan == null){
            XueChengPlusException.cast("没有该课程计划");
        }

        //判断课程计划是大章节还是小章节
        if(1==teachplan.getGrade()){
            //如果是大章节，删除第一级别的章时要求章下边没有小节方可删除。
            //查询有没有小章节
            LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper = queryWrapper.eq(Teachplan::getParentid,teachplan.getId());
            Integer count = teachplanMapper.selectCount(queryWrapper);
            if(count>0){
                XueChengPlusException.cast("课程计划信息还有子级信息，无法操作");
            }else{
                teachplanMapper.deleteById(teachplanId);
            }
        }
        else{
            //如果是小章节,删除第二级别的小节的同时需要将其它关联的视频信息也删除。
            Integer orderby = teachplan.getOrderby();
            teachplanMapper.deleteById(teachplanId);
            teachplanMapper.updateOrderby(orderby);
            LambdaQueryWrapper<TeachplanMedia> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper = queryWrapper.eq(TeachplanMedia::getTeachplanId,teachplanId);
            teachplanMediaMapper.delete(queryWrapper);
        }
    }

    @Override
    public void moveTeachplan(String movetype,Long teachplanId) {
        //判断是否存在课程计划
        Teachplan teachplan = teachplanMapper.selectById(teachplanId);
        if(teachplan == null){
            XueChengPlusException.cast("没有该课程计划");
        }
        //判断移动类型
        if("movedown".equals(movetype)){

            int orderby = teachplan.getOrderby();
            //判断边界条件
            int maxTeachplanOrder = getMaxTeachplanOrder(teachplan.getParentid(), teachplan.getCourseId());
            if(maxTeachplanOrder == orderby){
                XueChengPlusException.cast("该课程计划已经是最后一个，不可下移");
            }
            //下移
            else{
                //获取要交换的课程
                LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper = queryWrapper.eq(Teachplan::getOrderby,orderby+1).eq(Teachplan::getParentid,teachplan.getParentid()).eq(Teachplan::getCourseId,teachplan.getCourseId());
                Teachplan switchOne = teachplanMapper.selectOne(queryWrapper);
                //重新设置排序id
                switchOne.setOrderby(orderby);
                teachplan.setOrderby(orderby+1);
                teachplanMapper.updateById(teachplan);
                teachplanMapper.updateById(switchOne);
            }

        }
        else if("moveup".equals(movetype)){
            int orderby = teachplan.getOrderby();
            //判断边界条件
            if(1 == orderby){
                XueChengPlusException.cast("该课程计划已经是第一个，不可上移");
            }
            //上移
            else{
                //获取要交换的课程
                LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper = queryWrapper.eq(Teachplan::getOrderby,orderby-1).eq(Teachplan::getParentid,teachplan.getParentid()).eq(Teachplan::getCourseId,teachplan.getCourseId());
                Teachplan switchOne = teachplanMapper.selectOne(queryWrapper);
                //重新设置排序id
                switchOne.setOrderby(orderby);
                teachplan.setOrderby(orderby-1);
                teachplanMapper.updateById(teachplan);
                teachplanMapper.updateById(switchOne);
            }
        }
        else {
            XueChengPlusException.cast("移动类型参数错误");
        }
    }

    private int getMaxTeachplanOrder(Long parentid, Long courseId) {

        Integer count = teachplanMapper.getMaxOrderby(parentid,courseId);
        if(count == null){
            count = 0;
        }
        return count;
    }
}
