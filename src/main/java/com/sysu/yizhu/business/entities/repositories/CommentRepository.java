package com.sysu.yizhu.business.entities.repositories;


import com.sysu.yizhu.business.entities.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Integer> {
}
