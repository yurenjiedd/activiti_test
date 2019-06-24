package com.yrj.mybatis;

import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface MyCustomMapper {
    @Select("select * from act_ru_task")
    public List<Map<String,Object>> findAll();
}
