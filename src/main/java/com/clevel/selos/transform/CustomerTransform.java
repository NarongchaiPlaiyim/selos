package com.clevel.selos.transform;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.model.db.master.Education;
import com.clevel.selos.model.db.master.MaritalStatus;
import com.clevel.selos.model.db.master.Nationality;
import com.clevel.selos.model.db.master.Occupation;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.CustomerInfoView;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class CustomerTransform extends Transform {
    @Inject
    Logger log;

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

    public CustomerInfoView transformToView(Customer customer){
        CustomerInfoView customerInfoView = new CustomerInfoView();

        return customerInfoView;
    }

    public Customer transformToModel(CustomerInfoView customerInfoView, WorkCase workCase){
        Customer customer = new Customer();

        return customer;
    }

    public Customer transformToModel(CustomerInfoView customerInfoView, WorkCasePrescreen workCasePrescreen){
        Customer customer = new Customer();

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
            customerInfoView.setValidId(2);
            if(item.getCustomerEntity().getId() == 1){
                //Individual
                Individual individual = item.getIndividual();
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
                Customer customer = new Customer();
                /*if(item.getId() != 0){
                    customer.setId(item.getId());
                }*/
                customer.setWorkCase(workCase);
                customer.setWorkCasePrescreen(workCasePrescreen);

                if(item.getCustomerEntity() != null && item.getCustomerEntity().getId() != 0){
                    customer.setCustomerEntity(customerEntityDAO.findById(item.getCustomerEntity().getId()));
                } else {
                    customer.setCustomerEntity(null);
                }

                if(item.getDocumentType() != null && item.getDocumentType().getId() != 0){
                    customer.setDocumentType(documentTypeDAO.findById(item.getDocumentType().getId()));
                } else {
                    customer.setDocumentType(null);
                }

                customer.setIdNumber(item.getCustomerId());
                //customer.setExpireDate(item.getExpireDate());
                if(item.getTitleTh() != null && item.getTitleTh().getId() != 0){
                    customer.setTitle(titleDAO.findById(item.getTitleTh().getId()));
                } else {
                    customer.setTitle(null);
                }

                customer.setNameTh(item.getFirstNameTh());
                customer.setLastNameTh(item.getLastNameTh());
                customer.setAge(item.getAge());

                if(item.getRelation() != null && item.getRelation().getId() != 0){
                    customer.setRelation(relationDAO.findById(item.getRelation().getId()));
                }


                if(item.getCustomerEntity().getId() == 1){
                    //Individual
                    Individual individual = new Individual();
                    individual.setBirthDate(item.getDateOfBirth());
                    individual.setCitizenId(item.getCitizenId());
                    individual.setCustomer(customer);
                    if(item.getMaritalStatus() != null && item.getMaritalStatus().getId() != 0){
                        individual.setMaritalStatus(maritalStatusDAO.findById(item.getMaritalStatus().getId()));
                    } else {
                        individual.setMaritalStatus(null);
                    }
                    individual.setGender(item.getGender());
                    if(item.getEducation() != null && item.getEducation().getId() != 0){
                        individual.setEducation(educationDAO.findById(item.getEducation().getId()));
                    } else {
                        individual.setEducation(null);
                    }

                    if(item.getNationality() != null && item.getNationality().getId() != 0){
                        individual.setNationality(nationalityDAO.findById(item.getNationality().getId()));
                    } else {
                        individual.setNationality(null);
                    }

                    if(item.getOccupation() != null && item.getOccupation().getId() != 0){
                        individual.setOccupation(occupationDAO.findById(item.getOccupation().getId()));
                    } else {
                        individual.setOccupation(null);
                    }
                    customer.setIndividual(individual);
                    //customer.setIndividual(null);
                } else {
                    //Juristic
                    Juristic juristic = new Juristic();
                    //juristic.setCustomer(customer);
                    customer.setJuristic(juristic);
                }
                customerList.add(customer);
            }
        }
        return customerList;
    }
}
