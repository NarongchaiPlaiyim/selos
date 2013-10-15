package com.clevel.selos.transform;

import com.clevel.selos.model.db.master.OpenAccountPurpose;
import com.clevel.selos.model.db.working.OpenAccPurpose;
import com.clevel.selos.model.db.working.OpenAccount;
import com.clevel.selos.model.view.BasicInfoAccountPurposeView;

import java.util.ArrayList;
import java.util.List;

public class BasicInfoAccPurposeTransform extends Transform {

    public OpenAccPurpose transformToModel(BasicInfoAccountPurposeView basicInfoAccountPurposeView, OpenAccount openAccount){
        OpenAccPurpose openAccPurpose = new OpenAccPurpose();

        openAccPurpose.setOpenAccount(openAccount);

        if(basicInfoAccountPurposeView.getPurpose().getId() != 0){
            openAccPurpose.setId(basicInfoAccountPurposeView.getPurpose().getId());
        }

        openAccPurpose.setPurposeName(basicInfoAccountPurposeView.getPurpose().getName());

        return openAccPurpose;
    }

    public BasicInfoAccountPurposeView transformToView(OpenAccPurpose openAccPurpose){
        BasicInfoAccountPurposeView basicInfoAccountPurposeView = new BasicInfoAccountPurposeView();
        OpenAccountPurpose openAccountPurpose = new OpenAccountPurpose();

        basicInfoAccountPurposeView.setSelected(true);

        openAccountPurpose.setId(openAccPurpose.getId());
        openAccountPurpose.setName(openAccPurpose.getPurposeName());

        basicInfoAccountPurposeView.setPurpose(openAccountPurpose);

        return basicInfoAccountPurposeView;
    }

    public List<BasicInfoAccountPurposeView> transformToViewList(List<OpenAccPurpose> openAccPurposeList){
        List<BasicInfoAccountPurposeView> basicInfoAccountPurposeViews = new ArrayList<BasicInfoAccountPurposeView>();
        if(openAccPurposeList != null){
            for(OpenAccPurpose oap : openAccPurposeList){
                BasicInfoAccountPurposeView basicInfoAccountPurposeView = transformToView(oap);
                basicInfoAccountPurposeViews.add(basicInfoAccountPurposeView);
            }
        }
        return basicInfoAccountPurposeViews;
    }
}
