package com.sysu.yizhu.business.services;

import com.sysu.yizhu.business.entities.repositories.AnswerRepository;
import com.sysu.yizhu.business.entities.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepo;

    @Autowired
    private AnswerRepository answerRepo;

}
