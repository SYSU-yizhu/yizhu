package com.sysu.yizhu.business.entities.repositories;


import com.sysu.yizhu.business.entities.Question;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends CrudRepository<Question, String> {
}
