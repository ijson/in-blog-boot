package com.ijson.blog.controller.view;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/2/8 11:35 AM
 */

import com.ijson.blog.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller()
@RequestMapping("/oauth")
public class AuthController extends BaseController {


    @RequestMapping(value = "/callback/qq")
    public void login(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        //expires_in=7776000
        String access_token = request.getParameter("access_token");
        String expires_in = request.getParameter("expires_in");

        System.out.println(1);
        //return Result.ok("登录成功!");
    }
}
