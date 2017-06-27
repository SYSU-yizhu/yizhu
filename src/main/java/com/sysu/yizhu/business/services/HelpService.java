package com.sysu.yizhu.business.services;


import com.sysu.yizhu.business.entities.Help;
import com.sysu.yizhu.business.entities.HelpResponse;
import com.sysu.yizhu.business.entities.User;
import com.sysu.yizhu.business.entities.repositories.HelpRepository;
import com.sysu.yizhu.business.entities.repositories.HelpResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.List;

@Service
public class HelpService {
    @Autowired
    private HelpRepository helpRepo;

    @Autowired
    private HelpResponseRepository helpResponseRepo;

    public Help getHelp(Integer helpId) {
        return helpRepo.findOne(helpId);
    }

    public Help createPushHelp(User user, Double latitude, Double longitude, String title, String detail, Integer needs) {
        Help help = new Help();
        help.setPushUser(user);
        help.setFinished(Boolean.FALSE);
        help.setCreateTime(new Time(System.currentTimeMillis()));
        help.setLatitude(latitude);
        help.setLongitude(longitude);
        help.setTitle(title);
        help.setDetail(detail);
        help.setNeeds(needs);
        help.setResponseNum(0);
        return helpRepo.save(help);
    }

    public void finishHelp(Help help) {
        help.setFinished(Boolean.TRUE);
        helpRepo.save(help);
    }

    public HelpResponse createResponse(User user, Help help) {
        HelpResponse helpResponse = new HelpResponse();
        helpResponse.setHelpResponseUser(user);
        helpResponse.setHelp(help);
        help.setResponseNum(help.getResponseNum()+1);
        return helpResponseRepo.save(helpResponse);
    }

    public List<Integer> getAllValidHelpId() {
        return helpRepo.findAllHelpIdByFinished(Boolean.FALSE);
    }

    public Help getHelpWithUserById(Integer helpId) {
        return helpRepo.findWithUserById(helpId);
    }

    public List<String> getAllResponserByHelpId(Integer helpId) {
        return helpResponseRepo.findAllUserIdByHelpId(helpId);
    }
}
