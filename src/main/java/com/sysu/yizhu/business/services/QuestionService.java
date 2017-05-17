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

import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepo;

    @Autowired
    private AnswerRepository answerRepo;

    @Autowired
    private AnswerAgreeRepository answerAgreeRepo;


    public void createQuestion(Question question) {
        questionRepo.save(question);
    }

    public Question getQuestionById(Integer questionId) {
        return questionRepo.findOne(questionId);
    }

    public Answer getAnswerById(Integer answerId) {
        return answerRepo.findOne(answerId);
    }

    public Answer createAnswer(Answer answer) {
        if (answer.getQuestion().getQuestionId() != null) {
            return null;
        }
        answerRepo.save(answer);
        return answer;
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
        answerAgreeRepo.save(aa);
        return aa;
    }

    public List<Integer> getAllQuestionId() {
        return questionRepo.findAllId();
    }

    public Question getQuestionDigestById(Integer questionId) {
        return questionRepo.findDigestById(questionId);
    }

    public List<Integer> getAllAnswerIdByQuestionId(Integer questionId) {
        return answerRepo.findByQuestionId(questionId);
    }

    public Answer getAnswerByIdWithUser(Integer answerId) {
        return answerRepo.findByAnswerIdWithUser(answerId);
    }

}
