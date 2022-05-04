package com.reine.jobs;

import com.reine.constant.RedisConstant;
import com.reine.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * 自定义job，实现定时清理垃圾图片
 *
 * @author reine
 * @since 2022/4/23 21:05
 */
public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;

    public void clearImg() {
        // 根据redis中保存的两个set集合进行差值计算，删除图片
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if (!set.isEmpty()) {
            set.forEach(item -> {
                QiniuUtils.deleteFileFromQiniu(item);
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES, item);
            });
        }
    }
}
