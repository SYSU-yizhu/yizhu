package com.sysu.yizhu.business.entities.repositories;


import com.sysu.yizhu.business.entities.Answer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends CrudRepository<Answer, Integer> {

    @Query("select a.answerId from Answer a where a.question.questionId = ?1")
    List<Integer> findByQuestionId(Integer questionId);

    @Query("from Answer a left outer join fetch a.answerUser where a.answerId = ?1")
    Answer findByAnswerIdWithUser(Integer answerId);
}
