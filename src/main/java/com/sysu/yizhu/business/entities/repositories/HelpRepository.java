package com.sysu.yizhu.business.entities.repositories;


import com.sysu.yizhu.business.entities.Help;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelpRepository extends CrudRepository<Help, Integer> {
    @Query("select h.helpId from Help h where h.finished = ?1")
    List<Integer> findAllHelpIdByFinished(Boolean finished);

    @Query("from Help h left outer join fetch h.pushUser where h.helpId= ?1")
    Help findWithUserById(Integer helpId);
}
