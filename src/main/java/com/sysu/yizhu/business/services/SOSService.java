package com.sysu.yizhu.business.services;

import com.sysu.yizhu.business.entities.Response;
import com.sysu.yizhu.business.entities.SOS;
import com.sysu.yizhu.business.entities.User;
import com.sysu.yizhu.business.entities.repositories.ResponseRepository;
import com.sysu.yizhu.business.entities.repositories.SOSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class SOSService {

    @Autowired
    private SOSRepository sosRepo;

    @Autowired
    private ResponseRepository responseRepo;

    public SOS getSOS(Integer sosId) {
        return sosRepo.findOne(sosId);
    }

    public SOS createPushSOS(User user, Double latitude, Double longitude) {
        SOS sos = new SOS();
        sos.setPushUser(user);
        sos.setFinished(Boolean.FALSE);
        sos.setCreateDate(new Date(System.currentTimeMillis()));
        sos.setLatitude(latitude);
        sos.setLongitude(longitude);
        return sosRepo.save(sos);
    }

    public void finishSOS(SOS sos) {
        sos.setFinished(Boolean.TRUE);
        sosRepo.save(sos);
    }

    public Response createResponse(User user, SOS sos) {
        Response response = new Response();
        response.setResponseUser(user);
        response.setSos(sos);
        return responseRepo.save(response);
    }

    public List<Integer> getAllValidSOSId() {
        return sosRepo.findAllSOSIdByFinished(Boolean.FALSE);
    }

    public SOS getSOSWithUserById(Integer sosId) {
        return sosRepo.findWithUserById(sosId);
    }

    public List<String> getAllResponserBySOSId(Integer sosId) {
        return responseRepo.findAllUserIdBySOSId(sosId);
    }
}
