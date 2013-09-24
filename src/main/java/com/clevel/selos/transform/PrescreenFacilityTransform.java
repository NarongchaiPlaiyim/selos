package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.Prescreen;
import com.clevel.selos.model.db.working.PrescreenFacility;
import com.clevel.selos.model.view.FacilityView;

import java.util.ArrayList;
import java.util.List;

public class PrescreenFacilityTransform extends Transform {

    public List<PrescreenFacility> transformModel(List<FacilityView> facilityViewList, Prescreen prescreen){
        List<PrescreenFacility> prescreenFacilityList = new ArrayList<PrescreenFacility>();
        for(FacilityView facilityView : facilityViewList){
            PrescreenFacility prescreenFacility = new PrescreenFacility();
            prescreenFacility.setId(facilityView.getId());
            prescreenFacility.setPrescreen(prescreen);
            prescreenFacility.setCreditType(facilityView.getCreditType());
            prescreenFacility.setProductProgram(facilityView.getProductProgram());
            prescreenFacility.setRequestAmount(facilityView.getRequestAmount());
            prescreenFacilityList.add(prescreenFacility);
        }
        return prescreenFacilityList;
    }
}
