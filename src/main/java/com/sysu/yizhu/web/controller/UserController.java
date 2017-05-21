package com.sysu.yizhu.web.controller;

import com.sysu.yizhu.business.entities.User;
import com.sysu.yizhu.business.services.UserService;
import com.sysu.yizhu.util.PhoneNumUtil;
import com.sysu.yizhu.util.ReturnMsg;
import com.sysu.yizhu.util.SmsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;

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


    @RequestMapping(path = "/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ReturnMsg register(@RequestParam("userId") String userId,
                              @RequestParam("password") String password,
                              @RequestParam("code") String code,
                              @RequestParam("name") String name,
                              @RequestParam("gender") String gender,
                              @RequestParam("birthDate") String birthDate,
                              @RequestParam("location") String location, HttpServletRequest request, HttpServletResponse response) {

        if (!PhoneNumUtil.isPhone(userId)) {
            response.setStatus(403);
            return null;
        } else if (userService.findOne(userId) != null) {
            response.setStatus(400);
            return null;
        } else if (!smsUtil.checkSmsCode(userId, code)) {
            response.setStatus(450);
            return null;
        }
        User user = new User();
        try {
            user.setBirthDate(Date.valueOf(birthDate));
            user.setUserId(userId);
            user.setPassword(password);
            user.setName(name);
            user.setGender(gender);
            user.setLocation(location);
        } catch (IllegalArgumentException e) {
            response.setStatus(403);
            return null;
        }

        if (userService.createUserWithRawPassword(user) == null) {
            response.setStatus(403);
            return null;
        }

        request.getSession().setAttribute("user", user);
        response.setStatus(200);
        ReturnMsg result = new ReturnMsg();
        result.put("userId", userId);
        return result;
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ReturnMsg login(@RequestParam("userId") String userId,
                           @RequestParam("password") String password, HttpServletRequest request, HttpServletResponse response) {

        if (!PhoneNumUtil.isPhone(userId)) {
            response.setStatus(403);
            return null;
        }
        User user = userService.checkUserWithRawPassword(userId, password);
        if (user == null) {
            response.setStatus(400);
            return null;
        }

        request.getSession().setAttribute("user", user);
        ReturnMsg result = new ReturnMsg();
        result.put("userId", userId);
        return result;
    }

    @RequestMapping(path = "/modifyInfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ReturnMsg modifyInfo(@RequestParam("name") String name,
                              @RequestParam("gender") String gender,
                              @RequestParam("birthDate") String birthDate,
                              @RequestParam("location") String location, HttpServletRequest request, HttpServletResponse response) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.setStatus(401);
            return null;
        }

        try {
            if (!(gender.equals("male") || gender.equals("female"))) {
                throw new IllegalArgumentException("Gender invalid.");
            }
            user.setBirthDate(Date.valueOf(birthDate));
            user.setName(name);
            user.setGender(gender);
            user.setLocation(location);
            userService.updateUserInfo(user);
        } catch (IllegalArgumentException e) {
            response.setStatus(403);
            return null;
        }

        response.setStatus(200);
        ReturnMsg result = new ReturnMsg();
        result.put("userId", user.getUserId());
        return result;
    }

    @RequestMapping(path = "/info", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ReturnMsg info(HttpServletRequest request, HttpServletResponse response) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.setStatus(401);
            return null;
        }

        response.setStatus(200);
        ReturnMsg result = new ReturnMsg();
        result.put("userId", user.getUserId());
        result.put("name", user.getName());
        result.put("gender", user.getGender());
        result.put("birthDate", user.getBirthDate().toString());
        result.put("location", user.getLocation());
        return result;
    }
}