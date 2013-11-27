package com.clevel.selos.controller;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.relation.PrdGroupToPrdProgramDAO;
import com.clevel.selos.dao.relation.PrdProgramToCreditTypeDAO;
import com.clevel.selos.dao.relation.RelationCustomerDAO;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.relation.PrdGroupToPrdProgram;
import com.clevel.selos.model.db.relation.PrdProgramToCreditType;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.view.*;
import com.clevel.selos.util.FacesUtil;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

@ViewScoped
@ManagedBean(name = "creditFacExisting")
public class CreditFacExisting implements Serializable {
    @Inject
    @SELOS
    Logger log;
    //## session
    private long workCaseId;
    private long stepId;
    private String userId;
    private int rowIndex;
    private ProposeConditionDetailView conditionInfoDetailView;
    private List<ProposeConditionDetailView> conditionInfoDetailViewList;

    private ExistingCreditDetailView selectCreditDetail;
    private ExistingCollateralDetailView selectCollateralDetail;
    private ExistingGuarantorDetailView selectGuarantorDetail;

    private ProposeConditionDetailView selectConditionItem;
    private User user;
    private ExistingCreditView existingCreditView;

    private CreditTierDetailView creditTierDetailView;
    private List<CreditTierDetailView> creditTierDetailViewList;
    private SplitLineDetailView splitLineDetailView;
    private List<SplitLineDetailView> splitLineDetailViewList;
    private ProductProgram existProductProgram;
    private CreditType existCreditType;
    private BankAccountStatus existAccountStatus ;

    private List<ProductGroup> productGroupList;
    private List<PrdGroupToPrdProgram> prdGroupToPrdProgramList;
    private List<PrdProgramToCreditType> prdProgramToCreditTypeList;
    private ProductProgram productProgram;


    private ExistingCreditDetailView  existingCreditDetailView;
    private List<ExistingCreditDetailView> existingCreditDetailViewList;

    private List<ExistingCreditDetailView> borrowerExistingCreditDetailViewList;
    private List<ExistingCreditDetailView> relatedExistingCreditDetailViewList;

    private List<ProductProgram> productProgramList;
    private List<CreditType> creditTypeList;
    private List<BankAccountStatus> accountStatusList;
    private boolean showSplitLine;



    private ExistingCollateralDetailView existingCollateralDetailView;

    private List<ExistingCollateralDetailView> borrowerExistingCollateralDetailViewList;
    private List<ExistingCollateralDetailView> relatedExistingCollateralDetailViewList;


    private ExistingGuarantorDetailView existingGuarantorDetailView;
    private List<ExistingGuarantorDetailView> existingGuarantorDetailViewList;
    private List<ExistingCreditDetailView> collateralCreditDetailViewList;
    private List<ExistingCreditDetailView> borrowerCollateralCreditDetailViewList;
    private List<ExistingCreditDetailView> relatedCollateralCreditDetailViewList;
    private List<ExistingCreditDetailView> guarantorCreditDetailViewList;

    private List<Relation> relationList;
    private List<CollateralType> collateralTypeList;
    private List<PotentialCollateral> potentialCollateralList;
    private List<Customer> guarantorList;
    private Hashtable hashBorrower;
    private int seq=0;

    enum ModeForButton {ADD, EDIT}

    private ModeForButton modeForButton;
    private String typeOfList;

    @Inject
    private CreditTypeDAO creditTypeDAO;
    @Inject
    private ProductProgramDAO productProgramDAO;
    @Inject
    private BankAccountStatusDAO bankAccountStatusDAO;
    @Inject
    private PrdGroupToPrdProgramDAO prdGroupToPrdProgramDAO;        // find product program
    @Inject
    private PrdProgramToCreditTypeDAO prdProgramToCreditTypeDAO;    // find credit type
    @Inject
    private ProductFormulaDAO productFormulaDAO;

    @Inject
    CustomerDAO customerDAO;
    @Inject
    private CollateralTypeDAO collateralTypeDAO;
    @Inject
    private PotentialCollateralDAO potentialCollateralDAO;
    @Inject
    private RelationDAO relationDAO;

    public CreditFacExisting(){

    }

