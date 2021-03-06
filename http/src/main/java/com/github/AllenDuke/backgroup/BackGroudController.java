package com.github.AllenDuke.backgroup;


import com.github.AllenDuke.pojo.ao.UserAO;
import com.github.AllenDuke.result.ErrorCode;
import com.github.AllenDuke.result.Result;
import com.github.AllenDuke.util.redisUtil.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/***
 * @Description: 后台功能，实现简单的人数统计功能。小时、日、周的用户量
 *
 * @Author: hermanCho
 * @Date: 2020-05-07
 * @Param null:
 * @return: null
 **/
@RestController
@RequestMapping("/backgroup")
public class BackGroudController {

    // 不清楚是否支持自动注入，必要时直接换为饿汉单例
//    public static UVCounterService uvCounterService = new UVCounterService();
    @Autowired
    UVCounterService uvCounterService;

    @RequestMapping("/test")
    public void test() {
        String key = "USER:LOGIN:06290957";
        JedisUtil.pfAdd(key, "3");
        System.out.println(JedisUtil.pfCount(key));
    }


    @RequestMapping("/adminlogin")
    public Result adminlogin(@RequestBody UserAO userAO) {
        if("admin".equals(userAO.getChatNum())){
            if("admin".equals(userAO.getPassword())){
                return Result.success();
            }
        }
        return Result.fail(ErrorCode.UNAUTHORIZED, "管理员信息错误");
    }


    @RequestMapping("/getWeeklyUV")
    public long getWeeklyUV() {
        return uvCounterService.getWeeklyUV();
    }

    @RequestMapping("/getDailyUV")
    public long getDailyUV() {
        return uvCounterService.getDailyUV();
    }

    @RequestMapping("/getHourlyUV")
    public long getHourlyUV() {
        return uvCounterService.getHourlyUV();
    }

}
