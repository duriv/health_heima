package com.itheima.utils;

import org.junit.Test;

public class test11 {
    @Test
    public void test11(){
        String filePath="/Users/fushengweixue/Pictures/6.jpg";
        String fileName="abc.jpg";
        try {
            QiniuUtils.upload2Qiniu(filePath,fileName);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("上传失败");
        }
        System.out.println("上传成功");
    }
}
