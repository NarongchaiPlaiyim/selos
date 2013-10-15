package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.Prescreen;
import com.clevel.selos.model.db.working.PrescreenBusiness;
import com.clevel.selos.model.view.BizInfoDetailView;

import java.util.ArrayList;
import java.util.List;

public class PrescreenBusinessTransform extends Transform {

    public PrescreenBusiness transformToModel(BizInfoDetailView bizInfoDetailView, Prescreen prescreen){
        PrescreenBusiness prescreenBusiness = new PrescreenBusiness();

        prescreenBusiness.setBusinessDescription(bizInfoDetailView.getBizDesc());
        prescreenBusiness.setPrescreen(prescreen);

        return prescreenBusiness;
    }

    public List<PrescreenBusiness> transformToModelList(List<BizInfoDetailView> bizInfoDetailViews, Prescreen prescreen){
        List<PrescreenBusiness> prescreenBusinessList = new ArrayList<PrescreenBusiness>();

        for(BizInfoDetailView bizInfoDetailView : bizInfoDetailViews){
            PrescreenBusiness prescreenBusiness = new PrescreenBusiness();
            prescreenBusiness = transformToModel(bizInfoDetailView, prescreen);
            prescreenBusinessList.add(prescreenBusiness);
        }

        return prescreenBusinessList;
    }

    public BizInfoDetailView transformToView(PrescreenBusiness prescreenBusiness){
        BizInfoDetailView bizInfoDetailView = new BizInfoDetailView();
        bizInfoDetailView.reset();
        bizInfoDetailView.setBizDesc(prescreenBusiness.getBusinessDescription());

        return bizInfoDetailView;
    }

    public List<BizInfoDetailView> transformToViewList(List<PrescreenBusiness> prescreenBusinesses){
        List<BizInfoDetailView> bizInfoDetailViewList = new ArrayList<BizInfoDetailView>();

        for(PrescreenBusiness prescreenBusiness : prescreenBusinesses){
            BizInfoDetailView bizInfoDetailView = new BizInfoDetailView();
            bizInfoDetailView = transformToView(prescreenBusiness);
            bizInfoDetailViewList.add(bizInfoDetailView);
        }

        return bizInfoDetailViewList;
    }
}
