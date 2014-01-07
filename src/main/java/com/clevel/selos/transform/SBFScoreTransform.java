package com.clevel.selos.transform;

import com.clevel.selos.dao.master.SBFScoreDAO;
import com.clevel.selos.model.db.master.SBFScore;
import com.clevel.selos.model.view.SBFScoreView;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class SBFScoreTransform extends Transform{

    @Inject
    private SBFScoreDAO sbfScoreDAO;

    @Inject
    public SBFScoreTransform(){
    }

    public SBFScoreView transformToView(SBFScore sbfScore){
        SBFScoreView sbfScoreView = new SBFScoreView();
        if(sbfScore == null){
            log.debug("transformToView() sbfScore is null!");
            return sbfScoreView;
        }
        sbfScoreView.setId(sbfScore.getId());
        sbfScoreView.setScore(sbfScore.getScore());
        sbfScoreView.setActive(sbfScore.getActive());
        return sbfScoreView;
    }

    public SBFScore transformToModel(SBFScoreView sbfScoreView){
        log.info("transformToModel({})", sbfScoreView);
        SBFScore sbfScore = null;
        if(sbfScoreView != null){
            try{
                if(sbfScoreView.getId() != 0){
                    sbfScore = sbfScoreDAO.findById(sbfScoreView.getId());
                }
            } catch(EntityNotFoundException ex){
                log.info("Cannot find SBFScore {}", sbfScoreView);
            }
        }
        log.info("transformToModel return {}", sbfScore);
        return sbfScore;
    }

    public SBFScoreView transformToView(int score){
        log.debug("transformToView(int score), score = {}", score);
        SBFScoreView sbfScoreView = new SBFScoreView();
        try{
            SBFScore sbfScore = sbfScoreDAO.findByScore(score);
            sbfScoreView = transformToView(sbfScore);
        } catch (Exception ex){
            log.info("Cannot find SBFScore {}", score);
        }
        return sbfScoreView;
    }

    public List<SBFScoreView> transformToView(List<SBFScore> sbfScoreList){
        List<SBFScoreView> sbfScoreViewList = new ArrayList<SBFScoreView>();
        for(SBFScore sbfScore : sbfScoreList){
            sbfScoreViewList.add(transformToView(sbfScore));
        }
        return sbfScoreViewList;
    }
}
