package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.DocumentTypeDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.BRMSInterface;
import com.clevel.selos.integration.RMInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.corebanking.model.corporateInfo.CorporateResult;
import com.clevel.selos.integration.corebanking.model.individualInfo.IndividualResult;
import com.clevel.selos.model.BorrowerType;
import com.clevel.selos.model.db.master.CustomerEntity;
import com.clevel.selos.model.db.master.DocumentType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.CustomerInfoResultView;
import com.clevel.selos.model.view.CustomerInfoSummaryView;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.transform.CustomerTransform;
import com.clevel.selos.transform.business.CustomerBizTransform;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class CustomerInfoControl extends BusinessControl {
    @Inject
    @SELOS
    private Logger log;

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
    CustomerCSIDAO customerCSIDAO;
    @Inject
    NCBDAO ncbDAO;

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

        //update percent share for juristic
        for(CustomerInfoView cV : customerInfoViewList){
            if(cV.getCustomerEntity().getId() == BorrowerType.JURISTIC.value()){
                if(cV.getPercentShare() != null && cV.getPercentShare().compareTo(BigDecimal.ZERO) > 0){
                    if(cV.getTotalShare() != null && cV.getTotalShare().compareTo(BigDecimal.ZERO) > 0){
                        cV.setPercentShareSummary((cV.getPercentShare().divide(cV.getTotalShare(), RoundingMode.HALF_UP)).multiply(new BigDecimal(100)));
                    }
                }
            } else {
                if(cV.getPercentShare() != null && cV.getPercentShare().compareTo(BigDecimal.ZERO) > 0){
                    cV.setPercentShareSummary(cV.getPercentShare());
                } else {
                    cV.setPercentShareSummary(BigDecimal.ZERO);
                }
            }
        }

        List<CustomerInfoView> borrowerCustomerList = customerTransform.transformToBorrowerViewList(customerInfoViewList);
        customerInfoSummaryView.setBorrowerCustomerViewList(borrowerCustomerList);

        List<CustomerInfoView> guarantorCustomerList = customerTransform.transformToGuarantorViewList(customerInfoViewList);
        customerInfoSummaryView.setGuarantorCustomerViewList(guarantorCustomerList);

        List<CustomerInfoView> relatedCustomerList = customerTransform.transformToRelatedViewList(customerInfoViewList);
        customerInfoSummaryView.setRelatedCustomerViewList(relatedCustomerList);

        return customerInfoSummaryView;
    }

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
        if(customerInfoView.getIndividualViewList() != null && customerInfoView.getIndividualViewList().size() > 0){
            for(CustomerInfoView cus : customerInfoView.getIndividualViewList()){
                cus.getCurrentAddress().setId(0);
                cus.getRegisterAddress().setId(0);
                cus.getWorkAddress().setId(0);
                //for check delete spouse
                if(cus.getSpouseId() != 0){
                    Customer spouse = customerDAO.findById(cus.getSpouseId());
                    if(spouse.getAddressesList() != null && spouse.getAddressesList().size() > 0){
                        addressDAO.delete(spouse.getAddressesList());
                    }
                    if(spouse.getIndividual() != null){
                        individualDAO.delete(spouse.getIndividual());
                    }
                    customerDAO.delete(spouse);
                }
                cus.setId(0);
            }
        }

        //calculation age for juristic
        customerInfoView.setAge(Util.calAge(customerInfoView.getDateOfRegister()));

        Customer customerJuristic = customerTransform.transformToModel(customerInfoView, null, workCase);
        customerDAO.persist(customerJuristic);
        juristicDAO.persist(customerJuristic.getJuristic());
        addressDAO.persist(customerJuristic.getAddressesList());

        if(customerInfoView.getIndividualViewList() != null && customerInfoView.getIndividualViewList().size() > 0){
            for(CustomerInfoView cusIndividual : customerInfoView.getIndividualViewList()){
                cusIndividual.setIsCommittee(1);
                cusIndividual.setCommitteeId(customerJuristic.getId());
                if(cusIndividual.getSpouse() != null){
//                    cusIndividual.getSpouse().setIsCommittee(1);
                    cusIndividual.getSpouse().setIsCommittee(0);
//                    cusIndividual.getSpouse().setCommitteeId(customerJuristic.getId());
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

    public CustomerInfoView getCustomerJuristicById(long id){
        Customer customer = customerDAO.findById(id);
        CustomerInfoView customerInfoView = customerTransform.transformToView(customer);

        List<Customer> cusList = customerDAO.findCustomerByCommitteeId(customer.getId());
        List<CustomerInfoView> cusViewList = new ArrayList<CustomerInfoView>();
        if(cusList != null && cusList.size() > 0){
            cusViewList = customerTransform.transformToViewList(cusList);
        }
        customerInfoView.setIndividualViewList(cusViewList);
        return customerInfoView;
    }

    public void deleteCustomerIndividual(long customerId){
        Customer customer = customerDAO.findById(customerId);

        if(customer.getSpouseId() != 0){ // have spouse
            Customer cus = customerDAO.findById(customer.getSpouseId());
            if(cus != null){
                if(cus.getAddressesList() != null && cus.getAddressesList().size() > 0){
                    addressDAO.delete(cus.getAddressesList());
                }
                if(cus.getIndividual() != null){
                    individualDAO.delete(cus.getIndividual());
                }

                //for check customer CSI
                List<CustomerCSI> customerCSIList = customerCSIDAO.findCustomerCSIByCustomerId(cus.getId());
                if(customerCSIList != null && customerCSIList.size() > 0){
                    customerCSIDAO.delete(customerCSIList);
                }

                //for check customer ncb
                NCB ncbSpouse = ncbDAO.findNcbByCustomer(cus.getId());
                ncbDAO.delete(ncbSpouse);

                customerDAO.delete(cus);
            }
        }

        if(customer.getIsSpouse() == 1){ // if this is spouse
            Customer cus = customerDAO.findCustomerBySpouseId(customer.getId());
            cus.setSpouseId(0);
            customerDAO.persist(cus);
        }

        if(customer.getAddressesList() != null && customer.getAddressesList().size() > 0){
            addressDAO.delete(customer.getAddressesList());
        }
        if(customer.getIndividual() != null){
            individualDAO.delete(customer.getIndividual());
        }

        //for check customer CSI
        List<CustomerCSI> customerCSIList = customerCSIDAO.findCustomerCSIByCustomerId(customerId);
        if(customerCSIList != null && customerCSIList.size() > 0){
            customerCSIDAO.delete(customerCSIList);
        }

        //for check customer ncb
        NCB ncb = ncbDAO.findNcbByCustomer(customer.getId());
        ncbDAO.delete(ncb);

        customerDAO.delete(customer);
    }

    public void deleteCustomerJuristic(long customerId){
        Customer customer = customerDAO.findById(customerId);
        List<Customer> cusIndList = customerDAO.findCustomerByCommitteeId(customer.getId()); // find committee
        if(cusIndList != null && cusIndList.size() > 0){
            for(Customer cusInd : cusIndList){
                deleteCustomerIndividual(cusInd.getId());
            }
        }

        if(customer.getAddressesList() != null && customer.getAddressesList().size() > 0){
            addressDAO.delete(customer.getAddressesList());
        }
        if(customer.getJuristic() != null){
            juristicDAO.delete(customer.getJuristic());
        }

        //for check customer CSI
        List<CustomerCSI> customerCSIList = customerCSIDAO.findCustomerCSIByCustomerId(customer.getId());
        if(customerCSIList != null && customerCSIList.size() > 0){
            customerCSIDAO.delete(customerCSIList);
        }

        //for check customer ncb
        NCB ncb = ncbDAO.findNcbByCustomer(customer.getId());
        ncbDAO.delete(ncb);

        customerDAO.delete(customer);
    }

    public List<CustomerInfoView> getAllCustomerByWorkCase(long workCaseId){
        log.info("getAllCustomerByWorkCase ::: workCaseId : {}", workCaseId);

        List<Customer> customerList = customerDAO.findByWorkCaseId(workCaseId);
        List<CustomerInfoView> customerInfoViewList = customerTransform.transformToViewList(customerList);

        return customerInfoViewList;
    }

    //** function for integration **//

    // *** Function for RM *** //
    public CustomerInfoResultView getCustomerInfoFromRM(CustomerInfoView customerInfoView){
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

        User user = getCurrentUser();
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
