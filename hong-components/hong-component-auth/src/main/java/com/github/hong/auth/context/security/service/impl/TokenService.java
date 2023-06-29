package com.github.hong.auth.context.security.service.impl;


import com.github.hong.common.security.JwtUserDetails;
import com.github.hong.core.cache.RedisService;

/**
 * TOKEN 服务类
 *
 * @author hanson
 * @since 2023/6/13
 */
public class TokenService {


    /**
     * 处理token信息
     * 1. 如果不允许多用户则踢掉之前的所有用户信息
     * 2. 更新token 缓存时间信息
     * 3. 更新用户token列表
     **/
    public static void processTokenCache(JwtUserDetails userDetail, String cacheKey, Long expireSec) {

        userDetail.setCacheKey(cacheKey);  //设置cacheKey

        //当前用户的所有登录token 集合
//        if(!PropKit.isAllowMultiUser()){ //不允许多用户登录
//
//            List<String> allTokenList = new ArrayList<>();
//            for (String token : allTokenList) {
//                if(!cacheKey.equalsIgnoreCase(token)){
//                    RedisService.del(token);
//                }
//            }
//        }

        //保存token
        RedisService.set(cacheKey, userDetail, expireSec);  //缓存时间2小时, 保存具体信息而只是uid, 因为很多场景需要得到信息， 例如验证接口权限， 每次请求都需要获取。 将信息封装在一起减少磁盘请求次数， 如果放置多个key会增加非顺序读取。
    }

    /**
     * 退出时，清除token信息
     */
    public static void removeIToken(String iToken, Long currentUID) {
        //1. 清除token的信息
        RedisService.del(iToken);
    }


    public static void refreshData(JwtUserDetails currentUserInfo, Long expireSec) {
        //保存token 和 tokenList信息
        RedisService.set(currentUserInfo.getCacheKey(), currentUserInfo, expireSec);  //缓存时间2小时, 保存具体信息而只是uid, 因为很多场景需要得到信息， 例如验证接口权限， 每次请求都需要获取。 将信息封装在一起减少磁盘请求次数， 如果放置多个key会增加非顺序读取。
    }


}
