package com.clevel.selos.transform;

import com.clevel.selos.integration.ncrs.ncrsmodel.ResponseNCRSModel;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.working.Prescreen;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import com.clevel.selos.model.view.NcbView;
import com.clevel.selos.model.view.PrescreenView;

import java.util.ArrayList;
import java.util.List;

public class NcbTransform extends Transform {
    public List<NcbView> transformToView(List<ResponseNCRSModel> responseNCRSModels){
        List<NcbView> ncbViews = null;
        if(responseNCRSModels!=null && responseNCRSModels.size()>0){
            ncbViews = new ArrayList<NcbView>();
            for(ResponseNCRSModel responseNCRSModel: responseNCRSModels){
                NcbView ncbView = new NcbView();
                ncbView.setIdNumber(responseNCRSModel.getIdNumber());
                if(responseNCRSModel.getResult().equals("SUCCESS")){
                    ncbView.setResult(ActionResult.SUCCEED);
                } else {
                    ncbView.setResult(ActionResult.FAILED);
                    ncbView.setReason(responseNCRSModel.getReason());
                }
                ncbViews.add(ncbView);
            }
        }
        return ncbViews;
    }
}
