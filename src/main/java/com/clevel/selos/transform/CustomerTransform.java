package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.CustomerInfoView;

import java.util.ArrayList;
import java.util.List;

public class CustomerTransform extends Transform {

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
            if(item.getCustomerEntity().getId() == 1){
                //Individual
                customerInfoView.setCitizenId(item.getIndividual().getCitizenId());
            } else {
                //Juristic
                customerInfoView.setRegistrationId(item.getJuristic().getRegistrationId());
            }
            customerInfoViewList.add(customerInfoView);
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
                //customer.setCustomerEntity(item.getCustomerEntity());
                //customer.setDocumentType(item.getDocumentType());
                customer.setIdNumber(item.getCustomerId());
                //customer.setExpireDate(item.getExpireDate());
                //customer.setTitle(item.getTitleTh());
                customer.setNameTh(item.getFirstNameTh());
                customer.setLastNameTh(item.getLastNameTh());
                customer.setAge(item.getAge());
                //customer.setRelation(item.getRelation());

                if(item.getCustomerEntity().getId() == 1){
                    //Individual
                    Individual individual = new Individual();
                    /*individual.setBirthDate(item.getDateOfBirth());
                    individual.setCitizenId(item.getCitizenId());
                    //individual.setCustomer(customer);
                    individual.setMaritalStatus(item.getMaritalStatus());
                    individual.setGender(item.getGender());
                    individual.setEducation(item.getEducation());
                    individual.setNationality(item.getNationality());
                    individual.setOccupation(item.getOccupation());*/
                    //customer.setIndividual(individual);
                    customer.setIndividual(null);
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
