package com.sysu.yizhu.business.entities.repositories;


import com.sysu.yizhu.business.entities.Answer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends CrudRepository<Answer, Integer> {
}
