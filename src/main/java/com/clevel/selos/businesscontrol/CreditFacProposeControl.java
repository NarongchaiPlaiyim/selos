package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.relation.PrdProgramToCreditTypeDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.DBRMethod;
import com.clevel.selos.model.ExposureMethod;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.relation.PrdProgramToCreditType;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.transform.*;
import com.clevel.selos.util.Util;
import com.clevel.selos.util.ValidationUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
public class CreditFacProposeControl extends BusinessControl {
    @SELOS
    @Inject
    Logger log;
    @Inject
    CustomerTransform customerTransform;
    @Inject
    NewCreditFacilityTransform newCreditFacilityTransform;
    @Inject
    NewFeeDetailTransform newFeeDetailTransform;
    @Inject
    NewCreditDetailTransform newCreditDetailTransform;
    @Inject
    CreditTypeDetailTransform creditTypeDetailTransform;
    @Inject
    NewCollateralInfoTransform newCollateralInfoTransform;
    @Inject
    NewCollHeadDetailTransform newCollHeadDetailTransform;
    @Inject
    NewSubCollDetailTransform newSubCollDetailTransform;
    @Inject
    NewGuarantorDetailTransform newGuarantorDetailTransform;
    @Inject
    NewConditionDetailTransform newConditionDetailTransform;
    @Inject
    NewCreditTierTransform newCreditTierTransform;
    @Inject
    SubCollateralTypeDAO subCollateralTypeDAO;
    @Inject
    CollateralTypeDAO collateralTypeDAO;
    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    NewCreditFacilityDAO newCreditFacilityDAO;
    @Inject
    NewFeeCreditDAO newFeeCreditDAO;
    @Inject
    NewConditionDetailDAO newConditionDetailDAO;
    @Inject
    NewCreditDetailDAO newCreditDetailDAO;
    @Inject
    NewCreditTierDetailDAO newCreditTierDetailDAO;
    @Inject
    NewGuarantorDetailDAO newGuarantorDetailDAO;
    @Inject
    CreditTypeDetailDAO creditTypeDetailDAO;
    @Inject
    NewCollateralDetailDAO newCollateralDetailDAO;
    @Inject
    NewCollateralSubDetailDAO newCollateralSubDetailDAO;
    @Inject
    NewCollateralHeadDetailDAO newCollateralHeadDetailDAO;
    @Inject
    ExistingCreditDetailDAO existingCreditDetailDAO;
    @Inject
    ExistingCreditFacilityDAO existingCreditFacilityDAO;
    @Inject
    CreditFacExistingControl creditFacExistingControl;
    @Inject
    ProductFormulaDAO productFormulaDAO;
    @Inject
    PrdProgramToCreditTypeDAO prdProgramToCreditTypeDAO;
    @Inject
    BasicInfoControl basicInfoControl;
    @Inject
    CustomerInfoControl customerInfoControl;
    @Inject
    TCGInfoControl tcgInfoControl;
    @Inject
    ProductProgramDAO productProgramDAO;
    @Inject
    CreditTypeDAO creditTypeDAO;
    @Inject
    NewGuarantorRelationDAO newGuarantorRelationDAO;
    @Inject
    NewCollateralRelationDAO newCollateralRelationDAO;
    @Inject
    CustomerDAO customerDAO;
    @Inject
    NewSubCollCustomerDAO newSubCollCustomerDAO;
    @Inject
    NewSubCollMortgageDAO newSubCollMortgageDAO;
    @Inject
    NewSubCollRelateDAO newSubCollRelateDAO;
    @Inject
    MortgageTypeDAO mortgageTypeDAO;


    public CreditFacProposeControl() {
    }

    public NewCreditFacility getNewCreditFacilityViewByWorkCaseId(long workCaseId) {
        log.info("workCaseId :: {}", workCaseId);
        return newCreditFacilityDAO.findByWorkCaseId(workCaseId);
    }

