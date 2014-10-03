package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.CreditFacExistingControl;
import com.clevel.selos.businesscontrol.CustomerInfoControl;
import com.clevel.selos.businesscontrol.ExistingCreditControl;
import com.clevel.selos.businesscontrol.ProposeLineControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.relation.PrdProgramToCreditTypeDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.relation.PrdGroupToPrdProgram;
import com.clevel.selos.model.db.relation.PrdProgramToCreditType;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.transform.master.BankAccountStatusTransform;
import com.clevel.selos.transform.BaseRateTransform;
import com.clevel.selos.transform.ProductTransform;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import com.rits.cloning.Cloner;
import org.joda.time.DateTime;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

@ViewScoped
@ManagedBean(name = "creditFacExisting")
public class CreditFacExisting extends BaseController {
    @Inject
    @SELOS
    Logger log;
    @Inject
    @NormalMessage
    Message msg;
    //## session
    private long workCaseId;
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
    private BankAccountStatusView existAccountStatusView ;
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


    private ProductGroup productGroup;
    private List<ProductProgramView> productProgramViewList;
    private List<ProductProgramView> productProgramViewListSplit;
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

    private boolean isUsePCE;

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
    private PrdProgramToCreditTypeDAO prdProgramToCreditTypeDAO;    // find credit type
    @Inject
    private BaseRateDAO baseRateDAO;
    @Inject
    private ExistingProductFormulaDAO existingProductFormulaDAO;
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
    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    private ProposeLineControl proposeLineControl;


    public CreditFacExisting(){

    }

    public void preRender(){
        log.debug("preRender");
        HttpSession session = FacesUtil.getSession(false);

        if(checkSession(session)){
            //TODO Check valid step
            log.debug("preRender ::: Check valid stepId");

        }else{
            log.debug("preRender ::: No session for case found. Redirect to Inbox");
            FacesUtil.redirect("/site/inbox.jsf");
        }
    }

    @PostConstruct
    public void onCreation() {
        log.info("onCreation begin");
        HttpSession session = FacesUtil.getSession(false);

        if(checkSession(session)){
            try{
                workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
                String ownerCaseUserId = Util.parseString(session.getAttribute("caseOwner"), "");

                loadFieldControl(workCaseId, Screen.CREDIT_FACILITY_EXISTING, ownerCaseUserId);

                user = (User) session.getAttribute("user");

                productProgramViewList = productTransform.transformToView(productProgramDAO.findExistingProductProgram());
                productProgramViewListSplit = productTransform.transformToView(productProgramDAO.findExistingProductProgram());
                baseRateList = baseRateDAO.findAll();

                WorkCase workCase = workCaseDAO.findById(workCaseId);
                if(!Util.isNull(workCase)){
                    productGroup = workCase.getProductGroup();
                }

                prdGroupToPrdProgramList = creditFacExistingControl.getPrdGroupToPrdProgramProposeByGroup(productGroup);

                if(prdGroupToPrdProgramList == null){
                    prdGroupToPrdProgramList = new ArrayList<PrdGroupToPrdProgram>();
                }

                creditTypeList = creditTypeDAO.findAll();
                accountStatusList = accountStatusDAO.findAll();
                List<String> dataSourceExcept = new ArrayList<String>();
                dataSourceExcept.add("RLOS"); // TODO: Change get RLOS from master or enum
                bankAccountStatusList = bankAccountStatusDAO.findAllExceptDataSource(dataSourceExcept);
                relationList = relationDAO.findAll();
                collateralTypeList = collateralTypeDAO.findAll();
                potentialCollateralList = potentialCollateralDAO.findAll();
                mortgageTypeList = mortgageTypeDAO.findAll();
                hashBorrower = new Hashtable<String,String>();
                hashRelated  = new Hashtable<String,String>();

                isUsePCE = false;

                guarantorList = customerInfoControl.getGuarantorByWorkCase(workCaseId);
                CustomerInfoView customerInfoView = new CustomerInfoView();
                customerInfoView.setId(-1);
                customerInfoView.setFirstNameTh(msg.get("app.select.tcg"));
                guarantorList.add(customerInfoView);

                existAccountStatusView = new BankAccountStatusView();
                existProductProgramView = new ProductProgramView();
                existCreditTypeView = new CreditTypeView();

                existingCreditDetailView = new ExistingCreditDetailView();
                existingCreditDetailView.setExistAccountStatusView(existAccountStatusView);
                existingCreditDetailView.setExistProductProgramView(existProductProgramView);
                existingCreditDetailView.setExistCreditTypeView(existCreditTypeView);

                productProgram = new ProductProgram();
                existingSplitLineDetailView = new ExistingSplitLineDetailView();
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

                if(existingCreditFacilityView == null){
                    existingConditionDetailViewList = new ArrayList<ExistingConditionDetailView>();
                    existingCreditDetailViewList = new ArrayList<ExistingCreditDetailView>();
                    borrowerExistingCreditDetailViewList = new ArrayList<ExistingCreditDetailView>();
                    relatedExistingCreditDetailViewList = new ArrayList<ExistingCreditDetailView>();
                    borrowerExistingCollateralDetailViewList = new ArrayList<ExistingCollateralDetailView>() ;
                    relatedExistingCollateralDetailViewList  = new ArrayList<ExistingCollateralDetailView>() ;
                    collateralCreditDetailViewList = new ArrayList<ExistingCreditDetailView>();
                    borrowerExistingGuarantorDetailViewList = new ArrayList<ExistingGuarantorDetailView>();
                    existingGuarantorDetailView.setExistingCreditTypeDetailViewList(new ArrayList<ExistingCreditTypeDetailView>());

                    existingCreditFacilityView =  new ExistingCreditFacilityView();
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
                }else{
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

                    log.info("onCreation ::: getId " + existingCreditFacilityView.getId());

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
                }

                existingCreditFacilityView.setBorrowerComExistingCreditDeleteList(new ArrayList<ExistingCreditDetailView>());
                existingCreditFacilityView.setBorrowerRetailExistingCreditDeleteList(new ArrayList<ExistingCreditDetailView>());
                existingCreditFacilityView.setBorrowerAppInRLOSCreditDeleteList(new ArrayList<ExistingCreditDetailView>());
                existingCreditFacilityView.setBorrowerExistingCreditPreScreenDeleteList(new ArrayList<ExistingCreditDetailView>());

                existingCreditFacilityView.setRelatedComExistingCreditDeleteList(new ArrayList<ExistingCreditDetailView>());
                existingCreditFacilityView.setRelatedRetailExistingCreditDeleteList(new ArrayList<ExistingCreditDetailView>());
                existingCreditFacilityView.setRelatedAppInRLOSCreditDeleteList(new ArrayList<ExistingCreditDetailView>());
                existingCreditFacilityView.setRelateExistingCreditPresScreenDeleteList(new ArrayList<ExistingCreditDetailView>());

                calTotalCreditBorrower();
                calTotalCreditRelated();
                calTotalCreditGroup();
            }catch (Exception e){
                log.info("onCreation catch " + e.getMessage());
            }
        }
    }


