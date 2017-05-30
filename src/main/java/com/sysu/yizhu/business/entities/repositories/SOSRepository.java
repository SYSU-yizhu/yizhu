package com.sysu.yizhu.business.entities.repositories;

import com.sysu.yizhu.business.entities.SOS;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SOSRepository extends CrudRepository<SOS, Integer> {

    @Query("select s.sosId from SOS s where s.finished = ?1")
    List<Integer> findAllSOSIdByFinished(Boolean finished);

    @Query("from SOS s left outer join s.pushUser where s.sosId= ?1")
    SOS findWithUserById(Integer sosId);

}
