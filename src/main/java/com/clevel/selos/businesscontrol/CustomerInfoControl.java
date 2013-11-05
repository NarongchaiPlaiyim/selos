package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.DocumentTypeDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.BRMSInterface;
import com.clevel.selos.integration.RMInterface;
import com.clevel.selos.integration.corebanking.model.CustomerInfo;
import com.clevel.selos.integration.corebanking.model.corporateInfo.CorporateResult;
import com.clevel.selos.integration.corebanking.model.individualInfo.IndividualResult;
import com.clevel.selos.model.BorrowerType;
import com.clevel.selos.model.db.master.CustomerEntity;
import com.clevel.selos.model.db.master.DocumentType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.Address;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.db.working.Individual;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.CustomerInfoResultView;
import com.clevel.selos.model.view.CustomerInfoSummaryView;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.transform.CustomerTransform;
import com.clevel.selos.transform.business.CustomerBizTransform;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class CustomerInfoControl extends BusinessControl {
    @Inject
    Logger log;

    @Inject
    CustomerDAO customerDAO;
    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    IndividualDAO individualDAO;
    @Inject
    AddressDAO addressDAO;
    @Inject
    DocumentTypeDAO documentTypeDAO;
    @Inject
    JuristicDAO juristicDAO;

    @Inject
    RMInterface rmInterface;
    @Inject
    BRMSInterface brmsInterface;

    @Inject
    CustomerTransform customerTransform;
    @Inject
    CustomerBizTransform customerBizTransform;

    public CustomerInfoSummaryView getCustomerInfoSummary(long workCaseId){
        log.info("getCustomerInfoSummary ::: workCaseId : {}", workCaseId);
        CustomerInfoSummaryView customerInfoSummaryView = new CustomerInfoSummaryView();

        List<Customer> customerList = customerDAO.findByWorkCaseId(workCaseId);
        List<CustomerInfoView> customerInfoViewList = customerTransform.transformToViewList(customerList);

        List<CustomerInfoView> borrowerCustomerList = customerTransform.transformToBorrowerViewList(customerInfoViewList);
        customerInfoSummaryView.setBorrowerCustomerViewList(borrowerCustomerList);

        List<CustomerInfoView> guarantorCustomerList = customerTransform.transformToGuarantorViewList(customerInfoViewList);
        customerInfoSummaryView.setGuarantorCustomerViewList(guarantorCustomerList);

        List<CustomerInfoView> relatedCustomerList = customerTransform.transformToRelatedViewList(customerInfoViewList);
        customerInfoSummaryView.setRelatedCustomerViewList(relatedCustomerList);

        return customerInfoSummaryView;
    }

    // For Customer Info. Detail - Individual
    public int getCaseBorrowerTypeIdByWorkCase(long workCaseId){
        int caseBorrowerTypeId = 0;
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        if(workCase != null){
            if(workCase.getBorrowerType() != null){
                caseBorrowerTypeId = workCase.getBorrowerType().getId();
            }
        }
        return caseBorrowerTypeId;
    }

    public void saveCustomerInfoIndividual(CustomerInfoView customerInfoView, long workCaseId){
        WorkCase workCase = workCaseDAO.findById(workCaseId);

        customerInfoView.getCustomerEntity().setId(1);

        Customer customer = customerTransform.transformToModel(customerInfoView, null, workCase);
        customerDAO.persist(customer);
        individualDAO.persist(customer.getIndividual());
        addressDAO.persist(customer.getAddressesList());

        if(customer.getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()){
            if(customer.getIndividual().getMaritalStatus() != null
                    && customer.getIndividual().getMaritalStatus().getSpouseFlag() == 1){

                customerInfoView.getSpouse().getCustomerEntity().setId(1);

                Customer spouse = customerTransform.transformToModel(customerInfoView.getSpouse(), null, workCase);
                spouse.setIsSpouse(1);
                spouse.setSpouseId(0);
                customerDAO.persist(spouse);

                customer.setSpouseId(spouse.getId());
                customerDAO.persist(customer);

                individualDAO.persist(spouse.getIndividual());
                addressDAO.persist(spouse.getAddressesList());
            }else if(customer.getIndividual().getMaritalStatus() != null
                    && customer.getIndividual().getMaritalStatus().getSpouseFlag() != 1){
                if(customer.getSpouseId() != 0){
                    Customer cus = customerDAO.findById(customer.getSpouseId());
                    if(cus != null){
                        if(cus.getAddressesList() != null && cus.getAddressesList().size() > 0){
                            addressDAO.delete(cus.getAddressesList());
                        }
                        if(cus.getIndividual() != null){
                            individualDAO.delete(cus.getIndividual());
                        }
                        customerDAO.delete(cus);
                    }

                    customer.setSpouseId(0);
                    customerDAO.persist(customer);
                }
            }
        }
    }

    public void saveCustomerInfoJuristic(CustomerInfoView customerInfoView, long workCaseId){
        WorkCase workCase = workCaseDAO.findById(workCaseId);

        customerInfoView.getCustomerEntity().setId(2);

        //for delete customer where is committee & committee id = juristic id
        if(customerInfoView.getId() != 0){
            List<Customer> cusList = customerDAO.findCustomerByCommitteeId(customerInfoView.getId());
            for(Customer customer : cusList){
                if(customer.getAddressesList() != null && customer.getAddressesList().size() > 0){
                    addressDAO.delete(customer.getAddressesList());
                }
                if(customer.getIndividual() != null){
                    individualDAO.delete(customer.getIndividual());
                }
                customerDAO.delete(customer);
            }
        }

        //for add new
        Customer customerJuristic = customerTransform.transformToModel(customerInfoView, null, workCase);
        customerDAO.persist(customerJuristic);
        juristicDAO.persist(customerJuristic.getJuristic());
        addressDAO.persist(customerJuristic.getAddressesList());

        if(customerInfoView.getIndividualViewList() != null && customerInfoView.getIndividualViewList().size() > 0){
            for(CustomerInfoView cusIndividual : customerInfoView.getIndividualViewList()){
                cusIndividual.setIsCommittee(1);
                cusIndividual.setCommitteeId(customerJuristic.getId());
                if(cusIndividual.getSpouse() != null){
                    cusIndividual.getSpouse().setIsCommittee(1);
                    cusIndividual.getSpouse().setCommitteeId(customerJuristic.getId());
                }
                saveCustomerInfoIndividual(cusIndividual,workCaseId);
            }
        }
    }

    public CustomerInfoView getCustomerIndividualById(long id){
        Customer customer = customerDAO.findById(id);
        CustomerInfoView customerInfoView = customerTransform.transformToView(customer);
        if(customer.getSpouseId() != 0){
            Customer spouse = customerDAO.findById(customer.getSpouseId());
            CustomerInfoView spouseInfoView = customerTransform.transformToView(spouse);
            customerInfoView.setSpouse(spouseInfoView);
        }
        return customerInfoView;
    }

    //** function for integration **//

    // *** Function for RM *** //
    public CustomerInfoResultView getCustomerInfoFromRM(CustomerInfoView customerInfoView, User user){
        CustomerInfoResultView customerInfoResultSearch = new CustomerInfoResultView();
        log.info("getCustomerInfoFromRM ::: customerInfoView.getSearchBy : {}", customerInfoView.getSearchBy());
        log.info("getCustomerInfoFromRM ::: customerInfoView.getSearchId : {}", customerInfoView.getSearchId());

        DocumentType masterDocumentType = new DocumentType();

        RMInterface.SearchBy searcyBy = RMInterface.SearchBy.CUSTOMER_ID;
        if(customerInfoView.getSearchBy() == 1){
            searcyBy = RMInterface.SearchBy.CUSTOMER_ID;
            masterDocumentType = documentTypeDAO.findById(customerInfoView.getDocumentType().getId());
        }else if(customerInfoView.getSearchBy() == 2){
            searcyBy = RMInterface.SearchBy.TMBCUS_ID;
            masterDocumentType = documentTypeDAO.findById(1);
        }

        String userId = user.getId();
        String documentTypeCode = masterDocumentType.getDocumentTypeCode();
        log.info("getCustomerInfoFromRM ::: userId : {}", userId);
        log.info("getCustomerInfoFromRM ::: documentType : {}", masterDocumentType);
        log.info("getCustomerInfoFromRM ::: documentTypeCode : {}", documentTypeCode);

        RMInterface.DocumentType documentType = RMInterface.DocumentType.CITIZEN_ID;

        if(documentTypeCode.equalsIgnoreCase("CI")){
            documentType = RMInterface.DocumentType.CITIZEN_ID;
            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity.setId(1);
            customerInfoView.setCustomerEntity(customerEntity);
        }else if(documentTypeCode.equalsIgnoreCase("PP")){
            documentType = RMInterface.DocumentType.PASSPORT;
            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity.setId(1);
            customerInfoView.setCustomerEntity(customerEntity);
        }else if(documentTypeCode.equalsIgnoreCase("SC")){
            documentType = RMInterface.DocumentType.CORPORATE_ID;
            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity.setId(2);
            customerInfoView.setCustomerEntity(customerEntity);
        }

        log.info("getCustomerInfoFromRM ::: searchBy : {}", searcyBy);
        log.info("getCustomerInfoFromRM ::: documentType : {}", documentType);


        if(customerInfoView.getCustomerEntity().getId() == 1) {
            IndividualResult individualResult = rmInterface.getIndividualInfo(userId, customerInfoView.getSearchId(), documentType, searcyBy);
            log.info("getCustomerInfoFromRM ::: individualResult : {}", individualResult);
            customerInfoResultSearch = customerBizTransform.tranformIndividual(individualResult);
        } else if(customerInfoView.getCustomerEntity().getId() == 2){
            CorporateResult corporateResult = rmInterface.getCorporateInfo(userId, customerInfoView.getSearchId(), documentType, searcyBy);
            log.info("getCustomerInfoFromRM ::: corporateResult : {}", corporateResult);
            customerInfoResultSearch = customerBizTransform.tranformJuristic(corporateResult);
        }

        log.info("getCustomerInfoFromRM ::: success!!");
        log.info("getCustomerInfoFromRM ::: customerInfoSearch : {}", customerInfoResultSearch);
        return customerInfoResultSearch;
    }
}
