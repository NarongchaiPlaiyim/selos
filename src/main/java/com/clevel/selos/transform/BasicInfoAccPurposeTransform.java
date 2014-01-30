package com.clevel.selos.transform;

import com.clevel.selos.dao.master.BankAccountPurposeDAO;
import com.clevel.selos.model.db.master.BankAccountPurpose;
import com.clevel.selos.model.db.working.OpenAccountPurpose;
import com.clevel.selos.model.db.working.OpenAccount;
import com.clevel.selos.model.view.BasicInfoAccountPurposeView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class BasicInfoAccPurposeTransform extends Transform {

    @Inject
    private BankAccountPurposeDAO accountPurposeDAO;

    public OpenAccountPurpose transformToModel(BasicInfoAccountPurposeView basicInfoAccountPurposeView, OpenAccount openAccount) {

        OpenAccountPurpose openAccountPurpose = new OpenAccountPurpose();
        openAccountPurpose.setOpenAccount(openAccount);
        openAccountPurpose.setAccountPurpose(basicInfoAccountPurposeView.getPurpose());

        return openAccountPurpose;
    }

    public BasicInfoAccountPurposeView transformToView(OpenAccountPurpose openAccPurpose) {
        BasicInfoAccountPurposeView basicInfoAccountPurposeView = new BasicInfoAccountPurposeView();
        BankAccountPurpose openAccountPurpose = new BankAccountPurpose();

        basicInfoAccountPurposeView.setSelected(true);

        openAccountPurpose.setId(openAccPurpose.getId());
        //openAccountPurpose.setName(openAccPurpose.getPurposeName());

        basicInfoAccountPurposeView.setPurpose(openAccountPurpose);

        return basicInfoAccountPurposeView;
    }

    public List<BasicInfoAccountPurposeView> transformToViewList(List<OpenAccountPurpose> openAccPurposeList) {
        List<BasicInfoAccountPurposeView> basicInfoAccountPurposeViews = new ArrayList<BasicInfoAccountPurposeView>();
        if (openAccPurposeList != null) {
            for (OpenAccountPurpose oap : openAccPurposeList) {
                BasicInfoAccountPurposeView basicInfoAccountPurposeView = transformToView(oap);
                basicInfoAccountPurposeViews.add(basicInfoAccountPurposeView);
            }
        }
        return basicInfoAccountPurposeViews;
    }
}