    public NewCreditFacilityView findNewCreditFacilityByWorkCase(long workCaseId) {
        NewCreditFacilityView newCreditFacilityView = null;
        log.info("findNewCreditFacilityByWorkCase start ::::");
        try {
            WorkCase workCase = workCaseDAO.findById(workCaseId);
            if (workCase != null) {
                NewCreditFacility newCreditFacility = newCreditFacilityDAO.findByWorkCase(workCase);
                if (newCreditFacility != null) {
                    newCreditFacilityView = newCreditFacilityTransform.transformToView(newCreditFacility);

                    if (newCreditFacility.getNewFeeDetailList() != null) {
                        log.info("newCreditFacility.getNewFeeDetailList() :: {}", newCreditFacility.getNewFeeDetailList().size());
                        List<NewFeeDetailView> newFeeDetailViewList = newFeeDetailTransform.transformToView(newCreditFacility.getNewFeeDetailList());
                        newCreditFacilityView.setNewFeeDetailViewList(newFeeDetailViewList);
                    }

                    if (newCreditFacility.getNewCreditDetailList() != null) {
                        log.info("newCreditFacility.getNewCreditDetailList() :; {}", newCreditFacility.getNewCreditDetailList().size());
                        List<NewCreditDetailView> newCreditDetailViewList = newCreditDetailTransform.transformToView(newCreditFacility.getNewCreditDetailList());
                        newCreditFacilityView.setNewCreditDetailViewList(newCreditDetailViewList);
                    }

                    if (newCreditFacility.getNewGuarantorDetailList() != null) {
                        log.info("newCreditFacility.getNewGuarantorDetailList() :: {}", newCreditFacility.getNewGuarantorDetailList().size());
                        List<NewGuarantorDetailView> newGuarantorDetailViewList = newGuarantorDetailTransform.transformToView(newCreditFacility.getNewGuarantorDetailList());
                        newCreditFacilityView.setNewGuarantorDetailViewList(newGuarantorDetailViewList);
                    }

                    if (newCreditFacility.getNewCollateralDetailList() != null) {
                        log.info("newCreditFacility.getNewCollateralDetailList() :: {}", newCreditFacility.getNewCollateralDetailList().size());
                        List<NewCollateralInfoView> newCollateralInfoViewList = newCollateralInfoTransform.transformsToView(newCreditFacility.getNewCollateralDetailList());
                        newCreditFacilityView.setNewCollateralInfoViewList(newCollateralInfoViewList);
                    }

                    if (newCreditFacility.getNewConditionDetailList() != null) {
                        log.info("newCreditFacility.getNewConditionDetailList() :: {}", newCreditFacility.getNewConditionDetailList().size());
                        List<NewConditionDetailView> newConditionDetailViewList = newConditionDetailTransform.transformToView(newCreditFacility.getNewConditionDetailList());
                        newCreditFacilityView.setNewConditionDetailViewList(newConditionDetailViewList);
                    }
                }
            }
        } catch (Exception e) {
            log.error("findNewCreditFacilityByWorkCase  error ::: {}", e.getMessage());
        } finally {
            log.info("findNewCreditFacilityByWorkCase end");
        }

        return newCreditFacilityView;
    }

