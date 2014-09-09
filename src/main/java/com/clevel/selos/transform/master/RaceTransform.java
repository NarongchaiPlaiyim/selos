package com.clevel.selos.transform.master;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Race;
import com.clevel.selos.model.view.master.RaceView;
import com.clevel.selos.transform.Transform;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RaceTransform extends Transform {

    @Inject
    @SELOS
    private Logger logger;

    public RaceView transformToView(Race race){
        RaceView raceView = new RaceView();
        if(race != null){
            raceView.setId(race.getId());
            raceView.setCode(race.getCode());
            raceView.setName(race.getName());
            raceView.setActive(race.getActive());
        }
        return raceView;
    }

    public Map<Integer, RaceView> transformToCache(List<Race> raceList){
        if(raceList == null || raceList.size() == 0)
            return null;

        Map<Integer, RaceView> _tmpMap = new ConcurrentHashMap<Integer, RaceView>();
        for(Race race : raceList){
            RaceView raceView = transformToView(race);
            _tmpMap.put(raceView.getId(), raceView);
        }
        return _tmpMap;
    }

}
