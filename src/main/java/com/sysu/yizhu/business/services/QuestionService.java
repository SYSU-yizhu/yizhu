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
        }
        aa.setType(agreeOrNot);
        answerAgreeRepo.save(aa);
        return aa;
    }

    public List<Question> getAllQuestion() {
        return questionRepo.findAllDigest();
    }

}
