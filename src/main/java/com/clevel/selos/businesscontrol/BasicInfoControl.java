package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.CustomerEntityDAO;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.model.db.master.BAPaymentMethod;
import com.clevel.selos.model.db.master.CustomerEntity;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.BasicInfoAccountPurposeView;
import com.clevel.selos.model.view.BasicInfoAccountView;
import com.clevel.selos.model.view.BasicInfoView;
import com.clevel.selos.transform.BasicInfoAccPurposeTransform;
import com.clevel.selos.transform.BasicInfoAccountTransform;
import com.clevel.selos.transform.BasicInfoTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class BasicInfoControl extends BusinessControl {
    @Inject
    Logger log;

    @Inject
    BasicInfoDAO basicInfoDAO;
    @Inject
    CustomerDAO customerDAO;
    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    UserDAO userDAO;
    @Inject
    OpenAccountDAO openAccountDAO;
    @Inject
    OpenAccPurposeDAO openAccPurposeDAO;
    @Inject
    CustomerEntityDAO customerEntityDAO;

    @Inject
    BasicInfoTransform basicInfoTransform;
    @Inject
    BasicInfoAccountTransform basicInfoAccountTransform;
    @Inject
    BasicInfoAccPurposeTransform basicInfoAccPurposeTransform;

    public BasicInfoView getBasicInfo(long workCaseId){
        log.info("getBasicInfo ::: workCaseId : {}", workCaseId);
        BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
        WorkCase workCase = workCaseDAO.findById(workCaseId);

        if(basicInfo == null){
            basicInfo = new BasicInfo();
        }

        BasicInfoView basicInfoView = basicInfoTransform.transformToView(basicInfo,workCase);

//        Comment This for use transform

//        List<OpenAccount> openAccountList = openAccountDAO.findByBasicInfoId(basicInfo.getId());
//        List<BasicInfoAccountView> basicInfoAccountViews = new ArrayList<BasicInfoAccountView>();
//        for(OpenAccount oa : openAccountList){
//            List<OpenAccPurpose> openAccPurposeList = openAccPurposeDAO.findByOpenAccountId(oa.getId());
//            List<BasicInfoAccountPurposeView> bia = basicInfoAccPurposeTransform.transformToViewList(openAccPurposeList);
//            BasicInfoAccountView basicInfoAccountView = basicInfoAccountTransform.transformToView(oa);
//            basicInfoAccountView.setBasicInfoAccountPurposeView(bia);
//
//            StringBuilder stringBuilder = new StringBuilder();
//
//            for(int i=0;i<bia.size();i++){
//                if(i == 0){
//                    stringBuilder.append(bia.get(i).getPurpose().getName());
//                }else{
//                    stringBuilder.append(", "+bia.get(i).getPurpose().getName());
//                }
//            }
//            basicInfoAccountView.setPurposeForShow(stringBuilder.toString());
//            basicInfoAccountViews.add(basicInfoAccountView);
//        }
//        basicInfoView.setBasicInfoAccountViews(basicInfoAccountViews);

        log.info("getBasicInfo ::: basicInfoView : {}", basicInfoView);
        return basicInfoView;
    }

    public CustomerEntity getCustomerEntityByWorkCaseId(long workCaseId){
        CustomerEntity customerEntity;
        List<Customer> customerList = customerDAO.findByWorkCaseId(workCaseId);
        for(Customer customer : customerList){
            if(customer.getCustomerEntity() != null && customer.getCustomerEntity().getId() == 1){ // Customer Entity ; 1 = Individual ; 2 = Juristic
                customerEntity = customer.getCustomerEntity();
                return customerEntity;
            }
        }
        customerEntity = customerEntityDAO.findById(2);
        return customerEntity;
    }

    public void saveBasicInfo(BasicInfoView basicInfoView, long workCaseId, String userId){
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        User user = userDAO.findById(userId);

        if(basicInfoView.getQualitative() == 0){
            basicInfoView.setQualitative(2);
        }

        if(!basicInfoView.isApplyBA()){
            basicInfoView.setBaPaymentMethod(new BAPaymentMethod());
        }

        BasicInfo basicInfo = basicInfoTransform.transformToModel(basicInfoView, workCase, user);
        basicInfoDAO.persist(basicInfo);
//        return basicInfo;

        List<OpenAccount> openAccountList = openAccountDAO.findByBasicInfoId(basicInfo.getId());
        for(OpenAccount oa : openAccountList){
            List<OpenAccPurpose> openAccPurposeList = openAccPurposeDAO.findByOpenAccountId(oa.getId());
            openAccPurposeDAO.delete(openAccPurposeList);
        }
        openAccountDAO.delete(openAccountList);

        if(basicInfoView.getBasicInfoAccountViews().size() != 0){
            for(BasicInfoAccountView biav : basicInfoView.getBasicInfoAccountViews()){
                OpenAccount openAccount = basicInfoAccountTransform.transformToModel(biav,basicInfo);
                openAccountDAO.save(openAccount);

                for (BasicInfoAccountPurposeView biapv : biav.getBasicInfoAccountPurposeView()){
                    if(biapv.isSelected()){
                        OpenAccPurpose openAccPurpose = basicInfoAccPurposeTransform.transformToModel(biapv,openAccount);
                        openAccPurposeDAO.save(openAccPurpose);
                    }
                }
            }
        }

    }

//    public void deleteOpenAccount(long basicInfoId){
//        //for edit / delete - to open account
////        List<OpenAccount> openAccountList = openAccountDAO.findByBasicInfoId(basicInfo.getId());
//        List<OpenAccount> openAccountList = openAccountDAO.findByBasicInfoId(basicInfoId);
//        for(OpenAccount oa : openAccountList){
//            List<OpenAccPurpose> openAccPurposeList = openAccPurposeDAO.findByOpenAccountId(oa.getId());
//            openAccPurposeDAO.delete(openAccPurposeList);
//        }
//        openAccountDAO.delete(openAccountList);
//    }
//
//    public void addOpenAccount(BasicInfoView basicInfoView,BasicInfo basicInfo){
//        //for addNew Open Account
//        if(basicInfoView.getBasicInfoAccountViews().size() != 0){
//            for(BasicInfoAccountView biav : basicInfoView.getBasicInfoAccountViews()){
//                OpenAccount openAccount = basicInfoAccountTransform.transformToModel(biav,basicInfo);
//                openAccountDAO.save(openAccount);
//
//                for (BasicInfoAccountPurposeView biapv : biav.getBasicInfoAccountPurposeView()){
//                    if(biapv.isSelected()){
//                        OpenAccPurpose openAccPurpose = basicInfoAccPurposeTransform.transformToModel(biapv,openAccount);
//                        openAccPurposeDAO.save(openAccPurpose);
//                    }
//                }
//            }
//        }
//    }
}
