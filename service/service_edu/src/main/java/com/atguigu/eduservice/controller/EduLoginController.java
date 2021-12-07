package com.atguigu.eduservice.controller;

import com.atguigu.commonutils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(description = "模拟登陆")
@RequestMapping("/eduuser")
@CrossOrigin
public class EduLoginController {

    @PostMapping("/login")
    @ApiOperation(value = "登陆")
    public R login(String username, String password){
        return R.ok().data("token", "admin");
    }

    @GetMapping("/info")
    @ApiOperation(value = "用户信息")
    public R info(String token){
        return R.ok().data("roles","admin")
                .data("name","admin")
                .data("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    }
}
