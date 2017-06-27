package com.sysu.yizhu.business.entities.repositories;

import com.sysu.yizhu.business.entities.SOSResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SOSResponseRepository extends CrudRepository<SOSResponse, Integer> {

    @Query("select r.sosResponseUser.userId from SOSResponse r where r.sos.sosId = ?1")
    List<String> findAllUserIdBySOSId(Integer sosId);
}
