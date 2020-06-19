package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.CheckSetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;
import com.itheima.service.CheckSetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

/**
 * 套餐服务
 */
@Service(interfaceClass = CheckSetmealService.class)
//事务注解
@Transactional
public class CheckSetmealServiceImpl implements CheckSetmealService{
    @Autowired
    private CheckSetmealDao checkSetmealDao;
    //新增套餐.同时需要关联检查组
    @Autowired
    private JedisPool jedisPool;
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        checkSetmealDao.add(setmeal);
        Integer setmealId = setmeal.getId();
        this.setSetmealAndCheckgroup(setmealId,checkgroupIds);
        //将图片保存到redis中
        savePic2Redis(setmeal.getImg());
    }
    //设置检查组和套餐之间的多对多关系。
    public void setSetmealAndCheckgroup(Integer setmealId, Integer[] checkgroupIds){
        if (checkgroupIds!=null && checkgroupIds.length>0){
            for (Integer checkgroupId : checkgroupIds) {
                Map<String,Integer> map = new HashMap<String, Integer>();
                map.put("setmealId",setmealId);
                map.put("checkgroupId",checkgroupId);
                checkSetmealDao.setSetmealAndCheckgroup(map);
            }
        }
    }
    //将图片名称保存到redis
    private void savePic2Redis(String pic){
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,pic);
    }
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> page=checkSetmealDao.findByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }
}
