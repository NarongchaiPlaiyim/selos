package com.clevel.selos.transform;

import com.clevel.selos.model.db.master.OpenAccountProduct;
import com.clevel.selos.model.db.working.BasicInfo;
import com.clevel.selos.model.db.working.OpenAccount;
import com.clevel.selos.model.view.BankAccountTypeView;
import com.clevel.selos.model.view.BasicInfoAccountPurposeView;
import com.clevel.selos.model.view.BasicInfoAccountView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class BasicInfoAccountTransform extends Transform {

    @Inject
    BankAccountTypeTransform bankAccountTypeTransform;

    @Inject
    public BasicInfoAccountTransform() {
    }

    public OpenAccount transformToModel(BasicInfoAccountView basicInfoAccountView, BasicInfo basicInfo) {
        OpenAccount openAccount = new OpenAccount();

        openAccount.setBasicInfo(basicInfo);

        if (basicInfoAccountView.getId() != 0) {
            openAccount.setId(basicInfoAccountView.getId());
        }

        openAccount.setAccountName(basicInfoAccountView.getAccountName());

        openAccount.setAccountProduct(basicInfoAccountView.getProduct());
        if (openAccount.getAccountProduct().getId() == 0) {
            openAccount.setAccountProduct(null);
        }

        openAccount.setBankAccountType(bankAccountTypeTransform.getBankAccountType(basicInfoAccountView.getBankAccountTypeView()));
        if (openAccount.getBankAccountType().getId() == 0) {
            openAccount.setBankAccountType(null);
        }

        return openAccount;
    }

    public BasicInfoAccountView transformToView(OpenAccount openAccount) {
        BasicInfoAccountView basicInfoAccountView = new BasicInfoAccountView();

        basicInfoAccountView.setId(openAccount.getId());

        basicInfoAccountView.setAccountName(openAccount.getAccountName());

        basicInfoAccountView.setProduct(openAccount.getAccountProduct());
        if (basicInfoAccountView.getProduct() == null) {
            basicInfoAccountView.setProduct(new OpenAccountProduct());
            basicInfoAccountView.getProduct().setName("-"); // for view
        }

        basicInfoAccountView.setBankAccountTypeView(bankAccountTypeTransform.getBankAccountTypeView(openAccount.getBankAccountType()));
        if (basicInfoAccountView.getBankAccountTypeView() == null) {
            basicInfoAccountView.setBankAccountTypeView(new BankAccountTypeView());
            basicInfoAccountView.getBankAccountTypeView().setName("-"); // for view
        }

        BasicInfoAccPurposeTransform basicInfoAccPurposeTransform = new BasicInfoAccPurposeTransform();
        List<BasicInfoAccountPurposeView> basicInfoAccountPurposeViews = basicInfoAccPurposeTransform.transformToViewList(openAccount.getOpenAccPurposeList());

        basicInfoAccountView.setBasicInfoAccountPurposeView(basicInfoAccountPurposeViews);

        //for show view
        if(basicInfoAccountView.getBasicInfoAccountPurposeView().size() > 0){
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < basicInfoAccountView.getBasicInfoAccountPurposeView().size(); i++) {
                if (i == 0) {
                    stringBuilder.append(basicInfoAccountView.getBasicInfoAccountPurposeView().get(i).getPurpose().getName());
                } else {
                    stringBuilder.append(", " + basicInfoAccountView.getBasicInfoAccountPurposeView().get(i).getPurpose().getName());
                }
            }
            basicInfoAccountView.setPurposeForShow(stringBuilder.toString());
        } else {
            basicInfoAccountView.setPurposeForShow("-"); // for view
        }

        return basicInfoAccountView;
    }

    public List<BasicInfoAccountView> transformToViewList(List<OpenAccount> openAccountList) {
        List<BasicInfoAccountView> basicInfoAccountViews = new ArrayList<BasicInfoAccountView>();
        if (openAccountList != null) {
            for (OpenAccount op : openAccountList) {
                BasicInfoAccountView basicInfoAccountView = transformToView(op);
                basicInfoAccountViews.add(basicInfoAccountView);
            }
        }
        return basicInfoAccountViews;
    }
}
