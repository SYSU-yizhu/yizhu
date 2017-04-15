package com.sysu.yizhu.business.entities.repositories;

import com.sysu.yizhu.business.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by CrazeWong on 2017/4/6.
 */
@Repository
public interface UserRepository extends CrudRepository<User, String> {

}
