package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Setmeal;

import java.util.Map;

public interface CheckSetmealDao {
    public void add(Setmeal setmeal);

    public void setSetmealAndCheckgroup(Map map);

    public Page<Setmeal> findByCondition( String queryString);
}
