package com.clevel.selos.transform;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.AddressView;
import com.clevel.selos.model.view.CustomerInfoView;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class CustomerTransform extends Transform {
    @Inject
    Logger log;

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

    public CustomerInfoView transformToView(Customer customer){
        CustomerInfoView customerInfoView = new CustomerInfoView();

        return customerInfoView;
    }

    public Customer transformToModel(CustomerInfoView customerInfoView, WorkCase workCase){
        Customer customer = new Customer();

        return customer;
    }

    public Customer transformToModel(CustomerInfoView customerInfoView, WorkCasePrescreen workCasePrescreen, WorkCase workCase){
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

        customer.setIdNumber(customerInfoView.getCustomerId());
        //customer.setExpireDate(item.getExpireDate());
        if(customerInfoView.getTitleTh() != null && customerInfoView.getTitleTh().getId() != 0){
            customer.setTitle(titleDAO.findById(customerInfoView.getTitleTh().getId()));
        } else {
            customer.setTitle(null);
        }

        customer.setNameTh(customerInfoView.getFirstNameTh());
        customer.setLastNameTh(customerInfoView.getLastNameTh());
        customer.setAge(customerInfoView.getAge());
        customer.setNcbFlag(customerInfoView.isNcbFlag());

        if(customerInfoView.getRelation() != null && customerInfoView.getRelation().getId() != 0){
            customer.setRelation(relationDAO.findById(customerInfoView.getRelation().getId()));
        }

        log.info("transformToModel : customer before adding address : {}", customer);

        List<Address> addressList = new ArrayList<Address>();

        if(customerInfoView.getCurrentAddress() != null){
            Address address = new Address();
            AddressView currentAddress = customerInfoView.getCurrentAddress();
            if(currentAddress.getId() != 0){
                for(Address addressDb : customer.getAddressesList()){
                    if(addressDb.getAddressType().getId() == 1){
                        address = addressDb;
                    }
                }
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

            address.setPostalCode(currentAddress.getPostalCode());
            address.setCountry(currentAddress.getCountry());
            address.setPhoneNumber(currentAddress.getPhoneNumber());
            address.setExtension(currentAddress.getExtension());
            address.setContactName(currentAddress.getContactName());
            address.setContactPhone(currentAddress.getContactPhone());

            addressList.add(address);
        }

        if(customerInfoView.getRegisterAddress() != null){
            Address address = new Address();
            AddressView registerAddress = customerInfoView.getRegisterAddress();
            if(registerAddress.getId() != 0){
                for(Address addressDb : customer.getAddressesList()){
                    if(addressDb.getAddressType().getId() == 2){
                        address = addressDb;
                    }
                }
            }
            address.setCustomer(customer);

            //Get Address Type = Current//
            AddressType addressType = addressTypeDAO.findById(2);
            address.setAddressType(addressType);

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

            address.setPostalCode(registerAddress.getPostalCode());
            address.setCountry(registerAddress.getCountry());
            address.setPhoneNumber(registerAddress.getPhoneNumber());
            address.setExtension(registerAddress.getExtension());
            address.setContactName(registerAddress.getContactName());
            address.setContactPhone(registerAddress.getContactPhone());

            addressList.add(address);
        }

        if(customerInfoView.getWorkAddress() != null){
            Address address = new Address();

            AddressView workAddress = customerInfoView.getWorkAddress();
            /*if(workAddress.getId() != 0){
                address.setId(workAddress.getId());
            }*/
            address.setCustomer(customer);

            //Get Address Type = Current//
            AddressType addressType = addressTypeDAO.findById(3);
            address.setAddressType(addressType);

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

            address.setPostalCode(workAddress.getPostalCode());
            address.setCountry(workAddress.getCountry());
            address.setPhoneNumber(workAddress.getPhoneNumber());
            address.setExtension(workAddress.getExtension());
            address.setContactName(workAddress.getContactName());
            address.setContactPhone(workAddress.getContactPhone());

            addressList.add(address);
        }

        log.info("transformToModel : customer after adding address : {}", customer);

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

            if(customerInfoView.getOccupation() != null && customerInfoView.getOccupation().getId() != 0){
                individual.setOccupation(occupationDAO.findById(customerInfoView.getOccupation().getId()));
            } else {
                individual.setOccupation(null);
            }
            customer.setIndividual(individual);
            //customer.setIndividual(null);
        } else {
            //Juristic
            Juristic juristic = new Juristic();
            if(customer.getJuristic() != null){
                juristic = customer.getJuristic();
            }
            juristic.setRegistrationId(customerInfoView.getRegistrationId());

            customer.setJuristic(juristic);
        }

        return customer;
    }

    private Individual transformToIndividual(CustomerInfoView customerInfoView){
        Individual individual = new Individual();

        return individual;
    }

    public List<CustomerInfoView> transformToViewList(List<Customer> customers){
        List<CustomerInfoView> customerInfoViewList = new ArrayList<CustomerInfoView>();

        for(Customer item : customers){
            CustomerInfoView customerInfoView = new CustomerInfoView();
            customerInfoView.setId(item.getId());
            customerInfoView.setTitleTh(item.getTitle());
            customerInfoView.setFirstNameTh(item.getNameTh());
            customerInfoView.setLastNameTh(item.getLastNameTh());
            customerInfoView.setCustomerEntity(item.getCustomerEntity());
            customerInfoView.setAge(item.getAge());
            customerInfoView.setCustomerId(item.getIdNumber());
            customerInfoView.setRelation(item.getRelation());
            customerInfoView.setDocumentType(item.getDocumentType());
            customerInfoView.setNcbFlag(item.isNcbFlag());
            customerInfoView.setValidId(2);

            if(item.getAddressesList() != null){
                List<Address> addressList = item.getAddressesList();
                for(Address address : addressList){
                    AddressView addressView = new AddressView();
                    addressView.setAddressType(address.getAddressType());
                    addressView.setAddressNo(address.getAddressNo());
                    addressView.setMoo(address.getMoo());
                    addressView.setBuilding(address.getBuilding());
                    addressView.setRoad(address.getRoad());
                    addressView.setProvince(address.getProvince());
                    addressView.setDistrict(address.getDistrict());
                    addressView.setSubDistrict(address.getSubDistrict());
                    addressView.setPostalCode(address.getPostalCode());
                    addressView.setCountry(address.getCountry());
                    addressView.setPhoneNumber(address.getPhoneNumber());
                    addressView.setExtension(address.getExtension());
                    addressView.setContactName(address.getContactName());
                    addressView.setContactPhone(address.getContactPhone());

                    if(address.getAddressType().getId() == 1){
                        // Current address
                        customerInfoView.setCurrentAddress(addressView);
                    } else if(address.getAddressType().getId() == 2){
                        // Register Address
                        customerInfoView.setRegisterAddress(addressView);
                    } else if(address.getAddressType().getId() == 3){
                        // Work Address
                        customerInfoView.setWorkAddress(addressView);
                    }
                }
            } else {
                customerInfoView.setCurrentAddress(new AddressView());
                customerInfoView.setRegisterAddress(new AddressView());
                customerInfoView.setWorkAddress(new AddressView());
            }

            if(item.getCustomerEntity().getId() == 1){
                //Individual
                Individual individual = item.getIndividual();
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

                if(individual.getOccupation() != null){
                    customerInfoView.setOccupation(individual.getOccupation());
                } else {
                    customerInfoView.setOccupation(new Occupation());
                }
            } else {
                //Juristic
                customerInfoView.setRegistrationId(item.getJuristic().getRegistrationId());
            }
            customerInfoViewList.add(customerInfoView);
        }

        return customerInfoViewList;
    }

    public List<CustomerInfoView> transformToBorrowerViewList(List<CustomerInfoView> customerInfoViews){
        List<CustomerInfoView> customerInfoViewList = new ArrayList<CustomerInfoView>();

        for(CustomerInfoView item : customerInfoViews){
            log.info("transformToBorrowerViewList : CustomerInfoView : {}", item);
            if(item.getRelation() != null && item.getRelation().getId() == 1){
                customerInfoViewList.add(item);
            }
        }

        return customerInfoViewList;
    }

    public List<CustomerInfoView> transformToGuarantorViewList(List<CustomerInfoView> customerInfoViews){
        List<CustomerInfoView> customerInfoViewList = new ArrayList<CustomerInfoView>();

        for(CustomerInfoView item : customerInfoViews){
            if(item.getRelation() != null && item.getRelation().getId() == 2){
                customerInfoViewList.add(item);
            }
        }

        return customerInfoViewList;
    }

    public List<CustomerInfoView> transformToRelatedViewList(List<CustomerInfoView> customerInfoViews){
        List<CustomerInfoView> customerInfoViewList = new ArrayList<CustomerInfoView>();

        for(CustomerInfoView item : customerInfoViews){
            if(item.getRelation() != null && (item.getRelation().getId() == 3 || item.getRelation().getId() == 4)){
                customerInfoViewList.add(item);
            }
        }

        return customerInfoViewList;
    }

    public List<Customer> transformToModelList(List<CustomerInfoView> customerInfoViews, WorkCasePrescreen workCasePrescreen, WorkCase workCase){
        List<Customer> customerList = new ArrayList<Customer>();

        if(customerInfoViews != null){
            for(CustomerInfoView item : customerInfoViews){
                log.info("transformToModelList before item : {}", item);
                Customer customer = transformToModel(item, workCasePrescreen, workCase);
                log.info("transformToModelList after item : {}", customer);
                customerList.add(customer);
            }
        }
        return customerList;
    }
}