package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.DocumentTypeDAO;
import com.clevel.selos.dao.master.ReferenceDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.BRMSInterface;
import com.clevel.selos.integration.DWHInterface;
import com.clevel.selos.integration.RMInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.corebanking.model.corporateInfo.CorporateResult;
import com.clevel.selos.integration.corebanking.model.individualInfo.IndividualResult;
import com.clevel.selos.integration.dwh.obligation.model.ObligationResult;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.BorrowerType;
import com.clevel.selos.model.db.master.CustomerEntity;
import com.clevel.selos.model.db.master.DocumentType;
import com.clevel.selos.model.db.master.Reference;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.db.working.CustomerCSI;
import com.clevel.selos.model.db.working.NCB;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.AddressView;
import com.clevel.selos.model.view.CustomerInfoResultView;
import com.clevel.selos.model.view.CustomerInfoSummaryView;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.transform.CustomerTransform;
import com.clevel.selos.transform.business.CustomerBizTransform;
import com.clevel.selos.transform.business.ObligationBizTransform;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
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
    ReferenceDAO referenceDAO;

    @Inject
    RMInterface rmInterface;
    @Inject
    BRMSInterface brmsInterface;
    @Inject
    DWHInterface dwhInterface;

    @Inject
    CustomerTransform customerTransform;
    @Inject
    CustomerBizTransform customerBizTransform;
    @Inject
    ObligationBizTransform obligationBizTransform;

    public CustomerInfoSummaryView getCustomerInfoSummary(long workCaseId){
        log.info("getCustomerInfoSummary ::: workCaseId : {}", workCaseId);
        CustomerInfoSummaryView customerInfoSummaryView = new CustomerInfoSummaryView();

        List<Customer> customerList = customerDAO.findByWorkCaseId(workCaseId);
        List<CustomerInfoView> customerInfoViewList = customerTransform.transformToViewList(customerList);

        //update percent share for juristic
        /*for(CustomerInfoView cV : customerInfoViewList){
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
        }*/

        List<CustomerInfoView> borrowerCustomerList = customerTransform.transformToBorrowerViewList(customerInfoViewList);
        customerInfoSummaryView.setBorrowerCustomerViewList(borrowerCustomerList);

        List<CustomerInfoView> guarantorCustomerList = customerTransform.transformToGuarantorViewList(customerInfoViewList);
        customerInfoSummaryView.setGuarantorCustomerViewList(guarantorCustomerList);

        List<CustomerInfoView> relatedCustomerList = customerTransform.transformToRelatedViewList(customerInfoViewList);
        customerInfoSummaryView.setRelatedCustomerViewList(relatedCustomerList);

        return customerInfoSummaryView;
    }

    public int getCaseBorrowerTypeIdByWorkCase(long workCaseId){
        log.info("getCaseBorrowerTypeIdByWorkCase ::: workCaseId : {}", workCaseId);
        int caseBorrowerTypeId = 0;
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        if(workCase != null){
            if(workCase.getBorrowerType() != null){
                caseBorrowerTypeId = workCase.getBorrowerType().getId();
            }
        }
        return caseBorrowerTypeId;
    }

    public long saveCustomerInfoIndividual(CustomerInfoView customerInfoView, long workCaseId){
        log.info("saveCustomerInfoIndividual ::: workCaseId : {} , customerInfoView : {}", workCaseId,customerInfoView);
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
        return customer.getId();
    }

    public long saveCustomerInfoJuristic(CustomerInfoView customerInfoView, long workCaseId){
        log.info("saveCustomerInfoJuristic ::: workCaseId : {} , customerInfoView : {}", workCaseId,customerInfoView);
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
        if(customerJuristic.getReference() != null){
            Reference reference = referenceDAO.findById(customerJuristic.getReference().getId());
            if(reference != null && reference.getId() != 0 && !reference.getPercentShare().equalsIgnoreCase("-")){
                if(customerJuristic.getShares() != null && customerJuristic.getJuristic().getTotalShare() != null){
                    customerJuristic.setPercentShare(Util.divide(customerJuristic.getShares(),customerJuristic.getJuristic().getTotalShare()));
                }
            }
        }
        customerDAO.persist(customerJuristic);
        juristicDAO.persist(customerJuristic.getJuristic());
        addressDAO.persist(customerJuristic.getAddressesList());

        if(customerInfoView.getIndividualViewList() != null && customerInfoView.getIndividualViewList().size() > 0){
            for(CustomerInfoView cusIndividual : customerInfoView.getIndividualViewList()){
                if(cusIndividual.getReference() != null){
                    Reference reference = referenceDAO.findById(cusIndividual.getReference().getId());
                    if(reference != null && reference.getId() != 0 && !reference.getPercentShare().equalsIgnoreCase("-")){
                        if(customerJuristic.getShares() != null && cusIndividual.getShares() != null){
                            cusIndividual.setPercentShare(Util.divide(cusIndividual.getShares(),customerJuristic.getJuristic().getTotalShare()));
                        }
                    }
                }
                cusIndividual.setIsCommittee(1);
                cusIndividual.setCommitteeId(customerJuristic.getId());
                if(cusIndividual.getSpouse() != null){
                    if(cusIndividual.getSpouse().getReference() != null){
                        Reference reference = referenceDAO.findById(cusIndividual.getSpouse().getReference().getId());
                        if(reference != null && reference.getId() != 0 && !reference.getPercentShare().equalsIgnoreCase("-")){
                            if(customerJuristic.getShares() != null && cusIndividual.getSpouse().getShares() != null){
                                cusIndividual.getSpouse().setPercentShare(Util.divide(cusIndividual.getSpouse().getShares(),customerJuristic.getJuristic().getTotalShare()));
                            }
                        }
                    }
                    cusIndividual.getSpouse().setIsCommittee(0);
                }
                saveCustomerInfoIndividual(cusIndividual,workCaseId);
            }
        }
        return customerJuristic.getId();
    }

    public CustomerInfoView getCustomerIndividualById(long id){
        log.info("getCustomerIndividualById ::: id : {}", id);
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
        log.info("getCustomerJuristicById ::: id : {}", id);
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
        log.info("deleteCustomerIndividual ::: customerId : {}", customerId);
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
                if(ncbSpouse != null && ncbSpouse.getId() != 0){
                    ncbDAO.delete(ncbSpouse);
                }

                customerDAO.delete(cus);
            }
        }

        if(customer.getIsSpouse() == 1){ // if this is spouse
            Customer cus = customerDAO.findMainCustomerBySpouseId(customer.getId());
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
        if(ncb != null && ncb.getId() != 0){
            ncbDAO.delete(ncb);
        }

        customerDAO.delete(customer);
    }

    public void deleteCustomerJuristic(long customerId){
        log.info("deleteCustomerJuristic ::: customerId : {}", customerId);
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
        if(ncb != null && ncb.getId() != 0){
            ncbDAO.delete(ncb);
        }

        customerDAO.delete(customer);
    }

    public List<CustomerInfoView> getAllCustomerByWorkCase(long workCaseId){
        log.info("getAllCustomerByWorkCase ::: workCaseId : {}", workCaseId);

        List<Customer> customerList = customerDAO.findByWorkCaseId(workCaseId);
        List<CustomerInfoView> customerInfoViewList = customerTransform.transformToViewList(customerList);

        return customerInfoViewList;
    }

    //** function for integration **//

    public CustomerInfoResultView retrieveInterfaceInfo(CustomerInfoView customerInfoView){
        log.info("retrieveInterfaceInfo with {}", customerInfoView != null? customerInfoView.getId(): "");
        CustomerInfoResultView customerInfoResult = getCustomerInfoFromRM(customerInfoView);

        /*
        //This is for setting the CustomerInfoResult to test the obligation without RM Call.
        CustomerInfoResultView customerInfoResult = new CustomerInfoResultView();
        customerInfoResult.setActionResult(ActionResult.SUCCESS);
        customerInfoResult.setReason("Customer Found.");
        customerInfoResult.setCustomerInfoView(customerInfoView);
        */

        if(customerInfoResult.getActionResult() == ActionResult.SUCCESS){
            customerInfoResult.setCustomerInfoView(getCustomerCreditInfo(customerInfoResult.getCustomerInfoView()));
        }

        log.info("return {}", customerInfoResult.getActionResult());
        return customerInfoResult;
    }

    // *** Function for RM *** //
    public CustomerInfoResultView getCustomerInfoFromRM(CustomerInfoView customerInfoView){
        CustomerInfoResultView customerInfoResultSearch = new CustomerInfoResultView();
        log.info("getCustomerInfoFromRM ::: customerInfoView.getSearchBy : {}", customerInfoView.getSearchBy());
        log.info("getCustomerInfoFromRM ::: customerInfoView.getSearchId : {}", customerInfoView.getSearchId());

        DocumentType masterDocumentType = new DocumentType();

        RMInterface.SearchBy searchBy = RMInterface.SearchBy.CUSTOMER_ID;
        if(customerInfoView.getSearchBy() == 1){
            searchBy = RMInterface.SearchBy.CUSTOMER_ID;
            masterDocumentType = documentTypeDAO.findById(customerInfoView.getDocumentType().getId());
        }else if(customerInfoView.getSearchBy() == 2){
            searchBy = RMInterface.SearchBy.TMBCUS_ID;
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

        log.info("getCustomerInfoFromRM ::: searchBy : {}", searchBy);
        log.info("getCustomerInfoFromRM ::: documentType : {}", documentType);


        if(customerInfoView.getCustomerEntity().getId() == 1) {
            IndividualResult individualResult = rmInterface.getIndividualInfo(userId, customerInfoView.getSearchId(), documentType, searchBy);
            log.info("getCustomerInfoFromRM ::: individualResult : {}", individualResult);
            customerInfoResultSearch = customerBizTransform.tranformIndividual(individualResult);
        } else if(customerInfoView.getCustomerEntity().getId() == 2){
            CorporateResult corporateResult = rmInterface.getCorporateInfo(userId, customerInfoView.getSearchId(), documentType, searchBy);
            log.info("getCustomerInfoFromRM ::: corporateResult : {}", corporateResult);
            customerInfoResultSearch = customerBizTransform.tranformJuristic(corporateResult);
        }

        log.info("getCustomerInfoFromRM ::: success!!");
        log.info("getCustomerInfoFromRM ::: customerInfoSearch : {}", customerInfoResultSearch);
        return customerInfoResultSearch;
    }

    /**
     * To get the Customer Credit Information from Obligation and update into the same customerInfoView
     * @param customerInfoView
     * @return customerInfoView
     */
    public CustomerInfoView getCustomerCreditInfo(CustomerInfoView customerInfoView){
        if(customerInfoView != null && customerInfoView.getTmbCustomerId() != null){
            log.info("retrieveCustomerCreditInfo, with TMB Customer ID {}", customerInfoView.getTmbCustomerId());
            ObligationResult obligationResult = dwhInterface.getObligationData(getCurrentUserID(), customerInfoView.getTmbCustomerId());
            if(obligationResult.getActionResult().equals(ActionResult.SUCCESS)){
                customerInfoView = obligationBizTransform.getCustomerInfoView(obligationResult.getObligationList(), customerInfoView);
            }
        }
        return customerInfoView;
    }

    public List<CustomerInfoView> getGuarantorByWorkCase(long workCaseId){
        log.info("getGuarantorByWorkCase ::: workCaseId : {}", workCaseId);

        List<Customer> customerList = customerDAO.findGuarantorByWorkCaseId(workCaseId);
        List<CustomerInfoView> customerInfoViewList = customerTransform.transformToViewList(customerList);

        return customerInfoViewList;
    }

    public List<CustomerInfoView> getCollateralOwnerUWByWorkCase(long workCaseId){
        log.info("getCollateralOwnerUWByWorkCase ::: workCaseId : {}", workCaseId);

        List<Customer> customerList = customerDAO.findCollateralOwnerUWByWorkCaseId(workCaseId);
        List<CustomerInfoView> customerInfoViewList = customerTransform.transformToViewList(customerList);

        return customerInfoViewList;
    }

    public CustomerInfoView getCustomerById(CustomerInfoView customerInfo){
        log.info("getCustomerById ::: customerInfo : {}", customerInfo);
        Customer customer = customerDAO.findById(customerInfo.getId());
        CustomerInfoView customerInfoView = customerTransform.transformToView(customer);
        return customerInfoView;
    }

    public int checkAddress(AddressView add1, AddressView add2){
        log.info("checkAddress ::: add1 : {} , add2 : {}", add1,add2);
        //currentAddress = 1 is current address
        //currentAddress = 0 is other address
        int currentAddress = 0;
        if(add1.getAddressNo() != null && add2.getAddressNo() != null){
            if(!add1.getAddressNo().equalsIgnoreCase(add2.getAddressNo())){
                return currentAddress;
            }
        } else if(add1.getAddressNo() != null && add2.getAddressNo() == null){
            return currentAddress;
        } else if(add2.getAddressNo() != null && add1.getAddressNo() == null){
            return currentAddress;
        }

        if(add1.getMoo() != null && add2.getMoo() != null){
            if(!add1.getMoo().equalsIgnoreCase(add2.getMoo())){
                return currentAddress;
            }
        } else if(add1.getMoo() != null && add2.getMoo() == null){
            return currentAddress;
        } else if(add2.getMoo() != null && add1.getMoo() == null){
            return currentAddress;
        }

        if(add1.getBuilding() != null && add2.getBuilding() != null){
            if(!add1.getBuilding().equalsIgnoreCase(add2.getBuilding())){
                return currentAddress;
            }
        } else if(add1.getBuilding() != null && add2.getBuilding() == null){
            return currentAddress;
        } else if(add2.getBuilding() != null && add1.getBuilding() == null){
            return currentAddress;
        }

        if(add1.getRoad() != null && add2.getRoad() != null){
            if(!add1.getRoad().equalsIgnoreCase(add2.getRoad())){
                return currentAddress;
            }
        } else if(add1.getRoad() != null && add2.getRoad() == null){
            return currentAddress;
        } else if(add2.getRoad() != null && add1.getRoad() == null){
            return currentAddress;
        }

        if(add1.getPostalCode() != null && add2.getPostalCode() != null){
            if(!add1.getPostalCode().equalsIgnoreCase(add2.getPostalCode())){
                return currentAddress;
            }
        } else if(add1.getPostalCode() != null && add2.getPostalCode() == null){
            return currentAddress;
        } else if(add2.getPostalCode() != null && add1.getPostalCode() == null){
            return currentAddress;
        }

        if(add1.getPhoneNumber() != null && add2.getPhoneNumber() != null){
            if(!add1.getPhoneNumber().equalsIgnoreCase(add2.getPhoneNumber())){
                return currentAddress;
            }
        } else if(add1.getPhoneNumber() != null && add2.getPhoneNumber() == null){
            return currentAddress;
        } else if(add2.getPhoneNumber() != null && add1.getPhoneNumber() == null){
            return currentAddress;
        }

        if(add1.getExtension() != null && add2.getExtension() != null){
            if(!add1.getExtension().equalsIgnoreCase(add2.getExtension())){
                return currentAddress;
            }
        } else if(add1.getExtension() != null && add2.getExtension() == null){
            return currentAddress;
        } else if(add2.getExtension() != null && add1.getExtension() == null){
            return currentAddress;
        }

        if(add1.getContactName() != null && add2.getContactName() != null){
            if(!add1.getContactName().equalsIgnoreCase(add2.getContactName())){
                return currentAddress;
            }
        } else if(add1.getContactName() != null && add2.getContactName() == null){
            return currentAddress;
        } else if(add2.getContactName() != null && add1.getContactName() == null){
            return currentAddress;
        }

        if(add1.getContactPhone() != null && add2.getContactPhone() != null){
            if(!add1.getContactPhone().equalsIgnoreCase(add2.getContactPhone())){
                return currentAddress;
            }
        } else if(add1.getContactPhone() != null && add2.getContactPhone() == null){
            return currentAddress;
        } else if(add2.getContactPhone() != null && add1.getContactPhone() == null){
            return currentAddress;
        }

        if(add1.getAddress() != null && add2.getAddress() != null){
            if(!add1.getAddress().equalsIgnoreCase(add2.getAddress())){
                return currentAddress;
            }
        } else if(add1.getAddress() != null && add2.getAddress() == null){
            return currentAddress;
        } else if(add2.getAddress() != null && add1.getAddress() == null){
            return currentAddress;
        }

        if(add1.getProvince() != null && add2.getProvince() != null){
            if(add1.getProvince().getCode() != add2.getProvince().getCode()){
                return currentAddress;
            }
        } else if(add1.getProvince() != null && add2.getProvince() == null){
            return currentAddress;
        } else if(add2.getProvince() != null && add1.getProvince() == null){
            return currentAddress;
        }

        if(add1.getDistrict() != null && add2.getDistrict() != null){
            if(add1.getDistrict().getId() != add2.getDistrict().getId()){
                return currentAddress;
            }
        } else if(add1.getDistrict() != null && add2.getDistrict() == null){
            return currentAddress;
        } else if(add2.getDistrict() != null && add1.getDistrict() == null){
            return currentAddress;
        }

        if(add1.getSubDistrict() != null && add2.getSubDistrict() != null){
            if(add1.getSubDistrict().getCode() != add2.getSubDistrict().getCode()){
                return currentAddress;
            }
        } else if(add1.getSubDistrict() != null && add2.getSubDistrict() == null){
            return currentAddress;
        } else if(add2.getSubDistrict() != null && add1.getSubDistrict() == null){
            return currentAddress;
        }

        if(add1.getCountry() != null && add2.getCountry() != null){
            if(add1.getCountry().getCode() != add2.getCountry().getCode()){
                return currentAddress;
            }
        } else if(add1.getCountry() != null && add2.getCountry() == null){
            return currentAddress;
        } else if(add2.getCountry() != null && add1.getCountry() == null){
            return currentAddress;
        }

        currentAddress = 1;
        return currentAddress;
    }
}
