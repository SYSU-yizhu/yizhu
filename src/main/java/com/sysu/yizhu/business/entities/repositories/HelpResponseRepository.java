package com.sysu.yizhu.business.entities.repositories;

import com.sysu.yizhu.business.entities.HelpResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelpResponseRepository extends CrudRepository<HelpResponse, Integer> {

    @Query("select r.helpResponseUser.userId from HelpResponse r where r.help.helpId = ?1")
    List<String> findAllUserIdByHelpId(Integer sosId);
}