    public void onSaveNewCreditFacility(NewCreditFacilityView newCreditFacilityView, long workCaseId) {
        log.info("onSaveNewCreditFacility begin");
        log.info("workCaseId {} ", workCaseId);
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        User user = getCurrentUser();
        Date createDate = DateTime.now().toDate();
        Date modifyDate = DateTime.now().toDate();

        NewCreditFacility newCreditFacility = newCreditFacilityDAO.findByWorkCase(workCase);

        if (newCreditFacility == null) {
            newCreditFacility = newCreditFacilityTransform.transformToModelDB(newCreditFacilityView, workCase, user);
        }

        newCreditFacilityDAO.persist(newCreditFacility);
        log.info("persist :: creditFacilityPropose...");

        if (newCreditFacilityView.getNewFeeDetailViewList().size() > 0) {
            //Delete NewFeeDetail
            List<NewFeeDetail> newFeeDetailListDelete = newFeeCreditDAO.findByNewCreditFacility(newCreditFacility);
            if (newFeeDetailListDelete.size() > 0) {
                newFeeCreditDAO.delete(newFeeDetailListDelete);
                log.info(" End Delete  newFeeDetailListDelete :: ");
            }

            log.info("newCreditFacilityView.getNewFeeDetailViewList().size() ::  {}", newCreditFacilityView.getNewFeeDetailViewList().size());
            List<NewFeeDetail> newFeeDetailList = newFeeDetailTransform.transformToModel(newCreditFacilityView.getNewFeeDetailViewList(), newCreditFacility, user);
            newFeeCreditDAO.persist(newFeeDetailList);
            log.info("persist :: newFeeDetailList...");
        }

        if (newCreditFacilityView.getNewConditionDetailViewList().size() > 0) {
            //Delete NewConditionDetail
            List<NewConditionDetail> newConditionDetailListDelete = newConditionDetailDAO.findByNewCreditFacility(newCreditFacility);
            if (newConditionDetailListDelete.size() > 0) {
                newConditionDetailDAO.delete(newConditionDetailListDelete);
                log.info(" End Delete  newConditionDetailListDelete :: ");
            }

            log.info("newCreditFacilityView.getNewConditionDetailViewList().size() :: {}", newCreditFacilityView.getNewConditionDetailViewList().size());

            List<NewConditionDetail> newConditionDetailList = newConditionDetailTransform.transformToModel(newCreditFacilityView.getNewConditionDetailViewList(), newCreditFacility, user);
            newConditionDetailDAO.persist(newConditionDetailList);
            log.info("persist :: newConditionDetail ...");
        }


        if (newCreditFacilityView.getNewGuarantorDetailViewList() != null) {
            List<NewGuarantorDetail> newGuarantorDetailsDel = newGuarantorDetailDAO.findNewGuarantorByNewCreditFacility(newCreditFacility);
            if (newGuarantorDetailsDel != null) {
                for (NewGuarantorDetail newGuarantorDetailForDelete : newGuarantorDetailsDel) {
                    //Delete NewGuarantorRelCredit
                    List<NewGuarantorRelCredit> newGuarantorRelCreditDel = newGuarantorRelationDAO.getListGuarantorRelationByNewGuarantor(newGuarantorDetailForDelete);

                    if (newGuarantorRelCreditDel.size() > 0) {
                        log.info("newGuarantorRelCreditDel ::: length :: {}", newGuarantorRelCreditDel);
                        newGuarantorRelationDAO.delete(newGuarantorRelCreditDel);
                    }

                    newGuarantorDetailDAO.delete(newGuarantorDetailForDelete);
                    log.info("delete newGuarantorDetailForDelete :: ");
                }
            }

            List<NewGuarantorDetail> newGuarantorList = newGuarantorDetailTransform.transformToModel(newCreditFacilityView.getNewGuarantorDetailViewList(), newCreditFacility, user);
            newGuarantorDetailDAO.persist(newGuarantorList);
            log.info("persist newGuarantorList :: {}", newGuarantorList.size());
        }

        if (newCreditFacilityView.getNewCollateralInfoViewList() != null) {

            //NewCollateralDetail
            List<NewCollateralDetail> newCollateralDetails = newCollateralDetailDAO.findNewCollateralByNewCreditFacility(newCreditFacility);

            if (newCollateralDetails != null) {
                for (NewCollateralDetail newCollateralDelete : newCollateralDetails) {
                    //NewCollateralRelCredit
                    List<NewCollateralRelCredit> newCollateralRelCredits = newCollateralRelationDAO.getListCollRelationByNewCollateral(newCollateralDelete);

                    if (newCollateralRelCredits.size() > 0) {
                        log.info("newCollateralRelCredits ::: length :: {}", newCollateralRelCredits);
                        newCollateralRelationDAO.delete(newCollateralRelCredits);
                    }

                    //NewCollateralHeadDetail
                    List<NewCollateralHeadDetail> newCollateralHeadDetailsDel = newCollateralHeadDetailDAO.findByNewCollateralDetail(newCollateralDelete);

                    if (newCollateralHeadDetailsDel.size() > 0) {
                        for (NewCollateralHeadDetail newCollateralHeadDetailDel : newCollateralHeadDetailsDel) {

                            //newCollateralSubDetails
                            List<NewCollateralSubDetail> newCollateralSubDetails = newCollateralSubDetailDAO.getAllNewSubCollateralDetail(newCollateralHeadDetailDel);

                            if (newCollateralSubDetails.size() > 0) {
                                for (NewCollateralSubDetail newCollateralSubDelete : newCollateralSubDetails) {
                                    //NewCollateralSubCustomer
                                    List<NewCollateralSubCustomer> newCollateralSubCustomersDel = newSubCollCustomerDAO.getListNewCollateralSubCustomer(newCollateralSubDelete);
                                    if (newCollateralSubCustomersDel.size() > 0) {
                                        newSubCollCustomerDAO.delete(newCollateralSubCustomersDel);
                                        log.info("delete newCollateralSubCustomersDel :: ");
                                    }
                                    //NewCollateralSubMortgage
                                    List<NewCollateralSubMortgage> newCollateralSubMortgagesDel = newSubCollMortgageDAO.getListNewCollateralSubMortgage(newCollateralSubDelete);
                                    if (newCollateralSubMortgagesDel.size() > 0) {
                                        newSubCollMortgageDAO.delete(newCollateralSubMortgagesDel);
                                        log.info("delete newCollateralSubMortgagesDel :: ");
                                    }
                                    //NewCollateralSubRelate
                                    List<NewCollateralSubRelate> newCollateralSubRelatesDel = newSubCollRelateDAO.getListNewCollateralSubRelate(newCollateralSubDelete);
                                    if (newCollateralSubRelatesDel.size() > 0) {
                                        newSubCollRelateDAO.delete(newCollateralSubRelatesDel);
                                        log.info("delete newCollateralSubRelatesDel ::");
                                    }
                                    newCollateralSubDetailDAO.delete(newCollateralSubDelete);
                                    log.info("Delete :: newCollateralSubDelete");
                                }
                            }

                            newCollateralHeadDetailDAO.delete(newCollateralHeadDetailDel);
                            log.info("delete newCollateralHeadDetailDel ::");
                        }
                    }
                    newCollateralDetailDAO.delete(newCollateralDelete);
                    log.info("delete newCollateralDelete :: ");
                }
            }
            for (NewCollateralInfoView newCollateralInfoView : newCreditFacilityView.getNewCollateralInfoViewList()) {
                NewCollateralDetail newCollateralDetail = newCollateralInfoTransform.transformsNewCollateralInfoViewToModel(newCollateralInfoView, newCreditFacility, user);
                newCollateralDetailDAO.persist(newCollateralDetail);
                log.info("persist newCollateralDetail :: {}", newCollateralDetail.getId());


                if (newCollateralInfoView.getNewCollateralHeadDetailViewList() != null) {
                    for (NewCollateralHeadDetailView newCollateralHeadDetailView : newCollateralInfoView.getNewCollateralHeadDetailViewList()) {
                        NewCollateralHeadDetail newCollateralHeadDetail = newCollHeadDetailTransform.transformNewCollateralHeadDetailViewToModel(newCollateralHeadDetailView, newCollateralDetail, user);
                        newCollateralHeadDetailDAO.persist(newCollateralHeadDetail);
                        log.info("persist newCollateralHeadDetail...{}", newCollateralHeadDetail.getId());

                        if (newCollateralHeadDetailView.getNewSubCollateralDetailViewList() != null) {
                            for (NewSubCollateralDetailView newSubCollateralDetailView : newCollateralHeadDetailView.getNewSubCollateralDetailViewList()) {
                                NewCollateralSubDetail newCollateralSubDetail = newSubCollDetailTransform.transformNewSubCollateralDetailViewToModel(newSubCollateralDetailView, newCollateralHeadDetail, user);
                                newCollateralSubDetailDAO.persist(newCollateralSubDetail);
                                log.info("persist newCollateralSubDetail...{}", newCollateralSubDetail.getId());

                                for (NewSubCollateralDetailView newSubCollateralView : newCollateralHeadDetailView.getNewSubCollateralDetailViewList()) {
                                    if (newSubCollateralView.getCollateralOwnerUWList() != null) {
                                        List<NewCollateralSubCustomer> newCollateralSubCustomerList = new ArrayList<NewCollateralSubCustomer>();
                                        NewCollateralSubCustomer newCollateralSubCustomer;
                                        for (CustomerInfoView customerInfoView : newSubCollateralView.getCollateralOwnerUWList()) {
                                            Customer customer = customerDAO.findById(customerInfoView.getId());
                                            newCollateralSubCustomer = new NewCollateralSubCustomer();
                                            newCollateralSubCustomer.setCustomer(customer);
                                            newCollateralSubCustomer.setNewCollateralSubDetail(newCollateralSubDetail);
                                            newCollateralSubCustomerList.add(newCollateralSubCustomer);
                                        }

                                        newSubCollCustomerDAO.persist(newCollateralSubCustomerList);
                                        log.info("persist newCollateralSubCustomerList...{}", newCollateralSubCustomerList.size());

                                    }

                                    if (newSubCollateralView.getMortgageList() != null) {
                                        List<NewCollateralSubMortgage> newCollateralSubMortgageList = new ArrayList<NewCollateralSubMortgage>();
                                        NewCollateralSubMortgage newCollateralSubMortgage;

                                        for (MortgageType mortgageType : newSubCollateralView.getMortgageList()) {
                                            MortgageType mortgage = mortgageTypeDAO.findById(mortgageType.getId());
                                            newCollateralSubMortgage = new NewCollateralSubMortgage();
                                            newCollateralSubMortgage.setMortgageType(mortgage);
                                            newCollateralSubMortgage.setNewCollateralSubDetail(newCollateralSubDetail);
                                            newCollateralSubMortgageList.add(newCollateralSubMortgage);
                                        }

                                        newSubCollMortgageDAO.persist(newCollateralSubMortgageList);
                                        log.info("persist newCollateralSubMortgageList...{}", newCollateralSubMortgageList.size());

                                    }

                                    if (newSubCollateralView.getRelatedWithList() != null) {
                                        NewCollateralSubRelate newCollateralSubRelate;
                                        for (NewSubCollateralDetailView relatedView : newSubCollateralView.getRelatedWithList()) {
                                            log.info("relatedView.getId() ::: {} ", relatedView.getId());
                                            NewCollateralSubDetail relatedDetail = newCollateralSubDetailDAO.findById(relatedView.getRelatedWithId());
                                            log.info("relatedDetail.getId() ::: {} ", relatedDetail.getId());
                                            newCollateralSubRelate = new NewCollateralSubRelate();
                                            newCollateralSubRelate.setNewCollateralSubDetailRel(relatedDetail);
                                            newCollateralSubRelate.setNewCollateralSubDetail(newCollateralSubDetail);
                                            newSubCollRelateDAO.persist(newCollateralSubRelate);
                                            log.info("persist newCollateralSubRelate. id...{}", newCollateralSubRelate.getId());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (newCreditFacilityView.getNewCreditDetailViewList() != null) {
            //Delete NewCreditDetail
            List<NewCreditDetail> newCreditDetailsDelete = newCreditDetailDAO.findNewCreditDetailByNewCreditFacility(newCreditFacility);
//            onSaveRelationNewCreditDetail(newCreditFacilityView, workCaseId, newCreditDetailsDelete);

            if (newCreditDetailsDelete.size() > 0) {
                for (NewCreditDetail newCreditDetailDel : newCreditDetailsDelete) {
                    List<NewCreditTierDetail> newCreditTierDetailsDel = newCreditTierDetailDAO.findByNewCreditDetail(newCreditDetailDel);
                    if (newCreditTierDetailsDel.size() > 0) {
                        newCreditTierDetailDAO.delete(newCreditTierDetailsDel);
                        log.info("delete newCreditTierDetailsDel ::");
                    }
//                    newCreditDetailDAO.delete(newCreditDetailDel);
//                    log.info("delete newCreditDetails ::");
                }

                for (NewCreditDetailView newCreditDetailView : newCreditFacilityView.getNewCreditDetailViewList()) {
                    NewCreditDetail newCreditDetail = newCreditDetailTransform.transformToModelOne(newCreditDetailView, newCreditFacility, user);
                    newCreditDetailDAO.persist(newCreditDetail);
                    log.info("persist newCreditDetail...{}", newCreditDetail.getId());

                    if (newCreditDetailView.getNewCreditTierDetailViewList().size() > 0) {
                        List<NewCreditTierDetail> newCreditTierDetailList = newCreditTierTransform.transformToModel(newCreditDetailView.getNewCreditTierDetailViewList(), newCreditDetail, user);
                        newCreditTierDetailDAO.persist(newCreditTierDetailList);
                        log.info("persist newCreditTierDetailList...{}", newCreditTierDetailList.size());
                    }

                }
            }


        }
        log.info("onSaveNewCreditFacility  end :: {}", workCaseId);
    }

    public void onSaveRelationNewCreditDetail(NewCreditFacilityView newCreditFacilityView, long workCaseId) {
        try {
            log.info("onSaveRelationNewCreditDetail start");
            WorkCase workCase = workCaseDAO.findById(workCaseId);
            if (workCase != null) {
                NewCreditFacility newCreditFacility = newCreditFacilityDAO.findByWorkCase(workCase);
                if (newCreditFacility != null) {
                    List<NewCreditDetail> newCreditGuarantorList = null;
                    List<NewCreditDetail> newCreditCollateralList = null;
                    List<NewGuarantorDetail> newGuarantorDetails = newGuarantorDetailDAO.findNewGuarantorByNewCreditFacility(newCreditFacility);
                    List<NewCollateralDetail> newCollateralDetailList = newCollateralDetailDAO.findNewCollateralByNewCreditFacility(newCreditFacility);
                    List<NewCreditDetail> newCreditDetails = newCreditDetailDAO.findNewCreditDetailByNewCreditFacility(newCreditFacility);

                    if (newCreditFacilityView.getNewGuarantorDetailViewList() != null) {
                        for (int i = 0; i < newCreditFacilityView.getNewGuarantorDetailViewList().size(); i++) {
                            NewGuarantorDetailView newGuarantorDetailView = newCreditFacilityView.getNewGuarantorDetailViewList().get(i);
                            if (newGuarantorDetailView.getNewCreditDetailViewList() != null) {
                                newCreditGuarantorList = newCreditDetailTransform.getNewCreditDetailForGuarantor(newGuarantorDetailView.getNewCreditDetailViewList(), newCreditDetails);
                                onPersistNewGuarantorRelCredit(newGuarantorDetails.get(i), newCreditGuarantorList);
                            }
                        }
                    }

                    if (newCreditFacilityView.getNewCollateralInfoViewList() != null) {
                        for (int j = 0; j < newCreditFacilityView.getNewCollateralInfoViewList().size(); j++) {
                            NewCollateralInfoView newCollateralInfoView = newCreditFacilityView.getNewCollateralInfoViewList().get(j);
                            if (newCollateralInfoView.getNewCreditDetailViewList() != null) {
                                newCreditCollateralList = newCreditDetailTransform.getNewCreditDetailForCollateral(newCollateralInfoView.getNewCreditDetailViewList(), newCreditDetails);
                                onPersistNewCollateralRelCredit(newCollateralDetailList.get(j), newCreditCollateralList);
                            }
                        }
                    }

//                    newCreditDetailDAO.delete(newCreditDetails);
//                    log.info("delete newCreditDetails ::");
                }
            }
            calculateTotalProposeAmount(workCaseId);

        } catch (Exception e) {
            log.error("onSaveRelationNewCreditDetail  error ::: {}", e.getMessage());
        } finally {
            log.info("onSaveRelationNewCreditDetail end");
        }

    }

    public void onPersistNewGuarantorRelCredit(NewGuarantorDetail newGuarantorDetail, List<NewCreditDetail> newCreditDetailList) {
        log.info("onPersistNewGuarantorRelCredit start :: get newGuarantorDetail  :: {} ", newGuarantorDetail.getId());
        log.info("onPersistNewGuarantorRelCredit start :: get newCreditDetailList  :: {} ", newCreditDetailList.size());
        NewGuarantorRelCredit newGuarantorRelCredit;

        if (newGuarantorDetail != null && newCreditDetailList != null) {
            for (NewCreditDetail newCreditDetail : newCreditDetailList) {
                newGuarantorRelCredit = new NewGuarantorRelCredit();
                log.info("newCreditDetail :: {}", newCreditDetail.getId());
                newGuarantorRelCredit.setNewCreditDetail(newCreditDetail);
                newGuarantorRelCredit.setNewGuarantorDetail(newGuarantorDetail);
                newGuarantorRelCredit.setGuaranteeAmount(newCreditDetail.getGuaranteeAmount());
                newGuarantorRelationDAO.persist(newGuarantorRelCredit);
                log.info("persist newGuarantorRelCredit.. id...{}", newGuarantorRelCredit.getId());
            }
        }
        log.info("onPersistNewGuarantorRelCredit end");
    }

    public void onPersistNewCollateralRelCredit(NewCollateralDetail newCollateralDetail, List<NewCreditDetail> newCreditDetailList) {
        log.info("onPersistNewCollateralRelCredit start :: get newCollateralDetail  :: {}", newCollateralDetail.getId());
        log.info("onPersistNewCollateralRelCredit start :: get newCredit :: {}", newCreditDetailList.size());
        NewCollateralRelCredit newCollateralRelCredit;

        if (newCollateralDetail != null && newCreditDetailList != null) {
            for (NewCreditDetail newCreditDetail : newCreditDetailList) {
                newCollateralRelCredit = new NewCollateralRelCredit();
                newCollateralRelCredit.setNewCreditDetail(newCreditDetail);
                newCollateralRelCredit.setNewCollateralDetail(newCollateralDetail);
                newCollateralRelationDAO.persist(newCollateralRelCredit);
                log.info("newCollateralRelCredit .id ::: {}", newCollateralRelCredit.getId());
            }
        }

        log.info("onPersistNewCollateralRelCredit end");
    }

//    public void onSaveRelationSubRelatedDetail(NewSubCollateralDetailView newSubCollateralDetailView, long workCaseId) {
//        log.info("onSaveRelationSubRelatedDetail start:: newSubCollateralDetailView and workCaseID :: {}", newSubCollateralDetailView.getId(), workCaseId);
//        if (newSubCollateralDetailView.getRelatedWithList() != null) {
//            NewCollateralSubRelate newCollateralSubRelate = null;
//            List<NewCollateralSubDetail> newCollateralSubDetailList = findAllSubCollThisWorkCase(workCaseId);
//            if (newCollateralSubDetailList != null) {
//                List<NewCollateralSubDetail> relateDetailList = newSubCollDetailTransform.getNewSubDetailForRelated(newSubCollateralDetailView.getRelatedWithList(), newCollateralSubDetailList);
//                log.info("relateDetailList :: {} ", relateDetailList.size());
//                for (NewCollateralSubDetail relatedDetail : relateDetailList) {
//                    log.info("relatedDetail id:: {}", relatedDetail.getId());
//                    for (NewCollateralSubDetail newCollateralSub : newCollateralSubDetailList) {
//                        log.info("newCollateralSub.id  :::{}", newCollateralSub.getId());
//                        newCollateralSubRelate = new NewCollateralSubRelate();
//                        newCollateralSubRelate.setNewCollateralSubDetailRel(relatedDetail);
//                        newCollateralSubRelate.setNewCollateralSubDetail(newCollateralSub);
//                        newSubCollRelateDAO.persist(newCollateralSubRelate);
//                        log.info("persist newCollateralSubRelate. id...{}", newCollateralSubRelate.getId());
//                    }
//                }
//            }
//        }
//        log.info("onSaveRelationSubRelatedDetail end");
//    }

    public BigDecimal calTotalGuaranteeAmount(List<NewGuarantorDetailView> guarantorDetailViewList) {
        log.info("calTotalGuaranteeAmount start :: ");
        BigDecimal sumTotalGuaranteeAmount = BigDecimal.ZERO;
        if (guarantorDetailViewList == null || guarantorDetailViewList.size() == 0) {
            return sumTotalGuaranteeAmount;
        }

        for (NewGuarantorDetailView guarantorDetailView : guarantorDetailViewList) {
            sumTotalGuaranteeAmount = Util.add(sumTotalGuaranteeAmount, guarantorDetailView.getTotalLimitGuaranteeAmount());
        }

        log.info("calTotalGuaranteeAmount end :: ");
        return sumTotalGuaranteeAmount;
    }

    public void calculateTotalProposeAmount(long workCaseId) {
        log.info("calculateTotalProposeAmount start ::: ");
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        if (workCase != null) {
            NewCreditFacility newCreditFacility = newCreditFacilityDAO.findByWorkCase(workCase);
            if (newCreditFacility != null) {
                log.info("newCreditFacility .id::: {}", newCreditFacility.getId());
                BasicInfoView basicInfoView = basicInfoControl.getBasicInfo(workCaseId);
                TCGView tcgView = tcgInfoControl.getTcgView(workCaseId);

                BigDecimal sumTotalPropose = BigDecimal.ZERO;
                BigDecimal sumTotalCommercial = BigDecimal.ZERO;
                BigDecimal sumTotalCommercialAndOBOD = BigDecimal.ZERO; // wait confirm
                BigDecimal sumTotalExposure = BigDecimal.ZERO;
                BigDecimal sumTotalLoanDbr = BigDecimal.ZERO;
                BigDecimal sumTotalNonLoanDbr = BigDecimal.ZERO;

                if ((newCreditFacility != null) && (basicInfoView.getSpecialProgram() != null) && (tcgView != null)) {
                    List<NewCreditDetail> newCreditDetailList = newCreditFacility.getNewCreditDetailList();

                    if (newCreditDetailList != null && newCreditDetailList.size() != 0) {

                        for (NewCreditDetail newCreditDetail : newCreditDetailList) {
                            log.info("newCreditDetail id :: {}", newCreditDetail.getId());
                            if ((newCreditDetail.getProductProgram().getId() != 0) && (newCreditDetail.getCreditType().getId() != 0)) {
                                ProductProgram productProgram = productProgramDAO.findById(newCreditDetail.getProductProgram().getId());
                                CreditType creditType = creditTypeDAO.findById(newCreditDetail.getCreditType().getId());

                                if (productProgram != null && creditType != null) {
                                    PrdProgramToCreditType prdProgramToCreditType = prdProgramToCreditTypeDAO.getPrdProgramToCreditType(creditType, productProgram);

                                    ProductFormula productFormula = productFormulaDAO.findProductFormulaPropose(prdProgramToCreditType,
                                            newCreditFacility.getCreditCustomerType(), basicInfoView.getSpecialProgram(), tcgView.getTCG());

                                    if (productFormula != null) {
                                        log.info("productFormula id :: {}", productFormula.getId());
                                        //ExposureMethod
                                        if (productFormula.getExposureMethod() == ExposureMethod.NOT_CALCULATE.value()) { //ไม่คำนวณ
                                            log.info("NOT_CALCULATE :: productFormula.getExposureMethod() :: {}", productFormula.getExposureMethod());
                                            sumTotalPropose = sumTotalPropose.add(BigDecimal.ZERO);
                                        } else if (productFormula.getExposureMethod() == ExposureMethod.LIMIT.value()) { //limit
                                            log.info("LIMIT :: productFormula.getExposureMethod() :: {}", productFormula.getExposureMethod());
                                            sumTotalPropose = sumTotalPropose.add(newCreditDetail.getLimit());
                                        } else if (productFormula.getExposureMethod() == ExposureMethod.PCE_LIMIT.value()) {    //limit * %PCE
                                            log.info("PCE_LIMIT :: productFormula.getExposureMethod() :: {}", productFormula.getExposureMethod());
                                            sumTotalPropose = sumTotalPropose.add(Util.multiply(newCreditDetail.getLimit(), newCreditDetail.getPcePercent()));
                                        }

                                        log.info("sumTotalPropose :: {}", sumTotalPropose);
                                        //For DBR
                                        if (productFormula.getDbrCalculate() == 1)//N
                                        {
                                            log.info("NO :: productFormula.getDbrCalculate() :: {}", productFormula.getDbrCalculate());
                                            sumTotalNonLoanDbr = BigDecimal.ZERO;
                                        } else if (productFormula.getDbrCalculate() == 2)//Y
                                        {
                                            log.info("YES :: productFormula.getDbrCalculate() :: {}", productFormula.getDbrCalculate());
                                            if (productFormula.getDbrMethod() == DBRMethod.NOT_CALCULATE.value()) {// not calculate
                                                log.info("NOT_CALCULATE :: productFormula.getDbrMethod() :: {}", productFormula.getDbrMethod());
                                                sumTotalLoanDbr = sumTotalLoanDbr.add(BigDecimal.ZERO);
                                            } else if (productFormula.getDbrMethod() == DBRMethod.INSTALLMENT.value()) { //Installment
                                                log.info("INSTALLMENT :: productFormula.getDbrMethod() :: {}", productFormula.getDbrMethod());
                                                sumTotalLoanDbr = sumTotalLoanDbr.add(newCreditDetail.getInstallment());
                                            } else if (productFormula.getDbrMethod() == DBRMethod.INT_YEAR.value()) { //(Limit*((อัตราดอกเบี้ย+ Spread)/100))/12
                                                log.info("INT_YEAR :: productFormula.getDbrMethod() :: {}", productFormula.getDbrMethod());
                                                sumTotalLoanDbr = sumTotalLoanDbr.add(calTotalProposeLoanDBRForIntYear(newCreditDetail, productFormula.getDbrSpread()));
                                            }
                                        }
                                        log.info("sumTotalLoanDbr :: {}", sumTotalLoanDbr);
                                        //WC
                                        if (productFormula.getWcCalculate() == 0) {

                                        } else if (productFormula.getWcCalculate() == 1) {

                                        } else if (productFormula.getWcCalculate() == 2) {

                                        }
                                    }
                                }
                            }
                        }

                        newCreditFacility.setTotalPropose(sumTotalPropose); //sumTotalPropose
                        newCreditFacility.setTotalProposeLoanDBR(sumTotalLoanDbr);//sumTotalLoanDbr
                        newCreditFacility.setTotalProposeNonLoanDBR(sumTotalNonLoanDbr); //sumTotalNonLoanDbr

                        ExistingCreditFacilityView existingCreditFacilityView = creditFacExistingControl.onFindExistingCreditFacility(workCaseId);

                        if (existingCreditFacilityView != null) {
                            sumTotalCommercial = sumTotalCommercial.add(existingCreditFacilityView.getTotalBorrowerComLimit().add(sumTotalPropose));
                            newCreditFacility.setTotalCommercial(sumTotalCommercial); //sumTotalCommercial

                            sumTotalExposure = sumTotalExposure.add(existingCreditFacilityView.getTotalBorrowerComLimit().add(sumTotalPropose).add(existingCreditFacilityView.getTotalBorrowerRetailLimit()));
                            newCreditFacility.setTotalExposure(sumTotalExposure); //sumTotalExposure
                        }

                        log.info("sumTotalCommercial :: {}", sumTotalCommercial);
                        log.info("sumTotalExposure :: {}", sumTotalExposure);
                    }
                }

                newCreditFacilityDAO.persist(newCreditFacility);
            }
        }
    }

    public BigDecimal calTotalProposeLoanDBRForIntYear(NewCreditDetail newCreditDetail, BigDecimal dbrSpread) {
        log.info("calTotalProposeLoanDBRForIntYear start :: newCreditDetail and  dbrSpread ::{}", newCreditDetail, dbrSpread);
        BigDecimal sumTotalLoanDbr = BigDecimal.ZERO;
        BigDecimal sum;
        log.info("limit :: {}", newCreditDetail.getLimit());
        log.info("newCreditTierDetailViews.size :: {}", newCreditDetail.getProposeCreditTierDetailList().size());
        if (newCreditDetail.getProposeCreditTierDetailList() != null) {
            for (NewCreditTierDetail newCreditTierDetail : newCreditDetail.getProposeCreditTierDetailList()) //(Limit*((อัตราดอกเบี้ย+ Spread)/100))/12
            {
                sum = BigDecimal.ZERO;
                if (newCreditTierDetail != null) {
                    log.info("newCreditTierDetail.getFinalBasePrice().getValue() :: {}", newCreditTierDetail.getFinalBasePrice().getValue());
                    log.info("newCreditTierDetail.getFinalInterest() :: {}", newCreditTierDetail.getFinalInterest());
                    log.info("dbrSpread :: {}", dbrSpread);
                    sum = Util.divide((Util.multiply(newCreditTierDetail.getFinalBasePrice().getValue(), newCreditTierDetail.getFinalInterest()).add(dbrSpread)), 100);
                    log.info("newCreditTierDetail.getFinalBasePrice().getValue() :: {}", newCreditTierDetail.getFinalBasePrice().getValue());
                    sum = Util.multiply(newCreditDetail.getLimit(), sum);
                    sum = Util.divide(sum, 12);
                    sumTotalLoanDbr = sumTotalLoanDbr.add(sum);
                }
            }
        }

        log.info("calTotalProposeLoanDBRForIntYear end");
        return sumTotalLoanDbr;
    }


    public void wcCalculation(long workCaseId) {

    }

    /**
     * Formula: <br/>
     * if(standard > suggest) -> final = standard <br/>
     * else if(standard < suggest) -> final = suggest <br/>
     * else *(standard == suggest) -> final = (standard | suggest)
     *
     * @param standardBaseRateId
     * @param standardInterest
     * @param suggestBaseRateId
     * @param suggestInterest
     * @return Object[0]: (BaseRate) finalBaseRate, Object[1]: (BigDecimal) finalInterest, Object[2]: (String) finalPriceLabel,<br/>
     *         Object[3]: (BaseRate) standardBaseRate, Object[4]: (String) standardPriceLabel,<br/>
     *         Object[5]: (BaseRate) suggestBaseRate, Object[6]: (String) suggestPriceLabel
     */
    public Object[] findFinalPriceRate(int standardBaseRateId, BigDecimal standardInterest,
                                       int suggestBaseRateId, BigDecimal suggestInterest) {
        Object[] returnValues = new Object[7];

        BigDecimal standardPrice = BigDecimal.ZERO;
        StringBuilder standardPriceLabel = new StringBuilder("");

        BigDecimal suggestPrice = BigDecimal.ZERO;
        StringBuilder suggestPriceLabel = new StringBuilder("");

        BaseRate finalBaseRate = new BaseRate();
        BigDecimal finalInterest = BigDecimal.ZERO;
        String finalPriceLabel = "";

// Standard Price Rate
        BaseRate standardBaseRate = baseRateDAO.findById(standardBaseRateId);
        if (standardBaseRate != null && standardInterest != null) {
            standardPrice = standardBaseRate.getValue().add(standardInterest);

            if (ValidationUtil.isValueLessThanZero(standardInterest)) {
                standardPriceLabel.append(standardBaseRate.getName()).append(" ").append(standardInterest);
            } else {
                standardPriceLabel.append(standardBaseRate.getName()).append(" + ").append(standardInterest);
            }
        }

        // Suggest Price Rate
        BaseRate suggestBaseRate = baseRateDAO.findById(suggestBaseRateId);
        if (suggestBaseRate != null && suggestInterest != null) {
            suggestPrice = suggestBaseRate.getValue().add(suggestInterest);

            if (ValidationUtil.isValueLessThanZero(suggestInterest)) {
                suggestPriceLabel.append(suggestBaseRate.getName()).append(" ").append(suggestInterest);
            } else {
                suggestPriceLabel.append(suggestBaseRate.getName()).append(" + ").append(suggestInterest);
            }
        }

        // Compare for Final Price
        if (ValidationUtil.isGreaterThan(standardPrice, suggestPrice) || ValidationUtil.isValueEqual(standardPrice, suggestPrice)) {
            finalBaseRate = standardBaseRate;
            finalInterest = standardInterest;
            finalPriceLabel = standardPriceLabel.toString();
        } else {
            finalBaseRate = suggestBaseRate;
            finalInterest = suggestInterest;
            finalPriceLabel = suggestPriceLabel.toString();
        }

        returnValues[0] = finalBaseRate;
        returnValues[1] = finalInterest;
        returnValues[2] = finalPriceLabel;
        returnValues[3] = standardBaseRate != null ? standardBaseRate : new BaseRate();
        returnValues[4] = standardPriceLabel.toString();
        returnValues[5] = suggestBaseRate != null ? suggestBaseRate : new BaseRate();
        returnValues[6] = suggestPriceLabel.toString();
        return returnValues;
    }
}