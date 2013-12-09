package com.clevel.selos.controller;


import com.clevel.selos.model.BaPaType;
import com.clevel.selos.model.view.BaPaInfoAddView;
import com.clevel.selos.model.view.CreditTypeDetailView;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@ManagedBean(name ="baInfo")
public class BaInfo implements Serializable {


    List<CreditTypeDetailView> creditTypeDetailList;


    BaPaInfoAddView baPaInfoDlgView;

    BaPaType baPaType;


    public BaInfo(){

    }

    @PostConstruct
    public void onCreate(){

        List<CreditTypeDetailView>creditList=new ArrayList<CreditTypeDetailView>();
        CreditTypeDetailView creditTypeDetailView=new CreditTypeDetailView();
        creditTypeDetailView.setProductProgram("SmartBiz");
        creditTypeDetailView.setCreditFacility("Loan");
        creditTypeDetailView.setPurpose(BigDecimal.ZERO);
        creditTypeDetailView.setLimit(BigDecimal.ONE);
        creditList.add(creditTypeDetailView);
        creditTypeDetailView=new CreditTypeDetailView();
        creditTypeDetailView.setProductProgram("SmartBiz");
        creditTypeDetailView.setCreditFacility("OD");
        creditTypeDetailView.setPurpose(BigDecimal.valueOf(23232323313L));
        creditTypeDetailView.setLimit(BigDecimal.valueOf(23232323313L));
        creditList.add(creditTypeDetailView);

        baPaInfoDlgView=new BaPaInfoAddView();
        baPaInfoDlgView.setType(BaPaType.BA);
        baPaInfoDlgView.setCreditType(creditList);
        baPaInfoDlgView.setPremium(BigDecimal.valueOf(23232323313L));
        baPaInfoDlgView.setMorePay(BigDecimal.TEN);
    }

    public void submitBaPaInfoDialog(){
        System.out.println(baPaInfoDlgView.toString());
        System.out.println(baPaInfoDlgView.getCreditType().get(0).isNoFlag());

    }
    public BaPaType[] getbapaType(){
        return BaPaType.values();
    }

    public List<CreditTypeDetailView> getCreditTypeDetailList() {
        return creditTypeDetailList;
    }

    public void setCreditTypeDetailList(List<CreditTypeDetailView> creditTypeDetailView) {
        this.creditTypeDetailList = creditTypeDetailView;
    }

    public BaPaInfoAddView getBapaInfoView() {
        return baPaInfoDlgView;
    }

    public void setBapaInfoView(BaPaInfoAddView bapaInfoView) {
        this.baPaInfoDlgView = bapaInfoView;
    }
}
