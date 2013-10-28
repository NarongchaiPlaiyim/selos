package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.ContactRecordDetailDAO;
import com.clevel.selos.dao.working.CustomerAcceptanceDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.model.db.working.ContactRecordDetail;
import com.clevel.selos.model.db.working.CustomerAcceptance;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.ContactRecordDetailView;
import com.clevel.selos.model.view.CustomerAcceptanceView;
import com.clevel.selos.transform.ContactRecordDetailTransform;
import com.clevel.selos.transform.CustomerAcceptanceTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
@Stateless
public class CustomerAcceptanceControl  extends BusinessControl {
    @Inject
    Logger log;
    @Inject
    CustomerAcceptanceDAO customerAcceptanceDAO;
    @Inject
    ContactRecordDetailDAO contactRecordDetailDAO;
    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    CustomerAcceptanceTransform customerAcceptanceTransform;
    @Inject
    ContactRecordDetailTransform contactRecordDetailTransform;


    public void onSaveCustomerAcceptance(CustomerAcceptanceView customerAcceptanceView, List<ContactRecordDetailView> contactRecordDetailViewList,long workCaseId){

        log.info("onSaveCustomerAcceptance begin");
        log.info("onSaveCustomerAcceptance begin workCaseId " + workCaseId);
        log.info("onSaveCustomerAcceptance begin getApproveResult " + customerAcceptanceView.getApproveResult());
        log.info("contactRecordDetailViewList size begin  " + contactRecordDetailViewList.size());
        WorkCase workCase = workCaseDAO.findById(workCaseId);

        CustomerAcceptance customerAcceptance = customerAcceptanceTransform.transformToModel( customerAcceptanceView );
        customerAcceptance.setWorkCase(workCase);
        log.info("customerAcceptance getWorkCase before persist is " + customerAcceptance.getWorkCase());
        log.info("customerAcceptance getApproveResult before  persist is " + customerAcceptance.getApproveResult());
        customerAcceptanceDAO.persist(customerAcceptance);
        log.info("persist customerAcceptance");

        log.info("persist customerAcceptance after id is " + customerAcceptance.getId());
        if(contactRecordDetailViewList.size()>0){
            List<ContactRecordDetail> contactRecordDetailListDelete =  contactRecordDetailDAO.findRecordCallingByCustomerAcceptance(customerAcceptance.getId());
            log.info("recordCallingDetailViewListDelete :: {}", contactRecordDetailListDelete.size());

            contactRecordDetailDAO.delete(contactRecordDetailListDelete);
            log.info("delete contactRecordDetailListDelete");
        }

        List<ContactRecordDetail> contactRecordDetailList = contactRecordDetailTransform.transformToModel(contactRecordDetailViewList,customerAcceptance) ;
        log.info("contactRecordDetailTransform contactRecordDetailViewList before add size is " + contactRecordDetailList.size());

        contactRecordDetailDAO.persist(contactRecordDetailList);
        log.info("persist contactRecordDetailList");

    }

    public CustomerAcceptanceView getCustomerAcceptanceByWorkCase(long workCaseId){
        log.info("getCustomerAcceptanceByWorkCase :: customer id  :: {}", workCaseId);
        CustomerAcceptanceView customerAcceptanceView = null;

        try{
            WorkCase workCase = new WorkCase();
            workCase.setId(workCaseId);
            CustomerAcceptance customerAcceptance =  customerAcceptanceDAO.findCustomerAcceptanceByWorkCase(workCase);
            if(customerAcceptance != null){
                log.info("customerAcceptance getId :: {} ",customerAcceptance.getId());
                log.info("customerAcceptance getApproveResult :: {} ",customerAcceptance.getApproveResult());
                customerAcceptanceView  = customerAcceptanceTransform.transformToView(customerAcceptance);
            }
        }catch (Exception e){
            log.error( "getCustomerAcceptanceView error :: " + e.getMessage());
        }finally{
            log.info( "getCustomerAcceptanceView end" );
        }
        log.info("customerAcceptanceView getApproveResult :: {} ",customerAcceptanceView.getApproveResult());
        return customerAcceptanceView;
    }

    public List<ContactRecordDetailView> getContactRecordViewList(CustomerAcceptanceView customerAcceptanceView){
        log.info("getRecordCallingViewList :: customerAcceptanceViewId  :: {}", customerAcceptanceView.getId());
        List<ContactRecordDetailView> contactRecordDetailViewList = null;

        try{
            List<ContactRecordDetail> contactRecordDetailList =  contactRecordDetailDAO.findRecordCallingByCustomerAcceptance(customerAcceptanceView.getId());

            if(contactRecordDetailList.size() > 0){
                contactRecordDetailViewList = contactRecordDetailTransform.transformToView(contactRecordDetailList);
            }

        }catch (Exception e){
            log.error( "getRecordCallingViewList error :: " + e.getMessage());
        }finally{
            log.info( "getRecordCallingViewList end" );
        }

        return contactRecordDetailViewList;
    }
    
    
}
