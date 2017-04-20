package com.sysu.yizhu.business.services;

import com.sysu.yizhu.business.entities.User;
import com.sysu.yizhu.business.entities.repositories.UserRepository;
import com.sysu.yizhu.util.MD5Parser;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by CrazeWong on 2017/4/15.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    public UserService() {
    }

    public User findOne(String userId) {
        return userRepo.findOne(userId);
    }

    public User checkUserWithRawPassword(String userId, String rawPassword) {
        String md5Password = MD5Parser.getMD5(rawPassword);

        User user = userRepo.findOne(userId);
        if (user == null || !user.getPassword().equals(md5Password)) return null;

        return user;
    }

    public User createUserWithRawPassword(User user) {
        user.setPassword(MD5Parser.getMD5(user.getPassword()));

        User olduser = null;
        try {
            olduser = userRepo.save(user);
        } catch (Exception e) {
            Throwable t = e.getCause();
            while ((t != null) && !(t instanceof ConstraintViolationException)) {
                t = t.getCause();
            }
            if (t instanceof ConstraintViolationException) {
                System.out.println("123");
            }
        }

        return olduser;
    }
}
