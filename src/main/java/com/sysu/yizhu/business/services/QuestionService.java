package com.sysu.yizhu.business.services;

import com.sysu.yizhu.business.entities.Answer;
import com.sysu.yizhu.business.entities.AnswerAgree;
import com.sysu.yizhu.business.entities.Question;
import com.sysu.yizhu.business.entities.User;
import com.sysu.yizhu.business.entities.repositories.AnswerAgreeRepository;
import com.sysu.yizhu.business.entities.repositories.AnswerRepository;
import com.sysu.yizhu.business.entities.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepo;

    @Autowired
    private AnswerRepository answerRepo;

    @Autowired
    private AnswerAgreeRepository answerAgreeRepo;

    public Question createQuestion(User user, String title, String content) {
        Question question = new Question();
        question.setAskUser(user);
        question.setContent(content);
        question.setCreateDate(new Date(System.currentTimeMillis()));
        question.setTitle(title);
        return questionRepo.save(question);
    }

    public Question getQuestionById(Integer questionId) {
        return questionRepo.findOne(questionId);
    }

    public Answer getAnswerById(Integer answerId) {
        return answerRepo.findOne(answerId);
    }

    public Answer createAnswer(Question question, User user, String content) {
        if (question.getQuestionId() == null) {
            return null;
        }
        Answer answer = new Answer();
        answer.setQuestion(question);
        answer.setCreateDate(new Date(System.currentTimeMillis()));
        answer.setContent(content);
        answer.setAnswerUser(user);
        answer.setBad(0);
        answer.setGood(0);
        return answerRepo.save(answer);
    }

    public AnswerAgree setAgreement(Boolean agreeOrNot, User user, Answer answer) {
        AnswerAgree aa = answerAgreeRepo.findByUserAndAnswer(user, answer);
        if (aa == null) {
            aa = new AnswerAgree();
            aa.setAnswer(answer);
            aa.setUser(user);
        } else {
            if (aa.getType() == Boolean.TRUE) {
                answer.setGood(answer.getGood()-1);
            } else {
                answer.setBad(answer.getBad()-1);
            }
        }
        if (agreeOrNot == Boolean.TRUE) {
            answer.setGood(answer.getGood()+1);
        } else {
            answer.setBad(answer.getBad()+1);
        }
        aa.setType(agreeOrNot);
        answerRepo.save(answer);
        answerAgreeRepo.save(aa);
        return aa;
    }

    public List<Integer> getAllQuestionId() {
        return questionRepo.findAllId();
    }

    public Question getQuestionDigestById(Integer questionId) {
        List<Object[]> objs = questionRepo.findDigestById(questionId);
        Question question = new Question();
        User askUser = new User();
        askUser.setUserId((String) objs.get(0)[1]);
        askUser.setName((String) objs.get(0)[2]);

        question.setQuestionId((Integer) objs.get(0)[0]);

        question.setAskUser(askUser);
        question.setTitle((String) objs.get(0)[3]);
        question.setCreateDate((Date) objs.get(0)[4]);
        return question;
    }

    public List<Integer> getAllAnswerIdByQuestionId(Integer questionId) {
        return answerRepo.findByQuestionId(questionId);
    }

    public Answer getAnswerByIdWithUser(Integer answerId) {
        return answerRepo.findByAnswerIdWithUser(answerId);
    }

}
