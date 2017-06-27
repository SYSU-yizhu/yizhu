package com.sysu.yizhu.web.controller;

import com.sysu.yizhu.business.entities.Help;
import com.sysu.yizhu.business.entities.User;
import com.sysu.yizhu.business.services.HelpService;
import com.sysu.yizhu.business.services.UserService;
import com.sysu.yizhu.util.LCUtil;
import com.sysu.yizhu.util.ReturnMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/help")
public class HelpController {
    private static final Logger LOG = LoggerFactory.getLogger(HelpController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private LCUtil lcUtil;

    @Autowired
    private HelpService helpService;


    @RequestMapping(path = "/push", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ReturnMsg push(@RequestParam("latitude") Double latitude,
                          @RequestParam("longitude") Double longitude,
                          @RequestParam("title") String title,
                          @RequestParam("detail") String detail,
                          @RequestParam("needs") Integer needs, HttpServletRequest request, HttpServletResponse response) {
        String userId = (String)request.getSession().getAttribute("userId");
        if (userId == null) {
            response.setStatus(401);
            return null;
        }
        if (latitude > 90 || latitude < -90 || longitude > 180 || longitude < -180) {
            response.setStatus(403);
            return null;
        }
        if (needs < 1 || needs > 10) {
            response.setStatus(402);
            return null;
        }
        User user = userService.findOne(userId);
        if (user.getObjectId() == null) {
            response.setStatus(450);
            return null;
        }

        if (!lcUtil.pushHelp(latitude, longitude)) {
            response.setStatus(500);
            return null;
        }
        Help help = helpService.createPushHelp(user, latitude, longitude, title, detail, needs);

        response.setStatus(200);
        ReturnMsg result = new ReturnMsg();
        result.put("helpId", help.getHelpId());
        return result;
    }

    @RequestMapping(path = "/response", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ReturnMsg response(@RequestParam("helpId") Integer helpId, HttpServletRequest request, HttpServletResponse response) {
        String userId = (String)request.getSession().getAttribute("userId");
        if (userId == null) {
            response.setStatus(401);
            return null;
        }
        User user = userService.findOne(userId);
        if (user.getObjectId() == null) {
            response.setStatus(450);
            return null;
        }
        Help help = helpService.getHelp(helpId);
        if (help == null || help.getFinished().equals(Boolean.TRUE)) {
            response.setStatus(404);
            return null;
        } else if (help.getNeeds().equals(help.getResponseNum())) {
            response.setStatus(402);
            return null;
        }
        helpService.createResponse(user, help);
        response.setStatus(200);
        ReturnMsg result = new ReturnMsg();
        result.put("userId", user.getUserId());
        return result;
    }

    @RequestMapping(path = "/response/{helpId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ReturnMsg responsers(@PathVariable Integer helpId, HttpServletRequest request, HttpServletResponse response) {
        Help help = helpService.getHelp(helpId);
        if (help == null) {
            response.setStatus(404);
            return null;
        }
        List<String> res = helpService.getAllResponserByHelpId(helpId);

        response.setStatus(200);
        ReturnMsg result = new ReturnMsg();
        result.put("count", res.size());
        result.put("data", res);
        return result;
    }

    @RequestMapping(path = "/get/{helpId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ReturnMsg get(@PathVariable Integer helpId, HttpServletRequest request, HttpServletResponse response) {
        Help help = helpService.getHelpWithUserById(helpId);
        if (help == null) {
            response.setStatus(404);
            return null;
        }

        LOG.info(help.getResponseNum().toString());
        response.setStatus(200);
        ReturnMsg result = new ReturnMsg();
        result.put("helpId", help.getHelpId());
        result.put("latitude", help.getLatitude());
        result.put("longitude", help.getLongitude());
        result.put("finished", help.getFinished());
        result.put("title", help.getTitle());
        result.put("detail", help.getDetail());
        result.put("needs", help.getNeeds());
        result.put("responseNum", help.getResponseNum());
        result.put("createTime", help.getCreateTime());
        result.put("pushUserId", help.getPushUser().getUserId());
        return result;
    }

    @RequestMapping(path = "/allValidId", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ReturnMsg allValidId(HttpServletRequest request, HttpServletResponse response) {
        List<Integer> res = helpService.getAllValidHelpId();

        response.setStatus(200);
        ReturnMsg result = new ReturnMsg();
        result.put("count", res.size());
        result.put("data", res);
        return result;
    }

    @RequestMapping(path = "/finish", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ReturnMsg finish(@RequestParam("helpId") Integer helpId, HttpServletRequest request, HttpServletResponse response) {
        String userId = (String)request.getSession().getAttribute("userId");
        if (userId == null) {
            response.setStatus(401);
            return null;
        }
        Help help = helpService.getHelpWithUserById(helpId);
        if (help == null) {
            response.setStatus(404);
            return null;
        }

        User user = userService.findOne(userId);
        if (user.getObjectId() == null || !help.getPushUser().getUserId().equals(user.getUserId())) {
            response.setStatus(450);
            return null;
        }

        helpService.finishHelp(help);
        response.setStatus(200);
        ReturnMsg result = new ReturnMsg();
        result.put("helpId", help.getHelpId());
        return result;
    }
}
