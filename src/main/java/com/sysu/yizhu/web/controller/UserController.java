package com.sysu.yizhu.web.controller;

import com.sysu.yizhu.business.entities.User;
import com.sysu.yizhu.business.services.UserService;
import com.sysu.yizhu.util.PhoneNumUtil;
import com.sysu.yizhu.util.ReturnMsg;
import com.sysu.yizhu.util.SmsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by CrazeWong on 2017/4/15.
 */
@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SmsUtil smsUtil;

    @RequestMapping(path = "/sendSms/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public ReturnMsg sendSms(@PathVariable String userId, HttpServletRequest request, HttpServletResponse response) {
        if (!PhoneNumUtil.isPhone(userId)) {
            response.setStatus(403);
            return null;
        } else if (userService.findOne(userId) != null) {
            response.setStatus(400);
            return null;
        }

        smsUtil.sendSmsCode(userId);
        response.setStatus(200);
        ReturnMsg result = new ReturnMsg();
        result.put("userId", userId);
        return result;
    }


    @RequestMapping(path = "/register", method = RequestMethod.POST)
    @ResponseBody
    public ReturnMsg register(HttpServletRequest request, HttpServletResponse response) {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        String code = request.getParameter("code");

        if (!PhoneNumUtil.isPhone(userId)) {
            response.setStatus(403);
            return null;
        } else if (userService.findOne(userId) != null) {
            response.setStatus(400);
            return null;
        } else if (!smsUtil.checkSmsCode(userId, code)) {
            response.setStatus(450);
            return null;
        } else if (userService.createUserWithRawPassword(new User(userId, password)) == null) {
            response.setStatus(403);
            return null;
        }

        response.setStatus(200);
        ReturnMsg result = new ReturnMsg();
        result.put("userId", userId);
        return result;
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ReturnMsg login(HttpServletRequest request, HttpServletResponse response) {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        User user = userService.checkUserWithRawPassword(userId, password);
        if (user == null) {
            response.setStatus(404);
            return null;
        }
        ReturnMsg result = new ReturnMsg();
        result.put("userId", userId);
        return result;
    }
}