package com.clevel.selos.transform;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.working.AddressDAO;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.BorrowerType;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.AddressView;
import com.clevel.selos.model.view.CustomerCSIView;
import com.clevel.selos.model.view.CustomerInfoView;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomerTransform extends Transform {
    @Inject
    @SELOS
    Logger log;

    @Inject
    CustomerCSITransform customerCSITransform;

    @Inject
    CustomerDAO customerDAO;
    @Inject
    CustomerEntityDAO customerEntityDAO;
    @Inject
    DocumentTypeDAO documentTypeDAO;
    @Inject
    TitleDAO titleDAO;
    @Inject
    RelationDAO relationDAO;
    @Inject
    ReferenceDAO referenceDAO;
    @Inject
    MaritalStatusDAO maritalStatusDAO;
    @Inject
    EducationDAO educationDAO;
    @Inject
    NationalityDAO nationalityDAO;
    @Inject
    OccupationDAO occupationDAO;
    @Inject
    AddressTypeDAO addressTypeDAO;
    @Inject
    ProvinceDAO provinceDAO;
    @Inject
    DistrictDAO districtDAO;
    @Inject
    SubDistrictDAO subDistrictDAO;
    @Inject
    CountryDAO countryDAO;
    @Inject
    AddressDAO addressDAO;
    @Inject
    BusinessTypeDAO businessTypeDAO;
    @Inject
    WarningCodeDAO warningCodeDAO;
    @Inject
    KYCLevelDAO kycLevelDAO;
    @Inject
    RaceDAO raceDAO;

    public CustomerInfoView transformToView(Customer customer){

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
        customerInfoView.setServiceSegment(customer.getServiceSegment());
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
        customerInfoView.setConvenantFlag(customer.getConvenantFlag());
        customerInfoView.setReviewFlag(customer.getReviewFlag());
        customerInfoView.setReason(customer.getReason());

        customerInfoView.setKycLevel(customer.getKycLevel());
        if(customerInfoView.getKycLevel() == null){
            customerInfoView.setKycLevel(new KYCLevel());
        }
        customerInfoView.setNcbFlag(customer.getNcbFlag());
        customerInfoView.setCsiFlag(customer.getCsiFlag());
        customerInfoView.setServiceSegment(customer.getServiceSegment());
        customerInfoView.setSearchFromRM(customer.getSearchFromRM());

        customerInfoView.setMailingAddressType(customer.getMailingAddressType());
        if(customerInfoView.getMailingAddressType() == null){
            customerInfoView.setMailingAddressType(new AddressType());

		customerInfoView.setMailingAddressType(customer.getMailingAddressType());
        if(customerInfoView.getMailingAddressType() == null){
            customerInfoView.setMailingAddressType(new AddressType());
        }

        customerInfoView.setSourceIncome(customer.getSourceIncome());
        if(customerInfoView.getSourceIncome() == null){
            customerInfoView.setSourceIncome(new Country());
        }

        customerInfoView.setCountryIncome(customer.getCountryIncome());
        if(customerInfoView.getCountryIncome() == null){
            customerInfoView.setCountryIncome(new Country());
        }

        customerInfoView.setIsCommittee(customer.getIsCommittee());
        customerInfoView.setCommitteeId(customer.getJuristicId());
        }
        customerInfoView.setValidId(2);

        customerInfoView.setSourceIncome(customer.getSourceIncome());
        if(customerInfoView.getSourceIncome() == null){
            customerInfoView.setSourceIncome(new Country());
        }

        customerInfoView.setCountryIncome(customer.getCountryIncome());
        if(customerInfoView.getCountryIncome() == null){
            customerInfoView.setCountryIncome(new Country());
        }

        customerInfoView.setIsCommittee(customer.getIsCommittee());
        customerInfoView.setCommitteeId(customer.getJuristicId());

        customerInfoView.setCollateralOwner(customer.getCollateralOwner());
        customerInfoView.setPercentShare(customer.getPercentShare());

        customerInfoView.setSearchBy(customer.getSearchBy());
        customerInfoView.setSearchId(customer.getSearchId());

        customerInfoView.setPercentShare(customer.getPercentShare());

		customerInfoView.setCsiFlag(customer.getCsiFlag());

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

                if(customer.getCustomerEntity().getId() == 1){
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
                    }
                } else {
                    if(address.getAddressType().getId() == 4){
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

        //for new field
        customerInfoView.setAgeMonths(customer.getAgeMonths());
        customerInfoView.setIsExistingSMECustomer(customer.getIsExistingSMECustomer());
        customerInfoView.setLastReviewDate(customer.getLastReviewDate());
        customerInfoView.setExtendedReviewDate(customer.getExtendedReviewDate());
        customerInfoView.setExtendedReviewDateFlag(customer.getExtendedReviewDateFlag());
        customerInfoView.setNextReviewDate(customer.getNextReviewDate());
        customerInfoView.setNextReviewDateFlag(customer.getNextReviewDateFlag());
        customerInfoView.setLastContractDate(customer.getLastContractDate());
        customerInfoView.setNumberOfMonthsLastContractDate(customer.getNumberOfMonthsLastContractDate());
        customerInfoView.setAdjustClass(customer.getAdjustClass());
        customerInfoView.setRatingFinal(customer.getRatingFinal());
        customerInfoView.setUnpaidFeeInsurance(customer.getUnpaidFeeInsurance());
        customerInfoView.setNoPendingClaimLG(customer.getNoPendingClaimLG());

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

        log.info("Return Customer {}", customerInfoView);
        return customerInfoView;
    }

    public Customer transformToModel(CustomerInfoView customerInfoView, WorkCasePrescreen workCasePrescreen, WorkCase workCase){
        log.info("transformToModel ::: customerInfoView : {}", customerInfoView);
        Customer customer = new Customer();
        if(customerInfoView.getId() != 0){
            customer = customerDAO.findById(customerInfoView.getId());
        }
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
        customer.setServiceSegment(customerInfoView.getServiceSegment());
        customer.setCollateralOwner(customerInfoView.getCollateralOwner());
        customer.setPercentShare(customerInfoView.getPercentShare());
        customer.setApproxIncome(customerInfoView.getApproxIncome());
        customer.setTmbCustomerId(customerInfoView.getTmbCustomerId());
        customer.setDocumentExpiredDate(customerInfoView.getDocumentExpiredDate());

        if(customerInfoView.getTitleTh() != null && customerInfoView.getTitleTh().getId() != 0){
            customer.setTitle(titleDAO.findById(customerInfoView.getTitleTh().getId()));
        } else {
            customer.setTitle(null);
        }
        customer.setNameTh(customerInfoView.getFirstNameTh());
        customer.setLastNameTh(customerInfoView.getLastNameTh());

//        if(customerInfoView.getTitleEn() != null && customerInfoView.getTitleEn().getId() != 0){
//            customer.setTitleEn(titleDAO.findById(customerInfoView.getTitleEn().getId()));
//        } else {
//            customer.setTitleEn(null);
//        }
        customer.setNameEn(customerInfoView.getFirstNameEn());
        customer.setLastNameEn(customerInfoView.getLastNameEn());

        customer.setAge(customerInfoView.getAge());
        customer.setNcbFlag(customerInfoView.getNcbFlag());
        customer.setSearchFromRM(customerInfoView.getSearchFromRM());
        customer.setCsiFlag(customerInfoView.getCsiFlag());
        customer.setServiceSegment(customerInfoView.getServiceSegment());
        customer.setCollateralOwner(customerInfoView.getCollateralOwner());
        customer.setPercentShare(customerInfoView.getPercentShare());

        if(customerInfoView.getBusinessType() != null && customerInfoView.getBusinessType().getId() != 0){
            customer.setBusinessType(businessTypeDAO.findById(customerInfoView.getBusinessType().getId()));
        } else {
            customer.setBusinessType(null);
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
        customer.setConvenantFlag(customerInfoView.getConvenantFlag());
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
            customer.setSourceIncome(countryDAO.findById(customerInfoView.getSourceIncome().getId()));
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

        //for new field
        customer.setAgeMonths(customerInfoView.getAgeMonths());
        customer.setIsExistingSMECustomer(customerInfoView.getIsExistingSMECustomer());
        customer.setLastReviewDate(customerInfoView.getLastReviewDate());
        customer.setExtendedReviewDate(customerInfoView.getExtendedReviewDate());
        customer.setExtendedReviewDateFlag(customerInfoView.getExtendedReviewDateFlag());
        customer.setNextReviewDate(customerInfoView.getNextReviewDate());
        customer.setNextReviewDateFlag(customerInfoView.getNextReviewDateFlag());
        customer.setLastContractDate(customerInfoView.getLastContractDate());
        customer.setNumberOfMonthsLastContractDate(customerInfoView.getNumberOfMonthsLastContractDate());
        customer.setAdjustClass(customerInfoView.getAdjustClass());
        customer.setRatingFinal(customerInfoView.getRatingFinal());
        customer.setUnpaidFeeInsurance(customerInfoView.getUnpaidFeeInsurance());
        customer.setNoPendingClaimLG(customerInfoView.getNoPendingClaimLG());

        return customer;
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

    public HashMap<String, Customer> transformToHashMap(List<CustomerInfoView> customerInfoViews, WorkCasePrescreen workCasePrescreen, WorkCase workCase){
        HashMap<String, Customer> customerHashMap = new HashMap<String, Customer>();
        if(customerInfoViews != null){
            for(CustomerInfoView item : customerInfoViews){
                log.info("transformToModelList before item : {}", item);
                Customer customer = transformToModel(item, workCasePrescreen, workCase);
                log.info("transformToModelList after item : {}", customer);

                if(customer.getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()){
                    customerHashMap.put(customer.getIndividual().getCitizenId(), customer);
                } else {
                    customerHashMap.put(customer.getJuristic().getRegistrationId(), customer);
                }

                if(item.getMaritalStatus() != null && item.getMaritalStatus().getId() == 2){
                    if(item.getSpouse() != null){
                        log.debug("transformToModelList before item (spouse) : {}", item.getSpouse());
                        Customer spouse = transformToModel(item.getSpouse(), workCasePrescreen, workCase);
                        log.debug("transformToModelList after item (spouse) : {}", spouse);

                        spouse.setIsSpouse(1);
                        customerHashMap.put(spouse.getIndividual().getCitizenId(), spouse);
                    }
                }

            }
        }
        return customerHashMap;
    }

    public List<Customer> transformToModelList(List<CustomerInfoView> customerInfoViews, WorkCasePrescreen workCasePrescreen, WorkCase workCase){
        List<Customer> customerList = new ArrayList<Customer>();

        if(customerInfoViews != null){
            for(CustomerInfoView item : customerInfoViews){
                log.info("transformToModelList before item : {}", item);
                Customer customer = transformToModel(item, workCasePrescreen, workCase);
                log.info("transformToModelList after item : {}", customer);
                customerList.add(customer);
                if(item.getMaritalStatus() != null && item.getMaritalStatus().getId() == 2){
                    if(item.getSpouse() != null){
                        log.debug("transformToModelList before item (spouse) : {}", item.getSpouse());
                        Customer spouse = transformToModel(item.getSpouse(), workCasePrescreen, workCase);
                        log.debug("transformToModelList after item (spouse) : {}", spouse);
                        spouse.setIsSpouse(1);
                        customerList.add(spouse);
                    }
                }

            }
        }
        return customerList;
    }
}
