package com.clevel.selos.controller;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.relation.PrdGroupToPrdProgramDAO;
import com.clevel.selos.dao.relation.PrdProgramToCreditTypeDAO;
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
import java.math.RoundingMode;
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
    private String messageHeader;
    private String message;


    private ExistingConditionDetailView conditionInfoDetailView;
    private List<ExistingConditionDetailView> conditionInfoDetailViewList;
    private ExistingConditionDetailView selectConditionItem;

    ArrayList<ExistingCreditTypeDetailView> borrowerCreditTypeDetailList;
    ArrayList<ExistingCreditTypeDetailView> relatedCreditTypeDetailList;

    private User user;
    private ExistingCreditView existingCreditView;

    private ExistingCreditTierDetailView existingCreditTierDetailView;
    private List<ExistingCreditTierDetailView> existingCreditTierDetailViewList;
    private SplitLineDetailView splitLineDetailView;
    private List<SplitLineDetailView> splitLineDetailViewList;
    private ProductProgram existProductProgram;
    private CreditType existCreditType;
    private AccountStatus existAccountStatus ;


    private ExistingCreditDetailView  existingCreditDetailView;

    private List<ProductGroup> productGroupList;
    private List<PrdGroupToPrdProgram> prdGroupToPrdProgramList;
    private List<PrdProgramToCreditType> prdProgramToCreditTypeList;
    private ProductProgram productProgram;



    private List<ExistingCreditDetailView> existingCreditDetailViewList;
    private List<ExistingCreditDetailView> borrowerExistingCreditDetailViewList;
    private List<ExistingCreditDetailView> relatedExistingCreditDetailViewList;
    private ExistingCreditDetailView selectCreditDetail;


    private List<ProductProgram> productProgramList;
    private List<CreditType> creditTypeList;
    private List<AccountStatus> accountStatusList;
    private boolean showSplitLine;



    private ExistingCollateralDetailView existingCollateralDetailView;
    private List<Relation> relationList;
    private List<CollateralType> collateralTypeList;
    private List<PotentialCollateral> potentialCollateralList;
    private ExistingCollateralDetailView selectCollateralDetail;
    private List<ExistingCollateralDetailView> borrowerExistingCollateralDetailViewList;
    private List<ExistingCollateralDetailView> relatedExistingCollateralDetailViewList;

    private List<ExistingCreditDetailView> collateralCreditDetailViewList;
    private List<ExistingCreditDetailView> borrowerCollateralCreditDetailViewList;
    private List<ExistingCreditDetailView> relatedCollateralCreditDetailViewList;


    private ExistingGuarantorDetailView existingGuarantorDetailView;
    private List<MortgageType> mortgageTypeList;
    private ExistingGuarantorDetailView selectGuarantorDetail;
    private List<ExistingGuarantorDetailView> borrowerExistingGuarantorDetailViewList;
    private List<ExistingCreditTypeDetailView> borrowerGuarantorCreditTypeDetailViewList;

    private List<Customer> guarantorList;

    private int seqBorrower=0;
    private int seqRelated=0;
    private Hashtable hashBorrower;
    private Hashtable hashRelated;

    enum ModeForButton {ADD, EDIT}

    private ModeForButton modeForButton;
    private String typeOfList;

    @Inject
    private CreditTypeDAO creditTypeDAO;
    @Inject
    private ProductProgramDAO productProgramDAO;
    @Inject
    private AccountStatusDAO accountStatusDAO;
    @Inject
    private PrdGroupToPrdProgramDAO prdGroupToPrdProgramDAO;        // find product program
    @Inject
    private PrdProgramToCreditTypeDAO prdProgramToCreditTypeDAO;    // find credit type
    @Inject
    private ProductFormulaDAO productFormulaDAO;

    @Inject
    private CustomerDAO customerDAO;
    @Inject
    private CollateralTypeDAO collateralTypeDAO;
    @Inject
    private PotentialCollateralDAO potentialCollateralDAO;
    @Inject
    private RelationDAO relationDAO;
    @Inject
    private MortgageTypeDAO mortgageTypeDAO;

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
        accountStatusList = accountStatusDAO.findAll();
        relationList = relationDAO.findAll();
        collateralTypeList = collateralTypeDAO.findAll();
        potentialCollateralList = potentialCollateralDAO.findAll();
        mortgageTypeList = mortgageTypeDAO.findAll();
        hashBorrower = new Hashtable<String,String>();
        hashRelated  = new Hashtable<String,String>();
        if (session.getAttribute("workCaseId") != null) {
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            stepId = Long.parseLong(session.getAttribute("stepId").toString());
            userId = session.getAttribute("userId").toString();

            if (guarantorList == null) {
                guarantorList = new ArrayList<Customer>();
                Customer customer = new Customer();
                customer.setNameTh("พี่เทพ มวลมหาประชา");

                guarantorList.add(customer);

                //guarantorList = customerDAO.findGuarantorByWorkCaseId(workCaseId);
            }

            conditionInfoDetailViewList = new ArrayList<ExistingConditionDetailView>();
            existingCreditDetailViewList = new ArrayList<ExistingCreditDetailView>();

            borrowerExistingCreditDetailViewList = new ArrayList<ExistingCreditDetailView>();
            relatedExistingCreditDetailViewList = new ArrayList<ExistingCreditDetailView>();

            existingCreditView = new ExistingCreditView();

            existingCreditDetailView = new ExistingCreditDetailView();
            existAccountStatus = new AccountStatus();
            existProductProgram = new ProductProgram();
            existCreditType = new CreditType();

            existingCreditDetailView.setExistAccountStatus(existAccountStatus);
            existingCreditDetailView.setExistProductProgram(existProductProgram);
            existingCreditDetailView.setExistCreditType(existCreditType);

            splitLineDetailView = new SplitLineDetailView();
            productProgram = new ProductProgram();
            splitLineDetailView.setProductProgram(productProgram);

            existingCollateralDetailView = new ExistingCollateralDetailView();

            borrowerExistingCollateralDetailViewList = new ArrayList<ExistingCollateralDetailView>() ;
            relatedExistingCollateralDetailViewList  = new ArrayList<ExistingCollateralDetailView>() ;

            collateralCreditDetailViewList = new ArrayList<ExistingCreditDetailView>();
            existingCollateralDetailView.setCollateralType(new CollateralType());
            existingCollateralDetailView.setPotentialCollateral(new PotentialCollateral());
            existingCollateralDetailView.setRelation(new Relation());
            existingCollateralDetailView.setMortgageType(new MortgageType());
            existingCollateralDetailView.setExistingCreditTypeDetailViewList(new ArrayList<ExistingCreditTypeDetailView>());




            existingGuarantorDetailView = new ExistingGuarantorDetailView();
            borrowerExistingGuarantorDetailViewList = new ArrayList<ExistingGuarantorDetailView>();

            existingGuarantorDetailView.setExistingCreditTypeDetailViewList(new ArrayList<ExistingCreditTypeDetailView>());
            //*** Get Product Program List from Product Group ***//
            prdGroupToPrdProgramList = prdGroupToPrdProgramDAO.getListPrdProByPrdGroupForExistCredit();
            if(prdGroupToPrdProgramList == null){
                prdGroupToPrdProgramList = new ArrayList<PrdGroupToPrdProgram>();
            }

            existingCreditView.setBorrowerComExistingCredit(borrowerExistingCreditDetailViewList);
            existingCreditView.setRelatedComExistingCredit(relatedExistingCreditDetailViewList);
            existingCreditView.setBorrowerCollateralList(borrowerExistingCollateralDetailViewList);
            existingCreditView.setRelatedCollateralList(relatedExistingCollateralDetailViewList);
            existingCreditView.setBorrowerGuarantorList(borrowerExistingGuarantorDetailViewList);

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
        try{
            preRender();
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
    public void onAddCommercialCredit() {
        log.info("onAddCommercialCredit ::: ");
        existingCreditDetailView = new ExistingCreditDetailView();
        existAccountStatus = new AccountStatus();
        existProductProgram = new ProductProgram();
        existCreditType = new CreditType();

        existingCreditDetailView.setExistAccountStatus(existAccountStatus);
        existingCreditDetailView.setExistProductProgram(existProductProgram);
        existingCreditDetailView.setExistCreditType(existCreditType);

        existingCreditTierDetailView = new ExistingCreditTierDetailView();
        existingCreditTierDetailViewList = new ArrayList<ExistingCreditTierDetailView>();

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
        log.info("onEditCommercialCredit rowIndex   " + rowIndex);
        log.info("onEditCommercialCredit selectCreditDetail null is " + (selectCreditDetail==null));
        if(selectCreditDetail != null){
            log.info("onEditCommercialCredit is " + selectCreditDetail.toString());
        }
        modeForButton = ModeForButton.EDIT;
        existingCreditDetailView = new ExistingCreditDetailView();
        existingCreditDetailView.setAccountName(selectCreditDetail.getAccountName());
        existingCreditDetailView.setAccountNumber(selectCreditDetail.getAccountNumber());
        existingCreditDetailView.setExistAccountStatus(selectCreditDetail.getExistAccountStatus());
        existingCreditDetailView.setExistProductProgram(selectCreditDetail.getExistProductProgram());
        existingCreditDetailView.setExistCreditType(selectCreditDetail.getExistCreditType());

        splitLineDetailViewList= selectCreditDetail.getSplitLineDetailViewList();
        existingCreditDetailView.setSplitLineDetailViewList(selectCreditDetail.getSplitLineDetailViewList());
        existingCreditTierDetailViewList = selectCreditDetail.getExistingCreditTierDetailViewList();
        existingCreditDetailView.setExistingCreditTierDetailViewList(selectCreditDetail.getExistingCreditTierDetailViewList());

        log.info("onEditCommercialCredit end ::: mode : {}", modeForButton);
    }

    public void onDeleteCommercialCredit() {
        log.info("onDeleteCommercialCredit ::: mode : {} typeOfList " + typeOfList);
        log.info("onDeleteCommercialCredit rowIndex   " + rowIndex);
        log.info("onDeleteCommercialCredit selectCreditDetail null is " + (selectCreditDetail==null));
        if(selectCreditDetail != null){
            log.info("selectGuarantorDetail is " + selectCreditDetail.toString());
        }
        ExistingCreditDetailView existingCreditDetailViewDel = selectCreditDetail;
        int used;
        log.info("onDeleteCommercialCredit ::: mode : {} existingCreditDetailViewRow " + existingCreditDetailViewDel.toString());
        if(typeOfList.equals("borrower")){
            log.info("del 1");

            for (int l=0;l<hashBorrower.size();l++){
                log.info("hashBorrower.get(j) in use   :  "+ l + " is   " +hashBorrower.get(l).toString());
            }

            used = Integer.parseInt(hashBorrower.get(existingCreditDetailViewDel.getSeq()).toString());

            log.info("before del use is  " +  used);
             if(used ==0){
                 log.info("use 0 ");
                 borrowerExistingCreditDetailViewList.remove(existingCreditDetailViewDel);
             }else{
                 log.info("use > 0 ");
                 messageHeader = "เกิดข้อผิดพลาด";
                 message = "มีการใช้งาน Credit Type Record นี้แล้ว";
                 RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
             }

        }else if(typeOfList.equals("related")){
            log.info("del 1");

            for (int l=0;l<hashRelated.size();l++){
                log.info("hashRelated.get(j) in use   :  "+ l + " is   " +hashRelated.get(l).toString());
            }

            used = Integer.parseInt(hashRelated.get(existingCreditDetailViewDel.getSeq()).toString());

            log.info("before del use is  " +  used);
            if(used ==0){
                log.info("use 0 ");
                relatedExistingCreditDetailViewList.remove(existingCreditDetailViewDel);
            }else{
                log.info("use > 0 ");
                messageHeader = "เกิดข้อผิดพลาด";
                message = "มีการใช้งาน Credit Type Record นี้แล้ว";
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            }
        }
        calTotalCreditBorrower();
        calTotalCreditRelated();
    }
    public void onSaveCreditInfo() {
        log.info("onSaveCreditInfo ::: mode : {}", modeForButton);
        log.info("onSaveCreditInfo ::: rowIndex : {}", rowIndex);
        log.info("onSaveCreditInfo selectCreditDetail null is " + (selectCreditDetail==null));
        if(selectCreditDetail != null){
            log.info("selectGuarantorDetail is " + selectCreditDetail.toString());
        }

        boolean complete;
        RequestContext context = RequestContext.getCurrentInstance();

        log.info("typeOfList ::: "+ typeOfList);
        if(modeForButton != null && modeForButton.equals(ModeForButton.ADD)){
            log.info("add to list begin");
            ProductProgram  productProgram = productProgramDAO.findById(existingCreditDetailView.getExistProductProgram().getId());
            CreditType creditType = creditTypeDAO.findById( existingCreditDetailView.getExistCreditType().getId());
            AccountStatus accountStatus = accountStatusDAO.findById( existingCreditDetailView.getExistAccountStatus().getId());

            existingCreditDetailView.setExistProductProgram(productProgram);
            existingCreditDetailView.setExistCreditType(creditType);
            existingCreditDetailView.setExistAccountStatus(accountStatus);

            existingCreditDetailView.setSplitLineDetailViewList(splitLineDetailViewList);
            existingCreditDetailView.setExistingCreditTierDetailViewList(existingCreditTierDetailViewList);


            if(typeOfList.equals("borrower")){
                existingCreditDetailView.setSeq(seqBorrower);
                hashBorrower.put(seqBorrower,0);
                seqBorrower++;
                borrowerExistingCreditDetailViewList.add(existingCreditDetailView);
            }else if(typeOfList.equals("related")){
                existingCreditDetailView.setSeq(seqRelated);
                hashRelated.put(seqRelated,0);
                seqRelated++;
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
            AccountStatus accountStatus = accountStatusDAO.findById( existingCreditDetailView.getExistAccountStatus().getId());

            existingCreditDetailViewRow.setExistProductProgram(productProgram);
            existingCreditDetailViewRow.setExistCreditType(creditType);
            existingCreditDetailViewRow.setExistAccountStatus(accountStatus);
            existingCreditDetailViewRow.setAccountName(existingCreditDetailView.getAccountName());
            existingCreditDetailViewRow.setAccountNumber(existingCreditDetailView.getAccountNumber());

            existingCreditDetailViewRow.setSplitLineDetailViewList(splitLineDetailViewList);
            existingCreditDetailViewRow.setExistingCreditTierDetailViewList(existingCreditTierDetailViewList);

            log.info("update list end");
        }else {
            log.info("onSaveCreditInfo ::: Undefined modeForButton !!");
        }
        calTotalCreditBorrower();
        calTotalCreditRelated();
        complete = true;

        log.info("  complete >>>>  :  {}", complete);
        context.addCallbackParam("functionComplete", complete);

    }

    //  Start Tier Dialog //
    public void onAddCreditTierDetailView() {
        log.info("onAddCreditInfo ::: ");
        existingCreditTierDetailView = new ExistingCreditTierDetailView();
        existingCreditTierDetailViewList.add(existingCreditTierDetailView);
    }

    public void onDeleteCreditTierDetailView(int rowOnTable) {
        if(Integer.toString(rowOnTable) != null ){
            log.info("onDeleteCreditTierDetailView ::: rowOnTable " + rowOnTable);
            existingCreditTierDetailViewList.remove(rowOnTable);
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

    private void calTotalCreditBorrower(){
        double totalBorrowerCom =0;
        double totalBorrowerRetail =0;
        double totalBorrowerRlos =0;
        double limitRow;

        for(int i=0;i<borrowerExistingCreditDetailViewList.size();i++){
            limitRow = borrowerExistingCreditDetailViewList.get(i).getLimit().doubleValue();
            totalBorrowerCom += limitRow;
        }

        if(existingCreditView.getBorrowerRetailExistingCredit() != null){
            for(int i=0;i<existingCreditView.getBorrowerRetailExistingCredit().size();i++){
                limitRow = existingCreditView.getBorrowerRetailExistingCredit().get(i).getLimit().doubleValue();
                totalBorrowerRetail += limitRow;
            }
        }

        if(existingCreditView.getBorrowerAppInRLOSCredit() != null){
            for(int i=0;i<existingCreditView.getBorrowerAppInRLOSCredit().size();i++){
                limitRow = existingCreditView.getBorrowerAppInRLOSCredit().get(i).getLimit().doubleValue();
                totalBorrowerRlos += limitRow;
            }
        }

        existingCreditView.setTotalBorrowerComLimit( new BigDecimal(totalBorrowerCom).setScale(2, RoundingMode.HALF_UP));
        existingCreditView.setTotalBorrowerRetailLimit( new BigDecimal(totalBorrowerRetail).setScale(2, RoundingMode.HALF_UP));
        existingCreditView.setTotalBorrowerAppInRLOSLimit( new BigDecimal(totalBorrowerRlos).setScale(2, RoundingMode.HALF_UP));

    }

    private void calTotalCreditRelated(){
        double totalRelatedCom =0;
        double totalRelatedRetail =0;
        double totalRelatedRlos =0;
        double limitRow;

        for(int i=0;i<relatedExistingCreditDetailViewList.size();i++){
            limitRow = relatedExistingCreditDetailViewList.get(i).getLimit().doubleValue();
            totalRelatedCom += limitRow;
        }

        if(existingCreditView.getRelatedRetailExistingCredit() != null){
            for(int i=0;i<existingCreditView.getRelatedRetailExistingCredit().size();i++){
                limitRow = existingCreditView.getRelatedRetailExistingCredit().get(i).getLimit().doubleValue();
                totalRelatedRetail += limitRow;
            }
        }

        if(existingCreditView.getRelatedAppInRLOSCredit() != null){
            for(int i=0;i<existingCreditView.getRelatedAppInRLOSCredit().size();i++){
                limitRow = existingCreditView.getRelatedAppInRLOSCredit().get(i).getLimit().doubleValue();
                totalRelatedRlos += limitRow;
            }
        }

        existingCreditView.setTotalRelatedComLimit( new BigDecimal(totalRelatedCom).setScale(2, RoundingMode.HALF_UP));
        existingCreditView.setTotalRelatedRetailLimit( new BigDecimal(totalRelatedRetail).setScale(2, RoundingMode.HALF_UP));
        existingCreditView.setTotalRelatedAppInRLOSLimit( new BigDecimal(totalRelatedRlos).setScale(2, RoundingMode.HALF_UP));

    }

    public List<ExistingCreditTypeDetailView> findBorrowerCreditFacility() {
        ExistingCreditTypeDetailView existingCreditTypeDetailView;
        borrowerCreditTypeDetailList = new ArrayList<ExistingCreditTypeDetailView>();  // find credit existing and propose in this workCase

        if (existingCreditView.getBorrowerComExistingCredit().size() > 0) {
            for (int i = 0; i < existingCreditView.getBorrowerComExistingCredit().size(); i++) {
                existingCreditTypeDetailView = new ExistingCreditTypeDetailView();
                existingCreditTypeDetailView.setNoFlag(false);
                existingCreditTypeDetailView.setSeq(existingCreditView.getRelatedComExistingCredit().get(i).getSeq());
                existingCreditTypeDetailView.setAccountName(existingCreditView.getRelatedComExistingCredit().get(i).getAccountName());
                existingCreditTypeDetailView.setAccountNumber(existingCreditView.getRelatedComExistingCredit().get(i).getAccountNumber());
                existingCreditTypeDetailView.setAccountSuf(existingCreditView.getRelatedComExistingCredit().get(i).getAccountSuf());
                existingCreditTypeDetailView.setProductProgram(existingCreditView.getRelatedComExistingCredit().get(i).getExistProductProgram().getName());
                existingCreditTypeDetailView.setCreditFacility(existingCreditView.getRelatedComExistingCredit().get(i).getExistCreditType().getName());
                existingCreditTypeDetailView.setLimit(existingCreditView.getRelatedComExistingCredit().get(i).getLimit());
                borrowerCreditTypeDetailList.add(existingCreditTypeDetailView);
            }
        }

        return borrowerCreditTypeDetailList;
    }



    public List<ExistingCreditTypeDetailView> findRelatedCreditFacility() {
        ExistingCreditTypeDetailView existingCreditTypeDetailView;
        relatedCreditTypeDetailList = new ArrayList<ExistingCreditTypeDetailView>();  // find credit existing and propose in this workCase

        if (existingCreditView.getRelatedComExistingCredit().size() > 0) {
            for (int i = 0; i < existingCreditView.getRelatedComExistingCredit().size(); i++) {
                existingCreditTypeDetailView = new ExistingCreditTypeDetailView();
                existingCreditTypeDetailView.setNoFlag(false);
                existingCreditTypeDetailView.setSeq(existingCreditView.getRelatedComExistingCredit().get(i).getSeq());
                existingCreditTypeDetailView.setAccountName(existingCreditView.getRelatedComExistingCredit().get(i).getAccountName());
                existingCreditTypeDetailView.setAccountNumber(existingCreditView.getRelatedComExistingCredit().get(i).getAccountNumber());
                existingCreditTypeDetailView.setAccountSuf(existingCreditView.getRelatedComExistingCredit().get(i).getAccountSuf());
                existingCreditTypeDetailView.setProductProgram(existingCreditView.getRelatedComExistingCredit().get(i).getExistProductProgram().getName());
                existingCreditTypeDetailView.setCreditFacility(existingCreditView.getRelatedComExistingCredit().get(i).getExistCreditType().getName());
                existingCreditTypeDetailView.setLimit(existingCreditView.getRelatedComExistingCredit().get(i).getLimit());
                relatedCreditTypeDetailList.add(existingCreditTypeDetailView);
            }
        }

        return relatedCreditTypeDetailList;
    }

    //Start Condition Information //
    public void onAddConditionInfo() {
        log.info("onAddConditionInfo ::: ");
        conditionInfoDetailView = new ExistingConditionDetailView();
        modeForButton = ModeForButton.ADD;
        log.info("onAddConditionInfo ::: modeForButton : {}", modeForButton);

    }

    public void onSaveConditionInfoDlg(){
        log.info("onSaveConditionInfoDlg ::: mode : {}", modeForButton);

        RequestContext context = RequestContext.getCurrentInstance();
        boolean complete = false;

        if(modeForButton != null && modeForButton.equals(ModeForButton.ADD)){

            ExistingConditionDetailView conditionDetailViewAdd = new ExistingConditionDetailView();
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
        existingCollateralDetailView.setMortgageType(new MortgageType());
        for(int i=0;i<borrowerExistingCreditDetailViewList.size();i++){
            borrowerExistingCreditDetailViewList.get(i).setNoFlag(false);
        }
        if(typeOfList.equals("borrower")){
            existingCollateralDetailView.setExistingCreditTypeDetailViewList(findBorrowerCreditFacility());
        }else if(typeOfList.equals("related")){
            existingCollateralDetailView.setExistingCreditTypeDetailViewList(findRelatedCreditFacility());
        }


        modeForButton = ModeForButton.ADD;
        log.info("onAddCommercialCredit ::: modeForButton : {}", modeForButton);
    }

    public void onEditExistingCollateral() {
        log.info("onEditExistingCollateral ::: ");
        log.info("onEditExistingCollateral rowIndex   " + rowIndex);
        log.info("onEditExistingCollateral selectGuarantorDetail null is " + (selectCollateralDetail==null));
        if(selectCollateralDetail != null){
            log.info("selectGuarantorDetail is " + selectCollateralDetail.toString());
        }
        modeForButton = ModeForButton.EDIT;
        existingCollateralDetailView = new ExistingCollateralDetailView();
        collateralCreditDetailViewList = new ArrayList<ExistingCreditDetailView>();
        existingCollateralDetailView.setCollateralType(selectCollateralDetail.getCollateralType());
        existingCollateralDetailView.setPotentialCollateral(selectCollateralDetail.getPotentialCollateral());
        existingCollateralDetailView.setRelation(selectCollateralDetail.getRelation());
        existingCollateralDetailView.setMortgageType(selectCollateralDetail.getMortgageType());

        ExistingCreditDetailView borrowerExistingCreditDetailView;
        ExistingCreditDetailView borrowerCollateralCreditView;
        ExistingCreditDetailView relatedExistingCreditDetailView;
        ExistingCreditDetailView relatedCollateralCreditView;

        int tempSeq =0;
        List<ExistingCreditTypeDetailView> existingCreditTypeDetailList;


        if(typeOfList.equals("borrower")){

            existingCreditTypeDetailList = findBorrowerCreditFacility();
            existingCollateralDetailView.setExistingCreditTypeDetailViewList(existingCreditTypeDetailList);

            for (int i = 0; i < selectCollateralDetail.getExistingCreditTypeDetailViewList().size(); i++) {
                for (int j = tempSeq; j < existingCreditTypeDetailList.size(); j++) {
                    log.info("creditType at " + j + " seq is     " + existingCreditTypeDetailList.get(j).getSeq());

                    if (selectCollateralDetail.getExistingCreditTypeDetailViewList().get(i).getSeq() == existingCreditTypeDetailList.get(j).getSeq()) {
                        existingCollateralDetailView.getExistingCreditTypeDetailViewList().get(j).setNoFlag(true);
                        tempSeq = j;
                    }
                    continue;
                }
            }


        }else  if(typeOfList.equals("related")){
            existingCreditTypeDetailList = findRelatedCreditFacility();
            existingCollateralDetailView.setExistingCreditTypeDetailViewList(existingCreditTypeDetailList);

            for (int i = 0; i < selectCollateralDetail.getExistingCreditTypeDetailViewList().size(); i++) {
                for (int j = tempSeq; j < existingCreditTypeDetailList.size(); j++) {
                    log.info("creditType at " + j + " seq is     " + existingCreditTypeDetailList.get(j).getSeq());

                    if (selectCollateralDetail.getExistingCreditTypeDetailViewList().get(i).getSeq() == existingCreditTypeDetailList.get(j).getSeq()) {
                        existingCollateralDetailView.getExistingCreditTypeDetailViewList().get(j).setNoFlag(true);
                        tempSeq = j;
                    }
                    continue;
                }
            }
        }


        modeForButton = ModeForButton.EDIT;
        log.info("onAddCommercialCredit ::: modeForButton : {}", modeForButton);
    }

    public void onSaveExistingCollateral(){
        log.info("onSaveExistingCollateral ::: mode : {}", modeForButton);
        log.info("onSaveExistingCollateral selectGuarantorDetail null is " + (selectCollateralDetail==null));

        boolean complete;
        boolean checkPlus;
        ExistingCreditDetailView existingCreditDetailViewNoFlag;
        RequestContext context = RequestContext.getCurrentInstance();
        log.info("typeOfList ::: "+ typeOfList);
        int seqBorrowerTemp;
        int seqRelatedTemp;

        CollateralType  collateralType = collateralTypeDAO.findById(existingCollateralDetailView.getCollateralType().getId());
        PotentialCollateral potentialCollateral = potentialCollateralDAO.findById( existingCollateralDetailView.getPotentialCollateral().getId());
        Relation relation = relationDAO.findById( existingCollateralDetailView.getRelation().getId());

        if(modeForButton != null && modeForButton.equals(ModeForButton.ADD)){
            ExistingCollateralDetailView  existingCollateralDetailViewAdd  = new ExistingCollateralDetailView();

            existingCollateralDetailViewAdd.setCollateralType(collateralType);
            existingCollateralDetailViewAdd.setPotentialCollateral(potentialCollateral);
            existingCollateralDetailViewAdd.setRelation(relation);
            existingCollateralDetailViewAdd.setExistingCreditTypeDetailViewList(new ArrayList<ExistingCreditTypeDetailView>());

            if(typeOfList.equals("borrower")){
                for (ExistingCreditTypeDetailView existingCreditTypeDetail : existingCollateralDetailView.getExistingCreditTypeDetailViewList()) {
                    log.info("existingCreditTypeDetail.isNoFlag()  :: {}", existingCreditTypeDetail.isNoFlag());
                    if (existingCreditTypeDetail.isNoFlag()) {
                        existingCollateralDetailViewAdd.getExistingCreditTypeDetailViewList().add(existingCreditTypeDetail);
                        seqBorrowerTemp = existingCreditTypeDetail.getSeq();
                        hashBorrower.put(seqBorrowerTemp, Integer.parseInt(hashBorrower.get(seqBorrowerTemp).toString()) + 1);
                    }
                }

                borrowerExistingCollateralDetailViewList.add(existingCollateralDetailViewAdd);
            }else if(typeOfList.equals("related")){
                for (ExistingCreditTypeDetailView existingCreditTypeDetail : existingCollateralDetailView.getExistingCreditTypeDetailViewList()) {
                    log.info("existingCreditTypeDetail.isNoFlag()  :: {}", existingCreditTypeDetail.isNoFlag());
                    if (existingCreditTypeDetail.isNoFlag()) {
                        existingCollateralDetailViewAdd.getExistingCreditTypeDetailViewList().add(existingCreditTypeDetail);
                        seqRelatedTemp = existingCreditTypeDetail.getSeq();
                        hashRelated.put(seqRelatedTemp, Integer.parseInt(hashRelated.get(seqRelatedTemp).toString()) + 1);
                    }
                }

                relatedExistingCollateralDetailViewList.add(existingCollateralDetailViewAdd);
            }
        }else if(modeForButton != null && modeForButton.equals(ModeForButton.EDIT)){

            collateralCreditDetailViewList = new ArrayList<ExistingCreditDetailView>();

            if(typeOfList.equals("borrower")){
                ExistingCollateralDetailView borrowerCollateralDetailViewRow = borrowerExistingCollateralDetailViewList.get(rowIndex);
                borrowerCollateralDetailViewRow.setExistingCreditTypeDetailViewList(new ArrayList<ExistingCreditTypeDetailView>());

                existingCollateralDetailView.setExistingCreditTypeDetailViewList(new ArrayList<ExistingCreditTypeDetailView>());

                List<ExistingCreditTypeDetailView> existingCreditTypeDetailViewList;

                existingCreditTypeDetailViewList = existingCollateralDetailView.getExistingCreditTypeDetailViewList();

                for (int i = 0; i < existingCreditTypeDetailViewList.size(); i++) {
                    if (existingCreditTypeDetailViewList.get(i).isNoFlag() == true) {
                        borrowerCollateralDetailViewRow.getExistingCreditTypeDetailViewList().add(existingCreditTypeDetailViewList.get(i));
                        seqBorrowerTemp = existingCreditTypeDetailViewList.get(i).getSeq();
                        checkPlus = true;

                        for (int j = 0; j < selectCollateralDetail.getExistingCreditTypeDetailViewList().size(); j++) {
                            if (selectCollateralDetail.getExistingCreditTypeDetailViewList().get(j).getSeq() == seqBorrowerTemp) {
                                checkPlus = false;
                            }
                        }

                        if (checkPlus) {
                            hashBorrower.put(seqBorrowerTemp, Integer.parseInt(hashBorrower.get(seqBorrowerTemp).toString()) + 1);
                        }

                    } else if (existingCreditTypeDetailViewList.get(i).isNoFlag() == false) {
                        if (Integer.parseInt(hashBorrower.get(i).toString()) > 0) {
                            hashBorrower.put(i, Integer.parseInt(hashBorrower.get(i).toString()) - 1);
                        }
                    }
                }

                existingCreditView.getBorrowerCollateralList().get(rowIndex).setExistingCreditTypeDetailViewList(existingCollateralDetailView.getExistingCreditTypeDetailViewList());


            }else if(typeOfList.equals("related")){
                ExistingCollateralDetailView relatedCollateralDetailViewRow = relatedExistingCollateralDetailViewList.get(rowIndex);
                relatedCollateralDetailViewRow.setExistingCreditTypeDetailViewList(new ArrayList<ExistingCreditTypeDetailView>());

                existingCollateralDetailView.setExistingCreditTypeDetailViewList(new ArrayList<ExistingCreditTypeDetailView>());

                List<ExistingCreditTypeDetailView> existingCreditTypeDetailViewList;

                existingCreditTypeDetailViewList = existingCollateralDetailView.getExistingCreditTypeDetailViewList();

                for (int i = 0; i < existingCreditTypeDetailViewList.size(); i++) {
                    if (existingCreditTypeDetailViewList.get(i).isNoFlag() == true) {
                        relatedCollateralDetailViewRow.getExistingCreditTypeDetailViewList().add(existingCreditTypeDetailViewList.get(i));
                        seqRelatedTemp = existingCreditTypeDetailViewList.get(i).getSeq();
                        checkPlus = true;

                        for (int j = 0; j < selectCollateralDetail.getExistingCreditTypeDetailViewList().size(); j++) {
                            if (selectCollateralDetail.getExistingCreditTypeDetailViewList().get(j).getSeq() == seqRelatedTemp) {
                                checkPlus = false;
                            }
                        }

                        if (checkPlus) {
                            hashRelated.put(seqRelatedTemp, Integer.parseInt(hashRelated.get(seqRelatedTemp).toString()) + 1);
                        }

                    } else if (existingCreditTypeDetailViewList.get(i).isNoFlag() == false) {
                        if (Integer.parseInt(hashRelated.get(i).toString()) > 0) {
                            hashRelated.put(i, Integer.parseInt(hashRelated.get(i).toString()) - 1);
                        }
                    }
                }

                existingCreditView.getRelatedCollateralList().get(rowIndex).setExistingCreditTypeDetailViewList(existingCollateralDetailView.getExistingCreditTypeDetailViewList());


            }else {
            log.info("onSaveCreditInfo ::: Undefined modeForButton !!");
            }
        }

        complete = true;

        log.info("  complete >>>>  :  {}", complete);
        context.addCallbackParam("functionComplete", complete);

    }

    public void onDeleteExistingCollateral(){
        log.info("onDeleteExistingCollateral begin " );
        log.info("onDeleteExistingCollateral rowIndex is " + rowIndex);
        log.info("onDeleteExistingCollateral selectGuarantorDetail null is " + (selectCollateralDetail==null));
        if(selectCollateralDetail != null){
            log.info("selectGuarantorDetail is " + selectCollateralDetail.toString());
        }

        if(typeOfList.equals("borrower")){
            ExistingCollateralDetailView borrowerCollateralDetailViewDel = selectCollateralDetail;
            for (int l=0;l<hashBorrower.size();l++){
                log.info("before hashBorrower seq :  "+ l + " use is   " +hashBorrower.get(l).toString());
            }
            log.info("getCreditFacilityList().size() " + borrowerCollateralDetailViewDel.getExistingCreditTypeDetailViewList().size());

            for(int j=0;j<borrowerCollateralDetailViewDel.getExistingCreditTypeDetailViewList().size();j++){
                int seqBowForDel = borrowerCollateralDetailViewDel.getExistingCreditTypeDetailViewList().get(j).getSeq();
                if(Integer.parseInt(hashBorrower.get(borrowerCollateralDetailViewDel.getExistingCreditTypeDetailViewList().get(j).getSeq()).toString())>0){
                    hashBorrower.put( seqBowForDel,Integer.parseInt(hashBorrower.get(seqBowForDel).toString()) -1);
                }
            }

            borrowerExistingCollateralDetailViewList.remove(selectCollateralDetail);

            for (int l=0;l<hashBorrower.size();l++){
                log.info("after hashBorrower seq :  "+ l + " use is   " +hashBorrower.get(l).toString());
            }

         }else if(typeOfList.equals("related")){
            ExistingCollateralDetailView relatedCollateralDetailViewDel = selectCollateralDetail;
            for (int l=0;l<hashRelated.size();l++){
                log.info("before hashRelated seq :  "+ l + " use is   " +hashRelated.get(l).toString());
            }
            log.info("getCreditFacilityList().size() " + relatedCollateralDetailViewDel.getExistingCreditTypeDetailViewList().size());

            for(int j=0;j<relatedCollateralDetailViewDel.getExistingCreditTypeDetailViewList().size();j++){
                int seqRelatedForDel = relatedCollateralDetailViewDel.getExistingCreditTypeDetailViewList().get(j).getSeq();
                if(Integer.parseInt(hashBorrower.get(relatedCollateralDetailViewDel.getExistingCreditTypeDetailViewList().get(j).getSeq()).toString())>0){
                    hashRelated.put(seqRelatedForDel, Integer.parseInt(hashRelated.get(seqRelatedForDel).toString()) - 1);
                }
            }

            borrowerExistingCollateralDetailViewList.remove(selectCollateralDetail);

            for (int l=0;l<hashRelated.size();l++){
                log.info("after hashRelated seq :  "+ l + " use is   " +hashRelated.get(l).toString());
            }
        }
    }

    //Start Condition Information //
    public void onAddExistingGuarantor() {
        log.info("onAddExistingCollateral ::: ");
        existingGuarantorDetailView = new ExistingGuarantorDetailView();
        borrowerGuarantorCreditTypeDetailViewList = new ArrayList<ExistingCreditTypeDetailView>();
        existingGuarantorDetailView.setExistingCreditTypeDetailViewList(findBorrowerCreditFacility());

        modeForButton = ModeForButton.ADD;
        log.info("onAddCommercialCredit ::: modeForButton : {}", modeForButton);
    }

    public void onEditExistingGuarantor() {
        log.info("onEditExistingGuarantor ::: ");
        log.info("onEditExistingCollateral rowIndex   " + rowIndex);
        log.info("onEditExistingCollateral selectGuarantorDetail null is " + (selectGuarantorDetail==null));
        if(selectGuarantorDetail != null){
            log.info("selectGuarantorDetail is " + selectGuarantorDetail.toString());
        }

        modeForButton = ModeForButton.EDIT;
        existingGuarantorDetailView = new ExistingGuarantorDetailView();

        existingGuarantorDetailView.setTcgLgNo(selectGuarantorDetail.getTcgLgNo());
        existingGuarantorDetailView.setGuarantorName(selectGuarantorDetail.getGuarantorName());
        existingGuarantorDetailView.setGuaranteeAmount(selectGuarantorDetail.getGuaranteeAmount());
        log.info("onEditExistingCollateral 0.2" );
        ExistingCreditDetailView borrowerExistingCreditDetailView;
        ExistingCreditDetailView borrowerCollateralCreditView;
        log.info("onEditExistingCollateral 0.3" );
        int tempSeq =0;

        for(int i=0;i<borrowerExistingCreditDetailViewList.size();i++){
            borrowerExistingCreditDetailViewList.get(i).setNoFlag(false);
        }
        log.info("onEditExistingCollateral 0.4" );
        List<ExistingCreditTypeDetailView> existingCreditTypeDetailList;
        existingCreditTypeDetailList = findBorrowerCreditFacility();
        existingGuarantorDetailView.setExistingCreditTypeDetailViewList(existingCreditTypeDetailList);

        for (int i = 0; i < selectGuarantorDetail.getExistingCreditTypeDetailViewList().size(); i++) {
            for (int j = tempSeq; j < existingCreditTypeDetailList.size(); j++) {
                log.info("creditType at " + j + " seq is     " + existingCreditTypeDetailList.get(j).getSeq());

                if (selectGuarantorDetail.getExistingCreditTypeDetailViewList().get(i).getSeq() == existingCreditTypeDetailList.get(j).getSeq()) {
                    existingGuarantorDetailView.getExistingCreditTypeDetailViewList().get(j).setNoFlag(true);
                    tempSeq = j;
                }
                continue;
            }
        }

        log.info("onEditExistingGuarantor end" );
    }


    public void onSaveExistingGuarantor() {
        log.info("onSaveExistingGuarantor ::: mode : {}", modeForButton);
        log.info("onSaveExistingGuarantor rowIndex is " + rowIndex);
        log.info("onSaveExistingGuarantor selectGuarantorDetail null is " + (selectGuarantorDetail==null));
        if(selectGuarantorDetail != null){
            log.info("selectGuarantorDetail is " + selectGuarantorDetail.toString());
        }

        boolean complete = false;
        RequestContext context = RequestContext.getCurrentInstance();
        ExistingCreditDetailView existingCreditDetailViewNoFlag;
        int seqBorrowerTemp;
        boolean checkPlus = true;

        if(modeForButton != null && modeForButton.equals(ModeForButton.ADD)){

            ExistingGuarantorDetailView existingGuarantorDetailViewAdd = new ExistingGuarantorDetailView();

            existingGuarantorDetailViewAdd.setGuarantorName(existingGuarantorDetailView.getGuarantorName());
            existingGuarantorDetailViewAdd.setGuaranteeAmount(existingGuarantorDetailView.getGuaranteeAmount());
            existingGuarantorDetailViewAdd.setTcgLgNo(existingGuarantorDetailView.getTcgLgNo());

            existingGuarantorDetailViewAdd.setExistingCreditTypeDetailViewList(new ArrayList<ExistingCreditTypeDetailView>());
            for (ExistingCreditTypeDetailView existingCreditTypeDetail : existingGuarantorDetailView.getExistingCreditTypeDetailViewList()) {
                log.info("existingCreditTypeDetail.isNoFlag()  :: {}", existingCreditTypeDetail.isNoFlag());
                if (existingCreditTypeDetail.isNoFlag()) {
                    existingGuarantorDetailViewAdd.getExistingCreditTypeDetailViewList().add(existingCreditTypeDetail);
                    seqBorrowerTemp = existingCreditTypeDetail.getSeq();
                    hashBorrower.put(seqBorrowerTemp, Integer.parseInt(hashBorrower.get(seqBorrowerTemp).toString()) + 1);
                }
            }

            borrowerExistingGuarantorDetailViewList.add(existingGuarantorDetailViewAdd);

        }else if(modeForButton != null && modeForButton.equals(ModeForButton.EDIT)){
            ExistingGuarantorDetailView existingGuarantorDetailViewOnRow = borrowerExistingGuarantorDetailViewList.get(rowIndex);
            existingGuarantorDetailViewOnRow.setExistingCreditTypeDetailViewList(new ArrayList<ExistingCreditTypeDetailView>());


            List<ExistingCreditTypeDetailView> existingCreditTypeDetailViewList;
            existingCreditTypeDetailViewList = existingGuarantorDetailView.getExistingCreditTypeDetailViewList();

            for (int i = 0; i < existingCreditTypeDetailViewList.size(); i++) {
                if (existingCreditTypeDetailViewList.get(i).isNoFlag() == true) {
                    existingGuarantorDetailViewOnRow.getExistingCreditTypeDetailViewList().add(existingCreditTypeDetailViewList.get(i));
                    seqBorrowerTemp = existingCreditTypeDetailViewList.get(i).getSeq();
                    checkPlus = true;

                    for (int j = 0; j < selectCollateralDetail.getExistingCreditTypeDetailViewList().size(); j++) {
                        if (selectGuarantorDetail.getExistingCreditTypeDetailViewList().get(j).getSeq() == seqBorrowerTemp) {
                            checkPlus = false;
                        }
                    }

                    if (checkPlus) {
                        hashBorrower.put(seqBorrowerTemp, Integer.parseInt(hashBorrower.get(seqBorrowerTemp).toString()) + 1);
                    }

                } else if (existingCreditTypeDetailViewList.get(i).isNoFlag() == false) {
                    if (Integer.parseInt(hashBorrower.get(i).toString()) > 0) {
                        hashBorrower.put(i, Integer.parseInt(hashBorrower.get(i).toString()) - 1);
                    }
                }
            }

            existingCreditView.getBorrowerGuarantorList().get(rowIndex).setExistingCreditTypeDetailViewList(existingCollateralDetailView.getExistingCreditTypeDetailViewList());



        }else {
            log.info("onSaveCreditInfo ::: Undefined modeForButton !!");
        }

        complete = true;

        log.info(" onSaveExistingGuarantor complete >>>>  :  {}", complete);
        context.addCallbackParam("functionComplete", complete);

    }

    public void onDeleteExistingGuarantor(){
        log.info("onDeleteExistingGuarantor begin " );
        log.info("onDeleteExistingGuarantor rowIndex is " + rowIndex);
        log.info("onDeleteExistingGuarantor selectGuarantorDetail null is " + (selectGuarantorDetail==null));
        if(selectGuarantorDetail != null){
            log.info("selectGuarantorDetail is " + selectGuarantorDetail.toString());
        }


        ExistingGuarantorDetailView guarantorDetailViewDel = selectGuarantorDetail;

        for (int l=0;l<hashBorrower.size();l++){
            log.info("before hashBorrower seq :  "+ l + " use is   " +hashBorrower.get(l).toString());
        }
        log.info("getCreditFacilityList().size() " + guarantorDetailViewDel.getExistingCreditTypeDetailViewList().size());

        for(int j=0;j<guarantorDetailViewDel.getExistingCreditTypeDetailViewList().size();j++){
            int seqBowForDel = guarantorDetailViewDel.getExistingCreditTypeDetailViewList().get(j).getSeq();

            log.info("seq in seqBowForDel :  "+ seqBowForDel +" use feq is " + hashBorrower.get(seqBowForDel).toString());

            if(Integer.parseInt(hashBorrower.get(guarantorDetailViewDel.getExistingCreditTypeDetailViewList().get(j).getSeq()).toString())>0){
                hashBorrower.put( seqBowForDel,Integer.parseInt(hashBorrower.get(seqBowForDel).toString()) -1);
            }
        }

        borrowerExistingGuarantorDetailViewList.remove(guarantorDetailViewDel);

        for (int l=0;l<hashBorrower.size();l++){
            log.info("after hashBorrower seq :  "+ l + " use is   " +hashBorrower.get(l).toString());
        }

    }


    public ExistingConditionDetailView getConditionInfoDetailView() {
        return conditionInfoDetailView;
    }

    public void setConditionInfoDetailView(ExistingConditionDetailView conditionInfoDetailView) {
        this.conditionInfoDetailView = conditionInfoDetailView;
    }

    public List<ExistingConditionDetailView> getConditionInfoDetailViewList() {
        return conditionInfoDetailViewList;
    }

    public void setConditionInfoDetailViewList(List<ExistingConditionDetailView> conditionInfoDetailViewList) {
        this.conditionInfoDetailViewList = conditionInfoDetailViewList;
    }

    public ExistingConditionDetailView getSelectConditionItem() {
        return selectConditionItem;
    }

    public void setSelectConditionItem(ExistingConditionDetailView selectConditionItem) {
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

    public List<AccountStatus> getAccountStatusList() {
        return accountStatusList;
    }

    public void setAccountStatusList(List<AccountStatus> accountStatusList) {
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

    public List<ExistingCreditTierDetailView> getExistingCreditTierDetailViewList() {
        return existingCreditTierDetailViewList;
    }

    public void setExistingCreditTierDetailViewList(List<ExistingCreditTierDetailView> existingCreditTierDetailViewList) {
        this.existingCreditTierDetailViewList = existingCreditTierDetailViewList;
    }

    public ExistingCreditTierDetailView getExistingCreditTierDetailView() {
        return existingCreditTierDetailView;
    }

    public void setExistingCreditTierDetailView(ExistingCreditTierDetailView existingCreditTierDetailView) {
        this.existingCreditTierDetailView = existingCreditTierDetailView;
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

    public ArrayList<ExistingCreditTypeDetailView> getBorrowerCreditTypeDetailList() {
        return borrowerCreditTypeDetailList;
    }

    public void setBorrowerCreditTypeDetailList(ArrayList<ExistingCreditTypeDetailView> borrowerCreditTypeDetailList) {
        this.borrowerCreditTypeDetailList = borrowerCreditTypeDetailList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<ExistingGuarantorDetailView> getBorrowerExistingGuarantorDetailViewList() {
        return borrowerExistingGuarantorDetailViewList;
    }

    public void setBorrowerExistingGuarantorDetailViewList(List<ExistingGuarantorDetailView> borrowerExistingGuarantorDetailViewList) {
        this.borrowerExistingGuarantorDetailViewList = borrowerExistingGuarantorDetailViewList;
    }

    public List<ExistingCreditTypeDetailView> getBorrowerGuarantorCreditTypeDetailViewList() {
        return borrowerGuarantorCreditTypeDetailViewList;
    }

    public void setBorrowerGuarantorCreditTypeDetailViewList(List<ExistingCreditTypeDetailView> borrowerGuarantorCreditTypeDetailViewList) {
        this.borrowerGuarantorCreditTypeDetailViewList = borrowerGuarantorCreditTypeDetailViewList;
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

    public List<ExistingGuarantorDetailView> getBorrowerGuarantorDetailViewList() {
        return borrowerExistingGuarantorDetailViewList;
    }

    public void setBorrowerGuarantorDetailViewList(List<ExistingGuarantorDetailView> borrowerExistingGuarantorDetailViewList) {
        this.borrowerExistingGuarantorDetailViewList = borrowerExistingGuarantorDetailViewList;
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


    public List<MortgageType> getMortgageTypeList() {
        return mortgageTypeList;
    }

    public void setMortgageTypeList(List<MortgageType> mortgageTypeList) {
        this.mortgageTypeList = mortgageTypeList;
    }

    public String getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(String messageHeader) {
        this.messageHeader = messageHeader;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
