package com.sysu.yizhu.business.services;

import com.sysu.yizhu.business.entities.SOSResponse;
import com.sysu.yizhu.business.entities.SOS;
import com.sysu.yizhu.business.entities.User;
import com.sysu.yizhu.business.entities.repositories.SOSResponseRepository;
import com.sysu.yizhu.business.entities.repositories.SOSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.List;

@Service
public class SOSService {

    @Autowired
    private SOSRepository sosRepo;

    @Autowired
    private SOSResponseRepository sosResponseRepo;

    public SOS getSOS(Integer sosId) {
        return sosRepo.findOne(sosId);
    }

    public SOS createPushSOS(User user, Double latitude, Double longitude) {
        SOS sos = new SOS();
        sos.setPushUser(user);
        sos.setFinished(Boolean.FALSE);
        sos.setCreateTime(new Time(System.currentTimeMillis()));
        sos.setLatitude(latitude);
        sos.setLongitude(longitude);
        return sosRepo.save(sos);
    }

    public void finishSOS(SOS sos) {
        sos.setFinished(Boolean.TRUE);
        sosRepo.save(sos);
    }

    public SOSResponse createResponse(User user, SOS sos) {
        SOSResponse sosResponse = new SOSResponse();
        sosResponse.setSosResponseUser(user);
        sosResponse.setSos(sos);
        return sosResponseRepo.save(sosResponse);
    }

    public List<Integer> getAllValidSOSId() {
        return sosRepo.findAllSOSIdByFinished(Boolean.FALSE);
    }

    public SOS getSOSWithUserById(Integer sosId) {
        return sosRepo.findWithUserById(sosId);
    }

    public List<String> getAllResponserBySOSId(Integer sosId) {
        return sosResponseRepo.findAllUserIdBySOSId(sosId);
    }
}