    public void onChangeProductProgram(){
        log.info("onChangeProductProgram :::: productProgram : {}"+ existingCreditDetailView.getExistProductProgramView().getId());
        ProductProgram productProgram = productProgramDAO.findById(existingCreditDetailView.getExistProductProgramView().getId());
        //ProductProgramView productProgramView = productTransform.transformToView(productProgram);
        log.info("onChangeProductProgram :::: productProgram : {}", productProgram);

        if(productProgram!=null && productProgram.getId()!=0){
            prdProgramToCreditTypeList = prdProgramToCreditTypeDAO.getListCreditExistingByPrdprogram(productProgram.getId());
            if(prdProgramToCreditTypeList == null){
                prdProgramToCreditTypeList = new ArrayList<PrdProgramToCreditType>();
            }
            log.info("onChangeProductProgram :::: prdProgramToCreditTypeList.size ::: " +prdProgramToCreditTypeList.size());
        }
    }

    public void onChangeCreditType(){
        int id = existingCreditDetailView.getExistCreditTypeView().getId();
        if ((existingCreditDetailView.getExistProductProgramView().getId() != 0) && (existingCreditDetailView.getExistCreditTypeView().getId() != 0)) {
            canSaveCreditDetail = false;
            ProductProgram productProgram = productProgramDAO.findById(existingCreditDetailView.getExistProductProgramView().getId());
            CreditType creditType = creditTypeDAO.findById(existingCreditDetailView.getExistCreditTypeView().getId());

            showSplitLine = Util.isTrue(creditType.getCanSplit());

            //check for PCE
            if(CalLimitType.getCalLimitType(creditType.getCalLimitType()) == CalLimitType.PCE){
                isUsePCE = true;
            } else {
                isUsePCE = false;
            }

            if(productProgram != null && creditType != null){
                ExistingProductFormula productFormula = existingProductFormulaDAO.findProductFormulaDefaultByPrdProgAndCreditType(productProgram,creditType);
                log.debug("onChangeCreditType : productFormula : {}", productFormula);

                if(productFormula != null){
                    existingCreditDetailView.setProductCode(productFormula.getProductCode());
                    existingCreditDetailView.setProjectCode(productFormula.getProjectCode());
                    existingCreditDetailView.setProductSegment(productFormula.getProductSegment());
                } else {
                    existingCreditDetailView.setProductCode("-");
                    existingCreditDetailView.setProjectCode("-");
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
        existAccountStatusView = new BankAccountStatusView();
        existProductProgramView = new ProductProgramView();
        existCreditTypeView = new CreditTypeView();

        existingCreditDetailView.setExistAccountStatusView(existAccountStatusView);
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
        canSaveCreditDetail = false;
        isUsePCE = false;
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

        if(CalLimitType.getCalLimitType(creditType.getCalLimitType()) == CalLimitType.PCE){
            isUsePCE = true;
        } else {
            isUsePCE = false;
        }

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
            if(used==0){
                log.info("use 0 ");
                //check is used by propose
                if(existingCreditDetailViewDel.getId()!=0){
                    if(creditFacExistingControl.isUsedInProposeCredit(existingCreditDetailViewDel.getId())){
                        messageHeader = msg.get("app.header.error");
                        message = msg.get("app.credit.facility.message.err.credit.inused.propose");
                        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                    } else {
                        List<ExistingCreditDetailView> brComCreditDetailViewDeleteList = existingCreditFacilityView.getBorrowerComExistingCreditDeleteList();
                        brComCreditDetailViewDeleteList.add(existingCreditDetailViewDel);
                        existingCreditFacilityView.setBorrowerComExistingCreditDeleteList(brComCreditDetailViewDeleteList);
                        existingCreditFacilityView.getBorrowerComExistingCredit().remove(existingCreditDetailViewDel);
                    }
                } else {
                    existingCreditFacilityView.getBorrowerComExistingCredit().remove(existingCreditDetailViewDel);
                }
            }else{
                log.info("use > 0 ");
                messageHeader = msg.get("app.header.error");
                message = msg.get("app.credit.facility.message.err.credit.inused");
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            }
            onSetRowNoCreditDetail(existingCreditFacilityView.getBorrowerComExistingCredit());
            calTotalCreditBorrower();
            calTotalCreditGroup();
        }else if(typeOfList.equals("related")){
            log.info("del 1");

            for (int l=0;l<hashRelated.size();l++){
                log.info("hashRelated.get(j) in use   :  "+ l + " is   " +hashRelated.get(l+1).toString());
            }

            used = Integer.parseInt(hashRelated.get(existingCreditDetailViewDel.getNo()).toString());

            log.info("before del use is  " +  used);
            if(used ==0){
                log.info("use 0 ");
                //check is used by propose
                if(existingCreditDetailViewDel.getId()!=0){
                    if(creditFacExistingControl.isUsedInProposeCredit(existingCreditDetailViewDel.getId())){
                        messageHeader = msg.get("app.header.error");
                        message = msg.get("app.credit.facility.message.err.credit.inused.propose");
                        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                    } else {
                        List<ExistingCreditDetailView> rtComCreditDetailViewDeleteList = existingCreditFacilityView.getRelatedComExistingCreditDeleteList();
                        rtComCreditDetailViewDeleteList.add(existingCreditDetailViewDel);
                        existingCreditFacilityView.setRelatedComExistingCreditDeleteList(rtComCreditDetailViewDeleteList);
                        existingCreditFacilityView.getRelatedComExistingCredit().remove(existingCreditDetailViewDel);
                    }
                } else {
                    existingCreditFacilityView.getRelatedComExistingCredit().remove(existingCreditDetailViewDel);
                }
            }else{
                log.info("use > 0 ");
                messageHeader = msg.get("app.header.error");
                message = msg.get("app.credit.facility.message.err.credit.inused");
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            }
            onSetRowNoCreditDetail(existingCreditFacilityView.getRelatedComExistingCredit());
            calTotalCreditRelated();
            calTotalCreditGroup();
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

        if(creditType!=null && creditType.getId()!=0){
            if(CalLimitType.getCalLimitType(creditType.getCalLimitType()) == CalLimitType.PCE){
                BigDecimal limit = existingCreditDetailView.getLimit();
                BigDecimal percentPCE = existingCreditDetailView.getPcePercent();
                BigDecimal hundred = new BigDecimal(100);
                BigDecimal limitPCE = new BigDecimal(0);

                if(percentPCE==null) {
                    percentPCE = BigDecimal.ZERO;
                    existingCreditDetailView.setPcePercent(percentPCE);
                }

                if(limit!=null && limit.compareTo(BigDecimal.ZERO) > 0){
                    limitPCE = (limit.multiply(percentPCE)).divide(hundred,2,BigDecimal.ROUND_HALF_UP);
                }

                existingCreditDetailView.setPceLimit(limitPCE);
            }
        }
    }

    public void calPCELimit(){
        BigDecimal limit = existingCreditDetailView.getLimit();
        BigDecimal percentPCE = existingCreditDetailView.getPcePercent();
        BigDecimal hundred = new BigDecimal(100);
        BigDecimal limitPCE = new BigDecimal(0);

        if(percentPCE==null) {
            percentPCE = BigDecimal.ZERO;
            existingCreditDetailView.setPcePercent(percentPCE);
        }

        if(limit!=null && limit.compareTo(BigDecimal.ZERO) > 0){
            limitPCE = (limit.multiply(percentPCE)).divide(hundred,2,BigDecimal.ROUND_HALF_UP);
        }

        existingCreditDetailView.setPceLimit(limitPCE);
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
            BankAccountStatus bankAccountStatus = bankAccountStatusDAO.findById(existingCreditDetailView.getExistAccountStatusView().getId());

            existingCreditDetailView.setExistProductProgramView(productTransform.transformToView(productProgramDAO.findById(existingCreditDetailView.getExistProductProgramView().getId())));
            existingCreditDetailView.setExistCreditTypeView(productTransform.transformToView(creditType));
            existingCreditDetailView.setExistAccountStatusView(bankAccountStatusTransform.getBankAccountStatusView(bankAccountStatus));

            if(CalLimitType.getCalLimitType(creditType.getCalLimitType()) == CalLimitType.PCE){
                existingCreditDetailView.setUsePCE(true);
            } else {
                existingCreditDetailView.setUsePCE(false);
            }

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
            BankAccountStatus bankAccountStatus = bankAccountStatusDAO.findById(existingCreditDetailView.getExistAccountStatusView().getId());
            CreditType creditType = creditTypeDAO.findById(existingCreditDetailView.getExistCreditTypeView().getId());

            if(CalLimitType.getCalLimitType(creditType.getCalLimitType()) == CalLimitType.PCE){
                existingCreditDetailViewRow.setUsePCE(true);
            } else {
                existingCreditDetailViewRow.setUsePCE(false);
            }

            existingCreditDetailViewRow.setExistProductProgramView(productTransform.transformToView(productProgramDAO.findById(existingCreditDetailView.getExistProductProgramView().getId())));
            existingCreditDetailViewRow.setExistCreditTypeView(productTransform.transformToView(creditType));
            existingCreditDetailViewRow.setAccountName(existingCreditDetailView.getAccountName());
            existingCreditDetailViewRow.setAccountNumber(existingCreditDetailView.getAccountNumber());
            existingCreditDetailViewRow.setAccountSuf(existingCreditDetailView.getAccountSuf());
            existingCreditDetailViewRow.setExistAccountStatusView(bankAccountStatusTransform.getBankAccountStatusView(bankAccountStatus));

            existingCreditDetailViewRow.setLimit(existingCreditDetailView.getLimit());
            existingCreditDetailViewRow.setProductCode(existingCreditDetailView.getProductCode());
            existingCreditDetailViewRow.setProjectCode(existingCreditDetailView.getProjectCode());
            existingCreditDetailViewRow.setOutstanding(existingCreditDetailView.getOutstanding());
            existingCreditDetailViewRow.setPcePercent(existingCreditDetailView.getPcePercent());
            existingCreditDetailViewRow.setPceLimit(existingCreditDetailView.getPceLimit());
            existingCreditDetailViewRow.setProductSegment(existingCreditDetailView.getProductSegment());

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
            calTotalCreditGroup();
        }else if(typeOfList.equals("related")){
            calTotalCreditRelated();
            calTotalCreditGroup();
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
        BigDecimal totalBorrowerCom = BigDecimal.ZERO;
        BigDecimal totalBorrowerRetail = BigDecimal.ZERO;
        BigDecimal totalBorrowerRlos = BigDecimal.ZERO;

        BigDecimal totalBrCom = BigDecimal.ZERO;
        BigDecimal totalBrComOBOD = BigDecimal.ZERO;
        BigDecimal totalBrExpose = BigDecimal.ZERO;

        BigDecimal tmpTotal;

        int numberOfOD = 0;
        BigDecimal totalBrODLimit = BigDecimal.ZERO;

        if(existingCreditFacilityView.getBorrowerComExistingCredit() != null){
            for(int i=0;i< existingCreditFacilityView.getBorrowerComExistingCredit().size();i++){
                ExistingCreditDetailView existingCreditDetailViewTmp = existingCreditFacilityView.getBorrowerComExistingCredit().get(i);
                tmpTotal = new BigDecimal(0);
                if(existingCreditDetailViewTmp.getExistCreditTypeView()!=null && existingCreditDetailViewTmp.getExistCreditTypeView().getId()!=0){
                    switch (CalLimitType.getCalLimitType(existingCreditDetailViewTmp.getExistCreditTypeView().getCalLimitType())) {
                        case LIMIT:
                            tmpTotal = existingCreditDetailViewTmp.getLimit();
                            totalBorrowerCom = totalBorrowerCom.add(tmpTotal);
                            break;
                        case OUTSTANDING:
                            tmpTotal = existingCreditDetailViewTmp.getOutstanding();
                            totalBorrowerCom = totalBorrowerCom.add(tmpTotal);
                            break;
                        case PCE:
                            tmpTotal = existingCreditDetailViewTmp.getPceLimit();
                            totalBorrowerCom = totalBorrowerCom.add(tmpTotal);
                            break;
                        default:
                            tmpTotal = existingCreditDetailViewTmp.getLimit();
                            totalBorrowerCom = totalBorrowerCom.add(tmpTotal);
                            break;
                    }

                    if(CreditTypeGroup.getCreditTypeGroup(existingCreditDetailViewTmp.getExistCreditTypeView().getCreditGroup()) == CreditTypeGroup.OD){
                        numberOfOD = numberOfOD+1;
                        totalBrODLimit = totalBrODLimit.add(existingCreditDetailViewTmp.getLimit());
                    }
                } else {
                    tmpTotal = existingCreditDetailViewTmp.getLimit();
                    totalBorrowerCom = totalBorrowerCom.add(tmpTotal);
                }

                if((existingCreditDetailViewTmp.getExistCreditTypeView()!=null && existingCreditDetailViewTmp.getExistCreditTypeView().getId()!=0) &&
                        CreditTypeGroup.getCreditTypeGroup(existingCreditDetailViewTmp.getExistCreditTypeView().getCreditGroup()) == CreditTypeGroup.CASH_IN) {
                    totalBrComOBOD = totalBrComOBOD.add((tmpTotal));
                    totalBrExpose = totalBrExpose.add(tmpTotal);
                } else {
                    totalBrCom = totalBrCom.add(tmpTotal);
                    totalBrComOBOD = totalBrComOBOD.add(tmpTotal);
                    totalBrExpose = totalBrExpose.add(tmpTotal);
                }
            }
        }

        if(existingCreditFacilityView.getBorrowerRetailExistingCredit() != null){
            for(int i=0;i< existingCreditFacilityView.getBorrowerRetailExistingCredit().size();i++){
                ExistingCreditDetailView existingCreditDetailViewTmp = existingCreditFacilityView.getBorrowerRetailExistingCredit().get(i);
                if(existingCreditDetailViewTmp.getExistCreditTypeView()!=null && existingCreditDetailViewTmp.getExistCreditTypeView().getId()!=0){
                    switch (CalLimitType.getCalLimitType(existingCreditDetailViewTmp.getExistCreditTypeView().getCalLimitType())) {
                        case LIMIT:
                            totalBorrowerRetail = totalBorrowerRetail.add(existingCreditDetailViewTmp.getLimit());
                            break;
                        case OUTSTANDING:
                            totalBorrowerRetail = totalBorrowerRetail.add(existingCreditDetailViewTmp.getOutstanding());
                            break;
                        case PCE:
                            totalBorrowerRetail = totalBorrowerRetail.add(existingCreditDetailViewTmp.getPceLimit());
                            break;
                        default:
                            totalBorrowerRetail = totalBorrowerRetail.add(existingCreditDetailViewTmp.getLimit());
                            break;
                    }
                } else {
                    totalBorrowerRetail = totalBorrowerRetail.add(existingCreditDetailViewTmp.getLimit());
                }
            }

            totalBrExpose = totalBrExpose.add(totalBorrowerRetail);
        }

        if(existingCreditFacilityView.getBorrowerAppInRLOSCredit() != null){
            for(int i=0;i< existingCreditFacilityView.getBorrowerAppInRLOSCredit().size();i++){
                ExistingCreditDetailView existingCreditDetailViewTmp = existingCreditFacilityView.getBorrowerAppInRLOSCredit().get(i);
                if(existingCreditDetailViewTmp.getExistCreditTypeView()!=null && existingCreditDetailViewTmp.getExistCreditTypeView().getId()!=0){
                    switch (CalLimitType.getCalLimitType(existingCreditDetailViewTmp.getExistCreditTypeView().getCalLimitType())) {
                        case LIMIT:
                            totalBorrowerRlos = totalBorrowerRlos.add(existingCreditDetailViewTmp.getLimit());
                            break;
                        case OUTSTANDING:
                            totalBorrowerRlos = totalBorrowerRlos.add(existingCreditDetailViewTmp.getOutstanding());
                            break;
                        case PCE:
                            totalBorrowerRlos = totalBorrowerRlos.add(existingCreditDetailViewTmp.getPceLimit());
                            break;
                        default:
                            totalBorrowerRlos = totalBorrowerRlos.add(existingCreditDetailViewTmp.getLimit());
                            break;
                    }
                } else {
                    totalBorrowerRlos = totalBorrowerRlos.add(existingCreditDetailViewTmp.getLimit());
                }
            }

            totalBrExpose = totalBrExpose.add(totalBorrowerRlos);
        }

        existingCreditFacilityView.setTotalBorrowerComLimit(totalBorrowerCom);
        existingCreditFacilityView.setTotalBorrowerRetailLimit(totalBorrowerRetail);
        existingCreditFacilityView.setTotalBorrowerAppInRLOSLimit(totalBorrowerRlos);

        existingCreditFacilityView.setTotalBorrowerCom(totalBrCom);
        existingCreditFacilityView.setTotalBorrowerComOBOD(totalBrComOBOD);
        existingCreditFacilityView.setTotalBorrowerExposure(totalBrExpose);

        existingCreditFacilityView.setTotalBorrowerNumberOfExistingOD(new BigDecimal(numberOfOD));
        existingCreditFacilityView.setTotalBorrowerExistingODLimit(totalBrODLimit);

        //calTotalCreditGroup();

    }

    private void calTotalCreditRelated(){
        BigDecimal totalRelatedCom = BigDecimal.ZERO;
        BigDecimal totalRelatedRetail = BigDecimal.ZERO;
        BigDecimal totalRelatedRlos = BigDecimal.ZERO;

        BigDecimal totalRelCom = BigDecimal.ZERO;
        BigDecimal totalRelComOBOD = BigDecimal.ZERO;
        BigDecimal totalRelExpose = BigDecimal.ZERO;

        BigDecimal tmpTotal;

        if(existingCreditFacilityView.getRelatedComExistingCredit() != null){
            for(int i=0;i< existingCreditFacilityView.getRelatedComExistingCredit().size();i++){
                tmpTotal = new BigDecimal(0);
                ExistingCreditDetailView existingCreditDetailViewTmp = existingCreditFacilityView.getRelatedComExistingCredit().get(i);
                if(existingCreditDetailViewTmp.getExistCreditTypeView()!=null && existingCreditDetailViewTmp.getExistCreditTypeView().getId()!=0){
                    switch (CalLimitType.getCalLimitType(existingCreditDetailViewTmp.getExistCreditTypeView().getCalLimitType())) {
                        case LIMIT:
                            tmpTotal = existingCreditDetailViewTmp.getLimit();
                            totalRelatedCom = totalRelatedCom.add(tmpTotal);
                            break;
                        case OUTSTANDING:
                            tmpTotal = existingCreditDetailViewTmp.getOutstanding();
                            totalRelatedCom = totalRelatedCom.add(tmpTotal);
                            break;
                        case PCE:
                            tmpTotal = existingCreditDetailViewTmp.getPceLimit();
                            totalRelatedCom = totalRelatedCom.add(tmpTotal);
                            break;
                        default:
                            tmpTotal = existingCreditDetailViewTmp.getLimit();
                            totalRelatedCom = totalRelatedCom.add(tmpTotal);
                            break;
                    }
                } else {
                    tmpTotal = existingCreditDetailViewTmp.getLimit();
                    totalRelatedCom = totalRelatedCom.add(tmpTotal);
                }

                if((existingCreditDetailViewTmp.getExistCreditTypeView()!=null && existingCreditDetailViewTmp.getExistCreditTypeView().getId()!=0) &&
                        CreditTypeGroup.getCreditTypeGroup(existingCreditDetailViewTmp.getExistCreditTypeView().getCreditGroup()) == CreditTypeGroup.CASH_IN) {
                    totalRelComOBOD = totalRelComOBOD.add((tmpTotal));
                    totalRelExpose = totalRelExpose.add(tmpTotal);
                } else {
                    totalRelCom = totalRelCom.add(tmpTotal);
                    totalRelComOBOD = totalRelComOBOD.add(tmpTotal);
                    totalRelExpose = totalRelExpose.add(tmpTotal);
                }
            }
        }

        if(existingCreditFacilityView.getRelatedRetailExistingCredit() != null){
            for(int i=0;i< existingCreditFacilityView.getRelatedRetailExistingCredit().size();i++){
                ExistingCreditDetailView existingCreditDetailViewTmp = existingCreditFacilityView.getRelatedRetailExistingCredit().get(i);
                if(existingCreditDetailViewTmp.getExistCreditTypeView()!=null && existingCreditDetailViewTmp.getExistCreditTypeView().getId()!=0){
                    switch (CalLimitType.getCalLimitType(existingCreditDetailViewTmp.getExistCreditTypeView().getCalLimitType())) {
                        case LIMIT:
                            totalRelatedRetail = totalRelatedRetail.add(existingCreditDetailViewTmp.getLimit());
                            break;
                        case OUTSTANDING:
                            totalRelatedRetail = totalRelatedRetail.add(existingCreditDetailViewTmp.getOutstanding());
                            break;
                        case PCE:
                            totalRelatedRetail = totalRelatedRetail.add(existingCreditDetailViewTmp.getPceLimit());
                            break;
                        default:
                            totalRelatedRetail = totalRelatedRetail.add(existingCreditDetailViewTmp.getLimit());
                            break;
                    }
                } else {
                    totalRelatedRetail = totalRelatedRetail.add(existingCreditDetailViewTmp.getLimit());
                }
            }
            totalRelExpose = totalRelExpose.add(totalRelatedRetail);
        }

        if(existingCreditFacilityView.getRelatedAppInRLOSCredit() != null){
            for(int i=0;i< existingCreditFacilityView.getRelatedAppInRLOSCredit().size();i++){
                ExistingCreditDetailView existingCreditDetailViewTmp = existingCreditFacilityView.getRelatedAppInRLOSCredit().get(i);
                if(existingCreditDetailViewTmp.getExistCreditTypeView()!=null && existingCreditDetailViewTmp.getExistCreditTypeView().getId()!=0){
                    switch (CalLimitType.getCalLimitType(existingCreditDetailViewTmp.getExistCreditTypeView().getCalLimitType())) {
                        case LIMIT:
                            totalRelatedRlos = totalRelatedRlos.add(existingCreditDetailViewTmp.getLimit());
                            break;
                        case OUTSTANDING:
                            totalRelatedRlos = totalRelatedRlos.add(existingCreditDetailViewTmp.getOutstanding());
                            break;
                        case PCE:
                            totalRelatedRlos = totalRelatedRlos.add(existingCreditDetailViewTmp.getPceLimit());
                            break;
                        default:
                            totalRelatedRlos = totalRelatedRlos.add(existingCreditDetailViewTmp.getLimit());
                            break;
                    }
                } else {
                    totalRelatedRlos = totalRelatedRlos.add(existingCreditDetailViewTmp.getLimit());
                }
            }
            totalRelExpose = totalRelExpose.add(totalRelatedRlos);
        }
        existingCreditFacilityView.setTotalRelatedComLimit(totalRelatedCom);
        existingCreditFacilityView.setTotalRelatedRetailLimit(totalRelatedRetail);
        existingCreditFacilityView.setTotalRelatedAppInRLOSLimit(totalRelatedRlos);

        existingCreditFacilityView.setTotalRelatedCom(totalRelCom);
        existingCreditFacilityView.setTotalRelatedComOBOD(totalRelComOBOD);
        existingCreditFacilityView.setTotalRelatedExposure(totalRelExpose);
        //calTotalCreditGroup();

    }

    private void calTotalCreditGroup(){
        BigDecimal totalGroupCom = BigDecimal.ZERO;
        BigDecimal totalGroupComOBOD = BigDecimal.ZERO;
        BigDecimal totalGroupExposure = BigDecimal.ZERO;

        BigDecimal totalBorrowerCom = BigDecimal.ZERO;
        BigDecimal totalBorrowerComOBOD = BigDecimal.ZERO;
        BigDecimal totalBorrowerExposure = BigDecimal.ZERO;

        BigDecimal totalRelatedCom = BigDecimal.ZERO;
        BigDecimal totalRelatedComOBOD = BigDecimal.ZERO;
        BigDecimal totalRelatedExposure = BigDecimal.ZERO;

        if( existingCreditFacilityView.getTotalBorrowerCom() != null){
            totalBorrowerCom = existingCreditFacilityView.getTotalBorrowerCom();
        }
        if( existingCreditFacilityView.getTotalBorrowerComOBOD() != null){
            totalBorrowerComOBOD = existingCreditFacilityView.getTotalBorrowerComOBOD();
        }
        if( existingCreditFacilityView.getTotalBorrowerExposure() != null){
            totalBorrowerExposure = existingCreditFacilityView.getTotalBorrowerExposure();
        }

        if( existingCreditFacilityView.getTotalRelatedCom() != null){
            totalRelatedCom = existingCreditFacilityView.getTotalRelatedCom();
        }
        if( existingCreditFacilityView.getTotalRelatedComOBOD() != null){
            totalRelatedComOBOD = existingCreditFacilityView.getTotalRelatedComOBOD();
        }
        if( existingCreditFacilityView.getTotalRelatedExposure() != null){
            totalRelatedExposure = existingCreditFacilityView.getTotalRelatedExposure();
        }

        totalGroupCom = totalBorrowerCom.add(totalRelatedCom);
        totalGroupComOBOD = totalBorrowerComOBOD.add(totalRelatedComOBOD);
        totalGroupExposure = totalBorrowerExposure.add(totalRelatedExposure);

        existingCreditFacilityView.setTotalGroupCom(totalGroupCom);
        existingCreditFacilityView.setTotalGroupComOBOD(totalGroupComOBOD);
        existingCreditFacilityView.setTotalGroupExposure(totalGroupExposure);

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

        List<ExistingCreditTypeDetailView> existingCreditTypeDetailList;
        if(typeOfListCollateral.equals("borrower")){
            log.info("onEditExistingCollateral 3 b ");
            canSaveBorrowerCol = true;
            existingCreditTypeDetailList = findBorrowerCreditFacility();
            existingCollateralDetailView.setExistingCreditTypeDetailViewList(existingCreditTypeDetailList);

            if(selectCollateralDetail.getExistingCreditTypeDetailViewList()!=null && selectCollateralDetail.getExistingCreditTypeDetailViewList().size()>0){
                for (int i = 0; i < selectCollateralDetail.getExistingCreditTypeDetailViewList().size(); i++) {
                    for (int j = 0; j < existingCreditTypeDetailList.size(); j++) {
                        log.info("creditType at " + j + " seq is     " + existingCreditTypeDetailList.get(j).getNo());

                        if (selectCollateralDetail.getExistingCreditTypeDetailViewList().get(i).getNo() == existingCreditTypeDetailList.get(j).getNo()) {
                            existingCollateralDetailView.getExistingCreditTypeDetailViewList().get(j).setNoFlag(true);
                            break;
                        }
                    }
                }
            }

        }else  if(typeOfListCollateral.equals("related")){
            log.info("onEditExistingCollateral 3 r ");
            canSaveRelatedCol = true;
            existingCreditTypeDetailList = findRelatedCreditFacility();
            existingCollateralDetailView.setExistingCreditTypeDetailViewList(existingCreditTypeDetailList);

            if(selectCollateralDetail.getExistingCreditTypeDetailViewList()!=null && selectCollateralDetail.getExistingCreditTypeDetailViewList().size()>0){
                for (int i = 0; i < selectCollateralDetail.getExistingCreditTypeDetailViewList().size(); i++) {
                    for (int j = 0; j < existingCreditTypeDetailList.size(); j++) {
                        log.info("creditType at " + j + " seq is     " + existingCreditTypeDetailList.get(j).getNo());

                        if (selectCollateralDetail.getExistingCreditTypeDetailViewList().get(i).getNo() == existingCreditTypeDetailList.get(j).getNo()) {
                            existingCollateralDetailView.getExistingCreditTypeDetailViewList().get(j).setNoFlag(true);
                            break;
                        }
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
                //onSetRowNoCreditTypeDetail(existingCollateralDetailViewAdd.getExistingCreditTypeDetailViewList());

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
                //onSetRowNoCreditTypeDetail(existingCollateralDetailViewAdd.getExistingCreditTypeDetailViewList());
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
                            if (hashBorrower.containsKey(seqBorrowerTemp) && Integer.parseInt(hashBorrower.get(seqBorrowerTemp).toString()) > 0) {
                                hashBorrower.put(seqBorrowerTemp, 0);
                            }
                        }
                    }
                }

                for (int l=0;l<hashBorrower.size();l++){
                    log.info("hashBorrower.get(j) in use   :  "+ l + " is   " +hashBorrower.get(l+1).toString());
                }
                //onSetRowNoCreditTypeDetail(borrowerCollateralDetailViewRow.getExistingCreditTypeDetailViewList());
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
                            if (hashRelated.containsKey(seqRelatedTemp) && Integer.parseInt(hashRelated.get(seqRelatedTemp).toString()) > 0) {
                                hashRelated.put(seqRelatedTemp, 0);
                            }
                        }
                    }
                }

                for (int l=0;l<hashRelated.size();l++){
                    log.info("before hashRelated seq :  "+ l + " use is   " +hashRelated.get(l+1).toString());
                }
                //onSetRowNoCreditTypeDetail(relatedCollateralDetailViewRow.getExistingCreditTypeDetailViewList());
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
                if(hashBorrower.containsKey(borrowerCollateralDetailViewDel.getExistingCreditTypeDetailViewList().get(j).getNo()) &&
                        Integer.parseInt(hashBorrower.get(borrowerCollateralDetailViewDel.getExistingCreditTypeDetailViewList().get(j).getNo()).toString())>0){
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
                if(hashBorrower.containsKey(relatedCollateralDetailViewDel.getExistingCreditTypeDetailViewList().get(j).getNo()) &&
                        Integer.parseInt(hashBorrower.get(relatedCollateralDetailViewDel.getExistingCreditTypeDetailViewList().get(j).getNo()).toString())>0){
                    hashRelated.put(seqRelatedForDel, 0);
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

        canSaveGarantor = true;
        modeForButton = ModeForButton.EDIT;
        existingGuarantorDetailView = new ExistingGuarantorDetailView();

        Cloner cloner = new Cloner();
        existingGuarantorDetailView = cloner.deepClone(selectGuarantorDetail);

        existingGuarantorDetailView.setTcgLgNo(selectGuarantorDetail.getTcgLgNo());
        existingGuarantorDetailView.setGuarantorName(selectGuarantorDetail.getGuarantorName());
        existingGuarantorDetailView.setTotalLimitGuaranteeAmount(selectGuarantorDetail.getTotalLimitGuaranteeAmount());

        List<ExistingCreditTypeDetailView> existingCreditTypeDetailList;
        existingCreditTypeDetailList = findBorrowerCreditFacility();
        existingGuarantorDetailView.setExistingCreditTypeDetailViewList(existingCreditTypeDetailList);

        if(selectGuarantorDetail.getExistingCreditTypeDetailViewList()!=null && selectGuarantorDetail.getExistingCreditTypeDetailViewList().size()>0){
            for (int i = 0; i < selectGuarantorDetail.getExistingCreditTypeDetailViewList().size(); i++) {
                for (int j=0; j < existingCreditTypeDetailList.size(); j++) {
                    log.info("creditType at " + j + " seq is     " + existingCreditTypeDetailList.get(j).getNo());

                    if (selectGuarantorDetail.getExistingCreditTypeDetailViewList().get(i).getNo() == existingCreditTypeDetailList.get(j).getNo()) {
                        existingGuarantorDetailView.getExistingCreditTypeDetailViewList().get(j).setNoFlag(true);
                        existingGuarantorDetailView.getExistingCreditTypeDetailViewList().get(j).setGuaranteeAmount(selectGuarantorDetail.getExistingCreditTypeDetailViewList().get(i).getGuaranteeAmount());
                        break;
                    }
                }
            }
        }


        log.info("onEditExistingGuarantor end" );
    }


    public void onSaveExistingGuarantor() {
        RequestContext context = RequestContext.getCurrentInstance();

        boolean complete;
        boolean checkPlus;

        int seqBorrowerTemp;

        BigDecimal summaryGuaranteeAmount = BigDecimal.ZERO;

        if(modeForButton != null && modeForButton.equals(ModeForButton.ADD)){
            ExistingGuarantorDetailView existingGuarantorDetailViewAdd = new ExistingGuarantorDetailView();
            existingGuarantorDetailViewAdd.setNo(borrowerExistingGuarantorDetailViewList.size()+1);

            if(existingGuarantorDetailView.getGuarantorName() != null){
                if(existingGuarantorDetailView.getGuarantorName().getId() == -1){
                    existingGuarantorDetailViewAdd.setGuarantorCategory(GuarantorCategory.TCG);
                    CustomerInfoView customerInfoView = new CustomerInfoView();
                    customerInfoView.setId(-1);
                    customerInfoView.setFirstNameTh(msg.get("app.select.tcg"));
                    existingGuarantorDetailViewAdd.setGuarantorName(customerInfoView);
                } else {
                    CustomerInfoView customerInfoView = customerInfoControl.getCustomerInfoViewById(existingGuarantorDetailView.getGuarantorName().getId(), guarantorList);
                    existingGuarantorDetailViewAdd.setGuarantorName(customerInfoView);
                    if (customerInfoView.getCustomerEntity().getId() == GuarantorCategory.INDIVIDUAL.value()) {
                        existingGuarantorDetailViewAdd.setGuarantorCategory(GuarantorCategory.INDIVIDUAL);
                    } else if (customerInfoView.getCustomerEntity().getId() == GuarantorCategory.JURISTIC.value()) {
                        existingGuarantorDetailViewAdd.setGuarantorCategory(GuarantorCategory.JURISTIC);
                    } else {
                        existingGuarantorDetailViewAdd.setGuarantorCategory(GuarantorCategory.NA);
                    }
                }
            } else {
                existingGuarantorDetailViewAdd.setGuarantorName(null);
                existingGuarantorDetailViewAdd.setGuarantorCategory(null);
            }

            existingGuarantorDetailViewAdd.setTcgLgNo(existingGuarantorDetailView.getTcgLgNo());
            existingGuarantorDetailViewAdd.setExistingCreditTypeDetailViewList(new ArrayList<ExistingCreditTypeDetailView>());
            for (ExistingCreditTypeDetailView existingCreditTypeDetail : existingGuarantorDetailView.getExistingCreditTypeDetailViewList()) {
                if (existingCreditTypeDetail.isNoFlag()) {
                    existingGuarantorDetailViewAdd.getExistingCreditTypeDetailViewList().add(existingCreditTypeDetail);
                    summaryGuaranteeAmount = summaryGuaranteeAmount.add(existingCreditTypeDetail.getGuaranteeAmount());
                    seqBorrowerTemp = existingCreditTypeDetail.getNo();
                    hashBorrower.put(seqBorrowerTemp, 1);
                }
            }
            existingGuarantorDetailViewAdd.setTotalLimitGuaranteeAmount(summaryGuaranteeAmount);
            borrowerExistingGuarantorDetailViewList.add(existingGuarantorDetailViewAdd);
            existingCreditFacilityView.setBorrowerGuarantorList(borrowerExistingGuarantorDetailViewList);
        }else if(modeForButton != null && modeForButton.equals(ModeForButton.EDIT)){
            ExistingGuarantorDetailView existingGuarantorDetailViewOnRow = borrowerExistingGuarantorDetailViewList.get(rowIndex);
            existingGuarantorDetailViewOnRow.setExistingCreditTypeDetailViewList(new ArrayList<ExistingCreditTypeDetailView>());
            existingGuarantorDetailViewOnRow.setTcgLgNo(existingGuarantorDetailView.getTcgLgNo());

            if(existingGuarantorDetailView.getGuarantorName() != null){
                if(existingGuarantorDetailView.getGuarantorName().getId() == -1){
                    existingGuarantorDetailViewOnRow.setGuarantorCategory(GuarantorCategory.TCG);
                    CustomerInfoView customerInfoView = new CustomerInfoView();
                    customerInfoView.setId(-1);
                    customerInfoView.setFirstNameTh(msg.get("app.select.tcg"));
                    existingGuarantorDetailViewOnRow.setGuarantorName(customerInfoView);
                } else {
                    CustomerInfoView customerInfoView = customerInfoControl.getCustomerInfoViewById(existingGuarantorDetailView.getGuarantorName().getId(), guarantorList);
                    existingGuarantorDetailViewOnRow.setGuarantorName(customerInfoView);
                    if (customerInfoView.getCustomerEntity().getId() == GuarantorCategory.INDIVIDUAL.value()) {
                        existingGuarantorDetailViewOnRow.setGuarantorCategory(GuarantorCategory.INDIVIDUAL);
                    } else if (customerInfoView.getCustomerEntity().getId() == GuarantorCategory.JURISTIC.value()) {
                        existingGuarantorDetailViewOnRow.setGuarantorCategory(GuarantorCategory.JURISTIC);
                    } else {
                        existingGuarantorDetailViewOnRow.setGuarantorCategory(GuarantorCategory.NA);
                    }
                }
            } else {
                existingGuarantorDetailViewOnRow.setGuarantorName(null);
                existingGuarantorDetailViewOnRow.setGuarantorCategory(null);
            }

            List<ExistingCreditTypeDetailView> existingCreditTypeDetailViewList;
            existingCreditTypeDetailViewList = existingGuarantorDetailView.getExistingCreditTypeDetailViewList();
            if(existingCreditTypeDetailViewList!=null && existingCreditTypeDetailViewList.size()>0){
                for (int i = 0; i < existingCreditTypeDetailViewList.size(); i++) {
                    seqBorrowerTemp = existingCreditTypeDetailViewList.get(i).getNo();
                    if (existingCreditTypeDetailViewList.get(i).isNoFlag() == true) {

                        summaryGuaranteeAmount = summaryGuaranteeAmount.add(existingCreditTypeDetailViewList.get(i).getGuaranteeAmount());
                        checkPlus = true;

                        for (int j = 0; j <selectGuarantorDetail.getExistingCreditTypeDetailViewList().size(); j++) {
                            if (selectGuarantorDetail.getExistingCreditTypeDetailViewList().get(j).getNo() == seqBorrowerTemp) {
                                checkPlus = false;
                            }
                            if (checkPlus) {
                                hashBorrower.put(seqBorrowerTemp, 1);
                            }
                        }
                        existingGuarantorDetailViewOnRow.getExistingCreditTypeDetailViewList().add(existingCreditTypeDetailViewList.get(i));
                    } else if (existingCreditTypeDetailViewList.get(i).isNoFlag() == false) {
                        if (hashBorrower.containsKey(seqBorrowerTemp) && Integer.parseInt(hashBorrower.get(seqBorrowerTemp).toString()) > 0) {
                            hashBorrower.put(seqBorrowerTemp, 0);
                        }
                    }
                    existingGuarantorDetailViewOnRow.setTotalLimitGuaranteeAmount(summaryGuaranteeAmount);
                }
            }
            existingCreditFacilityView.setBorrowerGuarantorList(borrowerExistingGuarantorDetailViewList);
        } else {
            log.info("onSaveCreditDetail ::: Undefined modeForButton !!");
        }
        calTotalGuarantor();
        complete = true;
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
        ExistingGuarantorDetailView guarantorDetailViewDel = selectGuarantorDetail;

        for(int j=0;j<guarantorDetailViewDel.getExistingCreditTypeDetailViewList().size();j++){
            int seqBowForDel = guarantorDetailViewDel.getExistingCreditTypeDetailViewList().get(j).getNo();
            if(hashBorrower.containsKey(guarantorDetailViewDel.getExistingCreditTypeDetailViewList().get(j).getNo()) &&
                    Integer.parseInt(hashBorrower.get(guarantorDetailViewDel.getExistingCreditTypeDetailViewList().get(j).getNo()).toString())>0){
                hashBorrower.put(seqBowForDel, 0);
            }
        }

        borrowerExistingGuarantorDetailViewList.remove(guarantorDetailViewDel);
        onSetRowNoGuarantorDetail(borrowerExistingGuarantorDetailViewList);
        calTotalGuarantor();
    }

    private void onSetInUsed(){
        int inUsed;
        int seq;
        if(existingCreditFacilityView.getBorrowerComExistingCredit()!=null && existingCreditFacilityView.getBorrowerComExistingCredit().size()>0){
            for(int i = 0; i < existingCreditFacilityView.getBorrowerComExistingCredit().size(); i++) {
                seq = existingCreditFacilityView.getBorrowerComExistingCredit().get(i).getNo();
                inUsed = 0;
                if(hashBorrower.containsKey(seq)) {
                    inUsed = Integer.parseInt(hashBorrower.get(seq).toString());
                }
                existingCreditFacilityView.getBorrowerComExistingCredit().get(i).setInUsed(inUsed);
            }
        }

        if(existingCreditFacilityView.getRelatedComExistingCredit()!=null && existingCreditFacilityView.getRelatedComExistingCredit().size()>0){
            for (int i = 0; i < existingCreditFacilityView.getRelatedComExistingCredit().size(); i++) {
                seq = existingCreditFacilityView.getRelatedComExistingCredit().get(i).getNo();
                inUsed = 0;
                if(hashRelated.containsKey(seq)) {
                    inUsed = Integer.parseInt(hashRelated.get(seq).toString());
                }
                existingCreditFacilityView.getRelatedComExistingCredit().get(i).setInUsed(inUsed);
            }
        }

    }

    public void onSaveCreditFacExisting() {
        onSetInUsed();
        try {
            creditFacExistingControl.onSaveExistingCreditFacility(existingCreditFacilityView ,workCaseId,user);
            proposeLineControl.calculateTotalProposeAmountForExisting(existingCreditFacilityView, workCaseId);
            messageHeader = msg.get("app.header.save.success");
            message = msg.get("app.credit.facility.message.save.success");
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageRefreshDlg.show()");
        } catch (Exception ex) {
            log.error("Exception : {}", ex);
            messageHeader = msg.get("app.credit.facility.message.save.failed");

            if (ex.getCause() != null) {
                message = msg.get("app.credit.facility.message.save.failed") + " cause : " + ex.getCause().toString();
            } else {
                message = msg.get("app.credit.facility.message.save.failed") + ex.getMessage();
            }
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public void onRefreshExistingCredit() {
        log.info("Start on Retrieve Interface Info");

        Cloner cloner = new Cloner();
        List<ExistingCreditDetailView> existingBrwCreditDetailViews = cloner.deepClone(existingCreditFacilityView.getBorrowerComExistingCredit());
        List<ExistingCreditDetailView> existingRelCreditDetailViews = cloner.deepClone(existingCreditFacilityView.getRelatedComExistingCredit());
        if(existingBrwCreditDetailViews!=null && existingBrwCreditDetailViews.size()>0){
            for(ExistingCreditDetailView brExistingCreditDetailView : existingBrwCreditDetailViews){
                if(creditFacExistingControl.isUsedInProposeCredit(brExistingCreditDetailView.getId())){
                    messageHeader = msg.get("app.header.error");
                    message = msg.get("app.credit.facility.message.err.credit.inused.propose");
                    RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                    return;
                }
            }
        }

        if(existingRelCreditDetailViews!=null && existingRelCreditDetailViews.size()>0){
            for(ExistingCreditDetailView relExistingCreditDetailView : existingRelCreditDetailViews){
                if(creditFacExistingControl.isUsedInProposeCredit(relExistingCreditDetailView.getId())){
                    messageHeader = msg.get("app.header.error");
                    message = msg.get("app.credit.facility.message.err.credit.inused.propose");
                    RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                    return;
                }
            }
        }


        List<CustomerInfoView> customerInfoViews = creditFacExistingControl.getCustomerListByWorkCaseId(workCaseId);
        List<CustomerInfoView> customerInfoViewList = new ArrayList<CustomerInfoView>();
        if(customerInfoViews != null){
            customerInfoViewList = generateCustomerInfoList(customerInfoViews);
        }

        clearExistingCreditFacilityView();

        ExistingCreditFacilityView existingCreditFacilityViewTmp = creditFacExistingControl.onFindExistingCreditFacility(workCaseId);
        existingCreditFacilityView = existingCreditControl.refreshExistingCredit(customerInfoViewList);
        /*existingCreditFacilityView.setBorrowerComExistingCredit(new ArrayList<ExistingCreditDetailView>());
        existingCreditFacilityView.setRelatedComExistingCredit(new ArrayList<ExistingCreditDetailView>());*/

        existingCreditFacilityView.setBorrowerComExistingCreditDeleteList(new ArrayList<ExistingCreditDetailView>());
        existingCreditFacilityView.setBorrowerRetailExistingCreditDeleteList(new ArrayList<ExistingCreditDetailView>());
        existingCreditFacilityView.setBorrowerAppInRLOSCreditDeleteList(new ArrayList<ExistingCreditDetailView>());
        existingCreditFacilityView.setBorrowerExistingCreditPreScreenDeleteList(new ArrayList<ExistingCreditDetailView>());

        existingCreditFacilityView.setRelatedComExistingCreditDeleteList(new ArrayList<ExistingCreditDetailView>());
        existingCreditFacilityView.setRelatedRetailExistingCreditDeleteList(new ArrayList<ExistingCreditDetailView>());
        existingCreditFacilityView.setRelatedAppInRLOSCreditDeleteList(new ArrayList<ExistingCreditDetailView>());
        existingCreditFacilityView.setRelateExistingCreditPresScreenDeleteList(new ArrayList<ExistingCreditDetailView>());

        if(existingBrwCreditDetailViews!=null && existingBrwCreditDetailViews.size()>0){
            existingCreditFacilityView.setBorrowerComExistingCreditDeleteList(existingBrwCreditDetailViews);
        }

        if(existingRelCreditDetailViews!=null && existingRelCreditDetailViews.size()>0){
            existingCreditFacilityView.setRelatedComExistingCreditDeleteList(existingRelCreditDetailViews);
        }

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

        calTotalCreditBorrower();
        calTotalCreditRelated();
        calTotalCreditGroup();

        messageHeader = msg.get("app.header.information");
        message = msg.get("app.credit.facility.message.success.refresh");
        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
    }

    public List<CustomerInfoView> generateCustomerInfoList(List<CustomerInfoView> customerInfoViews){
        List<CustomerInfoView> customerInfoList = new ArrayList<CustomerInfoView>();
        for(CustomerInfoView item : customerInfoViews){
            customerInfoList.add(item);
            if(item.getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()){
                if(item.getMaritalStatus() != null && item.getMaritalStatus().getSpouseFlag() == 1){
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
        existAccountStatusView = new BankAccountStatusView();

        existingCreditDetailView.setExistAccountStatusView(existAccountStatusView);
        existingCreditDetailView.setExistProductProgramView(existProductProgramView);
        existingCreditDetailView.setExistCreditTypeView(existCreditTypeView);

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

    public List<ProductProgramView> getProductProgramViewListSplit() {
        return productProgramViewListSplit;
    }

    public void setProductProgramViewListSplit(List<ProductProgramView> productProgramViewListSplit) {
        this.productProgramViewListSplit = productProgramViewListSplit;
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

    public boolean isUsePCE() {
        return isUsePCE;
    }

    public void setUsePCE(boolean usePCE) {
        isUsePCE = usePCE;
    }
}
