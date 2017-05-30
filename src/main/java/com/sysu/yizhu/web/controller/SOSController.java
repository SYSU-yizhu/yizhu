package com.sysu.yizhu.web.controller;

import com.sysu.yizhu.business.entities.SOS;
import com.sysu.yizhu.business.entities.User;
import com.sysu.yizhu.business.services.SOSService;
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
@RequestMapping("/sos")
public class SOSController {

    private static final Logger LOG = LoggerFactory.getLogger(SOSController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private LCUtil lcUtil;

    @Autowired
    private SOSService sosService;


    @RequestMapping(path = "/push", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ReturnMsg push(@RequestParam("latitude") Double latitude,
                          @RequestParam("latitude") Double longitude, HttpServletRequest request, HttpServletResponse response) {
        String userId = (String)request.getSession().getAttribute("userId");
        if (userId == null) {
            response.setStatus(401);
            return null;
        }
        if (latitude > 90 || latitude < -90 || longitude > 180 || longitude < -180) {
            response.setStatus(403);
            return null;
        }
        User user = userService.findOne(userId);
        if (user.getObjectId() == null) {
            response.setStatus(450);
            return null;
        }

        if (!lcUtil.pushSOS(latitude, longitude)) {
            response.setStatus(500);
            return null;
        }
        SOS sos = sosService.createPushSOS(user, latitude, longitude);

        response.setStatus(200);
        ReturnMsg result = new ReturnMsg();
        result.put("sosId", sos.getSosId());
        return result;
    }

    @RequestMapping(path = "/response", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ReturnMsg response(@RequestParam("sosId") Integer sosId, HttpServletRequest request, HttpServletResponse response) {
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
        SOS sos = sosService.getSOS(sosId);
        if (sos == null || sos.getFinished().equals(Boolean.TRUE)) {
            response.setStatus(404);
            return null;
        }
        sosService.createResponse(user, sos);
        response.setStatus(200);
        ReturnMsg result = new ReturnMsg();
        result.put("userId", user.getUserId());
        return result;
    }

    @RequestMapping(path = "/response/{sosId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ReturnMsg responsers(@RequestParam("sosId") Integer sosId, HttpServletRequest request, HttpServletResponse response) {
        SOS sos = sosService.getSOS(sosId);
        if (sos == null) {
            response.setStatus(404);
            return null;
        }
        List<String> res = sosService.getAllResponserBySOSId(sosId);

        response.setStatus(200);
        ReturnMsg result = new ReturnMsg();
        result.put("count", res.size());
        result.put("data", res);
        return result;
    }

    @RequestMapping(path = "/get/{sosId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ReturnMsg get(@PathVariable Integer sosId, HttpServletRequest request, HttpServletResponse response) {
        SOS sos = sosService.getSOSWithUserById(sosId);
        if (sos == null) {
            response.setStatus(404);
            return null;
        }

        response.setStatus(200);
        ReturnMsg result = new ReturnMsg();
        result.put("sosId", sos.getSosId());
        result.put("latitude", sos.getLatitude());
        result.put("longitude", sos.getLongitude());
        result.put("finished", sos.getFinished());
        result.put("pushUserId", sos.getPushUser().getUserId());
        return result;
    }

    @RequestMapping(path = "/allValidId", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ReturnMsg allValidId(HttpServletRequest request, HttpServletResponse response) {
        List<Integer> res = sosService.getAllValidSOSId();

        response.setStatus(200);
        ReturnMsg result = new ReturnMsg();
        result.put("count", res.size());
        result.put("data", res);
        return result;
    }

    @RequestMapping(path = "/finish", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ReturnMsg finish(@RequestParam("sosId") Integer sosId, HttpServletRequest request, HttpServletResponse response) {
        String userId = (String)request.getSession().getAttribute("userId");
        if (userId == null) {
            response.setStatus(401);
            return null;
        }
        SOS sos = sosService.getSOSWithUserById(sosId);
        if (sos == null) {
            response.setStatus(404);
            return null;
        }

        User user = userService.findOne(userId);
        if (user.getObjectId() == null || !sos.getPushUser().getUserId().equals(user.getUserId())) {
            response.setStatus(450);
            return null;
        }

        sosService.finishSOS(sos);
        response.setStatus(200);
        ReturnMsg result = new ReturnMsg();
        result.put("sosId", sos.getSosId());
        return result;
    }
}
