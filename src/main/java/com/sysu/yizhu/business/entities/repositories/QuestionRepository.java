package com.sysu.yizhu.business.entities.repositories;


import com.sysu.yizhu.business.entities.Question;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends CrudRepository<Question, Integer> {

    @Query("select q.questionId from Question q")
    List<Integer> findAllId();

    @Query("select q.questionId, q.askUser.userId, q.askUser.name, " +
            "q.title, q.createDate from Question q " +
            "left outer join q.askUser where q.questionId= ?1")
    List<Object[]> findDigestById(Integer questionId);

    @Query("select q from Question q left outer join fetch q.askUser where q.questionId= ?1")
    Question findWholeById(Integer questionId);
}
