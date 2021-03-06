package com.sysu.yizhu.business.entities.repositories;

import com.sysu.yizhu.business.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

}
