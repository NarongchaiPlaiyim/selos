package com.clevel.selos.transform;

import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.BasicInfo;
import com.clevel.selos.model.db.working.OpenAccount;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.BasicInfoAccountView;
import com.clevel.selos.model.view.BasicInfoView;

import java.util.ArrayList;
import java.util.List;

public class BasicInfoAccountTransform extends Transform {

    public OpenAccount transformToModel(BasicInfoAccountView basicInfoAccountView, BasicInfo basicInfo){
        OpenAccount openAccount = new OpenAccount();

        openAccount.setBasicInfo(basicInfo);

        if(basicInfoAccountView.getId() != 0){
            openAccount.setId(basicInfoAccountView.getId());
        }

        openAccount.setAccountName(basicInfoAccountView.getAccountName());

        openAccount.setAccountProduct(basicInfoAccountView.getProduct());
        if(openAccount.getAccountProduct().getId() == 0){
            openAccount.setAccountProduct(null);
        }

        openAccount.setAccountType(basicInfoAccountView.getAccountType());
        if(openAccount.getAccountType().getId() == 0){
            openAccount.setAccountType(null);
        }

        return openAccount;
    }

    public BasicInfoAccountView transformToView(OpenAccount openAccount){
        BasicInfoAccountView basicInfoAccountView = new BasicInfoAccountView();

        basicInfoAccountView.setId(openAccount.getId());

        basicInfoAccountView.setAccountName(openAccount.getAccountName());

        basicInfoAccountView.setProduct(openAccount.getAccountProduct());
        if(basicInfoAccountView.getProduct() == null){
            basicInfoAccountView.setProduct(new OpenAccountProduct());
        }

        basicInfoAccountView.setAccountType(openAccount.getAccountType());
        if(basicInfoAccountView.getAccountType() == null){
            basicInfoAccountView.setAccountType(new OpenAccountType());
        }

        return basicInfoAccountView;
    }

    public List<BasicInfoAccountView> transformToViewList(List<OpenAccount> openAccountList){
        List<BasicInfoAccountView> basicInfoAccountViews = new ArrayList<BasicInfoAccountView>();
        if(openAccountList != null){
            for(OpenAccount op : openAccountList){
                BasicInfoAccountView basicInfoAccountView = transformToView(op);
                basicInfoAccountViews.add(basicInfoAccountView);
            }
        }
        return basicInfoAccountViews;
    }
}
