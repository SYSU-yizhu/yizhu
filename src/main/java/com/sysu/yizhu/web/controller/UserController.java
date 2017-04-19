package com.sysu.yizhu.web.controller;

import com.sysu.yizhu.business.entities.User;
import com.sysu.yizhu.business.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;

/**
 * Created by CrazeWong on 2017/4/15.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    @ResponseBody
    public LinkedHashMap<String, Object> register(HttpServletRequest request, HttpServletResponse response) {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        if (userService.createUserWithRawPassword(new User(userId, password)) == null) {
            response.setStatus(403);
            return null;
        }

        response.setStatus(200);
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("userId", userId);
        return result;
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    @ResponseBody
    public User login(HttpServletRequest request, HttpServletResponse response) {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        User user = userService.checkUserWithRawPassword(userId, password);
        if (user == null) {
            response.setStatus(404);
            return null;
        }

        return user;
    }
}