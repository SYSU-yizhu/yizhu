package com.sysu.yizhu.business.entities.repositories;


import com.sysu.yizhu.business.entities.Question;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends CrudRepository<Question, Integer> {

    @Query("select q.questionId, q.title from Question q")
    List<Question> findAllDigest();
}
