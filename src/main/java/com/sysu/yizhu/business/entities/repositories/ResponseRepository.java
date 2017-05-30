package com.sysu.yizhu.business.entities.repositories;

import com.sysu.yizhu.business.entities.Response;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResponseRepository extends CrudRepository<Response, Integer> {

    @Query("select r.responseUser.userId from Response r where r.sos.sosId = ?1")
    List<String> findAllUserIdBySOSId(Integer sosId);
}
