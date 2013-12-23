package com.clevel.selos.controller;


import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.BaPaType;
import com.clevel.selos.model.view.ApplyBaInfoView;
import com.clevel.selos.model.view.BaPaInfoAddView;
import com.clevel.selos.model.view.BaPaInfoView;
import com.clevel.selos.model.view.CreditTypeDetailView;
import com.clevel.selos.util.DateTimeUtil;
import org.joda.time.DateTime;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ViewScoped
@ManagedBean(name ="baInfo")
public class BaInfo implements Serializable {

    /*@Inject
    @SELOS
    Logger log;

    int selectRowNumber;

    List<CreditTypeDetailView> creditTypeDetailList;
    List<ApplyBaInfoView> applyBaInfoList;

    BaPaInfoView baPaInfoView;
    BaPaInfoAddView baPaInfoAddView;
    BaPaInfoAddView newBaPaInfoAddViewItem;
    ApplyBaInfoView applyBaInfoView;
    ApplyBaInfoView newApplyBaInfoView;

    ArrayList<BaPaInfoAddView> listBaPa ;

    enum ModeForButton{ ADD, EDIT }
    enum HealthCheckList {ตรวจ,ขอข้อมูลสุขภาพ}

    private ModeForButton modeForButton;

    public BaInfo(){

    }

    @PostConstruct
    public void onCreate(){

        baPaInfoView =new BaPaInfoView();
        listBaPa=new ArrayList<BaPaInfoAddView>();
        baPaInfoAddView=new BaPaInfoAddView();
        creditTypeDetailList=new ArrayList<CreditTypeDetailView>();
        applyBaInfoList=new ArrayList<ApplyBaInfoView>();
        applyBaInfoView =new ApplyBaInfoView();

        ApplyBaInfoView applyBaInfoView=new ApplyBaInfoView();
        applyBaInfoView.setChecked(false);
        applyBaInfoView.setCusName("Mr. A Example");
        applyBaInfoView.setContactNumber("08-1-111-2222");
        applyBaInfoView.setRelationship("Borrower");
        applyBaInfoView.setResultHealtcheck("ตรวจ");
        applyBaInfoView.setCheckDate(new Date());

        applyBaInfoList.add(applyBaInfoView);
        applyBaInfoView=new ApplyBaInfoView();
        applyBaInfoView.setChecked(false);
        applyBaInfoView.setCusName("Mr. B Example");
        applyBaInfoView.setContactNumber("08-1-111-2223");
        applyBaInfoView.setRelationship("Borrower");
        applyBaInfoView.setResultHealtcheck("ขอข้อมูลสุขภาพ");
        applyBaInfoView.setCheckDate(new Date());
        applyBaInfoList.add(applyBaInfoView);

        baPaInfoView.setBa(applyBaInfoList);

        CreditTypeDetailView creditTypeDetailView=new CreditTypeDetailView();
        creditTypeDetailView.setProductProgram("SmartBiz");
        creditTypeDetailView.setCreditFacility("Loan");
        creditTypeDetailView.setPurpose("คุ้มครองวงเงิน");
        creditTypeDetailView.setLimit(BigDecimal.valueOf(50000L));
        creditTypeDetailView.setNoFlag(false);
        creditTypeDetailList.add(creditTypeDetailView);

        creditTypeDetailView=new CreditTypeDetailView();
        creditTypeDetailView.setProductProgram("SmartBiz");
        creditTypeDetailView.setCreditFacility("OD");
        creditTypeDetailView.setPurpose("คุ้มครองวงเงิน O/D");
        creditTypeDetailView.setLimit(BigDecimal.valueOf(2000000L));
        creditTypeDetailView.setNoFlag(false);
        creditTypeDetailList.add(creditTypeDetailView);



    }

    public void onOpenApplyBaDialog(){
        log.debug("onOpenApplyBaDialog()");

        modeForButton = ModeForButton.EDIT;
        applyBaInfoView = new ApplyBaInfoView();
        applyBaInfoView.setCusName(newApplyBaInfoView.getCusName());
        applyBaInfoView.setContactNumber(newApplyBaInfoView.getContactNumber());
        applyBaInfoView.setRelationship(newApplyBaInfoView.getRelationship());
        applyBaInfoView.setResultHealtcheck(newApplyBaInfoView.getResultHealtcheck());

        applyBaInfoView.setCheckDate(newApplyBaInfoView.getCheckDate());


    }
    public void openAddDialog(){
        log.debug("openAddDialog()");
        modeForButton=ModeForButton.ADD;
        baPaInfoAddView =new BaPaInfoAddView();

        if(creditTypeDetailList!=null){
           for(int i=0;i<creditTypeDetailList.size();i++){
               creditTypeDetailList.get(i).setNoFlag(false);
           }
        }
    }

    public void openEditBaPaDialog(){
        log.debug("openEditBaPaDialog()");
        modeForButton=ModeForButton.EDIT;

        System.out.println(baPaInfoView.getBapa().get(selectRowNumber).toString());
        baPaInfoAddView=new BaPaInfoAddView();
        baPaInfoAddView.setType(newBaPaInfoAddViewItem.getType());
        baPaInfoAddView.setMorePay(newBaPaInfoAddViewItem.getMorePay());
        baPaInfoAddView.setLimit(newBaPaInfoAddViewItem.getLimit());
        baPaInfoAddView.setPremium(newBaPaInfoAddViewItem.getPremium());

        for(int i=0;i<creditTypeDetailList.size();i++){
                creditTypeDetailList.get(i).setNoFlag(false);
            for(int j=0 ;j<baPaInfoView.getBapa().get(selectRowNumber).getCreditType().size();j++){
                    if(baPaInfoView.getBapa().get(selectRowNumber).getCreditType().get(j).getSeq()==i){
                        creditTypeDetailList.get(i).setNoFlag(true);
                    }
            }
        }


    }

    public void onSubmitApplyBaDialog(){
         log.debug("onSubmitApplyBaDialog()");

        boolean complete =false;
        if(modeForButton !=null && modeForButton.equals(ModeForButton.EDIT)) {

        applyBaInfoList.get(selectRowNumber).setCusName(applyBaInfoView.getCusName());
        applyBaInfoList.get(selectRowNumber).setContactNumber(applyBaInfoView.getContactNumber());
        applyBaInfoList.get(selectRowNumber).setRelationship(applyBaInfoView.getRelationship());
        applyBaInfoList.get(selectRowNumber).setResultHealtcheck(applyBaInfoView.getResultHealtcheck());
        applyBaInfoList.get(selectRowNumber).setCheckDate(applyBaInfoView.getCheckDate());


        complete=true;
        }

        RequestContext.getCurrentInstance().addCallbackParam("functionComplete",complete);

    }


    public void onSubmitBaPaInfoDialog(){
        log.debug("submitBaPaInfoDialog()");
        log.info("modeForButton ::: {}", modeForButton);

        boolean complete=false;
        RequestContext context=RequestContext.getCurrentInstance();


        List<CreditTypeDetailView> listSelect=new ArrayList<CreditTypeDetailView>();
        CreditTypeDetailView creditTypeDetailView;
        BigDecimal limit =BigDecimal.ZERO;
        for(int i=0;i<creditTypeDetailList.size();i++){
            if(creditTypeDetailList.get(i).isNoFlag()){
                creditTypeDetailView=new CreditTypeDetailView();
                creditTypeDetailView.setProductProgram(creditTypeDetailList.get(i).getProductProgram());
                creditTypeDetailView.setCreditFacility(creditTypeDetailList.get(i).getCreditFacility());
                creditTypeDetailView.setPurpose(creditTypeDetailList.get(i).getPurpose());
                creditTypeDetailView.setSeq(i);
                limit =limit.add(creditTypeDetailList.get(i).getLimit());
                listSelect.add(creditTypeDetailView);
            }

        }
        if(baPaInfoAddView.getType().equals(BaPaType.BA)){
          baPaInfoAddView.setCustomerPay("No");
        }
        if(baPaInfoAddView.getType().equals(BaPaType.PA)){
          baPaInfoAddView.setCustomerPay("Yes");
        }
        baPaInfoAddView.setLimit(limit);
        baPaInfoAddView.setCreditType(listSelect);


        if(modeForButton !=null && modeForButton.equals(ModeForButton.ADD)){
            listBaPa.add(baPaInfoAddView);
            complete=true;

            baPaInfoView.setBapa(listBaPa);
        }

        if(modeForButton !=null && modeForButton.equals(ModeForButton.EDIT)){
            listBaPa.get(selectRowNumber).setPremium(baPaInfoAddView.getPremium());
            listBaPa.get(selectRowNumber).setMorePay(baPaInfoAddView.getMorePay());
            listBaPa.get(selectRowNumber).setType(baPaInfoAddView.getType());
            listBaPa.get(selectRowNumber).setCustomerPay(baPaInfoAddView.getCustomerPay());
            listBaPa.get(selectRowNumber).setCreditType(listSelect);
            complete=true;

            baPaInfoView.setBapa(listBaPa);
        }

        deleteButtonFlag();
        calculationSummaryBaPa();
        context.addCallbackParam("functionComplete",complete);
    }


    public void save(){
        log.debug("save()");

//        List<ApplyBaInfoView> listSelect= new ArrayList<ApplyBaInfoView>();
//        for(int i =0;i<applyBaInfoList.size();i++){
//            if(applyBaInfoList.get(i).isChecked()==true){
//                applyBaInfoView =new ApplyBaInfoView();
//                applyBaInfoView.setCusName(applyBaInfoList.get(i).getCusName());
//                applyBaInfoView.setContactNumber(applyBaInfoList.get(i).getContactNumber());
//                applyBaInfoView.setRelationship(applyBaInfoList.get(i).getRelationship());
//                applyBaInfoView.setResultHealtcheck(applyBaInfoList.get(i).getResultHealtcheck());
//                applyBaInfoView.setCheckDate(applyBaInfoList.get(i).getCheckDate());
//                listSelect.add(applyBaInfoView);
//            }
//        }
//        baPaInfoView.setBa(listSelect   );
        System.out.println(baPaInfoView.getBa().size());
        System.out.println(baPaInfoView.toString());
    }


    public void onRemoveBaPaList(){
        log.debug("onRemoveBaPaList()");

        baPaInfoView.getBapa().remove(selectRowNumber);
        calculationSummaryBaPa();
        deleteButtonFlag();

    }

    ////////////////////////////////////////////////////////

    private void calculationSummaryBaPa(){
        log.debug("calculationSummaryBaPa()");
        BigDecimal totalLimit = BigDecimal.ZERO;
        BigDecimal totalPremium = BigDecimal.ZERO;
        BigDecimal totalMorePay = BigDecimal.ZERO;

        for(int i=0;i<baPaInfoView.getBapa().size();i++){
            if(baPaInfoView.getBapa().get(i).getLimit()!=null){
                totalLimit = totalLimit.add(baPaInfoView.getBapa().get(i).getLimit());
            }
            if(baPaInfoView.getBapa().get(i).getPremium()!=null){
                totalPremium = totalPremium.add(baPaInfoView.getBapa().get(i).getPremium());
            }
            if(baPaInfoView.getBapa().get(i).getMorePay()!=null){
                totalMorePay = totalMorePay.add(baPaInfoView.getBapa().get(i).getMorePay());
            }

        }
        baPaInfoView.setTotalLimit(totalLimit);
        baPaInfoView.setTotalPremium(totalPremium);
        baPaInfoView.setTotalMorePay(totalMorePay);

    }

    private void deleteButtonFlag(){
        for(int i=0;i<listBaPa.size();i++){
            if(i==listBaPa.size()-1){
                listBaPa.get(i).setDeleteFlag(true);
            }else{
                listBaPa.get(i).setDeleteFlag(false);
            }
        }
    }


     // enumList

    public BaPaType[] getbapaType(){
        return BaPaType.values();
    }
    public HealthCheckList[] getHealCheckList(){
        return HealthCheckList.values();
    }


    // get set

    public List<CreditTypeDetailView> getCreditTypeDetailList() {
        return creditTypeDetailList;
    }

    public void setCreditTypeDetailList(List<CreditTypeDetailView> creditTypeDetailView) {
        this.creditTypeDetailList = creditTypeDetailView;
    }

    public BaPaInfoAddView getBaPaInfoAddView() {
        return baPaInfoAddView;
    }

    public void setBaPaInfoAddView(BaPaInfoAddView baPaInfoAddView) {
        this.baPaInfoAddView = baPaInfoAddView;
    }

    public BaPaInfoView getBaPaInfoView() {
        return baPaInfoView;
    }

    public void setBaPaInfoView(BaPaInfoView baPaInfoView) {
        this.baPaInfoView = baPaInfoView;
    }

    public int getSelectRowNumber() {
        return selectRowNumber;
    }

    public void setSelectRowNumber(int selectRowNumber) {
        this.selectRowNumber = selectRowNumber;
    }

    public BaPaInfoAddView getNewBaPaInfoAddViewItem() {
        return newBaPaInfoAddViewItem;
    }

    public void setNewBaPaInfoAddViewItem(BaPaInfoAddView newBaPaInfoAddViewItem) {
        this.newBaPaInfoAddViewItem = newBaPaInfoAddViewItem;
    }

    public ApplyBaInfoView getApplyBaInfoView() {
        return applyBaInfoView;
    }

    public void setApplyBaInfoView(ApplyBaInfoView applyBaInfoView) {
        this.applyBaInfoView = applyBaInfoView;
    }

    public ApplyBaInfoView getNewApplyBaInfoView() {
        return newApplyBaInfoView;
    }

    public void setNewApplyBaInfoView(ApplyBaInfoView newApplyBaInfoView) {
        this.newApplyBaInfoView = newApplyBaInfoView;
    }

    public List<ApplyBaInfoView> getApplyBaInfoList() {
        return applyBaInfoList;
    }

    public void setApplyBaInfoList(List<ApplyBaInfoView> applyBaInfoList) {
        this.applyBaInfoList = applyBaInfoList;
    }*/
}
