package com.sysu.yizhu.business.entities.repositories;

import com.sysu.yizhu.business.entities.Answer;
import com.sysu.yizhu.business.entities.AnswerAgree;
import com.sysu.yizhu.business.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface AnswerAgreeRepository extends CrudRepository<AnswerAgree, Integer> {
    AnswerAgree findByUserAndAnswer(User user, Answer answer);
}
