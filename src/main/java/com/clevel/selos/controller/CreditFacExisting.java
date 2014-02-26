package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.CreditFacExistingControl;
import com.clevel.selos.businesscontrol.CustomerInfoControl;
import com.clevel.selos.businesscontrol.ExistingCreditControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.relation.PrdGroupToPrdProgramDAO;
import com.clevel.selos.dao.relation.PrdProgramToCreditTypeDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.BorrowerType;
import com.clevel.selos.model.CreditCategory;
import com.clevel.selos.model.RelationValue;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.relation.PrdGroupToPrdProgram;
import com.clevel.selos.model.db.relation.PrdProgramToCreditType;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.transform.BankAccountStatusTransform;
import com.clevel.selos.transform.BaseRateTransform;
import com.clevel.selos.transform.ProductTransform;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import com.rits.cloning.Cloner;

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
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

@ViewScoped
@ManagedBean(name = "creditFacExisting")
public class CreditFacExisting implements Serializable {
    @Inject
    @SELOS
    Logger log;
    @Inject
    @NormalMessage
    Message msg;
    //## session
    private long workCaseId;
    private long stepId;
    private int rowIndex;
    private String messageHeader;
    private String message;


    private ExistingConditionDetailView existingConditionDetailView;
    private List<ExistingConditionDetailView> existingConditionDetailViewList;
    private ExistingConditionDetailView selectConditionItem;

    ArrayList<ExistingCreditTypeDetailView> borrowerCreditTypeDetailList;
    ArrayList<ExistingCreditTypeDetailView> relatedCreditTypeDetailList;

    private User user;
    private ExistingCreditFacilityView existingCreditFacilityView;

    private ExistingCreditTierDetailView existingCreditTierDetailView;
    private List<ExistingCreditTierDetailView> existingCreditTierDetailViewList;
    private ExistingSplitLineDetailView existingSplitLineDetailView;
    private List<ExistingSplitLineDetailView> existingSplitLineDetailViewList;
    private ProductProgramView existProductProgramView;
    private CreditTypeView existCreditTypeView;
    private AccountStatus existAccountStatus ;
    private List<BaseRate> baseRateList;
    private ExistingCreditDetailView  existingCreditDetailView;

    private List<ProductGroup> productGroupList;
    private List<PrdGroupToPrdProgram> prdGroupToPrdProgramList;
    private List<PrdProgramToCreditType> prdProgramToCreditTypeList;
    private ProductProgram productProgram;

    private List<ExistingCreditDetailView> existingCreditDetailViewList;
    private List<ExistingCreditDetailView> borrowerExistingCreditDetailViewList;
    private List<ExistingCreditDetailView> relatedExistingCreditDetailViewList;
    private ExistingCreditDetailView selectCreditDetail;


    private List<ProductProgramView> productProgramViewList;
    private List<CreditType> creditTypeList;
    private List<AccountStatus> accountStatusList;
    private List<BankAccountStatus> bankAccountStatusList;
    private boolean showSplitLine;
    private boolean cannotEditStandard = true;

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

    private List<CustomerInfoView> guarantorList;

    private int seqBorrower=0;
    private int seqRelated=0;
    private Hashtable hashBorrower;
    private Hashtable hashRelated;

    enum ModeForButton {ADD, EDIT}

    private ModeForButton modeForButton;
    private String typeOfList;
    private String typeOfListCollateral;

    private String currentDateDDMMYY;

    private boolean canSaveCreditDetail;
    private boolean canSaveBorrowerCol;
    private boolean canSaveRelatedCol;
    private boolean canSaveGarantor;

    @Inject
    private CreditTypeDAO creditTypeDAO;
    @Inject
    private ProductProgramDAO productProgramDAO;
    @Inject
    private AccountStatusDAO accountStatusDAO;
    @Inject
    private BankAccountStatusDAO bankAccountStatusDAO;
    @Inject
    BankAccountStatusTransform bankAccountStatusTransform;
    @Inject
    private PrdGroupToPrdProgramDAO prdGroupToPrdProgramDAO;        // find product program
    @Inject
    private PrdProgramToCreditTypeDAO prdProgramToCreditTypeDAO;    // find credit type
    @Inject
    private BaseRateDAO baseRateDAO;
    @Inject
    private ProductFormulaDAO productFormulaDAO;
    @Inject
    CustomerInfoControl customerInfoControl;
    @Inject
    private CollateralTypeDAO collateralTypeDAO;
    @Inject
    private PotentialCollateralDAO potentialCollateralDAO;
    @Inject
    private RelationDAO relationDAO;
    @Inject
    private MortgageTypeDAO mortgageTypeDAO;
    @Inject
    private CreditFacExistingControl creditFacExistingControl;
    @Inject
    private ExistingCreditControl existingCreditControl;
    @Inject
    private ProductTransform productTransform;
    @Inject
    private BaseRateTransform baseRateTransform;


    public CreditFacExisting(){

    }

