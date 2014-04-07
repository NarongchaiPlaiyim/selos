package com.clevel.selos.transform;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.working.AddressDAO;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.dao.working.CustomerOblAccountInfoDAO;
import com.clevel.selos.dao.working.CustomerOblInfoDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.AttorneyRelationType;
import com.clevel.selos.model.BorrowerType;
import com.clevel.selos.model.Gender;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.util.Util;

import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.inject.Inject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CustomerTransform extends Transform {
    @Inject
    @SELOS
    Logger log;

    @Inject
    private CustomerCSITransform customerCSITransform;
    @Inject
    private SBFScoreTransform sbfScoreTransform;
    @Inject
    private ServiceSegmentTransform serviceSegmentTransform;

    @Inject
    private CustomerDAO customerDAO;
    @Inject
    private CustomerEntityDAO customerEntityDAO;
    @Inject
    private DocumentTypeDAO documentTypeDAO;
    @Inject
    private TitleDAO titleDAO;
    @Inject
    private RelationDAO relationDAO;
    @Inject
    private ReferenceDAO referenceDAO;
    @Inject
    private MaritalStatusDAO maritalStatusDAO;
    @Inject
    private EducationDAO educationDAO;
    @Inject
    private NationalityDAO nationalityDAO;
    @Inject
    private OccupationDAO occupationDAO;
    @Inject
    private AddressTypeDAO addressTypeDAO;
    @Inject
    private ProvinceDAO provinceDAO;
    @Inject
    private DistrictDAO districtDAO;
    @Inject
    private SubDistrictDAO subDistrictDAO;
    @Inject
    private CountryDAO countryDAO;
    @Inject
    private AddressDAO addressDAO;
    @Inject
    private BusinessTypeDAO businessTypeDAO;
    @Inject
    private BusinessSubTypeDAO businessSubTypeDAO;
    @Inject
    private WarningCodeDAO warningCodeDAO;
    @Inject
    private KYCLevelDAO kycLevelDAO;
    @Inject
    private RaceDAO raceDAO;
    @Inject
    private CustomerOblInfoDAO customerOblInfoDAO;
    @Inject
    private CustomerOblAccountInfoDAO customerOblAccountInfoDAO;
    @Inject
    private IncomeSourceDAO incomeSourceDAO;

    public CustomerInfoView transformToView(Customer customer){
        log.info("Start - transformToView ::: customer : {}", customer);
        CustomerInfoView customerInfoView = new CustomerInfoView();

        customerInfoView.setId(customer.getId());

        customerInfoView.setCustomerEntity(customer.getCustomerEntity());
        if(customerInfoView.getCustomerEntity() == null){
            customerInfoView.setCustomerEntity(new CustomerEntity());
        }

        customerInfoView.setDocumentType(customer.getDocumentType());
        if(customerInfoView.getDocumentType() == null){
            customerInfoView.setDocumentType(new DocumentType());
        }

        customerInfoView.setDocumentAuthorizeBy(customer.getDocumentAuthorizeBy());

        customerInfoView.setCollateralOwner(customer.getCollateralOwner());
        customerInfoView.setPercentShare(customer.getPercentShare());
        customerInfoView.setApproxIncome(customer.getApproxIncome());
        customerInfoView.setTmbCustomerId(customer.getTmbCustomerId());
        customerInfoView.setDocumentExpiredDate(customer.getDocumentExpiredDate());
        customerInfoView.setTitleTh(customer.getTitle());
        if(customerInfoView.getTitleTh() == null){
            customerInfoView.setTitleTh(new Title());
        }
        customerInfoView.setFirstNameTh(customer.getNameTh());
        customerInfoView.setLastNameTh(customer.getLastNameTh());
        if(customerInfoView.getLastNameTh() == null){
            customerInfoView.setLastNameTh("");
        }

        customerInfoView.setTitleEn(customer.getTitle());
        if(customerInfoView.getTitleEn() == null){
            customerInfoView.setTitleEn(new Title());
        }
        customerInfoView.setFirstNameEn(customer.getNameEn());
        customerInfoView.setLastNameEn(customer.getLastNameEn());
        if(customerInfoView.getLastNameEn() == null){
            customerInfoView.setLastNameEn("");
        }

        customerInfoView.setAge(customer.getAge());
        customerInfoView.setNcbFlag(customer.getNcbFlag());
        customerInfoView.setSearchFromRM(customer.getSearchFromRM());

        customerInfoView.setBusinessType(customer.getBusinessType());
        if(customerInfoView.getBusinessType() == null){
            customerInfoView.setBusinessType(new BusinessType());
        }

        customerInfoView.setBusinessSubType(customer.getBusinessSubType());
        if(customerInfoView.getBusinessSubType() == null){
            customerInfoView.setBusinessSubType(new BusinessSubType());
        }

        customerInfoView.setRelation(customer.getRelation());
        if(customerInfoView.getRelation() == null){
            customerInfoView.setRelation(new Relation());
        }

        customerInfoView.setReference(customer.getReference());
        if(customerInfoView.getReference() == null){
            customerInfoView.setReference(new Reference());
        }

        List<CustomerCSIView> customerCSIViewList = customerCSITransform.transformToViewList(customer.getCustomerCSIList());
        customerInfoView.setCustomerCSIList(customerCSIViewList);

        customerInfoView.setIsSpouse(customer.getIsSpouse());
        customerInfoView.setSpouseId(customer.getSpouseId());
        customerInfoView.setSearchFromRM(customer.getSearchFromRM());
        customerInfoView.setDocumentAuthorizeDate(customer.getDocumentAuthorizeDate());
        customerInfoView.setKycReason(customer.getKycReason());
        customerInfoView.setWorthiness(customer.getWorthiness());
        customerInfoView.setMobileNumber(customer.getMobileNumber());
        customerInfoView.setFaxNumber(customer.getFaxNumber());
        customerInfoView.setEmail(customer.getEmail());
        customerInfoView.setCovenantFlag(customer.getCovenantFlag());
        customerInfoView.setReviewFlag(customer.getReviewFlag());
        customerInfoView.setReason(customer.getReason());

        customerInfoView.setKycLevel(customer.getKycLevel());
        if(customerInfoView.getKycLevel() == null){
            customerInfoView.setKycLevel(new KYCLevel());
        }
        customerInfoView.setNcbFlag(customer.getNcbFlag());
        customerInfoView.setCsiFlag(customer.getCsiFlag());
        customerInfoView.setSearchFromRM(customer.getSearchFromRM());

        customerInfoView.setMailingAddressType(customer.getMailingAddressType());
        if(customerInfoView.getMailingAddressType() == null){
            customerInfoView.setMailingAddressType(new AddressType());
        }

		customerInfoView.setMailingAddressType(customer.getMailingAddressType());
        if(customerInfoView.getMailingAddressType() == null){
            customerInfoView.setMailingAddressType(new AddressType());
        }

        customerInfoView.setSourceIncome(customer.getSourceIncome());
        if(customerInfoView.getSourceIncome() == null){
            customerInfoView.setSourceIncome(new IncomeSource());
        }

        customerInfoView.setCountryIncome(customer.getCountryIncome());
        if(customerInfoView.getCountryIncome() == null){
            customerInfoView.setCountryIncome(new Country());
        }

        customerInfoView.setIsCommittee(customer.getIsCommittee());
        customerInfoView.setCommitteeId(customer.getJuristicId());

        customerInfoView.setValidId(2);

        customerInfoView.setCollateralOwner(customer.getCollateralOwner());

        customerInfoView.setSearchBy(customer.getSearchBy());
        customerInfoView.setSearchId(customer.getSearchId());

		customerInfoView.setCsiFlag(customer.getCsiFlag());

		customerInfoView.setShares(customer.getShares());

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        if(customer.getAddressesList() != null && customer.getAddressesList().size() > 0){
            List<Address> addressList = customer.getAddressesList();
            for(Address address : addressList){
                AddressView addressView = new AddressView();
                addressView.setId(address.getId());
                addressView.setAddressType(address.getAddressType());
                addressView.setAddressNo(address.getAddressNo());
                addressView.setMoo(address.getMoo());
                addressView.setBuilding(address.getBuilding());
                addressView.setRoad(address.getRoad());
                addressView.setProvince(address.getProvince());
                if(addressView.getProvince() == null){
                    addressView.setProvince(new Province());
                }
                addressView.setDistrict(address.getDistrict());
                if(addressView.getDistrict() == null){
                    addressView.setDistrict(new District());
                }
                addressView.setSubDistrict(address.getSubDistrict());
                if(addressView.getSubDistrict() == null){
                    addressView.setSubDistrict(new SubDistrict());
                }
                addressView.setCountry(address.getCountry());
                if(addressView.getCountry() == null){
                    addressView.setCountry(new Country());
                }
                addressView.setPostalCode(address.getPostalCode());
                addressView.setPhoneNumber(address.getPhoneNumber());
                addressView.setExtension(address.getExtension());
                addressView.setContactName(address.getContactName());
                addressView.setContactPhone(address.getContactPhone());
                addressView.setAddress(address.getAddress());
                addressView.setAddressTypeFlag(address.getAddressTypeFlag());

                if(address.getAddressType().getId() == 1){
                    // Current address
                    customerInfoView.setCurrentAddress(addressView);
                    if(customerInfoView.getCurrentAddress() == null){
                        customerInfoView.setCurrentAddress(new AddressView());
                    }
                } else if(address.getAddressType().getId() == 2){
                    // Register Address
                    customerInfoView.setRegisterAddress(addressView);
                    if(customerInfoView.getRegisterAddress() == null){
                        customerInfoView.setRegisterAddress(new AddressView());
                    }
                } else if(address.getAddressType().getId() == 3){
                    // Work Address
                    customerInfoView.setWorkAddress(addressView);
                    if(customerInfoView.getWorkAddress() == null){
                        customerInfoView.setWorkAddress(new AddressView());
                    }
                } else if(address.getAddressType().getId() == 4){
                    // Register Address
                    customerInfoView.setRegisterAddress(addressView);
                    if(customerInfoView.getRegisterAddress() == null){
                        customerInfoView.setRegisterAddress(new AddressView());
                    }
                } else if(address.getAddressType().getId() == 5){
                    // Work Address
                    customerInfoView.setWorkAddress(addressView);
                    if(customerInfoView.getWorkAddress() == null){
                        customerInfoView.setWorkAddress(new AddressView());
                    }
                }
            }
        } else {
            customerInfoView.setCurrentAddress(new AddressView());
            customerInfoView.setRegisterAddress(new AddressView());
            customerInfoView.setWorkAddress(new AddressView());
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        if(customer.getCustomerEntity().getId() == 1){
            //Individual
            Individual individual = customer.getIndividual();

            if(individual != null){
            	customerInfoView.setNeedAttorney(RadioValue.YES.equals(individual.getAttorneyRequired()));
            	customerInfoView.setSignContract(customer.getReference().isCanSignContract());
                customerInfoView.setIndividualId(individual.getId());
                customerInfoView.setCitizenId(individual.getCitizenId());
                customerInfoView.setGender(individual.getGender());
                customerInfoView.setDateOfBirth(individual.getBirthDate());
                customerInfoView.setNumberOfChild(individual.getNumberOfChildren());

                if(individual.getEducation() != null){
                    customerInfoView.setEducation(individual.getEducation());
                } else {
                    customerInfoView.setEducation(new Education());
                }

                if(individual.getMaritalStatus() != null){
                    customerInfoView.setMaritalStatus(individual.getMaritalStatus());
                } else {
                    customerInfoView.setMaritalStatus(new MaritalStatus());
                }

                if(individual.getNationality() != null){
                    customerInfoView.setNationality(individual.getNationality());
                } else {
                    customerInfoView.setNationality(new Nationality());
                }

                if(individual.getSndNationality() != null){
                    customerInfoView.setSndNationality(individual.getSndNationality());
                } else {
                    customerInfoView.setSndNationality(new Nationality());
                }

                if(individual.getOccupation() != null){
                    customerInfoView.setOccupation(individual.getOccupation());
                } else {
                    customerInfoView.setOccupation(new Occupation());
                }

                if(individual.getCitizenCountry() != null){
                    customerInfoView.setCitizenCountry(individual.getCitizenCountry());
                } else {
                    customerInfoView.setCitizenCountry(new Country());
                }

                if(individual.getRace() != null){
                    customerInfoView.setOrigin(individual.getRace());
                } else {
                    customerInfoView.setOrigin(new Race());
                }
            } else {
                customerInfoView.setEducation(new Education());
                customerInfoView.setMaritalStatus(new MaritalStatus());
                customerInfoView.setNationality(new Nationality());
                customerInfoView.setSndNationality(new Nationality());
                customerInfoView.setOccupation(new Occupation());
                customerInfoView.setCitizenCountry(new Country());
                customerInfoView.setOrigin(new Race());
            }
        } else {
            //Juristic
            Juristic juristic = customer.getJuristic();
            log.info("transform juristic : {}", juristic);

            if(juristic != null){
                customerInfoView.setCapital(juristic.getCapital());
                customerInfoView.setPaidCapital(juristic.getPaidCapital());
                customerInfoView.setFinancialYear(juristic.getFinancialYear());
                customerInfoView.setDateOfRegister(juristic.getRegisterDate());
                customerInfoView.setSignCondition(juristic.getSignCondition());
                customerInfoView.setRegistrationId(juristic.getRegistrationId());
                customerInfoView.setDocumentIssueDate(juristic.getDocumentIssueDate());
                customerInfoView.setSalesFromFinancialStmt(juristic.getSalesFromFinancialStmt());
                customerInfoView.setShareHolderRatio(juristic.getShareHolderRatio());
                customerInfoView.setNumberOfAuthorizedUsers(juristic.getNumberOfAuthorizedUsers());
                customerInfoView.setTotalShare(juristic.getTotalShare());
                customerInfoView.setContactName(juristic.getContactName());
            }
        }

        // set customer credit information.
        if(customer.getCustomerOblInfo() != null){
            CustomerOblInfo customerOblInfo = customer.getCustomerOblInfo();
            customerInfoView.setCustomerOblInfoID(customerOblInfo.getId());
            customerInfoView.setServiceSegmentView(serviceSegmentTransform.transformToView(customerOblInfo.getServiceSegment()));
            customerInfoView.setExistingSMECustomer(customerOblInfo.getExistingSMECustomer());
            customerInfoView.setLastReviewDate(customerOblInfo.getLastReviewDate());
            customerInfoView.setExtendedReviewDate(customerOblInfo.getExtendedReviewDate());
            customerInfoView.setExtendedReviewDateFlag(customerOblInfo.getExtendedReviewDateFlag());
            customerInfoView.setNextReviewDate(customerOblInfo.getNextReviewDate());
            customerInfoView.setNextReviewDateFlag(customerOblInfo.getNextReviewDateFlag());
            customerInfoView.setLastContractDate(customerOblInfo.getLastContractDate());
            customerInfoView.setAdjustClass(customerOblInfo.getAdjustClass());
            customerInfoView.setRatingFinal(sbfScoreTransform.transformToView(customerOblInfo.getRatingFinal()));
            customerInfoView.setUnpaidFeeInsurance(customerOblInfo.getUnpaidFeeInsurance());
            customerInfoView.setPendingClaimLG(customerOblInfo.getPendingClaimLG());
        }

        //for show jurLv
        if(customer.getIsCommittee() == 1){
            Customer cusCommittee = customerDAO.findById(customer.getJuristicId());
            if(customer.getReference() != null){
                customerInfoView.setJurLv(customer.getReference().getDescription()+" of "+cusCommittee.getNameTh());
            }
        } else {
            customerInfoView.setJurLv("-");
        }

        //for show indLv
        if(customer.getIsSpouse() == 1){ // is spouse
            Customer mainCus = customerDAO.findMainCustomerBySpouseId(customer.getId());
            if(customer.getReference() != null){
                customerInfoView.setIndLv(customer.getReference().getDescription()+" of "+mainCus.getNameTh()+" "+mainCus.getLastNameTh());
            }
            if(mainCus.getIsCommittee() == 1){ // is customer from spouse is committee
                Customer mainJur = customerDAO.findById(mainCus.getJuristicId());
                if(mainCus.getReference() != null){
                    customerInfoView.setJurLv(mainCus.getReference().getDescription()+" of "+mainJur.getNameTh());
                }
            }
        } else {
            customerInfoView.setIndLv("-");
        }

        log.info("Return - transformToView ::: customerInfoView : {}", customerInfoView);
        return customerInfoView;
    }

    public Customer transformToModel(CustomerInfoView customerInfoView, WorkCasePrescreen workCasePrescreen, WorkCase workCase, User user){
        log.info("Start - transformToModel ::: customerInfoView : {}", customerInfoView);
        Customer customer = new Customer();
        if(customerInfoView.getId() != 0){
            customer = customerDAO.findById(customerInfoView.getId());
        }else{
            customer.setCreateDate(DateTime.now().toDate());
            customer.setCreateBy(user);
        }
        customer.setModifyDate(DateTime.now().toDate());
        customer.setModifyBy(user);
        customer.setWorkCase(workCase);
        customer.setWorkCasePrescreen(workCasePrescreen);

        if(customerInfoView.getCustomerEntity() != null && customerInfoView.getCustomerEntity().getId() != 0){
            customer.setCustomerEntity(customerEntityDAO.findById(customerInfoView.getCustomerEntity().getId()));
        } else {
            customer.setCustomerEntity(null);
        }

        if(customerInfoView.getDocumentType() != null && customerInfoView.getDocumentType().getId() != 0){
            customer.setDocumentType(documentTypeDAO.findById(customerInfoView.getDocumentType().getId()));
        } else {
            customer.setDocumentType(null);
        }

        customer.setTmbCustomerId(customerInfoView.getTmbCustomerId());
        customer.setDocumentAuthorizeBy(customerInfoView.getDocumentAuthorizeBy());
        customer.setCollateralOwner(customerInfoView.getCollateralOwner());
        customer.setShares(customerInfoView.getShares());
        customer.setApproxIncome(customerInfoView.getApproxIncome());
        customer.setDocumentExpiredDate(customerInfoView.getDocumentExpiredDate());

        if(customerInfoView.getTitleTh() != null && customerInfoView.getTitleTh().getId() != 0){
            customer.setTitle(titleDAO.findById(customerInfoView.getTitleTh().getId()));
        } else {
            customer.setTitle(null);
        }
        customer.setNameTh(customerInfoView.getFirstNameTh());
        customer.setLastNameTh(customerInfoView.getLastNameTh());
        customer.setNameEn(customerInfoView.getFirstNameEn());
        customer.setLastNameEn(customerInfoView.getLastNameEn());

        customer.setAge(customerInfoView.getAge());
        customer.setNcbFlag(customerInfoView.getNcbFlag());
        customer.setCsiFlag(customerInfoView.getCsiFlag());
        customer.setPercentShare(customerInfoView.getPercentShare());

        if(customerInfoView.getBusinessType() != null && customerInfoView.getBusinessType().getId() != 0){
            customer.setBusinessType(businessTypeDAO.findById(customerInfoView.getBusinessType().getId()));
        } else {
            customer.setBusinessType(null);
        }

        if(customerInfoView.getBusinessSubType() != null && customerInfoView.getBusinessSubType().getId() != 0){
            customer.setBusinessSubType(businessSubTypeDAO.findById(customerInfoView.getBusinessSubType().getId()));
        } else {
            customer.setBusinessSubType(null);
        }

        if(customerInfoView.getRelation() != null && customerInfoView.getRelation().getId() != 0){
            customer.setRelation(relationDAO.findById(customerInfoView.getRelation().getId()));
        } else {
            customer.setRelation(null);
        }

        if(customerInfoView.getReference() != null && customerInfoView.getReference().getId() != 0){
            customer.setReference(referenceDAO.findById(customerInfoView.getReference().getId()));
        } else {
            customer.setReference(null);
        }

        customer.setIsSpouse(customerInfoView.getIsSpouse());
        customer.setSpouseId(customerInfoView.getSpouseId());
        customer.setSearchFromRM(customerInfoView.getSearchFromRM());
        customer.setDocumentAuthorizeDate(customerInfoView.getDocumentAuthorizeDate());
        customer.setKycReason(customerInfoView.getKycReason());
        customer.setWorthiness(customerInfoView.getWorthiness());
        customer.setMobileNumber(customerInfoView.getMobileNumber());
        customer.setFaxNumber(customerInfoView.getFaxNumber());
        customer.setEmail(customerInfoView.getEmail());
        customer.setCovenantFlag(customerInfoView.getCovenantFlag());
        customer.setReviewFlag(customerInfoView.getReviewFlag());
        customer.setReason(customerInfoView.getReason());

        if(customerInfoView.getKycLevel() != null && customerInfoView.getKycLevel().getId() != 0){
            customer.setKycLevel(kycLevelDAO.findById(customerInfoView.getKycLevel().getId()));
        } else {
            customer.setKycLevel(null);
        }
        if(customerInfoView.getMailingAddressType() != null && customerInfoView.getMailingAddressType().getId() != 0){
            customer.setMailingAddressType(addressTypeDAO.findById(customerInfoView.getMailingAddressType().getId()));
        } else {
            customer.setMailingAddressType(null);
        }
        if(customerInfoView.getSourceIncome() != null && customerInfoView.getSourceIncome().getId() != 0){
            customer.setSourceIncome(incomeSourceDAO.findById(customerInfoView.getSourceIncome().getId()));
        } else {
            customer.setSourceIncome(null);
        }
        if(customerInfoView.getCountryIncome() != null && customerInfoView.getCountryIncome().getId() != 0){
            customer.setCountryIncome(countryDAO.findById(customerInfoView.getCountryIncome().getId()));
        } else {
            customer.setCountryIncome(null);
        }

        customer.setSearchBy(customerInfoView.getSearchBy());
        customer.setSearchId(customerInfoView.getSearchId());

        customer.setIsCommittee(customerInfoView.getIsCommittee());
        customer.setJuristicId(customerInfoView.getCommitteeId());

		customer.setCsiFlag(customerInfoView.getCsiFlag());

//        log.info("transformToModel : customer before adding address : {}", customer);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        List<Address> addressList = new ArrayList<Address>();

        if(customerInfoView.getCurrentAddress() != null){
            Address address = new Address();
            AddressView currentAddress = customerInfoView.getCurrentAddress();
            if(currentAddress.getId() != 0){
                address = addressDAO.findById(currentAddress.getId());
            }

            address.setCustomer(customer);

            //Get Address Type = Current//
            AddressType addressType = addressTypeDAO.findById(1);
            address.setAddressType(addressType);

            address.setAddressNo(currentAddress.getAddressNo());
            address.setMoo(currentAddress.getMoo());
            address.setBuilding(currentAddress.getBuilding());
            address.setRoad(currentAddress.getRoad());

            if(currentAddress.getProvince() != null && currentAddress.getProvince().getCode() != 0){
                Province province = provinceDAO.findById(currentAddress.getProvince().getCode());
                address.setProvince(province);
            } else {
                address.setProvince(null);
            }

            if(currentAddress.getDistrict() != null && currentAddress.getDistrict().getId() != 0){
                District district = districtDAO.findById(currentAddress.getDistrict().getId());
                address.setDistrict(district);
            } else {
                address.setDistrict(null);
            }

            if(currentAddress.getSubDistrict() != null && currentAddress.getSubDistrict().getCode() != 0){
                SubDistrict subDistrict = subDistrictDAO.findById(currentAddress.getSubDistrict().getCode());
                address.setSubDistrict(subDistrict);
            } else {
                address.setSubDistrict(null);
            }

            if(currentAddress.getCountry() != null && currentAddress.getCountry().getId() != 0){
                Country country = countryDAO.findById(currentAddress.getCountry().getId());
                address.setCountry(country);
            } else {
                address.setCountry(null);
            }

            address.setPostalCode(currentAddress.getPostalCode());

            address.setPhoneNumber(currentAddress.getPhoneNumber());
            address.setExtension(currentAddress.getExtension());
            address.setContactName(currentAddress.getContactName());
            address.setContactPhone(currentAddress.getContactPhone());
            address.setAddress(currentAddress.getAddress());
            address.setAddressTypeFlag(currentAddress.getAddressTypeFlag());

            addressList.add(address);
        }

        if(customerInfoView.getRegisterAddress() != null){
            Address address = new Address();
            AddressView registerAddress = customerInfoView.getRegisterAddress();
            if(registerAddress.getId() != 0){
                address = addressDAO.findById(registerAddress.getId());
            }
            address.setCustomer(customer);

            //Get Address Type = Register//
            if(customerInfoView.getCustomerEntity().getId() == 1){
                AddressType addressType = addressTypeDAO.findById(2);
                address.setAddressType(addressType);
            } else {
                AddressType addressType = addressTypeDAO.findById(4);
                address.setAddressType(addressType);
            }

            address.setAddressNo(registerAddress.getAddressNo());
            address.setMoo(registerAddress.getMoo());
            address.setBuilding(registerAddress.getBuilding());
            address.setRoad(registerAddress.getRoad());

            if(registerAddress.getProvince() != null && registerAddress.getProvince().getCode() != 0){
                Province province = provinceDAO.findById(registerAddress.getProvince().getCode());
                address.setProvince(province);
            } else {
                address.setProvince(null);
            }

            if(registerAddress.getDistrict() != null && registerAddress.getDistrict().getId() != 0){
                District district = districtDAO.findById(registerAddress.getDistrict().getId());
                address.setDistrict(district);
            } else {
                address.setDistrict(null);
            }

            if(registerAddress.getSubDistrict() != null && registerAddress.getSubDistrict().getCode() != 0){
                SubDistrict subDistrict = subDistrictDAO.findById(registerAddress.getSubDistrict().getCode());
                address.setSubDistrict(subDistrict);
            } else {
                address.setSubDistrict(null);
            }

            if(registerAddress.getCountry() != null && registerAddress.getCountry().getId() != 0){
                Country country = countryDAO.findById(registerAddress.getCountry().getId());
                address.setCountry(country);
            } else {
                address.setCountry(null);
            }

            address.setPostalCode(registerAddress.getPostalCode());
            address.setPhoneNumber(registerAddress.getPhoneNumber());
            address.setExtension(registerAddress.getExtension());
            address.setContactName(registerAddress.getContactName());
            address.setContactPhone(registerAddress.getContactPhone());
            address.setAddress(registerAddress.getAddress());
            address.setAddressTypeFlag(registerAddress.getAddressTypeFlag());

            addressList.add(address);
        }

        if(customerInfoView.getWorkAddress() != null){
            Address address = new Address();
            AddressView workAddress = customerInfoView.getWorkAddress();
            if(workAddress.getId() != 0){
                address = addressDAO.findById(workAddress.getId());
            }
            address.setCustomer(customer);

            //Get Address Type = Work//
            if(customerInfoView.getCustomerEntity().getId() == 1){
                AddressType addressType = addressTypeDAO.findById(3);
                address.setAddressType(addressType);
            } else {
                AddressType addressType = addressTypeDAO.findById(5);
                address.setAddressType(addressType);
            }

            address.setAddressNo(workAddress.getAddressNo());
            address.setMoo(workAddress.getMoo());
            address.setBuilding(workAddress.getBuilding());
            address.setRoad(workAddress.getRoad());

            if(workAddress.getProvince() != null && workAddress.getProvince().getCode() != 0){
                Province province = provinceDAO.findById(workAddress.getProvince().getCode());
                address.setProvince(province);
            } else {
                address.setProvince(null);
            }

            if(workAddress.getDistrict() != null && workAddress.getDistrict().getId() != 0){
                District district = districtDAO.findById(workAddress.getDistrict().getId());
                address.setDistrict(district);
            } else {
                address.setDistrict(null);
            }

            if(workAddress.getSubDistrict() != null && workAddress.getSubDistrict().getCode() != 0){
                SubDistrict subDistrict = subDistrictDAO.findById(workAddress.getSubDistrict().getCode());
                address.setSubDistrict(subDistrict);
            } else {
                address.setSubDistrict(null);
            }

            if(workAddress.getCountry() != null && workAddress.getCountry().getId() != 0){
                Country country = countryDAO.findById(workAddress.getCountry().getId());
                address.setCountry(country);
            } else {
                address.setCountry(null);
            }

            address.setPostalCode(workAddress.getPostalCode());
            address.setPhoneNumber(workAddress.getPhoneNumber());
            address.setExtension(workAddress.getExtension());
            address.setContactName(workAddress.getContactName());
            address.setContactPhone(workAddress.getContactPhone());
            address.setAddress(workAddress.getAddress());
            address.setAddressTypeFlag(workAddress.getAddressTypeFlag());

            addressList.add(address);
        }
        customer.setAddressesList(addressList);
//        log.info("transformToModel : customer after adding address : {}", customer);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        if(customerInfoView.getCustomerEntity().getId() == 1){
            //Individual
            Individual individual = new Individual();
            if(customer.getIndividual() != null){
                individual = customer.getIndividual();
            }
            individual.setBirthDate(customerInfoView.getDateOfBirth());
            individual.setCitizenId(customerInfoView.getCitizenId());
            individual.setCustomer(customer);
            if(customerInfoView.getMaritalStatus() != null && customerInfoView.getMaritalStatus().getId() != 0){
                individual.setMaritalStatus(maritalStatusDAO.findById(customerInfoView.getMaritalStatus().getId()));
            } else {
                individual.setMaritalStatus(null);
            }
            individual.setGender(customerInfoView.getGender());
            if(customerInfoView.getEducation() != null && customerInfoView.getEducation().getId() != 0){
                individual.setEducation(educationDAO.findById(customerInfoView.getEducation().getId()));
            } else {
                individual.setEducation(null);
            }

            if(customerInfoView.getNationality() != null && customerInfoView.getNationality().getId() != 0){
                individual.setNationality(nationalityDAO.findById(customerInfoView.getNationality().getId()));
            } else {
                individual.setNationality(null);
            }

            if(customerInfoView.getSndNationality() != null && customerInfoView.getSndNationality().getId() != 0){
                individual.setSndNationality(nationalityDAO.findById(customerInfoView.getSndNationality().getId()));
            } else {
                individual.setSndNationality(null);
            }

            if(customerInfoView.getOccupation() != null && customerInfoView.getOccupation().getId() != 0){
                individual.setOccupation(occupationDAO.findById(customerInfoView.getOccupation().getId()));
            } else {
                individual.setOccupation(null);
            }

            if(customerInfoView.getCitizenCountry() != null && customerInfoView.getCitizenCountry().getId() != 0){
                individual.setCitizenCountry(countryDAO.findById(customerInfoView.getCitizenCountry().getId()));
            } else {
                individual.setCitizenCountry(null);
            }

            if(customerInfoView.getOrigin() != null && customerInfoView.getOrigin().getId() != 0){
                individual.setRace(raceDAO.findById(customerInfoView.getOrigin().getId()));
            } else {
                individual.setRace(null);
            }

            individual.setNumberOfChildren(customerInfoView.getNumberOfChild());

            customer.setIndividual(individual);
        } else {
            //Juristic
            Juristic juristic = new Juristic();
            if(customer.getJuristic() != null){
                juristic = customer.getJuristic();
            }
            juristic.setCustomer(customer);
            juristic.setCapital(customerInfoView.getCapital());
            juristic.setPaidCapital(customerInfoView.getPaidCapital());
            juristic.setFinancialYear(customerInfoView.getFinancialYear());
            juristic.setRegisterDate(customerInfoView.getDateOfRegister());
            juristic.setRegistrationId(customerInfoView.getRegistrationId());
            juristic.setSignCondition(customerInfoView.getSignCondition());
            juristic.setTotalShare(customerInfoView.getTotalShare());
            juristic.setDocumentIssueDate(customerInfoView.getDocumentIssueDate());
            juristic.setSalesFromFinancialStmt(customerInfoView.getSalesFromFinancialStmt());
            juristic.setShareHolderRatio(customerInfoView.getShareHolderRatio());
            juristic.setNumberOfAuthorizedUsers(customerInfoView.getNumberOfAuthorizedUsers());
            juristic.setContactName(customerInfoView.getContactName());

            customer.setJuristic(juristic);
        }

        //set for Customer Obligation Info
        if(customerInfoView.getTmbCustomerId() != null && !Util.isEmpty(customerInfoView.getTmbCustomerId())){
            CustomerOblInfo customerOblInfo = null;
            if(customerInfoView.getCustomerOblInfoID() != 0) {
                try{
                    customerOblInfo = customerOblInfoDAO.findById(customerInfoView.getCustomerOblInfoID());
                }catch (Exception ex){
                    log.debug("cannot find customerOblInfo with id {}", customerInfoView.getCustomerOblInfoID());
                }
            }

            log.debug("customerOblInfo :: {}",customerOblInfo);
            if(customerOblInfo == null) {
                customerOblInfo = new CustomerOblInfo();
            }

            customerOblInfo.setServiceSegment(serviceSegmentTransform.transformToModel(customerInfoView.getServiceSegmentView()));
            customerOblInfo.setExistingSMECustomer(customerInfoView.getExistingSMECustomer());
            customerOblInfo.setLastReviewDate(customerInfoView.getLastReviewDate());
            customerOblInfo.setExtendedReviewDate(customerInfoView.getExtendedReviewDate());
            customerOblInfo.setExtendedReviewDateFlag(customerInfoView.getExtendedReviewDateFlag());
            customerOblInfo.setNextReviewDate(customerInfoView.getNextReviewDate());
            customerOblInfo.setNextReviewDateFlag(customerInfoView.getNextReviewDateFlag());
            customerOblInfo.setLastContractDate(customerInfoView.getLastContractDate());
            customerOblInfo.setAdjustClass(customerInfoView.getAdjustClass());
            customerOblInfo.setRatingFinal(sbfScoreTransform.transformToModel(customerInfoView.getRatingFinal()));
            if(customerInfoView.getUnpaidFeeInsurance() != null){
                customerOblInfo.setUnpaidFeeInsurance(customerInfoView.getUnpaidFeeInsurance());
            } else {
                customerOblInfo.setUnpaidFeeInsurance(BigDecimal.ZERO);
            }
            if(customerInfoView.getPendingClaimLG() != null){
                customerOblInfo.setPendingClaimLG(customerInfoView.getPendingClaimLG());
            } else {
                customerOblInfo.setPendingClaimLG(BigDecimal.ZERO);
            }

            customerOblInfo.setCustomer(customer);
            customer.setCustomerOblInfo(customerOblInfo);



        } else {
            customer.setCustomerOblInfo(null);
        }
        log.info("############# - transformToModel ::: customer.getCustomerOblInfo : {}", customer.getCustomerOblInfo());
        log.info("Return - transformToModel ::: customer : {}", customer);
        return customer;
    }

    public List<CustomerOblAccountInfo> getCustomerOblAccountInfo(CustomerInfoView customerInfoView, Customer customer){
        List<CustomerOblAccountInfo> customerOblAccountInfoList = new ArrayList<CustomerOblAccountInfo>();
        if(customerInfoView.getCustomerOblAccountInfoViewList() != null && customerInfoView.getCustomerOblAccountInfoViewList().size() > 0){
            for(CustomerOblAccountInfoView customerOblAccountInfoView : customerInfoView.getCustomerOblAccountInfoViewList()){

                CustomerOblAccountInfo customerOblAccountInfo = null;
                if(customerOblAccountInfoView.getId() != 0) {
                    try{
                        customerOblAccountInfo = customerOblAccountInfoDAO.findById(customerOblAccountInfoView.getId());
                    }catch (Exception ex){
                        log.debug("cannot find customerOblInfo with id {}", customerInfoView.getCustomerOblInfoID());
                        customerOblAccountInfo = new CustomerOblAccountInfo();
                    }
                }

                customerOblAccountInfo.setAccountRef(customerOblAccountInfoView.getAccountRef());
                customerOblAccountInfo.setAccountActiveFlag(customerOblAccountInfoView.isAccountActiveFlag());
                customerOblAccountInfo.setCardBlockCode(customerOblAccountInfoView.getCardBlockCode());
                customerOblAccountInfo.setTmbDelIntDay(customerOblAccountInfoView.getTmbDelIntDay());
                customerOblAccountInfo.setCusRelAccount(customerOblAccountInfoView.getCusRelAccount());
                customerOblAccountInfo.setDataSource(customerOblAccountInfoView.getDataSource());
                customerOblAccountInfo.setNumMonthIntPastDue(customerOblAccountInfoView.getNumMonthIntPastDue());
                customerOblAccountInfo.setNumMonthIntPastDueTDRAcc(customerOblAccountInfoView.getNumMonthIntPastDueTDRAcc());
                customerOblAccountInfo.setTdrFlag(customerOblAccountInfoView.getTdrFlag());
                customerOblAccountInfo.setTmbDelPriDay(customerOblAccountInfoView.getTmbDelPriDay());
                customerOblAccountInfo.setCustomer(customer);
            }
        }
        return customerOblAccountInfoList;
    }

    public List<CustomerInfoView> transformToViewList(List<Customer> customers){
        List<CustomerInfoView> customerInfoViewList = new ArrayList<CustomerInfoView>();

        for(Customer item : customers){
            CustomerInfoView customerInfoView = transformToView(item);
            customerInfoViewList.add(customerInfoView);
        }

        return customerInfoViewList;
    }

    public List<CustomerInfoView> transformToBorrowerViewList(List<CustomerInfoView> customerInfoViews){
        List<CustomerInfoView> customerInfoViewList = new ArrayList<CustomerInfoView>();
        int customerIndex = 0;
        for(CustomerInfoView item : customerInfoViews){
            log.info("transformToBorrowerViewList : CustomerInfoView : {}", item);
            if(item.getRelation() != null && item.getRelation().getId() == 1){
                item.setListIndex(customerIndex);
                item.setSubIndex(customerInfoViewList.size());
                item.setListName("BORROWER");
                if(item.getSpouse() == null){
                    CustomerInfoView spouse = new CustomerInfoView();
                    spouse.reset();
                    spouse.setSpouse(null);
                    item.setSpouse(spouse);
                }
                customerInfoViewList.add(item);
            }
            customerIndex = customerIndex + 1;
        }

        return customerInfoViewList;
    }

    public List<CustomerInfoView> transformToGuarantorViewList(List<CustomerInfoView> customerInfoViews){
        List<CustomerInfoView> customerInfoViewList = new ArrayList<CustomerInfoView>();
        int customerIndex = 0;
        for(CustomerInfoView item : customerInfoViews){
            if(item.getRelation() != null && item.getRelation().getId() == 2){
                item.setListIndex(customerIndex);
                item.setListName("GUARANTOR");
                item.setSubIndex(customerInfoViewList.size());
                if(item.getSpouse() == null){
                    CustomerInfoView spouse = new CustomerInfoView();
                    spouse.reset();
                    item.setSpouse(spouse);
                }
                customerInfoViewList.add(item);
            }
            customerIndex = customerIndex + 1;
        }

        return customerInfoViewList;
    }

    public List<CustomerInfoView> transformToRelatedViewList(List<CustomerInfoView> customerInfoViews){
        List<CustomerInfoView> customerInfoViewList = new ArrayList<CustomerInfoView>();
        int customerIndex = 0;
        for(CustomerInfoView item : customerInfoViews){
            if(item.getRelation() != null && (item.getRelation().getId() == 3 || item.getRelation().getId() == 4)){
                item.setListIndex(customerIndex);
                item.setSubIndex(customerInfoViewList.size());
                item.setListName("RELATED");
                if(item.getSpouse() == null){
                    CustomerInfoView spouse = new CustomerInfoView();
                    spouse.reset();
                    item.setSpouse(spouse);
                }
                customerInfoViewList.add(item);
            }
            customerIndex = customerIndex + 1;
        }

        return customerInfoViewList;
    }

    public HashMap<String, Customer> transformToHashMap(List<CustomerInfoView> customerInfoViews, WorkCasePrescreen workCasePrescreen, WorkCase workCase, User user){
        HashMap<String, Customer> customerHashMap = new HashMap<String, Customer>();
        if(customerInfoViews != null){
            for(CustomerInfoView item : customerInfoViews){
                log.info("transformToModelList before item : {}", item);
                Customer customer = transformToModel(item, workCasePrescreen, workCase, user);
                log.info("transformToModelList after item : {}", customer);

                if(customer.getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()){
                    customerHashMap.put(customer.getIndividual().getCitizenId(), customer);
                } else {
                    customerHashMap.put(customer.getJuristic().getRegistrationId(), customer);
                }

                if(item.getMaritalStatus() != null && item.getMaritalStatus().getId() == 2){
                    if(item.getSpouse() != null){
                        log.debug("transformToModelList before item (spouse) : {}", item.getSpouse());
                        Customer spouse = transformToModel(item.getSpouse(), workCasePrescreen, workCase, user);
                        log.debug("transformToModelList after item (spouse) : {}", spouse);

                        spouse.setIsSpouse(1);
                        customerHashMap.put(spouse.getIndividual().getCitizenId(), spouse);
                    }
                }

            }
        }
        return customerHashMap;
    }

    public List<Customer> transformToModelList(List<CustomerInfoView> customerInfoViews, WorkCasePrescreen workCasePrescreen, WorkCase workCase, User user){
        List<Customer> customerList = new ArrayList<Customer>();

        if(customerInfoViews != null){
            for(CustomerInfoView item : customerInfoViews){
                log.info("transformToModelList before item : {}", item);
                Customer customer = transformToModel(item, workCasePrescreen, workCase, user);
                log.info("transformToModelList after item : {}", customer);
                customerList.add(customer);
                if(item.getMaritalStatus() != null && item.getMaritalStatus().getId() == 2){
                    if(item.getSpouse() != null){
                        log.debug("transformToModelList before item (spouse) : {}", item.getSpouse());
                        Customer spouse = transformToModel(item.getSpouse(), workCasePrescreen, workCase, user);
                        log.debug("transformToModelList after item (spouse) : {}", spouse);
                        spouse.setIsSpouse(1);
                        customerList.add(spouse);
                    }
                }

            }
        }
        return customerList;
    }
    
    public CustomerInfoSimpleView transformToSimpleView(Customer model) {
    	CustomerInfoSimpleView view = new CustomerInfoSimpleView();
    	view.setId(model.getId());
    	view.setCustomerName(model.getDisplayName());
    	if (model.getIndividual() != null)
    		view.setCitizenId(model.getIndividual().getCitizenId());
    	else if (model.getJuristic() != null)
    		view.setCitizenId(model.getJuristic().getRegistrationId());
    	
    	view.setTmbCustomerId(model.getTmbCustomerId());
    	if (model.getRelation() != null)
    		view.setRelation(model.getRelation().getDescription());
    	if (model.getJuristic() != null)
    		view.setJuristic(true);
    	else
    		view.setJuristic(false);
    	return view;
    }
    
    public CustomerInfoPostIndvView transformToIndvPostView(Customer model) {
    	CustomerInfoPostIndvView view = new CustomerInfoPostIndvView();
    	if (model == null || model.getIndividual() == null)
    		return view;
    	_tranformBasePostView(model, view);
    	
    	view.setLastNameTH(model.getLastNameTh());
    	
    	Individual indv = model.getIndividual();
    	view.setIndividualId(indv.getId());
    	view.setBirthDate(indv.getBirthDate());
    	view.setGender(Gender.lookup(indv.getGender()));
    	if (indv.getRace() != null) {
    		view.setRaceId(indv.getRace().getId());
    		view.setDisplayRace(indv.getRace().getName());
    	}
    	
    	if (indv.getNationality() != null) {
    		view.setNationalityId(indv.getNationality().getId());
    		view.setDisplayNationality(indv.getNationality().getName());
    	}
    	if (indv.getMaritalStatus() != null) {
    		view.setMaritalStatusId(indv.getMaritalStatus().getId());
    		view.setDisplayMaritalStatus(indv.getMaritalStatus().getName());
    	}
    	
    	if (model.getSpouseId() > 0) {
    		Customer spouseModel = customerDAO.findById(model.getSpouseId());
    		if (spouseModel != null) {
    			if (spouseModel.getTitle() != null) {
    				view.setSpouseTitleId(spouseModel.getTitle().getId());
    				view.setDisplaySpouseTitle(model.getTitle().getTitleTh());
    			}
    			view.setSpouseNameTH(spouseModel.getNameTh());
    			view.setSpouseLastNameTH(spouseModel.getLastNameTh());
    			view.setHasSpouseData(true);
    		} else {
    			view.setHasSpouseData(false);
    		}
    	}
    	
    	if (indv.getFatherTitle() != null) {
    		view.setFatherTitleId(indv.getFatherTitle().getId());
    		view.setDisplayFatherTitle(indv.getFatherTitle().getTitleTh());
    	}
    	view.setFatherNameTH(indv.getFatherNameTh());
    	view.setFatherLastNameTH(indv.getFatherLastNameTh());
    	if (indv.getMotherTitle() != null) {
    		view.setMotherTitleId(indv.getMotherTitle().getId());
    		view.setDisplayMotherTitle(indv.getMotherTitle().getTitleTh());
    	}
    	view.setMotherNameTH(indv.getMotherNameTh());
    	view.setMotherLastNameTH(indv.getMotherLastNameTh());
    	
    	if (!RadioValue.YES.equals(indv.getAttorneyRequired())) {
    		view.setAttorneyRequired(RadioValue.NO);
    		view.setAttorneyRelationType(AttorneyRelationType.OTHERS);
    	} else {
	    	view.setAttorneyRequired(indv.getAttorneyRequired());
	    	view.setAttorneyRelationType(indv.getAttorneyRelation());
	    	if (indv.getCustomerAttorney() != null)
	    		view.setCustomerAttorneyId(indv.getCustomerAttorney().getId());
    	}
    	return view;
    }
    public CustomerInfoPostJurisView transformToJurisPostView(Customer model) {
    	CustomerInfoPostJurisView view = new CustomerInfoPostJurisView();
    	if (model == null || model.getJuristic() == null)
    		return view;
    	_tranformBasePostView(model, view);
    	
    	Juristic juris = model.getJuristic();
    	view.setJuristicId(juris.getId());
    	view.setRegistrationDate(juris.getRegisterDate());
    	view.setContactPerson(juris.getContactName());
    	
    	return view;
    }
    
    private void _tranformBasePostView(Customer model,CustomerInfoPostBaseView<?> view) {
    	view.setId(model.getId());
    	if (model.getWorkCase() != null)
    		view.setWorkCaseId(model.getWorkCase().getId());
    	view.setModifyDate(model.getModifyDate());
    	if (model.getModifyBy() != null)
    		view.setModifyUser(model.getModifyBy().getDisplayName());
    	
    	view.setTmbCustomerId(model.getTmbCustomerId());
    	if (model.getRelation() != null) {
    		view.setRelationId(model.getRelation().getId());
    		view.setDisplayRelation(model.getRelation().getDescription());
    	}
    	view.setCollateralOwner(RadioValue.lookup(model.getCollateralOwner()));
    	if (model.getDocumentType() != null)
    		view.setDisplayDocumentType(model.getDocumentType().getDescription());
    	
    	if (model.getIndividual() != null) {
    		view.setPersonalId(model.getIndividual().getCitizenId());
    	} else if (model.getJuristic() != null){
    		view.setPersonalId(model.getJuristic().getRegistrationId());
    	}
    	view.setAge(model.getAge());
    	if (model.getTitle() != null) {
    		view.setTitleId(model.getTitle().getId());
    		view.setDisplayTitle(model.getTitle().getTitleTh());
    	}
    	view.setNameTH(model.getNameTh());
    	view.setMobile(model.getMobileNumber());
    	view.setFax(model.getFaxNumber());
    	view.setEmail(model.getEmail());
    	if (model.getMailingAddressType() != null) {
    		view.setMailingAddressTypeId(model.getMailingAddressType().getId());
    		view.setDisplayMailingAddressType(model.getMailingAddressType().getName());
    	}
    	if (model.getBusinessType() !=null) {
    		view.setBusinessTypeId(model.getBusinessType().getId());
    		view.setDisplayBusinessType(model.getBusinessType().getName());
    	}
    	//address type
    	List<Address> addresses = model.getAddressesList();
    	HashMap<Integer, Address> addressMap = new HashMap<Integer, Address>();
    	for (Address address : addresses) {
    		addressMap.put(address.getAddressType().getId(), address);
    	}
    	
    	List<AddressType> addressTypes = addressTypeDAO.findByCustomerEntityId(view.getDefaultCustomerEntityId());
    	int index=0;
    	for (AddressType addressType : addressTypes) {
    		CustomerInfoPostAddressView addressView = new CustomerInfoPostAddressView();
			addressView.setAddressType(addressType.getId());
			addressView.setDisplayAddressType(addressType.getName());

			Address address = addressMap.get(addressType.getId());
			addressView.setIndex(index);
			
    		if (address != null) {
    			int flag = address.getAddressTypeFlag();
    			addressView.setId(address.getId());
    			
    			if (index != 0 && flag != 3 && flag < index
    					) { //dup data from address flag
    				CustomerInfoPostAddressView toClone = view.getAddresses().get(flag);
    				addressView.duplicateData(toClone);
    				addressView.setAddressFlag(flag);
    			} else {
	    			addressView.setAddressNo(address.getAddressNo());
	    			addressView.setMoo(address.getMoo());
	    			addressView.setBuilding(address.getBuilding());
	    			addressView.setRoad(address.getRoad());
	    			if (address.getProvince() != null) {
	    				addressView.setProvinceId(address.getProvince().getCode());
	    				addressView.setDisplayProvince(address.getProvince().getName());
	    			}
	    			if (address.getDistrict() != null) {
	    				addressView.setDistrictId(address.getDistrict().getId());
	    				addressView.setDisplayDistrict(address.getDistrict().getName());
	    			}
	    			if (address.getSubDistrict() != null) {
	    				addressView.setSubDistrictId(address.getSubDistrict().getCode());
	    				addressView.setDisplaySubDistrict(address.getSubDistrict().getName());
	    			}
	    			addressView.setPostalCode(address.getPostalCode());
	    			if (address.getCountry() != null) {
	    				addressView.setCountryId(address.getCountry().getId());
	    				addressView.setDisplayCountry(address.getCountry().getName());
	    			}
	    			addressView.setPhoneNumber(address.getPhoneNumber());
	    			addressView.setPhoneExt(address.getExtension());
	    			addressView.setAddressFlag(3);
    			}
    		} else {
    			addressView.setAddressFlag(0);
    			if (index != 0) {
    				CustomerInfoPostAddressView toClone = view.getAddresses().get(0);
    				addressView.duplicateData(toClone);
    			}
    		}
    		view.addAddress(addressView);
    		index++;
    	}
    }
    
    public void updateModelFromPostView(Customer model,CustomerInfoPostIndvView view,User user) {
    	_updateModelFromBasePostView(model, view,user);
    	//Read only list (No need to update)
    	//last name
    	
    	Individual indv = model.getIndividual();
    	indv.setBirthDate(view.getBirthDate());
    	indv.setGender(view.getGender().value());
    	indv.setRace(raceDAO.findRefById(view.getRaceId()));
    	indv.setNationality(nationalityDAO.findRefById(view.getNationalityId()));
    	
    	if (view.getFatherTitleId() > 0)
    		indv.setFatherTitle(titleDAO.findRefById(view.getFatherTitleId()));
    	else
    		indv.setFatherTitle(null);
    	indv.setFatherNameTh(view.getFatherNameTH());
    	indv.setFatherLastNameTh(view.getFatherLastNameTH());
    	
    	if (view.getMotherTitleId() > 0)
    		indv.setMotherTitle(titleDAO.findRefById(view.getMotherTitleId()));
    	else
    		indv.setMotherTitle(null);
    	indv.setMotherNameTh(view.getMotherNameTH());
    	indv.setMotherLastNameTh(view.getMotherLastNameTH());
    	
    	indv.setAttorneyRelation(view.getAttorneyRelationType());
    	indv.setAttorneyRequired(view.getAttorneyRequired());
    	
    	//For spouse this should be updated in controller
    }
    public void updateModelFromPostView(Customer model,CustomerInfoPostJurisView view,User user) {
    	_updateModelFromBasePostView(model, view,user);
    	//Read only list (No need to update)
    	//regist date, contact person
    }
    private void _updateModelFromBasePostView(Customer model,CustomerInfoPostBaseView<?> view,User user) {
    	//Read only list (No need to update)
    	// Relation, Collateral Owner, Document Type, Personal Id,
    	// NameTH , Mobile, Fax, Email, tmb customerId
    	
    	model.setModifyBy(user);
    	model.setModifyDate(new Date());
    	view.calculateAge();
    	model.setAge(view.getAge());
    	model.setTitle(titleDAO.findRefById(view.getTitleId()));
    	model.setMailingAddressType(addressTypeDAO.findRefById(view.getMailingAddressTypeId()));
    	model.setBusinessType(businessTypeDAO.findRefById(view.getBusinessTypeId()));
    	
    	//Update address
    	List<Address> addresses = model.getAddressesList();
    	HashMap<Long, Address> addressMap = new HashMap<Long, Address>();
    	for (Address address : addresses) {
    		addressMap.put(address.getId(), address);
    	}
    	for(CustomerInfoPostAddressView addressView : view.getAddresses()) {
    		Address address = addressMap.get(addressView.getId());
    		if (address == null) {
    			address = new Address();
    			address.setCustomer(model);
    			model.getAddressesList().add(address);
    		}
    		int flag = addressView.getAddressFlag();
    		if (addressView.getIndex() != 0) {
    			if (flag != 3 && flag >= addressView.getIndex())
    				flag = 0;
    			//save with data from flag
    			if (flag != 3)
    				addressView.duplicateData(view.getAddresses().get(flag));
    		}
    		address.setAddressTypeFlag(flag);
    		address.setAddressNo(addressView.getAddressNo());
    		address.setMoo(addressView.getMoo());
    		address.setBuilding(addressView.getBuilding());
    		address.setRoad(addressView.getRoad());
    		if (addressView.getProvinceId() > 0)
    			address.setProvince(provinceDAO.findRefById(addressView.getProvinceId()));
    		else
    			address.setProvince(null);
    		if (addressView.getDistrictId() > 0)
    			address.setDistrict(districtDAO.findRefById(addressView.getDistrictId()));
    		else
    			address.setDistrict(null);
    		if (addressView.getSubDistrictId() > 0)
    			address.setSubDistrict(subDistrictDAO.findRefById(addressView.getSubDistrictId()));
    		else
    			address.setSubDistrict(null);
    		address.setPostalCode(addressView.getPostalCode());
    		if (addressView.getCountryId() > 0)
    			address.setCountry(countryDAO.findRefById(addressView.getCountryId()));
    		else
    			address.setCountry(null);
    		address.setPhoneNumber(addressView.getPhoneNumber());
    		address.setExtension(addressView.getPhoneExt());
    	}
    }

    public List<CustomerInfoView> transformToSelectList(List<Customer> customerList){
        List<CustomerInfoView> customerInfoViewList = new ArrayList<CustomerInfoView>();
        for(Customer item : customerList){
            CustomerInfoView customerInfoView = new CustomerInfoView();
            customerInfoView.setId(item.getId());
            customerInfoView.setFirstNameTh(item.getNameTh());
            if(!Util.isNull(item.getLastNameTh())){
                customerInfoView.setLastNameTh(item.getLastNameTh());
            }

            customerInfoViewList.add(customerInfoView);
        }

        return customerInfoViewList;
    }
}