    public void preRender() {
        HttpSession session = FacesUtil.getSession(false);
        session.setAttribute("workCaseId", 10001);
        session.setAttribute("stepId", 1006);
        session.setAttribute("userId", 10001);
        log.info("preRender ::: 1 ");

        session = FacesUtil.getSession(true);
        productProgramList = productProgramDAO.findAll();
        creditTypeList = creditTypeDAO.findAll();
        accountStatusList = bankAccountStatusDAO.findAll();
        relationList = relationDAO.findAll();
        collateralTypeList = collateralTypeDAO.findAll();
        potentialCollateralList = potentialCollateralDAO.findAll();
        hashBorrower = new Hashtable<String,String>();
        if (session.getAttribute("workCaseId") != null) {
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            stepId = Long.parseLong(session.getAttribute("stepId").toString());
            userId = session.getAttribute("userId").toString();

            if (guarantorList == null) {
                guarantorList = new ArrayList<Customer>();
                Customer customer = new Customer();
                customer.setNameTh("อาริยา เอ ฮาเก้ต");

                guarantorList.add(customer);

                //guarantorList = customerDAO.findGuarantorByWorkCaseId(workCaseId);
            }

            conditionInfoDetailViewList = new ArrayList<ProposeConditionDetailView>();
            existingCreditDetailViewList = new ArrayList<ExistingCreditDetailView>();

            borrowerExistingCreditDetailViewList = new ArrayList<ExistingCreditDetailView>();
            relatedExistingCreditDetailViewList = new ArrayList<ExistingCreditDetailView>();

            existingCreditView = new ExistingCreditView();
            existingCreditDetailView = new ExistingCreditDetailView();
            existAccountStatus = new BankAccountStatus();
            existProductProgram = new ProductProgram();
            existCreditType = new CreditType();

            existingCreditDetailView.setExistAccountStatus(existAccountStatus);
            existingCreditDetailView.setExistProductProgram(existProductProgram);
            existingCreditDetailView.setExistCreditType(existCreditType);

            splitLineDetailView = new SplitLineDetailView();
            productProgram = new ProductProgram();
            splitLineDetailView.setProductProgram(productProgram);

            existingCollateralDetailView = new ExistingCollateralDetailView();
            collateralCreditDetailViewList = new ArrayList<ExistingCreditDetailView>();
            existingCollateralDetailView.setCollateralType(new CollateralType());
            existingCollateralDetailView.setPotentialCollateral(new PotentialCollateral());
            existingCollateralDetailView.setRelation(new Relation());
            existingCollateralDetailView.setCreditFacilityList(collateralCreditDetailViewList);

            borrowerExistingCollateralDetailViewList = new ArrayList<ExistingCollateralDetailView>() ;
            relatedExistingCollateralDetailViewList  = new ArrayList<ExistingCollateralDetailView>() ;


            existingGuarantorDetailView = new ExistingGuarantorDetailView();
            existingGuarantorDetailViewList = new ArrayList<ExistingGuarantorDetailView>();
            guarantorCreditDetailViewList = new ArrayList<ExistingCreditDetailView>();
            existingGuarantorDetailView.setCreditFacilityList(guarantorCreditDetailViewList);
            //*** Get Product Program List from Product Group ***//
            prdGroupToPrdProgramList = prdGroupToPrdProgramDAO.getListPrdProByPrdGroupForExistCredit();
            if(prdGroupToPrdProgramList == null){
                prdGroupToPrdProgramList = new ArrayList<PrdGroupToPrdProgram>();
            }
            log.info("preRender ::: end ");
        } else {
            //TODO return to inbox
            log.info("preRender ::: workCaseId is null.");
            try {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath() + "/site/inbox.jsf");
                return;
            } catch (Exception ex) {
                log.info("Exception :: {}", ex);
            }
        }
    }

    @PostConstruct
    public void onCreation() {
        log.info("onCreation begin");
        preRender();
        try{

        }catch (Exception e){
            log.info("onCreation catch");
        }
        log.info("onCreation end");

    }


    public void onChangeProductProgram(){
        ProductProgram productProgram = productProgramDAO.findById(existingCreditDetailView.getExistProductProgram().getId());
        log.debug("onChangeProductProgram :::: productProgram : {}", productProgram);

        prdProgramToCreditTypeList = prdProgramToCreditTypeDAO.getListPrdProByPrdprogram(productProgram);
        if(prdProgramToCreditTypeList == null){
            prdProgramToCreditTypeList = new ArrayList<PrdProgramToCreditType>();
        }
        log.debug("onChangeProductProgram :::: prdProgramToCreditTypeList.size ::: " +prdProgramToCreditTypeList.size());
    }

    //Start Condition Information //
    public void onAddCommercialCredit() {
        log.info("onAddCommercialCredit ::: ");
        existingCreditDetailView = new ExistingCreditDetailView();
        existAccountStatus = new BankAccountStatus();
        existProductProgram = new ProductProgram();
        existCreditType = new CreditType();

        existingCreditDetailView.setExistAccountStatus(existAccountStatus);
        existingCreditDetailView.setExistProductProgram(existProductProgram);
        existingCreditDetailView.setExistCreditType(existCreditType);

        creditTierDetailView = new CreditTierDetailView();
        creditTierDetailViewList = new ArrayList<CreditTierDetailView>();

        splitLineDetailView = new SplitLineDetailView();
        productProgram = new ProductProgram();
        splitLineDetailView.setProductProgram(productProgram);
        splitLineDetailViewList = new ArrayList<SplitLineDetailView>();
        showSplitLine = false;
        modeForButton = ModeForButton.ADD;
        log.info("onAddCommercialCredit ::: modeForButton : {}", modeForButton);
    }

    public void onEditCommercialCredit() {
        log.info("onEditCommercialCredit ::: mode : {} typeOfList " + typeOfList);
        modeForButton = ModeForButton.EDIT;
        existingCreditDetailView = new ExistingCreditDetailView();

        log.info("onrow x");
        existingCreditDetailView.setAccountName(selectCreditDetail.getAccountName());
        existingCreditDetailView.setAccountNumber(selectCreditDetail.getAccountNumber());
        existingCreditDetailView.setExistAccountStatus(selectCreditDetail.getExistAccountStatus());
        existingCreditDetailView.setExistProductProgram(selectCreditDetail.getExistProductProgram());
        existingCreditDetailView.setExistCreditType(selectCreditDetail.getExistCreditType());

        splitLineDetailViewList= selectCreditDetail.getSplitLineDetailViewList();
        existingCreditDetailView.setSplitLineDetailViewList(selectCreditDetail.getSplitLineDetailViewList());
        creditTierDetailViewList = selectCreditDetail.getCreditTierDetailViewList();
        existingCreditDetailView.setCreditTierDetailViewList(selectCreditDetail.getCreditTierDetailViewList());


        log.info("onEditCommercialCredit end ::: mode : {}", modeForButton);
    }

    public void onDeleteCommercialCredit() {
        log.info("onDeleteCommercialCredit ::: mode : {} typeOfList " + typeOfList);
        if(typeOfList.equals("borrower")){
            log.info("del 1");
            borrowerExistingCreditDetailViewList.remove(selectCreditDetail);
        }else if(typeOfList.equals("related")){
            log.info("del 2");
            relatedExistingCreditDetailViewList.remove(selectCreditDetail);
        }
    }
    public void onSaveCreditInfo() {
        log.info("onSaveCreditInfo ::: mode : {}", modeForButton);

        boolean complete = false;
        RequestContext context = RequestContext.getCurrentInstance();

        log.info("typeOfList ::: "+ typeOfList);
        if(modeForButton != null && modeForButton.equals(ModeForButton.ADD)){
                log.info("add to list begin");
                ProductProgram  productProgram = productProgramDAO.findById(existingCreditDetailView.getExistProductProgram().getId());
                CreditType creditType = creditTypeDAO.findById( existingCreditDetailView.getExistCreditType().getId());
                BankAccountStatus bankAccountStatus = bankAccountStatusDAO.findById( existingCreditDetailView.getExistAccountStatus().getId());

                existingCreditDetailView.setExistProductProgram(productProgram);
                existingCreditDetailView.setExistCreditType(creditType);
                existingCreditDetailView.setExistAccountStatus(bankAccountStatus);

                existingCreditDetailView.setSplitLineDetailViewList(splitLineDetailViewList);
                existingCreditDetailView.setCreditTierDetailViewList(creditTierDetailViewList);
                existingCreditDetailView.setSeq(seq);
                log.info("getSplitLineDetailViewList  size " + existingCreditDetailView.getSplitLineDetailViewList().size());
                log.info("getCreditTierDetailViewList size " + existingCreditDetailView.getCreditTierDetailViewList().size());
                hashBorrower.put(seq,0);
                seq++;
                if(typeOfList.equals("borrower")){
                    borrowerExistingCreditDetailViewList.add(existingCreditDetailView);
                }else if(typeOfList.equals("related")){
                    relatedExistingCreditDetailViewList.add(existingCreditDetailView);
                }
                log.info("add to list end");

            }else if(modeForButton != null && modeForButton.equals(ModeForButton.EDIT)){
                log.info("update list begin");
                ExistingCreditDetailView  existingCreditDetailViewRow = new ExistingCreditDetailView();
                if(typeOfList.equals("borrower")){
                    existingCreditDetailViewRow = borrowerExistingCreditDetailViewList.get(rowIndex);
                }else if(typeOfList.equals("related")){
                    existingCreditDetailViewRow = relatedExistingCreditDetailViewList.get(rowIndex);
                }

                ProductProgram  productProgram = productProgramDAO.findById(existingCreditDetailView.getExistProductProgram().getId());
                CreditType creditType = creditTypeDAO.findById( existingCreditDetailView.getExistCreditType().getId());
                BankAccountStatus bankAccountStatus = bankAccountStatusDAO.findById( existingCreditDetailView.getExistAccountStatus().getId());

                existingCreditDetailViewRow.setExistProductProgram(productProgram);
                existingCreditDetailViewRow.setExistCreditType(creditType);
                existingCreditDetailViewRow.setExistAccountStatus(bankAccountStatus);
                existingCreditDetailViewRow.setAccountName(existingCreditDetailView.getAccountName());
                existingCreditDetailViewRow.setAccountNumber(existingCreditDetailView.getAccountNumber());


                log.info("getSplitLineDetailViewList  size " + existingCreditDetailView.getSplitLineDetailViewList().size());
                log.info("getCreditTierDetailViewList size " + existingCreditDetailView.getCreditTierDetailViewList().size());

                existingCreditDetailViewRow.setSplitLineDetailViewList(splitLineDetailViewList);
                existingCreditDetailViewRow.setCreditTierDetailViewList(creditTierDetailViewList);

                log.info("update list end");
            }else {
                log.info("onSaveCreditInfo ::: Undefined modeForButton !!");
            }

            complete = true;

        log.info("  complete >>>>  :  {}", complete);
        context.addCallbackParam("functionComplete", complete);

    }


    //  Start Tier Dialog //
    public void onAddCreditTierDetailView() {
        log.info("onAddCreditInfo ::: ");
        creditTierDetailView = new CreditTierDetailView();
        creditTierDetailViewList.add(creditTierDetailView);
    }

    public void onDeleteCreditTierDetailView(int rowOnTable) {
        if(Integer.toString(rowOnTable) != null ){
            log.info("onDeleteCreditTierDetailView ::: rowOnTable " + rowOnTable);
            creditTierDetailViewList.remove(rowOnTable);
        }
    }


    //  Start SplitLine Dialog //
    public void onAddSplitLineDetailView() {
        log.info("onAddCreditInfo ::: begin");
        splitLineDetailView = new SplitLineDetailView();
        productProgram = new ProductProgram();
        splitLineDetailView.setProductProgram(productProgram);
        splitLineDetailViewList.add(splitLineDetailView);
        log.info("onAddCreditInfo ::: end");
    }

    public void onDeleteSplitLineDetailView(int rowOnTable) {
        if(Integer.toString(rowOnTable) != null ){
            log.info("splitLineDetailViewList ::: rowOnTable " + rowOnTable);
            splitLineDetailViewList.remove(rowOnTable);
        }
    }

    public void onChangeCreditType(){
        int id = existingCreditDetailView.getExistCreditType().getId();
        showSplitLine = false;
        if(id == 9){
            showSplitLine = true;
        }

        if ((existingCreditDetailView.getExistProductProgram().getId() != 0) && (existingCreditDetailView.getExistCreditType().getId() != 0)) {
            ProductProgram productProgram = productProgramDAO.findById(existingCreditDetailView.getExistProductProgram().getId());
            CreditType creditType = creditTypeDAO.findById(existingCreditDetailView.getExistCreditType().getId());

            if(productProgram != null && creditType != null){
                PrdProgramToCreditType prdProgramToCreditType = prdProgramToCreditTypeDAO.getPrdProgramToCreditType(creditType,productProgram);
                ProductFormula productFormula = productFormulaDAO.findProductFormula(prdProgramToCreditType);
                log.debug("onChangeCreditType :::: productFormula : {}", productFormula.getId());

                if(productFormula != null){
                    existingCreditDetailView.setProductCode(productFormula.getProductCode());
                    existingCreditDetailView.setProjectCode(productFormula.getProjectCode());
                }
            }
        }

    }

    //Start Condition Information //
    public void onAddConditionInfo() {
        log.info("onAddConditionInfo ::: ");
        conditionInfoDetailView = new ProposeConditionDetailView();
        modeForButton = ModeForButton.ADD;
        log.info("onAddConditionInfo ::: modeForButton : {}", modeForButton);

    }

    public void onSaveConditionInfoDlg(){
        log.info("onSaveConditionInfoDlg ::: mode : {}", modeForButton);

        RequestContext context = RequestContext.getCurrentInstance();
        boolean complete = false;

        if(modeForButton != null && modeForButton.equals(ModeForButton.ADD)){

            ProposeConditionDetailView conditionDetailViewAdd = new ProposeConditionDetailView();
            conditionDetailViewAdd.setLoanType(conditionInfoDetailView.getLoanType());
            conditionDetailViewAdd.setConditionDesc(conditionInfoDetailView.getConditionDesc());
            conditionInfoDetailViewList.add(conditionDetailViewAdd);
            complete = true;

        } else {

            log.info("onSaveConditionInfoDlg ::: validation failed.");
            complete = false;
        }

        log.info("  complete >>>>  :  {}", complete);
        context.addCallbackParam("functionComplete", complete);
    }

    public void onDeleteConditionInfo() {
        log.info("onDeleteConditionInfo :: ");
        conditionInfoDetailViewList.remove(selectConditionItem);
    }

    //Start Condition Information //
    public void onAddExistingCollateral() {
        log.info("onAddExistingCollateral ::: ");
        existingCollateralDetailView = new ExistingCollateralDetailView();
        collateralCreditDetailViewList = new ArrayList<ExistingCreditDetailView>();
        existingCollateralDetailView.setCollateralType(new CollateralType());
        existingCollateralDetailView.setPotentialCollateral(new PotentialCollateral());
        existingCollateralDetailView.setRelation(new Relation());
        existingCollateralDetailView.setCreditFacilityList(collateralCreditDetailViewList);

        modeForButton = ModeForButton.ADD;
        log.info("onAddCommercialCredit ::: modeForButton : {}", modeForButton);
    }

    public void onEditExistingCollateral() {
        log.info("onEditExistingCollateral ::: ");
        modeForButton = ModeForButton.EDIT;
        existingCollateralDetailView = new ExistingCollateralDetailView();
        collateralCreditDetailViewList = new ArrayList<ExistingCreditDetailView>();
        existingCollateralDetailView.setCollateralType(selectCollateralDetail.getCollateralType());
        existingCollateralDetailView.setPotentialCollateral(selectCollateralDetail.getPotentialCollateral());
        existingCollateralDetailView.setRelation(selectCollateralDetail.getRelation());

        ExistingCreditDetailView borrowerExistingCreditDetailView;
        ExistingCreditDetailView borrowerCollateralCreditView;
        int tempSeq =0;

        for(int j=0;j<selectCollateralDetail.getCreditFacilityList().size();j++){
            borrowerCollateralCreditView = selectCollateralDetail.getCreditFacilityList().get(j);
            for(int i=tempSeq;i<borrowerExistingCreditDetailViewList.size();i++){
                borrowerExistingCreditDetailView = borrowerExistingCreditDetailViewList.get(i);
                if(borrowerCollateralCreditView.getSeq() == borrowerExistingCreditDetailView.getSeq()){
                    borrowerExistingCreditDetailView.setNoFlag(true);
                    tempSeq = i;
                }
                break;
            }
        }

        existingCollateralDetailView.setCreditFacilityList(collateralCreditDetailViewList);

        modeForButton = ModeForButton.ADD;
        log.info("onAddCommercialCredit ::: modeForButton : {}", modeForButton);
    }

    public void onSaveExistingCollateral() {
        log.info("onSaveExistingCollateral ::: mode : {}", modeForButton);
        boolean complete = false;
        ExistingCreditDetailView existingCreditDetailViewNoFlag;
        RequestContext context = RequestContext.getCurrentInstance();
        log.info("typeOfList ::: "+ typeOfList);
        int seqBorrowerTemp;
        int seqRelatedTemp;

        if(modeForButton != null && modeForButton.equals(ModeForButton.ADD)){
            ExistingCollateralDetailView  existingCollateralDetailViewAdd  = new ExistingCollateralDetailView();
            CollateralType  collateralType = collateralTypeDAO.findById(existingCollateralDetailView.getCollateralType().getId());
            PotentialCollateral potentialCollateral = potentialCollateralDAO.findById( existingCollateralDetailView.getPotentialCollateral().getId());
            Relation relation = relationDAO.findById( existingCollateralDetailView.getRelation().getId());


            existingCollateralDetailView.setCollateralType(collateralType);
            existingCollateralDetailView.setPotentialCollateral(potentialCollateral);
            existingCollateralDetailView.setRelation(relation);
            log.info("existingCollateralDetailView ::: mode : {}", existingCollateralDetailView.toString());


            for (int l=0;l<hashBorrower.size();l++){
                log.info("hashBorrower.get(j) in use   :  "+ l + " is   " +hashBorrower.get(l).toString());
            }

            if(typeOfList.equals("borrower")){
                for(int i=0;i<borrowerExistingCreditDetailViewList.size();i++){
                    existingCreditDetailViewNoFlag = borrowerExistingCreditDetailViewList.get(i);
                    log.info("LIST 1 ::: mode : {}", existingCreditDetailViewNoFlag.toString());
                    if(existingCreditDetailViewNoFlag.isNoFlag()==true){
                        collateralCreditDetailViewList.add(existingCreditDetailViewNoFlag);
                        seqBorrowerTemp = existingCreditDetailViewNoFlag.getSeq();
                        log.info("hashBorrower.get(seq) in use   :  "+ seqBorrowerTemp + " is   " + hashBorrower.get(seqBorrowerTemp).toString());
                        hashBorrower.put(seqBorrowerTemp,Integer.parseInt(hashBorrower.get(seqBorrowerTemp).toString()) +1);
                    }
                    existingCreditDetailViewNoFlag.setNoFlag(false);
                }
                for (int j=0;j<hashBorrower.size();j++){
                    log.info("hashBorrower.get(j) in use   :  "+ j + " is   " +hashBorrower.get(j).toString());
                }
            }else if(typeOfList.equals("related")){
                for(int i=0;i<relatedExistingCreditDetailViewList.size();i++){
                    existingCreditDetailViewNoFlag = relatedExistingCreditDetailViewList.get(i);
                    log.info("LIST 1 ::: mode : {}", existingCreditDetailViewNoFlag.toString());
                    if(existingCreditDetailViewNoFlag.isNoFlag()==true){

                        collateralCreditDetailViewList.add(existingCreditDetailViewNoFlag);
                        seqRelatedTemp = existingCreditDetailViewNoFlag.getSeq();
                        log.info("hashBorrower.get(seq) in use   :  "+ seqRelatedTemp + " is   " + hashBorrower.get(seqRelatedTemp).toString());
                        hashBorrower.put(seqRelatedTemp, Integer.parseInt(hashBorrower.get(seqRelatedTemp).toString()) + 1);
                    }
                    existingCreditDetailViewNoFlag.setNoFlag(false);
                }
            }


            existingCollateralDetailViewAdd.setCreditFacilityList(collateralCreditDetailViewList);

            if(typeOfList.equals("borrower")){
                borrowerExistingCollateralDetailViewList.add(existingCollateralDetailView);
            }else if(typeOfList.equals("related")){
                relatedExistingCollateralDetailViewList.add(existingCollateralDetailView);
            }

            existingCreditDetailViewList.add(existingCreditDetailView);

        }else if(modeForButton != null && modeForButton.equals(ModeForButton.EDIT)){


        }else {
            log.info("onSaveCreditInfo ::: Undefined modeForButton !!");
        }

        complete = true;

        log.info("  complete >>>>  :  {}", complete);
        context.addCallbackParam("functionComplete", complete);

    }


    //Start Condition Information //
    public void onAddExistingGuarantor() {
        log.info("onAddExistingCollateral ::: ");
        existingGuarantorDetailView = new ExistingGuarantorDetailView();
        collateralCreditDetailViewList = new ArrayList<ExistingCreditDetailView>();
        existingCollateralDetailView.setCollateralType(new CollateralType());
        existingCollateralDetailView.setPotentialCollateral(new PotentialCollateral());
        existingCollateralDetailView.setRelation(new Relation());
        existingCollateralDetailView.setCreditFacilityList(collateralCreditDetailViewList);

        modeForButton = ModeForButton.ADD;
        log.info("onAddCommercialCredit ::: modeForButton : {}", modeForButton);
    }

    public void onSaveExistingGuarantor() {
        log.info("onSaveExistingGuarantor ::: mode : {}", modeForButton);
        boolean complete = false;
        RequestContext context = RequestContext.getCurrentInstance();
        ExistingCreditDetailView existingCreditDetailViewNoFlag;

        if(modeForButton != null && modeForButton.equals(ModeForButton.ADD)){

            for(int i=0;i<borrowerExistingCreditDetailViewList.size();i++){
                existingCreditDetailViewNoFlag = borrowerExistingCreditDetailViewList.get(i);
                log.info("LIST 1 ::: mode : {}", existingCreditDetailViewNoFlag.toString());
                if(existingCreditDetailViewNoFlag.isNoFlag()==true){
                    collateralCreditDetailViewList.add(existingCreditDetailViewNoFlag);
                }
                existingCreditDetailViewNoFlag.setNoFlag(false);
            }
            existingGuarantorDetailView.setCreditFacilityList(collateralCreditDetailViewList);

            existingGuarantorDetailViewList.add(existingGuarantorDetailView);

        }else if(modeForButton != null && modeForButton.equals(ModeForButton.EDIT)){


        }else {
            log.info("onSaveCreditInfo ::: Undefined modeForButton !!");
        }

        complete = true;

        log.info(" onSaveExistingGuarantor complete >>>>  :  {}", complete);
        context.addCallbackParam("functionComplete", complete);

    }


    public ProposeConditionDetailView getConditionInfoDetailView() {
        return conditionInfoDetailView;
    }

    public void setConditionInfoDetailView(ProposeConditionDetailView conditionInfoDetailView) {
        this.conditionInfoDetailView = conditionInfoDetailView;
    }

    public List<ProposeConditionDetailView> getConditionInfoDetailViewList() {
        return conditionInfoDetailViewList;
    }

    public void setConditionInfoDetailViewList(List<ProposeConditionDetailView> conditionInfoDetailViewList) {
        this.conditionInfoDetailViewList = conditionInfoDetailViewList;
    }

    public ProposeConditionDetailView getSelectConditionItem() {
        return selectConditionItem;
    }

    public void setSelectConditionItem(ProposeConditionDetailView selectConditionItem) {
        this.selectConditionItem = selectConditionItem;
    }

    public ExistingCreditDetailView getSelectCreditDetail() {
        return selectCreditDetail;
    }

    public void setSelectCreditDetail(ExistingCreditDetailView selectCreditDetail) {
        this.selectCreditDetail = selectCreditDetail;
    }


    public List<ExistingCreditDetailView> getExistingCreditDetailViewList() {
        return existingCreditDetailViewList;
    }

    public void setExistingCreditDetailViewList(List<ExistingCreditDetailView> existingCreditDetailViewList) {
        this.existingCreditDetailViewList = existingCreditDetailViewList;
    }

    public ExistingCreditDetailView getExistingCreditDetailView() {
        return existingCreditDetailView;
    }

    public void setExistingCreditDetailView(ExistingCreditDetailView existingCreditDetailView) {
        this.existingCreditDetailView = existingCreditDetailView;
    }

    public ExistingCreditView getExistingCreditView() {
        return existingCreditView;
    }

    public void setExistingCreditView(ExistingCreditView existingCreditView) {
        this.existingCreditView = existingCreditView;
    }

    public SplitLineDetailView getSplitLineDetailView() {
        return splitLineDetailView;
    }

    public void setSplitLineDetailView(SplitLineDetailView splitLineDetailView) {
        this.splitLineDetailView = splitLineDetailView;
    }

    public List<SplitLineDetailView> getSplitLineDetailViewList() {
        return splitLineDetailViewList;
    }

    public void setSplitLineDetailViewList(List<SplitLineDetailView> splitLineDetailViewList) {
        this.splitLineDetailViewList = splitLineDetailViewList;
    }

    public List<ProductProgram> getProductProgramList() {
        return productProgramList;
    }

    public void setProductProgramList(List<ProductProgram> productProgramList) {
        this.productProgramList = productProgramList;
    }

    public List<CreditType> getCreditTypeList() {
        return creditTypeList;
    }

    public void setCreditTypeList(List<CreditType> creditTypeList) {
        this.creditTypeList = creditTypeList;
    }

    public List<BankAccountStatus> getAccountStatusList() {
        return accountStatusList;
    }

    public void setAccountStatusList(List<BankAccountStatus> accountStatusList) {
        this.accountStatusList = accountStatusList;
    }

    public List<PrdProgramToCreditType> getPrdProgramToCreditTypeList() {
        return prdProgramToCreditTypeList;
    }

    public void setPrdProgramToCreditTypeList(List<PrdProgramToCreditType> prdProgramToCreditTypeList) {
        this.prdProgramToCreditTypeList = prdProgramToCreditTypeList;
    }

    public List<PrdGroupToPrdProgram> getPrdGroupToPrdProgramList() {
        return prdGroupToPrdProgramList;
    }

    public void setPrdGroupToPrdProgramList(List<PrdGroupToPrdProgram> prdGroupToPrdProgramList) {
        this.prdGroupToPrdProgramList = prdGroupToPrdProgramList;
    }

    public List<ProductGroup> getProductGroupList() {
        return productGroupList;
    }

    public void setProductGroupList(List<ProductGroup> productGroupList) {
        this.productGroupList = productGroupList;
    }

    public CreditType getExistCreditType() {
        return existCreditType;
    }

    public void setExistCreditType(CreditType existCreditType) {
        this.existCreditType = existCreditType;
    }

    public List<CreditTierDetailView> getCreditTierDetailViewList() {
        return creditTierDetailViewList;
    }

    public void setCreditTierDetailViewList(List<CreditTierDetailView> creditTierDetailViewList) {
        this.creditTierDetailViewList = creditTierDetailViewList;
    }

    public CreditTierDetailView getCreditTierDetailView() {
        return creditTierDetailView;
    }

    public void setCreditTierDetailView(CreditTierDetailView creditTierDetailView) {
        this.creditTierDetailView = creditTierDetailView;
    }

    public boolean isShowSplitLine() {
        return showSplitLine;
    }

    public void setShowSplitLine(boolean showSplitLine) {
        this.showSplitLine = showSplitLine;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public List<Relation> getRelationList() {
        return relationList;
    }

    public void setRelationList(List<Relation> relationList) {
        this.relationList = relationList;
    }

    public List<CollateralType> getCollateralTypeList() {
        return collateralTypeList;
    }

    public void setCollateralTypeList(List<CollateralType> collateralTypeList) {
        this.collateralTypeList = collateralTypeList;
    }

    public List<PotentialCollateral> getPotentialCollateralList() {
        return potentialCollateralList;
    }

    public void setPotentialCollateralList(List<PotentialCollateral> potentialCollateralList) {
        this.potentialCollateralList = potentialCollateralList;
    }

    public ExistingCollateralDetailView getExistingCollateralDetailView() {
        return existingCollateralDetailView;
    }

    public void setExistingCollateralDetailView(ExistingCollateralDetailView existingCollateralDetailView) {
        this.existingCollateralDetailView = existingCollateralDetailView;
    }

    public List<ExistingCreditDetailView> getCollateralCreditDetailViewList() {
        return collateralCreditDetailViewList;
    }

    public void setCollateralCreditDetailViewList(List<ExistingCreditDetailView> collateralCreditDetailViewList) {
        this.collateralCreditDetailViewList = collateralCreditDetailViewList;
    }

    public List<ExistingCreditDetailView> getGuarantorCreditDetailViewList() {
        return guarantorCreditDetailViewList;
    }

    public void setGuarantorCreditDetailViewList(List<ExistingCreditDetailView> guarantorCreditDetailViewList) {
        this.guarantorCreditDetailViewList = guarantorCreditDetailViewList;
    }

    public ExistingGuarantorDetailView getExistingGuarantorDetailView() {
        return existingGuarantorDetailView;
    }

    public void setExistingGuarantorDetailView(ExistingGuarantorDetailView existingGuarantorDetailView) {
        this.existingGuarantorDetailView = existingGuarantorDetailView;
    }

    public String getTypeOfList() {
        return typeOfList;
    }

    public void setTypeOfList(String typeOfList) {
        this.typeOfList = typeOfList;
    }

    public List<Customer> getGuarantorList() {
        return guarantorList;
    }

    public void setGuarantorList(List<Customer> guarantorList) {
        this.guarantorList = guarantorList;
    }

    public List<ExistingCreditDetailView> getBorrowerExistingCreditDetailViewList() {
        return borrowerExistingCreditDetailViewList;
    }

    public void setBorrowerExistingCreditDetailViewList(List<ExistingCreditDetailView> borrowerExistingCreditDetailViewList) {
        this.borrowerExistingCreditDetailViewList = borrowerExistingCreditDetailViewList;
    }

    public List<ExistingCreditDetailView> getRelatedExistingCreditDetailViewList() {
        return relatedExistingCreditDetailViewList;
    }

    public void setRelatedExistingCreditDetailViewList(List<ExistingCreditDetailView> relatedExistingCreditDetailViewList) {
        this.relatedExistingCreditDetailViewList = relatedExistingCreditDetailViewList;
    }

    public List<ExistingCollateralDetailView> getBorrowerExistingCollateralDetailViewList() {
        return borrowerExistingCollateralDetailViewList;
    }

    public void setBorrowerExistingCollateralDetailViewList(List<ExistingCollateralDetailView> borrowerExistingCollateralDetailViewList) {
        this.borrowerExistingCollateralDetailViewList = borrowerExistingCollateralDetailViewList;
    }

    public List<ExistingCollateralDetailView> getRelatedExistingCollateralDetailViewList() {
        return relatedExistingCollateralDetailViewList;
    }

    public void setRelatedExistingCollateralDetailViewList(List<ExistingCollateralDetailView> relatedExistingCollateralDetailViewList) {
        this.relatedExistingCollateralDetailViewList = relatedExistingCollateralDetailViewList;
    }

    public List<ExistingGuarantorDetailView> getExistingGuarantorDetailViewList() {
        return existingGuarantorDetailViewList;
    }

    public void setExistingGuarantorDetailViewList(List<ExistingGuarantorDetailView> existingGuarantorDetailViewList) {
        this.existingGuarantorDetailViewList = existingGuarantorDetailViewList;
    }

    public List<ExistingCreditDetailView> getBorrowerCollateralCreditDetailViewList() {
        return borrowerCollateralCreditDetailViewList;
    }

    public void setBorrowerCollateralCreditDetailViewList(List<ExistingCreditDetailView> borrowerCollateralCreditDetailViewList) {
        this.borrowerCollateralCreditDetailViewList = borrowerCollateralCreditDetailViewList;
    }

    public List<ExistingCreditDetailView> getRelatedCollateralCreditDetailViewList() {
        return relatedCollateralCreditDetailViewList;
    }

    public void setRelatedCollateralCreditDetailViewList(List<ExistingCreditDetailView> relatedCollateralCreditDetailViewList) {
        this.relatedCollateralCreditDetailViewList = relatedCollateralCreditDetailViewList;
    }

    public ExistingGuarantorDetailView getSelectGuarantorDetail() {
        return selectGuarantorDetail;
    }

    public void setSelectGuarantorDetail(ExistingGuarantorDetailView selectGuarantorDetail) {
        this.selectGuarantorDetail = selectGuarantorDetail;
    }

    public ExistingCollateralDetailView getSelectCollateralDetail() {
        return selectCollateralDetail;
    }

    public void setSelectCollateralDetail(ExistingCollateralDetailView selectCollateralDetail) {
        this.selectCollateralDetail = selectCollateralDetail;
    }
}