    public void preRender() {
        HttpSession session = FacesUtil.getSession(false);
        log.info("info WorkCase : {}", session.getAttribute("workCaseId"));

        if( session.getAttribute("workCaseId") == null && session.getAttribute("workCaseId").equals("")){
            FacesUtil.redirect("/site/inbox.jsf");
        }

       /* session.setAttribute("stepId", 1006);*/
        user = (User) session.getAttribute("user");
        log.info("preRender ::: 1 ");

        session = FacesUtil.getSession(true);
        productProgramViewList = productTransform.transformToView(productProgramDAO.findAll());
        baseRateList = baseRateDAO.findAll();
        //*** Get Product Program List from Product Group ***//
        prdGroupToPrdProgramList = prdGroupToPrdProgramDAO.getListPrdProByPrdGroupForExistCredit();
        if(prdGroupToPrdProgramList == null){
            prdGroupToPrdProgramList = new ArrayList<PrdGroupToPrdProgram>();
        }

        creditTypeList = creditTypeDAO.findAll();
        accountStatusList = accountStatusDAO.findAll();
        log.info("preRender ::: 1.2 ");
        bankAccountStatusList = bankAccountStatusDAO.findByBankAccountType(2);
        log.info("preRender ::: 1.3 ");
        relationList = relationDAO.findAll();
        log.info("preRender ::: 1.4 ");
        collateralTypeList = collateralTypeDAO.findAll();
        log.info("preRender ::: 1.5 ");
        potentialCollateralList = potentialCollateralDAO.findAll();
        log.info("preRender ::: 1.6 ");
        mortgageTypeList = mortgageTypeDAO.findAll();
        log.info("preRender ::: 1.7 ");
        hashBorrower = new Hashtable<String,String>();
        hashRelated  = new Hashtable<String,String>();


        log.info("preRender ::: 2 ");
        if (session.getAttribute("workCaseId") != null) {
            log.info("session.getAttribute('workCaseId') != null");
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            log.info("preRender ::: 3.1 " + workCaseId );
            stepId = Long.parseLong(session.getAttribute("stepId").toString());
            log.info("preRender ::: 3.2 " + stepId);

            guarantorList = customerInfoControl.getGuarantorByWorkCase(workCaseId);

            log.info("guarantorList size :: {}", guarantorList.size());
            if (guarantorList == null) {
                guarantorList = new ArrayList<CustomerInfoView>();
                CustomerInfoView customer = new CustomerInfoView();
                customer.setId(63);
                customer.setFirstNameTh("พี่เทพ มวลมหาประชา");
                guarantorList.add(customer);
            }

            existingCreditDetailView = new ExistingCreditDetailView();

            existProductProgramView = new ProductProgramView();
            existCreditTypeView = new CreditTypeView();

            existingCreditDetailView.setExistAccountStatus(new BankAccountStatus());
            existingCreditDetailView.setExistProductProgramView(existProductProgramView);
            existingCreditDetailView.setExistCreditTypeView(existCreditTypeView);
            existingCreditDetailView.setAccountStatus(new BankAccountStatusView());

            existingSplitLineDetailView = new ExistingSplitLineDetailView();
            productProgram = new ProductProgram();
            existingSplitLineDetailView.setProductProgram(productProgram);

            existingCollateralDetailView = new ExistingCollateralDetailView();
            existingCollateralDetailView.setCollateralType(new CollateralType());
            existingCollateralDetailView.setPotentialCollateral(new PotentialCollateral());
            existingCollateralDetailView.setRelation(new Relation());
            existingCollateralDetailView.setMortgageType(new MortgageType());
            existingCollateralDetailView.setExistingCreditTypeDetailViewList(new ArrayList<ExistingCreditTypeDetailView>());

            existingCreditTierDetailView = new ExistingCreditTierDetailView();
            existingGuarantorDetailView = new ExistingGuarantorDetailView();
            existingGuarantorDetailView.setGuarantorName(new CustomerInfoView());


            existingCreditFacilityView = creditFacExistingControl.onFindExistingCreditFacility(workCaseId);

            if(existingCreditFacilityView==null){
                existingCreditFacilityView =  new ExistingCreditFacilityView();
                log.info("preRender ::: 3.5 ");

                log.info("preRender ::: 4 ");
                existingConditionDetailViewList = new ArrayList<ExistingConditionDetailView>();
                existingCreditDetailViewList = new ArrayList<ExistingCreditDetailView>();

                borrowerExistingCreditDetailViewList = new ArrayList<ExistingCreditDetailView>();
                relatedExistingCreditDetailViewList = new ArrayList<ExistingCreditDetailView>();

                borrowerExistingCollateralDetailViewList = new ArrayList<ExistingCollateralDetailView>() ;
                relatedExistingCollateralDetailViewList  = new ArrayList<ExistingCollateralDetailView>() ;

                log.info("preRender ::: 6 ");
                collateralCreditDetailViewList = new ArrayList<ExistingCreditDetailView>();

                borrowerExistingGuarantorDetailViewList = new ArrayList<ExistingGuarantorDetailView>();
                existingGuarantorDetailView.setExistingCreditTypeDetailViewList(new ArrayList<ExistingCreditTypeDetailView>());

                log.info("preRender ::: 7 ");

                log.info("preRender ::: 7.1 ");
                existingCreditFacilityView.setBorrowerComExistingCredit(borrowerExistingCreditDetailViewList);
                existingCreditFacilityView.setBorrowerRetailExistingCredit(new ArrayList<ExistingCreditDetailView>());
                existingCreditFacilityView.setBorrowerAppInRLOSCredit(new ArrayList<ExistingCreditDetailView>());
                log.info("preRender ::: 7.2 ");
                existingCreditFacilityView.setExistingConditionDetailViewList(existingConditionDetailViewList);
                log.info("preRender ::: 7.3 ");
                existingCreditFacilityView.setRelatedComExistingCredit(relatedExistingCreditDetailViewList);
                existingCreditFacilityView.setRelatedRetailExistingCredit(new ArrayList<ExistingCreditDetailView>());
                existingCreditFacilityView.setRelatedAppInRLOSCredit(new ArrayList<ExistingCreditDetailView>());
                log.info("preRender ::: 7.4 ");
                existingCreditFacilityView.setBorrowerCollateralList(borrowerExistingCollateralDetailViewList);
                existingCreditFacilityView.setRelatedCollateralList(relatedExistingCollateralDetailViewList);
                log.info("preRender ::: 7.5 ");
                existingCreditFacilityView.setBorrowerGuarantorList(borrowerExistingGuarantorDetailViewList);



            }else{
                log.info("preRender ::: if(existingCreditFacilityView != null)");

                if(existingCreditFacilityView.getBorrowerComExistingCredit()!=null && existingCreditFacilityView.getBorrowerComExistingCredit().size()>0){
                    for(int i = 0; i < existingCreditFacilityView.getBorrowerComExistingCredit().size(); i++) {
                        hashBorrower.put(existingCreditFacilityView.getBorrowerComExistingCredit().get(i).getNo(), existingCreditFacilityView.getBorrowerComExistingCredit().get(i).getInUsed());
                    }
                    for (int l=0;l<hashBorrower.size();l++){
                        log.info("hashBorrower.get(j) in use   :  "+ l + " is   " +hashBorrower.get(l+1).toString());
                    }
                }

                if(existingCreditFacilityView.getRelatedComExistingCredit()!=null && existingCreditFacilityView.getRelatedComExistingCredit().size()>0){
                    for (int i = 0; i < existingCreditFacilityView.getRelatedComExistingCredit().size(); i++) {
                        hashRelated.put(existingCreditFacilityView.getRelatedComExistingCredit().get(i).getNo(), existingCreditFacilityView.getRelatedComExistingCredit().get(i).getInUsed());
                    }
                    for (int l=0;l<hashRelated.size();l++){
                        log.info("hashRelated.get(j) in use   :  "+ l + " is   " +hashRelated.get(l+1).toString());
                    }
                }


                log.info("preRender ::: getId " + existingCreditFacilityView.getId());

                if(existingCreditFacilityView.getBorrowerComExistingCredit() == null ){
                    borrowerExistingCreditDetailViewList    = new ArrayList<ExistingCreditDetailView>();
                    existingCreditFacilityView.setBorrowerComExistingCredit(borrowerExistingCreditDetailViewList);
                }else{
                    borrowerExistingCreditDetailViewList = existingCreditFacilityView.getBorrowerComExistingCredit();
                }
                if(existingCreditFacilityView.getBorrowerRetailExistingCredit() == null ){
                    existingCreditFacilityView.setBorrowerRetailExistingCredit(new ArrayList<ExistingCreditDetailView>());
                }
                if(existingCreditFacilityView.getBorrowerAppInRLOSCredit() == null ){
                    existingCreditFacilityView.setBorrowerAppInRLOSCredit(new ArrayList<ExistingCreditDetailView>());
                }
                if(existingCreditFacilityView.getExistingConditionDetailViewList() == null ){
                    existingConditionDetailViewList = new ArrayList<ExistingConditionDetailView>();
                    existingCreditFacilityView.setExistingConditionDetailViewList(existingConditionDetailViewList );
                }else{
                    existingConditionDetailViewList = existingCreditFacilityView.getExistingConditionDetailViewList();
                }

                if(existingCreditFacilityView.getRelatedComExistingCredit() == null ){
                    relatedExistingCreditDetailViewList = new ArrayList<ExistingCreditDetailView>();
                    existingCreditFacilityView.setRelatedComExistingCredit(relatedExistingCreditDetailViewList);
                }else{
                    relatedExistingCreditDetailViewList = existingCreditFacilityView.getRelatedComExistingCredit();
                }

                if(existingCreditFacilityView.getRelatedRetailExistingCredit() == null ){
                    existingCreditFacilityView.setRelatedRetailExistingCredit(new ArrayList<ExistingCreditDetailView>());
                }
                if(existingCreditFacilityView.getRelatedAppInRLOSCredit() == null ){
                    existingCreditFacilityView.setRelatedAppInRLOSCredit(new ArrayList<ExistingCreditDetailView>());
                }

                if(existingCreditFacilityView.getBorrowerCollateralList() == null ){
                    borrowerExistingCollateralDetailViewList = new ArrayList<ExistingCollateralDetailView>();
                    existingCreditFacilityView.setBorrowerCollateralList(borrowerExistingCollateralDetailViewList);
                }else{
                    borrowerExistingCollateralDetailViewList = existingCreditFacilityView.getBorrowerCollateralList();
                }

                if(existingCreditFacilityView.getRelatedCollateralList() == null ){
                    relatedExistingCollateralDetailViewList = new ArrayList<ExistingCollateralDetailView>();
                    existingCreditFacilityView.setRelatedCollateralList(relatedExistingCollateralDetailViewList);
                }else{
                    relatedExistingCollateralDetailViewList = existingCreditFacilityView.getRelatedCollateralList();
                }

                if(existingCreditFacilityView.getBorrowerGuarantorList() == null ){
                    borrowerExistingGuarantorDetailViewList =   new ArrayList<ExistingGuarantorDetailView>();
                    existingCreditFacilityView.setBorrowerGuarantorList(borrowerExistingGuarantorDetailViewList);
                }else{
                    borrowerExistingGuarantorDetailViewList = existingCreditFacilityView.getBorrowerGuarantorList();
                }
                /*
                log.info("preRender ::: getBorrowerComExistingCredit " + existingCreditFacilityView.getBorrowerComExistingCredit().size());
                log.info("preRender ::: getBorrowerRetailExistingCredit " + existingCreditFacilityView.getBorrowerRetailExistingCredit().size());
                log.info("preRender ::: getBorrowerAppInRLOSCredit "+  existingCreditFacilityView.getBorrowerAppInRLOSCredit().size()) ;

                log.info("preRender ::: getExistingConditionDetailViewList " + existingCreditFacilityView.getExistingConditionDetailViewList().size());

                log.info("preRender ::: getRelatedComExistingCredit " + existingCreditFacilityView.getRelatedComExistingCredit().size());
                log.info("preRender ::: getRelatedRetailExistingCredit " + existingCreditFacilityView.getRelatedRetailExistingCredit().size());
                log.info("preRender ::: getRelatedAppInRLOSCredit "+  existingCreditFacilityView.getRelatedAppInRLOSCredit().size()) ;

                log.info("preRender ::: getBorrowerCollateralList " + existingCreditFacilityView.getBorrowerCollateralList().size());
                log.info("preRender ::: getRelatedCollateralList " + existingCreditFacilityView.getRelatedCollateralList().size());

                log.info("preRender ::: getBorrowerGuarantorList "+  existingCreditFacilityView.getBorrowerGuarantorList().size()) ;*/
            }

            log.info("preRender ::: end ");
        } else {
            log.info("preRender ::: workCaseId is null.");
            try {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath() + "/site/inbox.jsf");
                return;
            } catch (Exception ex) {
                log.info("Exception :: {}", ex);
            }
        }
        log.info("preRender ::: x ");
    }

    @PostConstruct
    public void onCreation() {
        log.info("onCreation begin");
        try{
            preRender();
        }catch (Exception e){
            log.info("onCreation catch " + e.getMessage());
        }
        log.info("onCreation end");

    }


    public void onChangeProductProgram(){
        log.info("onChangeProductProgram :::: productProgram : {}"+ existingCreditDetailView.getExistProductProgramView().getId());
        ProductProgram productProgram = productProgramDAO.findById(existingCreditDetailView.getExistProductProgramView().getId());
        //ProductProgramView productProgramView = productTransform.transformToView(productProgram);
        log.info("onChangeProductProgram :::: productProgram : {}", productProgram);

        prdProgramToCreditTypeList = prdProgramToCreditTypeDAO.getListPrdProByPrdprogram(productProgram);
        if(prdProgramToCreditTypeList == null){
            prdProgramToCreditTypeList = new ArrayList<PrdProgramToCreditType>();
        }
        log.info("onChangeProductProgram :::: prdProgramToCreditTypeList.size ::: " +prdProgramToCreditTypeList.size());
    }

    public void onChangeCreditType(){
        int id = existingCreditDetailView.getExistCreditTypeView().getId();
        if ((existingCreditDetailView.getExistProductProgramView().getId() != 0) && (existingCreditDetailView.getExistCreditTypeView().getId() != 0)) {
            canSaveCreditDetail = false;
            ProductProgram productProgram = productProgramDAO.findById(existingCreditDetailView.getExistProductProgramView().getId());
            CreditType creditType = creditTypeDAO.findById(existingCreditDetailView.getExistCreditTypeView().getId());

            showSplitLine = Util.isTrue(creditType.getCanSplit());

            if(productProgram != null && creditType != null){
                PrdProgramToCreditType prdProgramToCreditType = prdProgramToCreditTypeDAO.getPrdProgramToCreditType(creditType,productProgram);
                ProductFormula productFormula = productFormulaDAO.findProductFormula(prdProgramToCreditType);
                log.debug("onChangeCreditType :::: productFormula : {}", productFormula.getId());

                if(productFormula != null){
                    existingCreditDetailView.setProductCode(productFormula.getProductCode());
                    existingCreditDetailView.setProjectCode(productFormula.getProjectCode());
                }
            }

            if(Util.isTrue(creditType.getCanSplit())){
                BigDecimal totalLimit = existingCreditDetailView.getLimit();
                BigDecimal splitLimit = BigDecimal.ZERO;
                if(existingSplitLineDetailViewList!=null && existingSplitLineDetailViewList.size()>0){
                    for(ExistingSplitLineDetailView existingSplitLineDetailViewTmp : existingSplitLineDetailViewList){
                        if(existingSplitLineDetailViewTmp.getLimit()!=null){
                            splitLimit = splitLimit.add(existingSplitLineDetailViewTmp.getLimit());
                        }
                    }
                    if(totalLimit.compareTo(splitLimit)==0){
                        canSaveCreditDetail = true;
                    }
                } else {
                    canSaveCreditDetail = true;
                }
            } else {
                canSaveCreditDetail = true;
            }
        }
    }

    //Start Condition Information //
    public void onAddCommercialCredit() {
        log.info("onAddCommercialCredit ::: ");
        existingCreditDetailView = new ExistingCreditDetailView();
        existAccountStatus = new AccountStatus();
        existProductProgramView = new ProductProgramView();
        existCreditTypeView = new CreditTypeView();

        existingCreditDetailView.setAccountStatus(new BankAccountStatusView());
        existingCreditDetailView.setExistAccountStatus(new BankAccountStatus());
        existingCreditDetailView.setExistProductProgramView(existProductProgramView);
        existingCreditDetailView.setExistCreditTypeView(existCreditTypeView);

        existingCreditTierDetailView = new ExistingCreditTierDetailView();
        existingCreditTierDetailViewList = new ArrayList<ExistingCreditTierDetailView>();

        existingSplitLineDetailView = new ExistingSplitLineDetailView();
        productProgram = new ProductProgram();
        existingSplitLineDetailView.setProductProgram(productProgram);
        existingSplitLineDetailViewList = new ArrayList<ExistingSplitLineDetailView>();
        showSplitLine = false;
        modeForButton = ModeForButton.ADD;
        log.info("onAddCommercialCredit ::: modeForButton : {}", modeForButton);
    }

    public void onEditCommercialCredit() {
        log.info("onEditCommercialCredit ::: mode : {} typeOfList " + typeOfList);
        log.info("onEditCommercialCredit rowIndex   " + rowIndex);
        log.info("onEditCommercialCredit selectCreditDetail null is " + (selectCreditDetail==null));
        if(selectCreditDetail != null){
           // log.info("onEditCommercialCredit is " + selectCreditDetail.toString());
        }

        ExistingCreditDetailView existingCreditDetailViewTmp = new ExistingCreditDetailView();
        if(typeOfList.equals("borrower")){
            existingCreditDetailViewTmp = existingCreditFacilityView.getBorrowerComExistingCredit().get(rowIndex);
        } else if(typeOfList.equals("related")){
            existingCreditDetailViewTmp = existingCreditFacilityView.getRelatedComExistingCredit().get(rowIndex);
        }
        modeForButton = ModeForButton.EDIT;
        canSaveCreditDetail = false;

        existingCreditDetailView = new ExistingCreditDetailView();
        Cloner cloner = new Cloner();
        existingCreditDetailView = cloner.deepClone(existingCreditDetailViewTmp);
        existingSplitLineDetailViewList= existingCreditDetailView.getExistingSplitLineDetailViewList();
        existingCreditTierDetailViewList = existingCreditDetailView.getExistingCreditTierDetailViewList();

        onChangeProductProgram();

        CreditType creditType = creditTypeDAO.findById(existingCreditDetailView.getExistCreditTypeView().getId());
        if(Util.isTrue(creditType.getCanSplit())){
            showSplitLine = true;
            BigDecimal totalLimit = existingCreditDetailView.getLimit();
            BigDecimal splitLimit = BigDecimal.ZERO;
            if(existingSplitLineDetailViewList!=null && existingSplitLineDetailViewList.size()>0){
                for(ExistingSplitLineDetailView existingSplitLineDetailViewTmp : existingSplitLineDetailViewList){
                    if(existingSplitLineDetailViewTmp.getLimit()!=null){
                        splitLimit = splitLimit.add(existingSplitLineDetailViewTmp.getLimit());
                    }
                }
                if(totalLimit.compareTo(splitLimit)==0){
                    canSaveCreditDetail = true;
                }
            } else {
                canSaveCreditDetail = true;
            }
        } else {
            canSaveCreditDetail = true;
            showSplitLine = false;
        }

        log.info("onEditCommercialCredit end ::: mode : {}", modeForButton);
    }

    public void onDeleteCommercialCredit() {
        log.info("onDeleteCommercialCredit ::: mode : {} typeOfList " + typeOfList);
        log.info("onDeleteCommercialCredit rowIndex   " + rowIndex);
        log.info("onDeleteCommercialCredit selectCreditDetail null is " + (selectCreditDetail==null));
        if(selectCreditDetail != null){
            //log.info("selectGuarantorDetail is " + selectCreditDetail.toString());
        }
        ExistingCreditDetailView existingCreditDetailViewDel = selectCreditDetail;
        int used;
        log.info("onDeleteCommercialCredit ::: mode : {} existingCreditDetailViewRow " + existingCreditDetailViewDel.toString());
        if(typeOfList.equals("borrower")){
            log.info("del 1");

            for (int l=0;l<hashBorrower.size();l++){
                log.info("hashBorrower.get(j) in use   :  "+ l + " is   " +hashBorrower.get(l+1).toString());
            }

            used = Integer.parseInt(hashBorrower.get(existingCreditDetailViewDel.getNo()).toString());

            log.info("before del use is  " +  used);
             if(used ==0){
                 log.info("use 0 ");
                 existingCreditFacilityView.getBorrowerComExistingCredit().remove(existingCreditDetailViewDel);
             }else{
                 log.info("use > 0 ");
                 messageHeader = msg.get("app.header.error");
                 message = msg.get("app.credit.facility.message.err.credit.inused");
                 RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
             }
            onSetRowNoCreditDetail(existingCreditFacilityView.getBorrowerComExistingCredit());
            calTotalCreditBorrower();
        }else if(typeOfList.equals("related")){
            log.info("del 1");

            for (int l=0;l<hashRelated.size();l++){
                log.info("hashRelated.get(j) in use   :  "+ l + " is   " +hashRelated.get(l+1).toString());
            }

            used = Integer.parseInt(hashRelated.get(existingCreditDetailViewDel.getNo()).toString());

            log.info("before del use is  " +  used);
            if(used ==0){
                log.info("use 0 ");
                existingCreditFacilityView.getRelatedComExistingCredit().remove(existingCreditDetailViewDel);
            }else{
                log.info("use > 0 ");
                messageHeader = msg.get("app.header.error");
                message = msg.get("app.credit.facility.message.err.credit.inused");
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            }
            onSetRowNoCreditDetail(existingCreditFacilityView.getRelatedComExistingCredit());
            calTotalCreditRelated();
        }
    }

    public void onSetRowNoCreditDetail(List<ExistingCreditDetailView> ExistingCreditDetailViewListSetRow){
        ExistingCreditDetailView existingCreditDetailViewTemp = new ExistingCreditDetailView();
        for(int i=0;i<ExistingCreditDetailViewListSetRow.size();i++){
            existingCreditDetailViewTemp = ExistingCreditDetailViewListSetRow.get(i);
            existingCreditDetailViewTemp.setNo(i+1);
        }
    }
    public void onSetRowNoCreditTierDetail(List<ExistingCreditTierDetailView> existingCreditTierDetailViewSetRow ){
        ExistingCreditTierDetailView existingCreditTierDetailViewTemp = new ExistingCreditTierDetailView();
        for(int i=0;i<existingCreditTierDetailViewSetRow.size();i++){
            existingCreditTierDetailViewTemp = existingCreditTierDetailViewSetRow.get(i);
            existingCreditTierDetailViewTemp.setNo(i+1);
        }
    }

    public void onSetRowNoSplitLineDetail(List<ExistingSplitLineDetailView> existingSplitLineDetailViewSetRow ){
        ExistingSplitLineDetailView existingSplitLineDetailViewTemp = new ExistingSplitLineDetailView();
        for(int i=0;i<existingSplitLineDetailViewSetRow.size();i++){
            existingSplitLineDetailViewTemp = existingSplitLineDetailViewSetRow.get(i);
            existingSplitLineDetailViewTemp.setNo(i+1);
        }
    }

    public void onSetRowNoConditionDetail(List<ExistingConditionDetailView> existingConditionDetailViewListSetRow){
        ExistingConditionDetailView existingConditionDetailViewTemp = new ExistingConditionDetailView();
        for(int i=0;i<existingConditionDetailViewListSetRow.size();i++){
            existingConditionDetailViewTemp = existingConditionDetailViewListSetRow.get(i);
            existingConditionDetailViewTemp.setNo(i+1);
        }
    }
    
    public void onSetRowNoGuarantorDetail(List<ExistingGuarantorDetailView> existingGuarantorDetailViewListSetRow){
        ExistingGuarantorDetailView existingGuarantorDetailViewTemp = new ExistingGuarantorDetailView();
        for(int i=0;i<existingGuarantorDetailViewListSetRow.size();i++){
            existingGuarantorDetailViewTemp = existingGuarantorDetailViewListSetRow.get(i);
            existingGuarantorDetailViewTemp.setNo(i+1);
        }
    }

    public void onSetRowNoCreditTypeDetail(List<ExistingCreditTypeDetailView> existingCreditTypeDetailViewSetRow ){
        ExistingCreditTypeDetailView existingCreditTypeDetailViewTemp = new ExistingCreditTypeDetailView();
        for(int i=0;i<existingCreditTypeDetailViewSetRow.size();i++){
            existingCreditTypeDetailViewTemp = existingCreditTypeDetailViewSetRow.get(i);
            existingCreditTypeDetailViewTemp.setNo(i+1);
        }
    }

    public void checkTotalLimit(){
        canSaveCreditDetail = false;
        CreditType creditType = creditTypeDAO.findById(existingCreditDetailView.getExistCreditTypeView().getId());
        if(Util.isTrue(creditType.getCanSplit())){
            BigDecimal totalLimit = existingCreditDetailView.getLimit();
            BigDecimal splitLimit = BigDecimal.ZERO;
            if(existingSplitLineDetailViewList!=null && existingSplitLineDetailViewList.size()>0){
                for(ExistingSplitLineDetailView existingSplitLineDetailViewTmp : existingSplitLineDetailViewList){
                    if(existingSplitLineDetailViewTmp.getLimit()!=null){
                        splitLimit = splitLimit.add(existingSplitLineDetailViewTmp.getLimit());
                    }
                }
                if(totalLimit.compareTo(splitLimit)==0){
                    canSaveCreditDetail = true;
                }
            } else {
                canSaveCreditDetail = true;
            }
        } else {
            canSaveCreditDetail = true;
        }
    }

    public void onSaveCreditDetail() {
        log.info("onSaveCreditDetail ::: mode : {}", modeForButton);
        log.info("onSaveCreditDetail ::: rowIndex : {}", rowIndex);
        log.info("onSaveCreditDetail selectCreditDetail null is " + (selectCreditDetail==null));
        if(selectCreditDetail != null){
            //log.info("selectCreditDetail is " + selectCreditDetail.toString());
        }

        boolean complete;
        RequestContext context = RequestContext.getCurrentInstance();

        log.info("typeOfList ::: "+ typeOfList);
        if(modeForButton != null && modeForButton.equals(ModeForButton.ADD)){
            log.info("add to list begin");
            //ProductProgram  productProgram = productProgramDAO.findById(existingCreditDetailView.getExistProductProgramView().getId());
            CreditType creditType = creditTypeDAO.findById(existingCreditDetailView.getExistCreditTypeView().getId());
            //AccountStatus accountStatus = accountStatusDAO.findById( existingCreditDetailView.getExistAccountStatus().getId());
            BankAccountStatus bankAccountStatus = bankAccountStatusDAO.findById( existingCreditDetailView.getExistAccountStatus().getId());

            BankAccountStatusView bankAccountStatusV = bankAccountStatusTransform.getBankAccountStatusView(bankAccountStatus);
            existingCreditDetailView.setExistProductProgramView(productTransform.transformToView(productProgramDAO.findById(existingCreditDetailView.getExistProductProgramView().getId())));
            existingCreditDetailView.setExistCreditTypeView(productTransform.transformToView(creditType));
            existingCreditDetailView.setExistAccountStatus(bankAccountStatus);

            if(Util.isTrue(creditType.getCanSplit())){
                for(int i=0;i<existingSplitLineDetailViewList.size();i++){
                    productProgram = productProgramDAO.findById(existingSplitLineDetailViewList.get(i).getProductProgram().getId());
                    existingSplitLineDetailViewList.get(i).setProductProgram(productProgram);
                }
                existingCreditDetailView.setExistingSplitLineDetailViewList(existingSplitLineDetailViewList);
            } else {
                existingCreditDetailView.setExistingSplitLineDetailViewList(new ArrayList<ExistingSplitLineDetailView>());
            }

            for(int i=0;i<existingCreditTierDetailViewList.size();i++){
                BaseRate baseRate = baseRateDAO.findById(existingCreditTierDetailViewList.get(i).getFinalBasePrice().getId());
                BaseRateView baseRateView = baseRateTransform.transformToView(baseRate);
                existingCreditTierDetailViewList.get(i).setFinalBasePrice(baseRateView);
            }

            existingCreditDetailView.setExistingCreditTierDetailViewList(existingCreditTierDetailViewList);

            int index = 0;
            if(typeOfList.equals("borrower")){
                if(existingCreditFacilityView.getBorrowerComExistingCredit()!=null){
                    if(existingCreditFacilityView.getBorrowerComExistingCredit().size()>0) {
                        index = existingCreditFacilityView.getBorrowerComExistingCredit().size();
                    }
                } else {
                    existingCreditFacilityView.setBorrowerComExistingCredit(new ArrayList<ExistingCreditDetailView>());
                }

                existingCreditDetailView.setNo(index+1);
                existingCreditDetailView.setBorrowerType(RelationValue.BORROWER.value());
                existingCreditDetailView.setCreditCategory(CreditCategory.COMMERCIAL);
                existingCreditDetailView.setExistingCreditFrom(1);
                existingCreditDetailView.setSeq(seqBorrower);
                hashBorrower.put(index+1, 0);
                seqBorrower++;
                existingCreditFacilityView.getBorrowerComExistingCredit().add(existingCreditDetailView);
                //borrowerExistingCreditDetailViewList.add(existingCreditDetailView);

                for (int l=0;l<hashBorrower.size();l++){
                    log.info("hashBorrower.get(j) in use   :  "+ l + " is   " +hashBorrower.get(l+1).toString());
                }
            }else if(typeOfList.equals("related")){
                if(existingCreditFacilityView.getRelatedComExistingCredit()!=null){
                    if(existingCreditFacilityView.getRelatedComExistingCredit().size()>0) {
                        index = existingCreditFacilityView.getRelatedComExistingCredit().size();
                    }
                } else {
                    existingCreditFacilityView.setRelatedComExistingCredit(new ArrayList<ExistingCreditDetailView>());
                }

                existingCreditDetailView.setNo(index+1);
                existingCreditDetailView.setBorrowerType(RelationValue.DIRECTLY_RELATED.value());
                existingCreditDetailView.setCreditCategory(CreditCategory.COMMERCIAL);
                existingCreditDetailView.setExistingCreditFrom(1);
                existingCreditDetailView.setSeq(seqRelated);
                hashRelated.put(index+1,0);
                seqRelated++;
                existingCreditFacilityView.getRelatedComExistingCredit().add(existingCreditDetailView);
                //relatedExistingCreditDetailViewList.add(existingCreditDetailView);

            }
            log.info("add to list end");
        }else if(modeForButton != null && modeForButton.equals(ModeForButton.EDIT)){
            log.info("update list begin");
            ExistingCreditDetailView  existingCreditDetailViewRow = new ExistingCreditDetailView();
            if(typeOfList.equals("borrower")){
                existingCreditDetailViewRow = existingCreditFacilityView.getBorrowerComExistingCredit().get(rowIndex);
            }else if(typeOfList.equals("related")){
                existingCreditDetailViewRow = existingCreditFacilityView.getRelatedComExistingCredit().get(rowIndex);
            }

            //ProductProgram  productProgram = productProgramDAO.findById(existingCreditDetailView.getExistProductProgram().getId());
            //AccountStatus accountStatus = accountStatusDAO.findById( existingCreditDetailView.getExistAccountStatus().getId());
            BankAccountStatus bankAccountStatus = bankAccountStatusDAO.findById(existingCreditDetailView.getExistAccountStatus().getId());
            BankAccountStatusView bankAccountStatusV = bankAccountStatusTransform.getBankAccountStatusView(bankAccountStatus);
            CreditType creditType = creditTypeDAO.findById(existingCreditDetailView.getExistCreditTypeView().getId());

            existingCreditDetailViewRow.setExistProductProgramView(productTransform.transformToView(productProgramDAO.findById(existingCreditDetailView.getExistProductProgramView().getId())));
            existingCreditDetailViewRow.setExistCreditTypeView(productTransform.transformToView(creditType));
            existingCreditDetailViewRow.setAccountStatus(bankAccountStatusV);
            existingCreditDetailViewRow.setAccountName(existingCreditDetailView.getAccountName());
            existingCreditDetailViewRow.setAccountNumber(existingCreditDetailView.getAccountNumber());
            existingCreditDetailViewRow.setAccountSuf(existingCreditDetailView.getAccountSuf());
            existingCreditDetailViewRow.setExistAccountStatus(bankAccountStatus);

            existingCreditDetailViewRow.setLimit(existingCreditDetailView.getLimit());
            existingCreditDetailViewRow.setProductCode(existingCreditDetailView.getProductCode());
            existingCreditDetailViewRow.setProjectCode(existingCreditDetailView.getProjectCode());
            existingCreditDetailViewRow.setOutstanding(existingCreditDetailView.getOutstanding());
            existingCreditDetailViewRow.setPcePercent(existingCreditDetailView.getPcePercent());
            existingCreditDetailViewRow.setPceLimit(existingCreditDetailView.getPceLimit());

            if(Util.isTrue(creditType.getCanSplit())){
                for(int i=0;i<existingSplitLineDetailViewList.size();i++){
                    productProgram = productProgramDAO.findById(existingSplitLineDetailViewList.get(i).getProductProgram().getId());
                    existingSplitLineDetailViewList.get(i).setProductProgram(productProgram);
                }
                existingCreditDetailViewRow.setExistingSplitLineDetailViewList(existingSplitLineDetailViewList);
            } else {
                existingCreditDetailViewRow.setExistingSplitLineDetailViewList(new ArrayList<ExistingSplitLineDetailView>());
            }

            for(int i=0;i<existingCreditTierDetailViewList.size();i++){
                BaseRate baseRate = baseRateDAO.findById(existingCreditTierDetailViewList.get(i).getFinalBasePrice().getId());
                BaseRateView baseRateView = baseRateTransform.transformToView(baseRate);
                existingCreditTierDetailViewList.get(i).setFinalBasePrice(baseRateView);
            }
            existingCreditDetailViewRow.setExistingCreditTierDetailViewList(existingCreditTierDetailViewList);

            if(typeOfList.equals("borrower")){
                existingCreditFacilityView.getBorrowerComExistingCredit().remove(rowIndex);
                existingCreditFacilityView.getBorrowerComExistingCredit().add(rowIndex,existingCreditDetailViewRow);
            }else if(typeOfList.equals("related")){
                existingCreditFacilityView.getRelatedComExistingCredit().remove(rowIndex);
                existingCreditFacilityView.getRelatedComExistingCredit().add(rowIndex,existingCreditDetailViewRow);
            }

            log.info("update list end");
        }else {
            log.info("onSaveCreditDetail ::: Undefined modeForButton !!");
        }

        if(typeOfList.equals("borrower")){
            calTotalCreditBorrower();
        }else if(typeOfList.equals("related")){
            calTotalCreditRelated();
        }

        complete = true;

        log.info("  complete >>>>  :  {}", complete);
        context.addCallbackParam("functionComplete", complete);

    }

    //  Start Tier Dialog //
    public void onAddCreditTierDetailView() {
        log.info("onAddCreditTierDetailView ::: ");
        existingCreditTierDetailView = new ExistingCreditTierDetailView();
        existingCreditTierDetailView.setFinalBasePrice(new BaseRateView());
        existingCreditTierDetailView.setNo(existingCreditTierDetailViewList.size()+1);
        existingCreditTierDetailViewList.add(existingCreditTierDetailView);
    }

    public void onDeleteCreditTierDetailView(int rowOnTable) {
        if(Integer.toString(rowOnTable) != null ){
            log.info("onDeleteCreditTierDetailView ::: rowOnTable " + rowOnTable);
            existingCreditTierDetailViewList.remove(rowOnTable);
            onSetRowNoCreditTierDetail(existingCreditTierDetailViewList);
        }
    }

    //  Start SplitLine Dialog //
    public void onAddExistingSplitLineDetailView() {
        log.info("onAddCreditDetail ::: begin");
        existingSplitLineDetailView = new ExistingSplitLineDetailView();
        productProgram = new ProductProgram();
        existingSplitLineDetailView.setProductProgram(productProgram);
        existingSplitLineDetailView.setNo(existingSplitLineDetailViewList.size()+1);
        existingSplitLineDetailViewList.add(existingSplitLineDetailView);

        canSaveCreditDetail = false;
    }

    public void onDeleteExistingSplitLineDetailView(int rowOnTable) {
        if(Integer.toString(rowOnTable) != null ){
            log.info("existingSplitLineDetailViewList ::: rowOnTable " + rowOnTable);
            existingSplitLineDetailViewList.remove(rowOnTable);
            onSetRowNoSplitLineDetail(existingSplitLineDetailViewList);

            BigDecimal totalLimit = existingCreditDetailView.getLimit();
            BigDecimal splitLimit = BigDecimal.ZERO;
            if(existingSplitLineDetailViewList!=null && existingSplitLineDetailViewList.size()>0){
                for(ExistingSplitLineDetailView existingSplitLineDetailViewTmp : existingSplitLineDetailViewList){
                    if(existingSplitLineDetailViewTmp.getLimit()!=null){
                        splitLimit = splitLimit.add(existingSplitLineDetailViewTmp.getLimit());
                    }
                }
                if(totalLimit.compareTo(splitLimit)==0){
                    canSaveCreditDetail = true;
                }
            } else {
                canSaveCreditDetail = true;
            }
            log.info("onAddCreditDetail ::: end");
        }
    }

    private void calTotalCreditBorrower(){
        double totalBorrowerCom =0;
        double totalBorrowerRetail =0;
        double totalBorrowerRlos =0;
        double limitRow;

        if(existingCreditFacilityView.getBorrowerComExistingCredit() != null){
            for(int i=0;i< existingCreditFacilityView.getBorrowerComExistingCredit().size();i++){
                limitRow = existingCreditFacilityView.getBorrowerComExistingCredit().get(i).getLimit().doubleValue();
                totalBorrowerCom += limitRow;
            }
        }

        if(existingCreditFacilityView.getBorrowerRetailExistingCredit() != null){
            for(int i=0;i< existingCreditFacilityView.getBorrowerRetailExistingCredit().size();i++){
                limitRow = existingCreditFacilityView.getBorrowerRetailExistingCredit().get(i).getLimit().doubleValue();
                totalBorrowerRetail += limitRow;
            }
        }

        if(existingCreditFacilityView.getBorrowerAppInRLOSCredit() != null){
            for(int i=0;i< existingCreditFacilityView.getBorrowerAppInRLOSCredit().size();i++){
                limitRow = existingCreditFacilityView.getBorrowerAppInRLOSCredit().get(i).getLimit().doubleValue();
                totalBorrowerRlos += limitRow;
            }
        }
        existingCreditFacilityView.setTotalBorrowerComLimit( new BigDecimal(totalBorrowerCom).setScale(2, RoundingMode.HALF_UP));
        existingCreditFacilityView.setTotalBorrowerRetailLimit( new BigDecimal(totalBorrowerRetail).setScale(2, RoundingMode.HALF_UP));
        existingCreditFacilityView.setTotalBorrowerAppInRLOSLimit( new BigDecimal(totalBorrowerRlos).setScale(2, RoundingMode.HALF_UP));
        calTotalCreditGroup();

    }

    private void calTotalCreditRelated(){
        double totalRelatedCom =0;
        double totalRelatedRetail =0;
        double totalRelatedRlos =0;
        double limitRow;

        if(existingCreditFacilityView.getRelatedComExistingCredit() != null){
            for(int i=0;i< existingCreditFacilityView.getRelatedComExistingCredit().size();i++){
                limitRow = existingCreditFacilityView.getRelatedComExistingCredit().get(i).getLimit().doubleValue();
                totalRelatedCom += limitRow;
            }
        }

        if(existingCreditFacilityView.getRelatedRetailExistingCredit() != null){
            for(int i=0;i< existingCreditFacilityView.getRelatedRetailExistingCredit().size();i++){
                limitRow = existingCreditFacilityView.getRelatedRetailExistingCredit().get(i).getLimit().doubleValue();
                totalRelatedRetail += limitRow;
            }
        }

        if(existingCreditFacilityView.getRelatedAppInRLOSCredit() != null){
            for(int i=0;i< existingCreditFacilityView.getRelatedAppInRLOSCredit().size();i++){
                limitRow = existingCreditFacilityView.getRelatedAppInRLOSCredit().get(i).getLimit().doubleValue();
                totalRelatedRlos += limitRow;
            }
        }
        existingCreditFacilityView.setTotalRelatedComLimit( new BigDecimal(totalRelatedCom).setScale(2, RoundingMode.HALF_UP));
        existingCreditFacilityView.setTotalRelatedRetailLimit( new BigDecimal(totalRelatedRetail).setScale(2, RoundingMode.HALF_UP));
        existingCreditFacilityView.setTotalRelatedAppInRLOSLimit( new BigDecimal(totalRelatedRlos).setScale(2, RoundingMode.HALF_UP));
        calTotalCreditGroup();

    }

    private void calTotalCreditGroup(){

        double totalGroupCom = 0;
        double totalGroupRetail = 0;
        double totalGroupRlos = 0;

        double totalBorrowerCom    = 0;
        double totalBorrowerRetail = 0;
        double totalBorrowerRlos   = 0;

        double totalRelatedCom    = 0;
        double totalRelatedRetail = 0;
        double totalRelatedRlos   = 0;

        if( existingCreditFacilityView.getTotalBorrowerComLimit() != null){
            totalBorrowerCom    = existingCreditFacilityView.getTotalBorrowerComLimit().doubleValue();
        }
        if( existingCreditFacilityView.getTotalBorrowerRetailLimit() != null){
            totalBorrowerRetail = existingCreditFacilityView.getTotalBorrowerRetailLimit().doubleValue();
        }
        if( existingCreditFacilityView.getTotalBorrowerAppInRLOSLimit() != null){
            totalBorrowerRlos    = existingCreditFacilityView.getTotalBorrowerAppInRLOSLimit().doubleValue();
        }

        if( existingCreditFacilityView.getTotalRelatedComLimit() != null){
            totalRelatedCom    = existingCreditFacilityView.getTotalRelatedComLimit().doubleValue();
        }
        if( existingCreditFacilityView.getTotalRelatedRetailLimit() != null){
            totalRelatedRetail = existingCreditFacilityView.getTotalRelatedRetailLimit().doubleValue();
        }
        if( existingCreditFacilityView.getTotalRelatedAppInRLOSLimit() != null){
            totalRelatedRlos    = existingCreditFacilityView.getTotalRelatedAppInRLOSLimit().doubleValue();
        }

        totalGroupCom    = totalBorrowerCom + totalRelatedCom;
        totalGroupRetail = totalBorrowerRetail + totalRelatedRetail;
        totalGroupRlos   = totalBorrowerRlos + totalRelatedRlos;

        existingCreditFacilityView.setTotalGroupCom(new BigDecimal(totalGroupCom).setScale(2, RoundingMode.HALF_UP));
        existingCreditFacilityView.setTotalGroupComOBOD(new BigDecimal(totalGroupRetail).setScale(2, RoundingMode.HALF_UP));
        existingCreditFacilityView.setTotalGroupExposure(new BigDecimal(totalGroupRlos).setScale(2, RoundingMode.HALF_UP));

    }


    public List<ExistingCreditTypeDetailView> findBorrowerCreditFacility() {
        log.info("findBorrowerCreditFacility ::: " );
        ExistingCreditTypeDetailView existingCreditTypeDetailView;
        borrowerCreditTypeDetailList = new ArrayList<ExistingCreditTypeDetailView>();  // find credit existing and propose in this workCase

        if (existingCreditFacilityView.getBorrowerComExistingCredit()!=null && existingCreditFacilityView.getBorrowerComExistingCredit().size() > 0) {
            for (int i = 0; i < existingCreditFacilityView.getBorrowerComExistingCredit().size(); i++) {
                existingCreditTypeDetailView = new ExistingCreditTypeDetailView();
                existingCreditTypeDetailView.setNoFlag(existingCreditFacilityView.getBorrowerComExistingCredit().get(i).isNoFlag());
                existingCreditTypeDetailView.setSeq(existingCreditFacilityView.getBorrowerComExistingCredit().get(i).getSeq());
                existingCreditTypeDetailView.setAccountName(existingCreditFacilityView.getBorrowerComExistingCredit().get(i).getAccountName());
                existingCreditTypeDetailView.setAccountNumber(existingCreditFacilityView.getBorrowerComExistingCredit().get(i).getAccountNumber());
                existingCreditTypeDetailView.setAccountSuf(existingCreditFacilityView.getBorrowerComExistingCredit().get(i).getAccountSuf());
                existingCreditTypeDetailView.setProductProgram(existingCreditFacilityView.getBorrowerComExistingCredit().get(i).getExistProductProgramView().getName());
                existingCreditTypeDetailView.setCreditFacility(existingCreditFacilityView.getBorrowerComExistingCredit().get(i).getExistCreditTypeView().getName());
                existingCreditTypeDetailView.setLimit(existingCreditFacilityView.getBorrowerComExistingCredit().get(i).getLimit());
                existingCreditTypeDetailView.setNo(existingCreditFacilityView.getBorrowerComExistingCredit().get(i).getNo());
                borrowerCreditTypeDetailList.add(existingCreditTypeDetailView);
            }
        }

        return borrowerCreditTypeDetailList;
    }

    public List<ExistingCreditTypeDetailView> findRelatedCreditFacility() {
        log.info("findRelatedCreditFacility ::: " );
        ExistingCreditTypeDetailView existingCreditTypeDetailView;
        relatedCreditTypeDetailList = new ArrayList<ExistingCreditTypeDetailView>();  // find credit existing and propose in this workCase

        if (existingCreditFacilityView.getRelatedComExistingCredit()!=null && existingCreditFacilityView.getRelatedComExistingCredit().size() > 0) {
            for (int i = 0; i < existingCreditFacilityView.getRelatedComExistingCredit().size(); i++) {
                existingCreditTypeDetailView = new ExistingCreditTypeDetailView();
                existingCreditTypeDetailView.setNoFlag(false);
                existingCreditTypeDetailView.setSeq(existingCreditFacilityView.getRelatedComExistingCredit().get(i).getSeq());
                existingCreditTypeDetailView.setAccountName(existingCreditFacilityView.getRelatedComExistingCredit().get(i).getAccountName());
                existingCreditTypeDetailView.setAccountNumber(existingCreditFacilityView.getRelatedComExistingCredit().get(i).getAccountNumber());
                existingCreditTypeDetailView.setAccountSuf(existingCreditFacilityView.getRelatedComExistingCredit().get(i).getAccountSuf());
                existingCreditTypeDetailView.setProductProgram(existingCreditFacilityView.getRelatedComExistingCredit().get(i).getExistProductProgramView().getName());
                existingCreditTypeDetailView.setCreditFacility(existingCreditFacilityView.getRelatedComExistingCredit().get(i).getExistCreditTypeView().getName());
                existingCreditTypeDetailView.setLimit(existingCreditFacilityView.getRelatedComExistingCredit().get(i).getLimit());
                existingCreditTypeDetailView.setNo(existingCreditFacilityView.getRelatedComExistingCredit().get(i).getNo());
                relatedCreditTypeDetailList.add(existingCreditTypeDetailView);
            }
        }

        return relatedCreditTypeDetailList;
    }

    //Start Condition Information //
    public void onAddConditionDetail() {
        log.info("onAddConditionDetail ::: ");
        existingConditionDetailView = new ExistingConditionDetailView();
        modeForButton = ModeForButton.ADD;
        log.info("onAddConditionDetail ::: modeForButton : {}", modeForButton);

    }

    public void onSaveConditionDetailDlg(){
        log.info("onSaveConditionDetailDlg ::: mode : {}", modeForButton);

        RequestContext context = RequestContext.getCurrentInstance();
        boolean complete = false;

        if(modeForButton != null && modeForButton.equals(ModeForButton.ADD)){
            if(existingCreditFacilityView.getExistingConditionDetailViewList()==null){
                existingCreditFacilityView.setExistingConditionDetailViewList(new ArrayList<ExistingConditionDetailView>());
            }

            ExistingConditionDetailView existingConditionDetailViewAdd = new ExistingConditionDetailView();
            existingConditionDetailViewAdd.setNo(existingCreditFacilityView.getExistingConditionDetailViewList().size()+1);
            existingConditionDetailViewAdd.setLoanType(existingConditionDetailView.getLoanType());
            existingConditionDetailViewAdd.setConditionDesc(existingConditionDetailView.getConditionDesc());
            existingConditionDetailViewList.add(existingConditionDetailViewAdd);
            existingCreditFacilityView.setExistingConditionDetailViewList(existingConditionDetailViewList);
            complete = true;

        } else {

            log.info("onSaveConditionDetailDlg ::: validation failed.");
            complete = false;
        }

        log.info("  complete >>>>  :  {}", complete);
        context.addCallbackParam("functionComplete", complete);
    }

    public void onDeleteConditionDetail() {
        log.info("onDeleteConditionDetail :: ");
        existingConditionDetailViewList.remove(selectConditionItem);
        existingCreditFacilityView.setExistingConditionDetailViewList(existingConditionDetailViewList);
        onSetRowNoConditionDetail(existingCreditFacilityView.getExistingConditionDetailViewList());
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

        log.info("typeOfListCollateral ::: " + typeOfListCollateral);
        if(typeOfListCollateral.equals("borrower")){
            canSaveBorrowerCol = false;
            existingCollateralDetailView.setExistingCreditTypeDetailViewList(findBorrowerCreditFacility());
            if(existingCollateralDetailView.getExistingCreditTypeDetailViewList()!=null && existingCollateralDetailView.getExistingCreditTypeDetailViewList().size()>0){
                canSaveBorrowerCol = true;
            }
        }else if(typeOfListCollateral.equals("related")){
            canSaveRelatedCol = false;
            existingCollateralDetailView.setExistingCreditTypeDetailViewList(findRelatedCreditFacility());
            if(existingCollateralDetailView.getExistingCreditTypeDetailViewList()!=null && existingCollateralDetailView.getExistingCreditTypeDetailViewList().size()>0){
                canSaveRelatedCol = true;
            }
        }


        modeForButton = ModeForButton.ADD;
        log.info("onAddCommercialCredit ::: modeForButton : {}", modeForButton);
    }

    public void onEditExistingCollateral() {
        log.info("onEditExistingCollateral ::: ");
        log.info("onEditExistingCollateral rowIndex   " + rowIndex);
        log.info("onEditExistingCollateral selectCollateralDetail null is " + (selectCollateralDetail==null));
        if(selectCollateralDetail != null){
            //log.info("selectCollateralDetail is " + selectCollateralDetail.toString());
        }
        log.info("onEditExistingCollateral 1 ");

        collateralCreditDetailViewList = new ArrayList<ExistingCreditDetailView>();

        Cloner cloner = new Cloner();
        existingCollateralDetailView = cloner.deepClone(selectCollateralDetail);
        existingCollateralDetailView.setCollateralType(selectCollateralDetail.getCollateralType());
        existingCollateralDetailView.setPotentialCollateral(selectCollateralDetail.getPotentialCollateral());
        existingCollateralDetailView.setRelation(selectCollateralDetail.getRelation());
        existingCollateralDetailView.setMortgageType(selectCollateralDetail.getMortgageType());
        existingCollateralDetailView.setAppraisalValue(selectCollateralDetail.getAppraisalValue());
        existingCollateralDetailView.setMortgageValue(selectCollateralDetail.getMortgageValue());
        existingCollateralDetailView.setOwner(selectCollateralDetail.getOwner());
        existingCollateralDetailView.setAccountNumber(selectCollateralDetail.getAccountNumber());
        existingCollateralDetailView.setCollateralLocation(selectCollateralDetail.getCollateralLocation());
        existingCollateralDetailView.setRemark(selectCollateralDetail.getRemark());
        existingCollateralDetailView.setAppraisalDate(selectCollateralDetail.getAppraisalDate());
        existingCollateralDetailView.setCollateralNumber(selectCollateralDetail.getCollateralNumber());

        log.info("onEditExistingCollateral 2 ");

        int tempSeq =0;
        List<ExistingCreditTypeDetailView> existingCreditTypeDetailList;
        if(typeOfListCollateral.equals("borrower")){
            log.info("onEditExistingCollateral 3 b ");
            existingCreditTypeDetailList = findBorrowerCreditFacility();
            existingCollateralDetailView.setExistingCreditTypeDetailViewList(existingCreditTypeDetailList);

            if(selectCollateralDetail.getExistingCreditTypeDetailViewList()!=null && selectCollateralDetail.getExistingCreditTypeDetailViewList().size()>0){
                for (int i = 0; i < selectCollateralDetail.getExistingCreditTypeDetailViewList().size(); i++) {
                    for (int j = tempSeq; j < existingCreditTypeDetailList.size(); j++) {
                        log.info("creditType at " + j + " seq is     " + existingCreditTypeDetailList.get(j).getNo());

                        if (selectCollateralDetail.getExistingCreditTypeDetailViewList().get(i).getNo() == existingCreditTypeDetailList.get(j).getNo()) {
                            existingCollateralDetailView.getExistingCreditTypeDetailViewList().get(j).setNoFlag(true);
                            tempSeq = j;
                        }
                        continue;
                    }
                }
            }

        }else  if(typeOfListCollateral.equals("related")){
            log.info("onEditExistingCollateral 3 r ");
            existingCreditTypeDetailList = findRelatedCreditFacility();
            existingCollateralDetailView.setExistingCreditTypeDetailViewList(existingCreditTypeDetailList);

            if(selectCollateralDetail.getExistingCreditTypeDetailViewList()!=null && selectCollateralDetail.getExistingCreditTypeDetailViewList().size()>0){
                for (int i = 0; i < selectCollateralDetail.getExistingCreditTypeDetailViewList().size(); i++) {
                    for (int j = tempSeq; j < existingCreditTypeDetailList.size(); j++) {
                        log.info("creditType at " + j + " seq is     " + existingCreditTypeDetailList.get(j).getNo());

                        if (selectCollateralDetail.getExistingCreditTypeDetailViewList().get(i).getNo() == existingCreditTypeDetailList.get(j).getNo()) {
                            existingCollateralDetailView.getExistingCreditTypeDetailViewList().get(j).setNoFlag(true);
                            tempSeq = j;
                        }
                        continue;
                    }
                }
            }

        }


        modeForButton = ModeForButton.EDIT;
        log.info("onAddCommercialCredit ::: modeForButton : {}", modeForButton);
    }

    public void onSaveExistingCollateral(){
        log.info("onSaveExistingCollateral ::: mode : {}", modeForButton);
        log.info("onSaveExistingCollateral selectCollateralDetail null is " + (selectCollateralDetail==null));

        boolean complete;
        boolean checkPlus;
        ExistingCreditDetailView existingCreditDetailViewNoFlag;
        RequestContext context = RequestContext.getCurrentInstance();
        log.info("typeOfListCollateral ::: "+ typeOfListCollateral);
        int seqBorrowerTemp;
        int seqRelatedTemp;

        CollateralType  collateralType = collateralTypeDAO.findById(existingCollateralDetailView.getCollateralType().getId());
        PotentialCollateral potentialCollateral = potentialCollateralDAO.findById( existingCollateralDetailView.getPotentialCollateral().getId());
        Relation relation = relationDAO.findById( existingCollateralDetailView.getRelation().getId());
        MortgageType mortgageType = mortgageTypeDAO.findById( existingCollateralDetailView.getMortgageType().getId());

        if(modeForButton != null && modeForButton.equals(ModeForButton.ADD)){
            ExistingCollateralDetailView  existingCollateralDetailViewAdd  = new ExistingCollateralDetailView();

            existingCollateralDetailViewAdd.setCollateralType(collateralType);
            existingCollateralDetailViewAdd.setPotentialCollateral(potentialCollateral);
            existingCollateralDetailViewAdd.setRelation(relation);
            existingCollateralDetailViewAdd.setMortgageType(mortgageType);
            existingCollateralDetailViewAdd.setExistingCreditTypeDetailViewList(new ArrayList<ExistingCreditTypeDetailView>());

            existingCollateralDetailViewAdd.setOwner(existingCollateralDetailView.getOwner());
            existingCollateralDetailViewAdd.setAccountNumber(existingCollateralDetailView.getAccountNumber());
            existingCollateralDetailViewAdd.setCollateralLocation(existingCollateralDetailView.getCollateralLocation());
            existingCollateralDetailViewAdd.setRemark(existingCollateralDetailView.getRemark());
            existingCollateralDetailViewAdd.setAppraisalDate(existingCollateralDetailView.getAppraisalDate());
            existingCollateralDetailViewAdd.setAppraisalValue(existingCollateralDetailView.getAppraisalValue());
            existingCollateralDetailViewAdd.setMortgageValue(existingCollateralDetailView.getMortgageValue());
            existingCollateralDetailViewAdd.setCollateralNumber(existingCollateralDetailView.getCollateralNumber());

            if(typeOfListCollateral.equals("borrower")){
                if(existingCreditFacilityView.getBorrowerCollateralList()==null){
                    existingCreditFacilityView.setBorrowerCollateralList(new ArrayList<ExistingCollateralDetailView>());
                }
                existingCollateralDetailViewAdd.setNo(existingCreditFacilityView.getBorrowerCollateralList().size()+1);
                existingCollateralDetailViewAdd.setBorrowerType(RelationValue.BORROWER.value());
                for (int l=0;l<hashBorrower.size();l++){
                    log.info("hashBorrower.get(j) in use   :  "+ l + " is   " +hashBorrower.get(l+1).toString());
                }
                for (ExistingCreditTypeDetailView existingCreditTypeDetail : existingCollateralDetailView.getExistingCreditTypeDetailViewList()) {
                    log.info("existingCreditTypeDetail.isNoFlag()  :: {}", existingCreditTypeDetail.isNoFlag());
                    if (existingCreditTypeDetail.isNoFlag()) {
                        existingCollateralDetailViewAdd.getExistingCreditTypeDetailViewList().add(existingCreditTypeDetail);
                        seqBorrowerTemp = existingCreditTypeDetail.getNo();
                        hashBorrower.put(seqBorrowerTemp, 1);
                    }
                }

                for (int l=0;l<hashBorrower.size();l++){
                    log.info("hashBorrower.get(j) in use   :  "+ l + " is   " +hashBorrower.get(l+1).toString());
                }
                existingCreditFacilityView.getBorrowerCollateralList().add(existingCollateralDetailViewAdd);
                //borrowerExistingCollateralDetailViewList.add(existingCollateralDetailViewAdd);
                onSetRowNoCreditTypeDetail(existingCollateralDetailViewAdd.getExistingCreditTypeDetailViewList());

            }else if(typeOfListCollateral.equals("related")){
                if(existingCreditFacilityView.getRelatedCollateralList()==null){
                    existingCreditFacilityView.setRelatedCollateralList(new ArrayList<ExistingCollateralDetailView>());
                }
                existingCollateralDetailViewAdd.setNo(existingCreditFacilityView.getRelatedCollateralList().size()+1);
                existingCollateralDetailViewAdd.setBorrowerType(RelationValue.DIRECTLY_RELATED.value());
                for (ExistingCreditTypeDetailView existingCreditTypeDetail : existingCollateralDetailView.getExistingCreditTypeDetailViewList()) {
                    log.info("existingCreditTypeDetail.isNoFlag()  :: {}", existingCreditTypeDetail.isNoFlag());
                    if (existingCreditTypeDetail.isNoFlag()) {
                        existingCollateralDetailViewAdd.getExistingCreditTypeDetailViewList().add(existingCreditTypeDetail);
                        seqRelatedTemp = existingCreditTypeDetail.getNo();
                        hashRelated.put(seqRelatedTemp, 1);
                    }
                }
                existingCreditFacilityView.getRelatedCollateralList().add(existingCollateralDetailViewAdd);
                //relatedExistingCollateralDetailViewList.add(existingCollateralDetailViewAdd);
                onSetRowNoCreditTypeDetail(existingCollateralDetailViewAdd.getExistingCreditTypeDetailViewList());
            }
        }else if(modeForButton != null && modeForButton.equals(ModeForButton.EDIT)){

            collateralCreditDetailViewList = new ArrayList<ExistingCreditDetailView>();

            if(typeOfListCollateral.equals("borrower")){
                borrowerExistingCollateralDetailViewList = existingCreditFacilityView.getBorrowerCollateralList();
                ExistingCollateralDetailView borrowerCollateralDetailViewRow = borrowerExistingCollateralDetailViewList.get(rowIndex);

                borrowerCollateralDetailViewRow.setExistingCreditTypeDetailViewList(new ArrayList<ExistingCreditTypeDetailView>());
                borrowerCollateralDetailViewRow.setCollateralType(collateralType);
                borrowerCollateralDetailViewRow.setPotentialCollateral(potentialCollateral);
                borrowerCollateralDetailViewRow.setRelation(relation);
                borrowerCollateralDetailViewRow.setMortgageType(mortgageType);
                borrowerCollateralDetailViewRow.setOwner(existingCollateralDetailView.getOwner());
                borrowerCollateralDetailViewRow.setAccountNumber(existingCollateralDetailView.getAccountNumber());
                borrowerCollateralDetailViewRow.setCollateralLocation(existingCollateralDetailView.getCollateralLocation());
                borrowerCollateralDetailViewRow.setRemark(existingCollateralDetailView.getRemark());
                borrowerCollateralDetailViewRow.setAppraisalDate(existingCollateralDetailView.getAppraisalDate());
                borrowerCollateralDetailViewRow.setAppraisalValue(existingCollateralDetailView.getAppraisalValue());
                borrowerCollateralDetailViewRow.setMortgageValue(existingCollateralDetailView.getMortgageValue());
                borrowerCollateralDetailViewRow.setCollateralNumber(existingCollateralDetailView.getCollateralNumber());
                borrowerCollateralDetailViewRow.setBorrowerType(RelationValue.BORROWER.value());

                List<ExistingCreditTypeDetailView> existingCreditTypeDetailViewList;
                existingCreditTypeDetailViewList = existingCollateralDetailView.getExistingCreditTypeDetailViewList();
                for (int l=0;l<hashBorrower.size();l++){
                    log.info("hashBorrower.get(j) in use   :  "+ l + " is   " +hashBorrower.get(l+1).toString());
                }

                borrowerCollateralDetailViewRow.setExistingCreditTypeDetailViewList(new ArrayList<ExistingCreditTypeDetailView>());
                if(existingCreditTypeDetailViewList!=null && existingCreditTypeDetailViewList.size()>0){
                    for (int i = 0; i < existingCreditTypeDetailViewList.size(); i++) {
                        if (existingCreditTypeDetailViewList.get(i).isNoFlag() == true) {
                            seqBorrowerTemp = existingCreditTypeDetailViewList.get(i).getNo();
                            checkPlus = true;
                        /*for (int j = 0; j < selectCollateralDetail.getExistingCreditTypeDetailViewList().size(); j++) {
                            if (selectCollateralDetail.getExistingCreditTypeDetailViewList().get(j).getNo() == seqBorrowerTemp) {
                                checkPlus = false;
                            }
                            if (checkPlus) {
                                hashBorrower.put(seqBorrowerTemp, 1);
                            }
                        }*/
                            hashBorrower.put(seqBorrowerTemp, 1);
                            borrowerCollateralDetailViewRow.getExistingCreditTypeDetailViewList().add(existingCreditTypeDetailViewList.get(i));
                        } else if (existingCreditTypeDetailViewList.get(i).isNoFlag() == false) {
                            seqBorrowerTemp = existingCreditTypeDetailViewList.get(i).getNo();
                            if (Integer.parseInt(hashBorrower.get(seqBorrowerTemp).toString()) > 0) {
                                hashBorrower.put(seqBorrowerTemp, 0);
                            }
                        }
                    }
                }

                for (int l=0;l<hashBorrower.size();l++){
                    log.info("hashBorrower.get(j) in use   :  "+ l + " is   " +hashBorrower.get(l+1).toString());
                }
                onSetRowNoCreditTypeDetail(borrowerCollateralDetailViewRow.getExistingCreditTypeDetailViewList());
                existingCreditFacilityView.getBorrowerCollateralList().remove(rowIndex);
                existingCreditFacilityView.getBorrowerCollateralList().add(rowIndex,borrowerCollateralDetailViewRow);
                //existingCreditFacilityView.getBorrowerCollateralList().get(rowIndex).setExistingCreditTypeDetailViewList(existingCollateralDetailView.getExistingCreditTypeDetailViewList());


            }else if(typeOfListCollateral.equals("related")){
                for (int l=0;l<hashRelated.size();l++){
                    log.info("before hashRelated seq :  "+ l + " use is   " +hashRelated.get(l+1).toString());
                }
                ExistingCollateralDetailView relatedCollateralDetailViewRow = relatedExistingCollateralDetailViewList.get(rowIndex);

                relatedCollateralDetailViewRow.setExistingCreditTypeDetailViewList(new ArrayList<ExistingCreditTypeDetailView>());
                relatedCollateralDetailViewRow.setCollateralType(collateralType);
                relatedCollateralDetailViewRow.setPotentialCollateral(potentialCollateral);
                relatedCollateralDetailViewRow.setRelation(relation);
                relatedCollateralDetailViewRow.setMortgageType(mortgageType);
                relatedCollateralDetailViewRow.setOwner(existingCollateralDetailView.getOwner());
                relatedCollateralDetailViewRow.setAccountNumber(existingCollateralDetailView.getAccountNumber());
                relatedCollateralDetailViewRow.setCollateralLocation(existingCollateralDetailView.getCollateralLocation());
                relatedCollateralDetailViewRow.setRemark(existingCollateralDetailView.getRemark());
                relatedCollateralDetailViewRow.setAppraisalDate(existingCollateralDetailView.getAppraisalDate());
                relatedCollateralDetailViewRow.setAppraisalValue(existingCollateralDetailView.getAppraisalValue());
                relatedCollateralDetailViewRow.setMortgageValue(existingCollateralDetailView.getMortgageValue());
                relatedCollateralDetailViewRow.setCollateralNumber(existingCollateralDetailView.getCollateralNumber());
                relatedCollateralDetailViewRow.setBorrowerType(RelationValue.DIRECTLY_RELATED.value());

                List<ExistingCreditTypeDetailView> existingCreditTypeDetailViewList;

                existingCreditTypeDetailViewList = existingCollateralDetailView.getExistingCreditTypeDetailViewList();

                relatedCollateralDetailViewRow.setExistingCreditTypeDetailViewList(new ArrayList<ExistingCreditTypeDetailView>());
                if(existingCreditTypeDetailViewList!=null && existingCreditTypeDetailViewList.size()>0){
                    for (int i = 0; i < existingCreditTypeDetailViewList.size(); i++) {
                        seqRelatedTemp = existingCreditTypeDetailViewList.get(i).getNo();
                        if (existingCreditTypeDetailViewList.get(i).isNoFlag() == true) {
                            checkPlus = true;
                        /*for (int j = 0; j < selectCollateralDetail.getExistingCreditTypeDetailViewList().size(); j++) {
                            if (selectCollateralDetail.getExistingCreditTypeDetailViewList().get(j).getNo() == seqRelatedTemp) {
                                checkPlus = false;
                            }
                            if (checkPlus) {
                                hashRelated.put(seqRelatedTemp, Integer.parseInt(hashRelated.get(seqRelatedTemp).toString()) + 1);
                            }
                        }*/
                            hashRelated.put(seqRelatedTemp, 1);
                            relatedCollateralDetailViewRow.getExistingCreditTypeDetailViewList().add(existingCreditTypeDetailViewList.get(i));

                        } else if (existingCreditTypeDetailViewList.get(i).isNoFlag() == false) {
                            if (Integer.parseInt(hashRelated.get(seqRelatedTemp).toString()) > 0) {
                                hashRelated.put(seqRelatedTemp, 0);
                            }
                        }
                    }
                }

                for (int l=0;l<hashRelated.size();l++){
                    log.info("before hashRelated seq :  "+ l + " use is   " +hashRelated.get(l+1).toString());
                }
                onSetRowNoCreditTypeDetail(relatedCollateralDetailViewRow.getExistingCreditTypeDetailViewList());
                existingCreditFacilityView.getRelatedCollateralList().remove(rowIndex);
                existingCreditFacilityView.getRelatedCollateralList().add(rowIndex,relatedCollateralDetailViewRow);
                //relatedCollateralDetailViewRow.setExistingCreditTypeDetailViewList(existingCollateralDetailView.getExistingCreditTypeDetailViewList());

            }else {
                log.info("onSaveCreditDetail ::: Undefined modeForButton !!");
            }
        }

        complete = true;
        calTotalCollateral();
        log.info("  complete >>>>  :  {}", complete);
        context.addCallbackParam("functionComplete", complete);

    }

    private void calTotalCollateral(){
        BigDecimal totalAppraisal = BigDecimal.ZERO;
        BigDecimal totalMortgage = BigDecimal.ZERO;

        if(typeOfListCollateral.equals("borrower")){
            borrowerExistingCollateralDetailViewList = existingCreditFacilityView.getBorrowerCollateralList();
            for(int i=0;i<borrowerExistingCollateralDetailViewList.size();i++){
                totalAppraisal = totalAppraisal.add(borrowerExistingCollateralDetailViewList.get(i).getAppraisalValue());
                totalMortgage = totalMortgage.add(borrowerExistingCollateralDetailViewList.get(i).getMortgageValue());
            }
            existingCreditFacilityView.setTotalBorrowerAppraisalValue(totalAppraisal);
            existingCreditFacilityView.setTotalBorrowerMortgageValue(totalMortgage);
        }else if(typeOfListCollateral.equals("related")){
            relatedExistingCollateralDetailViewList = existingCreditFacilityView.getRelatedCollateralList();
            for(int i=0;i<relatedExistingCollateralDetailViewList.size();i++){
                totalAppraisal = totalAppraisal.add(relatedExistingCollateralDetailViewList.get(i).getAppraisalValue());
                totalMortgage = totalMortgage.add(relatedExistingCollateralDetailViewList.get(i).getMortgageValue());
            }
            existingCreditFacilityView.setTotalRelatedAppraisalValue(totalAppraisal);
            existingCreditFacilityView.setTotalRelatedMortgageValue(totalMortgage);
        }
    }

    public void onSetRowNoCollateralDetail(List<ExistingCollateralDetailView> existingCollateralDetailViewSetRow){
        ExistingCollateralDetailView existingCollateralDetailViewTemp = new ExistingCollateralDetailView();
        for(int i=0;i<existingCollateralDetailViewSetRow.size();i++){
            existingCollateralDetailViewTemp = existingCollateralDetailViewSetRow.get(i);
            existingCollateralDetailViewTemp.setNo(i+1);
        }
    }

    public void onDeleteExistingCollateral(){
        log.info("onDeleteExistingCollateral begin " );
        log.info("onDeleteExistingCollateral rowIndex is " + rowIndex);
        log.info("onDeleteExistingCollateral selectCollateralDetail null is " + (selectCollateralDetail==null));
        if(selectCollateralDetail != null){
            //log.info("selectCollateralDetail is " + selectCollateralDetail.toString());
        }

        if(typeOfListCollateral.equals("borrower")){
            ExistingCollateralDetailView borrowerCollateralDetailViewDel = selectCollateralDetail;
            for (int l=0;l<hashBorrower.size();l++){
                log.info("before hashBorrower seq :  "+ l + " use is   " +hashBorrower.get(l+1).toString());
            }
            log.info("getCreditFacilityList().size() " + borrowerCollateralDetailViewDel.getExistingCreditTypeDetailViewList().size());

            for(int j=0;j<borrowerCollateralDetailViewDel.getExistingCreditTypeDetailViewList().size();j++){
                int seqBowForDel = borrowerCollateralDetailViewDel.getExistingCreditTypeDetailViewList().get(j).getNo();
                if(Integer.parseInt(hashBorrower.get(borrowerCollateralDetailViewDel.getExistingCreditTypeDetailViewList().get(j).getNo()).toString())>0){
                    hashBorrower.put(seqBowForDel,0);
                }
            }

            borrowerExistingCollateralDetailViewList.remove(selectCollateralDetail);
            onSetRowNoCollateralDetail(borrowerExistingCollateralDetailViewList);
            for (int l=0;l<hashBorrower.size();l++){
                log.info("after hashBorrower seq :  "+ l + " use is   " +hashBorrower.get(l+1).toString());
            }

        }else if(typeOfListCollateral.equals("related")){
            ExistingCollateralDetailView relatedCollateralDetailViewDel = selectCollateralDetail;
            for (int l=0;l<hashRelated.size();l++){
                log.info("before hashRelated seq :  "+ l + " use is   " +hashRelated.get(l+1).toString());
            }
            log.info("getCreditFacilityList().size() " + relatedCollateralDetailViewDel.getExistingCreditTypeDetailViewList().size());

            for(int j=0;j<relatedCollateralDetailViewDel.getExistingCreditTypeDetailViewList().size();j++){
                int seqRelatedForDel = relatedCollateralDetailViewDel.getExistingCreditTypeDetailViewList().get(j).getNo();
                if(Integer.parseInt(hashBorrower.get(relatedCollateralDetailViewDel.getExistingCreditTypeDetailViewList().get(j).getNo()).toString())>0){
                    hashRelated.put(seqRelatedForDel, Integer.parseInt(hashRelated.get(seqRelatedForDel).toString()) - 1);
                }
            }

            relatedExistingCollateralDetailViewList.remove(selectCollateralDetail);
            onSetRowNoCollateralDetail(relatedExistingCollateralDetailViewList);
            for (int l=0;l<hashRelated.size();l++){
                log.info("after hashRelated seq :  "+ l + " use is   " +hashRelated.get(l+1).toString());
            }
        }
        calTotalCollateral();
    }

    //Start Condition Information //
    public void onAddExistingGuarantor() {
        log.info("onAddExistingCollateral ::: ");
        existingGuarantorDetailView = new ExistingGuarantorDetailView();
        borrowerGuarantorCreditTypeDetailViewList = new ArrayList<ExistingCreditTypeDetailView>();
        canSaveGarantor = false;
        existingGuarantorDetailView.setExistingCreditTypeDetailViewList(findBorrowerCreditFacility());
        if(existingGuarantorDetailView.getExistingCreditTypeDetailViewList()!=null && existingGuarantorDetailView.getExistingCreditTypeDetailViewList().size()>0){
            canSaveGarantor = true;
        }

        modeForButton = ModeForButton.ADD;
        log.info("onAddCommercialCredit ::: modeForButton : {}", modeForButton);
    }

    public void onEditExistingGuarantor() {
        log.info("onEditExistingGuarantor ::: ");
        log.info("onEditExistingCollateral rowIndex   " + rowIndex);
        log.info("onEditExistingCollateral selectGuarantorDetail null is " + (selectGuarantorDetail==null));
        if(selectGuarantorDetail != null){
            ///log.info("selectGuarantorDetail is " + selectGuarantorDetail.toString());
        }

        modeForButton = ModeForButton.EDIT;
        existingGuarantorDetailView = new ExistingGuarantorDetailView();

        Cloner cloner = new Cloner();
        existingGuarantorDetailView = cloner.deepClone(selectGuarantorDetail);

        existingGuarantorDetailView.setTcgLgNo(selectGuarantorDetail.getTcgLgNo());
        existingGuarantorDetailView.setGuarantorName(selectGuarantorDetail.getGuarantorName());
        existingGuarantorDetailView.setTotalLimitGuaranteeAmount(selectGuarantorDetail.getTotalLimitGuaranteeAmount());

        int tempSeq =0;
        List<ExistingCreditTypeDetailView> existingCreditTypeDetailList;
        existingCreditTypeDetailList = findBorrowerCreditFacility();
        existingGuarantorDetailView.setExistingCreditTypeDetailViewList(existingCreditTypeDetailList);

        if(selectGuarantorDetail.getExistingCreditTypeDetailViewList()!=null && selectGuarantorDetail.getExistingCreditTypeDetailViewList().size()>0){
            for (int i = 0; i < selectGuarantorDetail.getExistingCreditTypeDetailViewList().size(); i++) {
                for (int j = tempSeq; j < existingCreditTypeDetailList.size(); j++) {
                    log.info("creditType at " + j + " seq is     " + existingCreditTypeDetailList.get(j).getNo());

                    if (selectGuarantorDetail.getExistingCreditTypeDetailViewList().get(i).getNo() == existingCreditTypeDetailList.get(j).getNo()) {
                        existingGuarantorDetailView.getExistingCreditTypeDetailViewList().get(j).setNoFlag(true);
                        existingGuarantorDetailView.getExistingCreditTypeDetailViewList().get(j).setGuaranteeAmount(selectGuarantorDetail.getExistingCreditTypeDetailViewList().get(i).getGuaranteeAmount());
                        tempSeq = j;
                    }
                    continue;
                }
            }
        }


        log.info("onEditExistingGuarantor end" );
    }


    public void onSaveExistingGuarantor() {
        log.info("onSaveExistingGuarantor ::: mode : {}", modeForButton);
        log.info("onSaveExistingGuarantor rowIndex is " + rowIndex);
        log.info("onSaveExistingGuarantor selectGuarantorDetail null is " + (selectGuarantorDetail==null));
        List<ExistingCreditTypeDetailView> typeCheckLastTime;
        //typeCheckLastTime = selectGuarantorDetail.getExistingCreditTypeDetailViewList();
        if(selectGuarantorDetail != null){
            log.info("selectGuarantorDetail is " + selectGuarantorDetail.toString());
           //typeCheckLastTime = selectGuarantorDetail.getExistingCreditTypeDetailViewList();
            //log.info("onSaveExistingGuarantor typeCheckLastTime size is " + typeCheckLastTime.size());
        }

        boolean complete = false;
        RequestContext context = RequestContext.getCurrentInstance();
        int seqBorrowerTemp;
        boolean checkPlus = true;
        BigDecimal summaryGuaranteeAmount = BigDecimal.ZERO;

        if(modeForButton != null && modeForButton.equals(ModeForButton.ADD)){
            for (int l=0;l<hashBorrower.size();l++){
                log.info("hashBorrower.get(j) in use   :  "+ l + " is   " +hashBorrower.get(l+1).toString());
            }
            ExistingGuarantorDetailView existingGuarantorDetailViewAdd = new ExistingGuarantorDetailView();

            existingGuarantorDetailViewAdd.setNo(borrowerExistingGuarantorDetailViewList.size()+1);
            existingGuarantorDetailViewAdd.setGuarantorName(customerInfoControl.getCustomerById(existingGuarantorDetailView.getGuarantorName()));
            existingGuarantorDetailViewAdd.setTcgLgNo(existingGuarantorDetailView.getTcgLgNo());

            existingGuarantorDetailViewAdd.setExistingCreditTypeDetailViewList(new ArrayList<ExistingCreditTypeDetailView>());
            for (ExistingCreditTypeDetailView existingCreditTypeDetail : existingGuarantorDetailView.getExistingCreditTypeDetailViewList()) {
                log.info("existingCreditTypeDetail.isNoFlag()  :: {}", existingCreditTypeDetail.isNoFlag());
                if (existingCreditTypeDetail.isNoFlag()) {
                    existingGuarantorDetailViewAdd.getExistingCreditTypeDetailViewList().add(existingCreditTypeDetail);
                    summaryGuaranteeAmount = summaryGuaranteeAmount.add(existingCreditTypeDetail.getGuaranteeAmount());
                    seqBorrowerTemp = existingCreditTypeDetail.getNo();
                    hashBorrower.put(seqBorrowerTemp, 1);
                }
            }
            existingGuarantorDetailViewAdd.setTotalLimitGuaranteeAmount(summaryGuaranteeAmount);
            for (int l=0;l<hashBorrower.size();l++){
                log.info("hashBorrower.get(j) in use   :  "+ l + " is   " +hashBorrower.get(l+1).toString());
            }
            borrowerExistingGuarantorDetailViewList.add(existingGuarantorDetailViewAdd);
            existingCreditFacilityView.setBorrowerGuarantorList(borrowerExistingGuarantorDetailViewList);
            onSetRowNoCreditTypeDetail(existingGuarantorDetailViewAdd.getExistingCreditTypeDetailViewList());
        }else if(modeForButton != null && modeForButton.equals(ModeForButton.EDIT)){
            ExistingGuarantorDetailView existingGuarantorDetailViewOnRow = borrowerExistingGuarantorDetailViewList.get(rowIndex);
            existingGuarantorDetailViewOnRow.setExistingCreditTypeDetailViewList(new ArrayList<ExistingCreditTypeDetailView>());
            existingGuarantorDetailViewOnRow.setTcgLgNo(existingGuarantorDetailView.getTcgLgNo());
            existingGuarantorDetailViewOnRow.setGuarantorName(customerInfoControl.getCustomerById(existingGuarantorDetailView.getGuarantorName()));

            List<ExistingCreditTypeDetailView> existingCreditTypeDetailViewList;
            existingCreditTypeDetailViewList = existingGuarantorDetailView.getExistingCreditTypeDetailViewList();
            for (int l=0;l<hashBorrower.size();l++){
                log.info("hashBorrower.get(j) in use   :  "+ l + " is   " +hashBorrower.get(l+1).toString());
            }
            log.info("selectGuarantorDetail begin old check last size " +selectGuarantorDetail.getExistingCreditTypeDetailViewList().size());

            if(existingCreditTypeDetailViewList!=null && existingCreditTypeDetailViewList.size()>0){
                for (int i = 0; i < existingCreditTypeDetailViewList.size(); i++) {
                    seqBorrowerTemp = existingCreditTypeDetailViewList.get(i).getNo();
                    if (existingCreditTypeDetailViewList.get(i).isNoFlag() == true) {

                        summaryGuaranteeAmount = summaryGuaranteeAmount.add(existingCreditTypeDetailViewList.get(i).getGuaranteeAmount());
                        checkPlus = true;

                        log.info("selectGuarantorDetail in process old check last size " +selectGuarantorDetail.getExistingCreditTypeDetailViewList().size());

                        //for (int j = 0; j < selectGuarantorDetail.getExistingCreditTypeDetailViewList().size(); j++) {
                        for (int j = 0; j <selectGuarantorDetail.getExistingCreditTypeDetailViewList().size(); j++) {
                            log.info("selectGuarantorDetail old check last getSeq  " + j + "  is " + selectGuarantorDetail.getExistingCreditTypeDetailViewList().get(j).getNo());
                            if (selectGuarantorDetail.getExistingCreditTypeDetailViewList().get(j).getNo() == seqBorrowerTemp) {
                                checkPlus = false;
                            }

                            log.info("checkPlus is  " + checkPlus);

                            if (checkPlus) {
                                log.info("put hash ");
                                hashBorrower.put(seqBorrowerTemp, 1);

                                for (int l=0;l<hashBorrower.size();l++){
                                    log.info("hashBorrower.get(j) in use   :  "+ l + " is   " +hashBorrower.get(l+1).toString());
                                }
                            }
                        }
                        existingGuarantorDetailViewOnRow.getExistingCreditTypeDetailViewList().add(existingCreditTypeDetailViewList.get(i));
                    } else if (existingCreditTypeDetailViewList.get(i).isNoFlag() == false) {
                        if (Integer.parseInt(hashBorrower.get(seqBorrowerTemp).toString()) > 0) {
                            hashBorrower.put(seqBorrowerTemp, 0);
                        }
                    }
                    existingGuarantorDetailViewOnRow.setTotalLimitGuaranteeAmount(summaryGuaranteeAmount);
                }
            }

            for (int l=0;l<hashBorrower.size();l++){
                log.info("hashBorrower.get(l) in use   :  "+ l + " is   " +hashBorrower.get(l+1).toString());
            }
            //existingCreditFacilityView.getBorrowerGuarantorList().get(rowIndex).setExistingCreditTypeDetailViewList(existingCollateralDetailView.getExistingCreditTypeDetailViewList());
            existingCreditFacilityView.setBorrowerGuarantorList(borrowerExistingGuarantorDetailViewList);
            onSetRowNoCreditTypeDetail(existingGuarantorDetailViewOnRow.getExistingCreditTypeDetailViewList());


        }else {
            log.info("onSaveCreditDetail ::: Undefined modeForButton !!");
        }
        calTotalGuarantor();
        complete = true;

        log.info(" onSaveExistingGuarantor complete >>>>  :  {}", complete);
        context.addCallbackParam("functionComplete", complete);

    }

    private void calTotalGuarantor(){
        BigDecimal totalGuaranteeAmountFinal = BigDecimal.ZERO;
        for(int i=0;i<borrowerExistingGuarantorDetailViewList.size();i++){
            totalGuaranteeAmountFinal = totalGuaranteeAmountFinal.add(borrowerExistingGuarantorDetailViewList.get(i).getTotalLimitGuaranteeAmount());
        }
        existingCreditFacilityView.setTotalGuaranteeAmount(totalGuaranteeAmountFinal);
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
            log.info("before hashBorrower seq :  "+ l + " use is   " +hashBorrower.get(l+1).toString());
        }
        log.info("getCreditFacilityList().size() " + guarantorDetailViewDel.getExistingCreditTypeDetailViewList().size());

        for(int j=0;j<guarantorDetailViewDel.getExistingCreditTypeDetailViewList().size();j++){
            int seqBowForDel = guarantorDetailViewDel.getExistingCreditTypeDetailViewList().get(j).getNo();

            log.info("seq in seqBowForDel :  "+ seqBowForDel +" use feq is " + hashBorrower.get(seqBowForDel).toString());

            if(Integer.parseInt(hashBorrower.get(guarantorDetailViewDel.getExistingCreditTypeDetailViewList().get(j).getNo()).toString())>0){
                hashBorrower.put(seqBowForDel, 0);
            }
        }

        borrowerExistingGuarantorDetailViewList.remove(guarantorDetailViewDel);
        onSetRowNoGuarantorDetail(borrowerExistingGuarantorDetailViewList);

        for (int l=0;l<hashBorrower.size();l++){
            log.info("after hashBorrower seq :  "+ l + " use is   " +hashBorrower.get(l+1).toString());
        }
        calTotalGuarantor();
    }

    private void onSetInUsed(){
        int inUsed;
        int  seq;
        if(existingCreditFacilityView.getBorrowerComExistingCredit()!=null && existingCreditFacilityView.getBorrowerComExistingCredit().size()>0){
            for(int i = 0; i < existingCreditFacilityView.getBorrowerComExistingCredit().size(); i++) {
                seq = existingCreditFacilityView.getBorrowerComExistingCredit().get(i).getNo();
                inUsed = Integer.parseInt(hashBorrower.get(seq).toString());
                existingCreditFacilityView.getBorrowerComExistingCredit().get(i).setInUsed(inUsed);
            }
        }

        if(existingCreditFacilityView.getRelatedComExistingCredit()!=null && existingCreditFacilityView.getRelatedComExistingCredit().size()>0){
            for (int i = 0; i < existingCreditFacilityView.getRelatedComExistingCredit().size(); i++) {
                seq = existingCreditFacilityView.getRelatedComExistingCredit().get(i).getNo();
                inUsed = Integer.parseInt(hashRelated.get(seq).toString());
                existingCreditFacilityView.getRelatedComExistingCredit().get(i).setInUsed(inUsed);
            }
        }

    }

    public void onSaveCreditFacExisting() {

        log.info("onSaveCreditFacExisting ::: ModeForDB  {}");
        onSetInUsed();
        log.info("check seq  ::: inused ");
        if(existingCreditFacilityView.getBorrowerComExistingCredit()!=null && existingCreditFacilityView.getBorrowerComExistingCredit().size()>0){
            for(int i = 0; i < existingCreditFacilityView.getBorrowerComExistingCredit().size(); i++) {
                log.info("borrower  seq is >>> " + existingCreditFacilityView.getBorrowerComExistingCredit().get(i).getNo()  + " inuse is >> " + existingCreditFacilityView.getBorrowerComExistingCredit().get(i).getInUsed());
            }
        }

        if(existingCreditFacilityView.getRelatedComExistingCredit()!=null && existingCreditFacilityView.getRelatedComExistingCredit().size()>0){
            for (int i = 0; i < existingCreditFacilityView.getRelatedComExistingCredit().size(); i++) {
                log.info("related   seq is >>> " + existingCreditFacilityView.getRelatedComExistingCredit().get(i).getNo()  + " inuse is >> " + existingCreditFacilityView.getRelatedComExistingCredit().get(i).getInUsed());
            }
        }


        try {
            creditFacExistingControl.onSaveExistingCreditFacility(existingCreditFacilityView ,workCaseId,user);
            messageHeader = msg.get("app.header.save.success");
            message = msg.get("app.credit.facility.message.save.success");
            //onCreation();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");

        } catch (Exception ex) {
            log.error("Exception : {}", ex);
            messageHeader = msg.get("app.credit.facility.message.save.failed");

            if (ex.getCause() != null) {
                message = msg.get("app.credit.facility.message.save.failed") + " cause : " + ex.getCause().toString();
            } else {
                message = msg.get("app.credit.facility.message.save.failed") + ex.getMessage();
            }

            //messageErr = true;
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public void onRefreshExistingCredit() {
        log.info("Start on Retrieve Interface Info");
        List<CustomerInfoView> customerInfoViews = creditFacExistingControl.getCustomerListByWorkCaseId(workCaseId);
        List<CustomerInfoView> customerInfoViewList = new ArrayList<CustomerInfoView>();
        if(customerInfoViews != null){
            customerInfoViewList = generateCustomerInfoList(customerInfoViews);
        }

        clearExistingCreditFacilityView();

        ExistingCreditFacilityView existingCreditFacilityViewTmp = creditFacExistingControl.onFindExistingCreditFacility(workCaseId);
        existingCreditFacilityView = existingCreditControl.refreshExistingCredit(customerInfoViewList);

        if(existingCreditFacilityViewTmp!=null && existingCreditFacilityViewTmp.getId()!=0){
            existingCreditFacilityView.setId(existingCreditFacilityViewTmp.getId());
        }

        if(existingCreditFacilityView.getBorrowerComExistingCredit()!=null && existingCreditFacilityView.getBorrowerComExistingCredit().size()>0){
            for(ExistingCreditDetailView existingCreditDetailView1 : existingCreditFacilityView.getBorrowerComExistingCredit()) {
                hashBorrower.put(existingCreditDetailView1.getNo(),0);
            }
        }

        if(existingCreditFacilityView.getRelatedComExistingCredit()!=null && existingCreditFacilityView.getRelatedComExistingCredit().size()>0){
            for(ExistingCreditDetailView existingCreditDetailView2 : existingCreditFacilityView.getRelatedComExistingCredit()) {
                hashBorrower.put(existingCreditDetailView2.getNo(),0);
            }
        }

        messageHeader = msg.get("app.header.information");
        message = msg.get("app.credit.facility.message.success.refresh");
        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
    }

    public List<CustomerInfoView> generateCustomerInfoList(List<CustomerInfoView> customerInfoViews){
        List<CustomerInfoView> customerInfoList = new ArrayList<CustomerInfoView>();
        for(CustomerInfoView item : customerInfoViews){
            customerInfoList.add(item);
            if(item.getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()){
                if(item.getMaritalStatus() != null && item.getMaritalStatus().getId() == 2){
                    CustomerInfoView spouse = new CustomerInfoView();
                    spouse = item.getSpouse();
                    if(spouse != null){
                        customerInfoList.add(spouse);
                    }
                }
            }
        }
        return customerInfoList;
    }

    public void clearExistingCreditFacilityView(){
        existingCreditDetailView = new ExistingCreditDetailView();
        existProductProgramView = new ProductProgramView();
        existCreditTypeView = new CreditTypeView();

        existingCreditDetailView.setExistAccountStatus(new BankAccountStatus());
        existingCreditDetailView.setExistProductProgramView(existProductProgramView);
        existingCreditDetailView.setExistCreditTypeView(existCreditTypeView);
        existingCreditDetailView.setAccountStatus(new BankAccountStatusView());

        existingSplitLineDetailView = new ExistingSplitLineDetailView();
        productProgram = new ProductProgram();
        existingSplitLineDetailView.setProductProgram(productProgram);

        existingCollateralDetailView = new ExistingCollateralDetailView();
        existingCollateralDetailView.setCollateralType(new CollateralType());
        existingCollateralDetailView.setPotentialCollateral(new PotentialCollateral());
        existingCollateralDetailView.setRelation(new Relation());
        existingCollateralDetailView.setMortgageType(new MortgageType());
        existingCollateralDetailView.setExistingCreditTypeDetailViewList(new ArrayList<ExistingCreditTypeDetailView>());

        existingCreditTierDetailView = new ExistingCreditTierDetailView();
        existingGuarantorDetailView = new ExistingGuarantorDetailView();
        existingGuarantorDetailView.setGuarantorName(new CustomerInfoView());

        existingCreditFacilityView =  new ExistingCreditFacilityView();
        existingConditionDetailViewList = new ArrayList<ExistingConditionDetailView>();
        existingCreditDetailViewList = new ArrayList<ExistingCreditDetailView>();
        borrowerExistingCreditDetailViewList = new ArrayList<ExistingCreditDetailView>();
        relatedExistingCreditDetailViewList = new ArrayList<ExistingCreditDetailView>();
        borrowerExistingCollateralDetailViewList = new ArrayList<ExistingCollateralDetailView>() ;
        relatedExistingCollateralDetailViewList  = new ArrayList<ExistingCollateralDetailView>() ;
        collateralCreditDetailViewList = new ArrayList<ExistingCreditDetailView>();
        borrowerExistingGuarantorDetailViewList = new ArrayList<ExistingGuarantorDetailView>();
        existingGuarantorDetailView.setExistingCreditTypeDetailViewList(new ArrayList<ExistingCreditTypeDetailView>());

        existingCreditFacilityView.setBorrowerComExistingCredit(borrowerExistingCreditDetailViewList);
        existingCreditFacilityView.setBorrowerRetailExistingCredit(new ArrayList<ExistingCreditDetailView>());
        existingCreditFacilityView.setBorrowerAppInRLOSCredit(new ArrayList<ExistingCreditDetailView>());
        existingCreditFacilityView.setExistingConditionDetailViewList(existingConditionDetailViewList);
        existingCreditFacilityView.setRelatedComExistingCredit(relatedExistingCreditDetailViewList);
        existingCreditFacilityView.setRelatedRetailExistingCredit(new ArrayList<ExistingCreditDetailView>());
        existingCreditFacilityView.setRelatedAppInRLOSCredit(new ArrayList<ExistingCreditDetailView>());
        existingCreditFacilityView.setBorrowerCollateralList(borrowerExistingCollateralDetailViewList);
        existingCreditFacilityView.setRelatedCollateralList(relatedExistingCollateralDetailViewList);
        existingCreditFacilityView.setBorrowerGuarantorList(borrowerExistingGuarantorDetailViewList);
    }


    public ExistingConditionDetailView getExistingConditionDetailView() {
        return existingConditionDetailView;
    }

    public void setExistingConditionDetailView(ExistingConditionDetailView existingConditionDetailView) {
        this.existingConditionDetailView = existingConditionDetailView;
    }

    public List<ExistingConditionDetailView> getExistingConditionDetailViewList() {
        return existingConditionDetailViewList;
    }

    public void setExistingConditionDetailViewList(List<ExistingConditionDetailView> existingConditionDetailViewList) {
        this.existingConditionDetailViewList = existingConditionDetailViewList;
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

    public ExistingCreditFacilityView getExistingCreditFacilityView() {
        return existingCreditFacilityView;
    }

    public void setExistingCreditFacilityView(ExistingCreditFacilityView existingCreditFacilityView) {
        this.existingCreditFacilityView = existingCreditFacilityView;
    }

    public ExistingSplitLineDetailView getExistingSplitLineDetailView() {
        return existingSplitLineDetailView;
    }

    public void setExistingSplitLineDetailView(ExistingSplitLineDetailView existingSplitLineDetailView) {
        this.existingSplitLineDetailView = existingSplitLineDetailView;
    }

    public List<ExistingSplitLineDetailView> getExistingSplitLineDetailViewList() {
        return existingSplitLineDetailViewList;
    }

    public void setExistingSplitLineDetailViewList(List<ExistingSplitLineDetailView> existingSplitLineDetailViewList) {
        this.existingSplitLineDetailViewList = existingSplitLineDetailViewList;
    }

    public List<ProductProgramView> getProductProgramViewList() {
        return productProgramViewList;
    }

    public void setProductProgramViewList(List<ProductProgramView> productProgramViewList) {
        this.productProgramViewList = productProgramViewList;
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

    public CreditTypeView getExistCreditTypeView() {
        return existCreditTypeView;
    }

    public void setExistCreditType(CreditTypeView existCreditTypeView) {
        this.existCreditTypeView = existCreditTypeView;
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

    public List<CustomerInfoView> getGuarantorList() {
        return guarantorList;
    }

    public void setGuarantorList(List<CustomerInfoView> guarantorList) {
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

    public String getTypeOfListCollateral() {
        return typeOfListCollateral;
    }

    public void setTypeOfListCollateral(String typeOfListCollateral) {
        this.typeOfListCollateral = typeOfListCollateral;
    }

    public List<BankAccountStatus> getBankAccountStatusList() {
        return bankAccountStatusList;
    }

    public void setBankAccountStatusList(List<BankAccountStatus> bankAccountStatusList) {
        this.bankAccountStatusList = bankAccountStatusList;
    }

    public List<BaseRate> getBaseRateList() {
        return baseRateList;
    }

    public void setBaseRateList(List<BaseRate> baseRateList) {
        this.baseRateList = baseRateList;
    }

    public boolean isCannotEditStandard() {
        return cannotEditStandard;
    }

    public void setCannotEditStandard(boolean cannotEditStandard) {
        this.cannotEditStandard = cannotEditStandard;
    }

    public Date getCurrentDate() {
        return DateTime.now().toDate();
    }

    public String getCurrentDateDDMMYY() {
        log.debug("current date : {}", getCurrentDate());
        return  currentDateDDMMYY = DateTimeUtil.convertToStringDDMMYYYY(getCurrentDate());
    }

    public boolean isCanSaveCreditDetail() {
        return canSaveCreditDetail;
    }

    public void setCanSaveCreditDetail(boolean canSaveCreditDetail) {
        this.canSaveCreditDetail = canSaveCreditDetail;
    }

    public boolean isCanSaveGarantor() {
        return canSaveGarantor;
    }

    public void setCanSaveGarantor(boolean canSaveGarantor) {
        this.canSaveGarantor = canSaveGarantor;
    }

    public boolean isCanSaveBorrowerCol() {
        return canSaveBorrowerCol;
    }

    public void setCanSaveBorrowerCol(boolean canSaveBorrowerCol) {
        this.canSaveBorrowerCol = canSaveBorrowerCol;
    }

    public boolean isCanSaveRelatedCol() {
        return canSaveRelatedCol;
    }

    public void setCanSaveRelatedCol(boolean canSaveRelatedCol) {
        this.canSaveRelatedCol = canSaveRelatedCol;
    }
}
