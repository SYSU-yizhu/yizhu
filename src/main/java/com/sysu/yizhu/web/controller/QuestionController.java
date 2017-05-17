package com.sysu.yizhu.web.controller;

import com.sysu.yizhu.business.entities.Answer;
import com.sysu.yizhu.business.entities.AnswerAgree;
import com.sysu.yizhu.business.entities.Question;
import com.sysu.yizhu.business.entities.User;
import com.sysu.yizhu.business.services.QuestionService;
import com.sysu.yizhu.business.services.UserService;
import com.sysu.yizhu.util.PhoneNumUtil;
import com.sysu.yizhu.util.ReturnMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.List;

@Controller
@RequestMapping("/question")
public class QuestionController {
    private static final Logger LOG = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/ask", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ReturnMsg ask(@RequestParam("userId") String userId,
                              @RequestParam("password") String password,
                              @RequestParam("title") String title,
                              @RequestParam("content") String content, HttpServletRequest request, HttpServletResponse response) {

        if (!PhoneNumUtil.isPhone(userId)) {
            response.setStatus(403);
            return null;
        }
        User user = userService.checkUserWithRawPassword(userId, password);
        if (user == null) {
            response.setStatus(404);
            return null;
        }
        Question question = new Question();
        question.setAskUser(user);
        question.setContent(content);
        question.setCreateDate(new Date(System.currentTimeMillis()));
        question.setTitle(title);
        questionService.createQuestion(question);

        response.setStatus(200);
        ReturnMsg result = new ReturnMsg();
        result.put("questionId", question.getQuestionId());
        return result;
    }

    @RequestMapping(path = "/answer", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ReturnMsg answer(@RequestParam("userId") String userId,
                         @RequestParam("password") String password,
                         @RequestParam("questionId") Integer questionId,
                         @RequestParam("content") String content, HttpServletRequest request, HttpServletResponse response) {

        if (!PhoneNumUtil.isPhone(userId)) {
            response.setStatus(403);
            return null;
        }
        User user = userService.checkUserWithRawPassword(userId, password);
        if (user == null) {
            response.setStatus(404);
            return null;
        }
        Question question = questionService.getQuestionById(questionId);
        if (question == null) {
            response.setStatus(450);
            return null;
        }
        Answer answer = new Answer();
        answer.setQuestion(question);
        answer.setCreateDate(new Date(System.currentTimeMillis()));
        answer.setContent(content);
        answer.setAnswerUser(user);
        answer.setBad(0);
        answer.setGood(0);

        response.setStatus(200);
        ReturnMsg result = new ReturnMsg();
        result.put("questionId", question.getQuestionId());
        return result;
    }

    @RequestMapping(path = "/agreeAnswer", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ReturnMsg agreeAnswer(@RequestParam("userId") String userId,
                               @RequestParam("password") String password,
                               @RequestParam("answerId") Integer answerId,
                               @RequestParam("agreeOrNot") Boolean agreeOrNot, HttpServletRequest request, HttpServletResponse response) {

        if (!PhoneNumUtil.isPhone(userId)) {
            response.setStatus(403);
            return null;
        }
        User user = userService.checkUserWithRawPassword(userId, password);
        if (user == null) {
            response.setStatus(404);
            return null;
        }
        Answer answer = questionService.getAnswerById(answerId);
        if (answer == null) {
            response.setStatus(450);
            return null;
        }
        AnswerAgree aa = questionService.setAgreement(agreeOrNot, user, answer);

        response.setStatus(200);
        ReturnMsg result = new ReturnMsg();
        result.put("answerAgreeId", aa.getAnswerAgreeId());
        return result;
    }

    @RequestMapping(path = "/getAllId", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ReturnMsg getAllId(HttpServletRequest request, HttpServletResponse response) {
        List<Integer> data = questionService.getAllQuestionId();

        response.setStatus(200);
        ReturnMsg result = new ReturnMsg();
        result.put("count", data.size());
        result.put("data", data);
        return result;
    }

    @RequestMapping(path = "/digest/{questionId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ReturnMsg getDigest(@PathVariable Integer questionId, HttpServletRequest request, HttpServletResponse response) {
        Question question = questionService.getQuestionDigestById(questionId);
        if (question == null) {
            response.setStatus(404);
            return null;
        }

        response.setStatus(200);
        ReturnMsg result = new ReturnMsg();
        result.put("questionId", question.getQuestionId());
        result.put("userId", question.getAskUser().getUserId());
        result.put("userName", question.getAskUser().getName());
        result.put("title", question.getTitle());
        result.put("createDate", question.getCreateDate().toString());

        return result;
    }

    @RequestMapping(path = "/getAnswerIds/{questionId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ReturnMsg getAnswers(@PathVariable Integer questionId, HttpServletRequest request, HttpServletResponse response) {
        List<Integer> data = questionService.getAllAnswerIdByQuestionId(questionId);
        if (data.size() == 0) {
            response.setStatus(404);
            return null;
        }

        response.setStatus(200);
        ReturnMsg result = new ReturnMsg();
        result.put("count", data.size());
        result.put("data", data);

        return result;
    }

    @RequestMapping(path = "/getAnswer/{answerId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ReturnMsg getAnswer(@PathVariable Integer answerId, HttpServletRequest request, HttpServletResponse response) {
        Answer answer = questionService.getAnswerByIdWithUser(answerId);
        if (answer == null) {
            response.setStatus(404);
            return null;
        }

        response.setStatus(200);
        ReturnMsg result = new ReturnMsg();
        result.put("answerId", answer.getAnswerId());
        result.put("userId", answer.getAnswerUser().getUserId());
        result.put("userName", answer.getAnswerUser().getName());
        result.put("content", answer.getContent());
        result.put("createDate", answer.getCreateDate().toString());
        result.put("good", answer.getGood());
        result.put("bad", answer.getBad());

        return result;
    }
}
