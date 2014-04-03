package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.RelationValue;
import com.clevel.selos.model.db.working.Customer;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.annotations.Fetch;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

import java.util.List;

public class CustomerDAO extends GenericDAO<Customer, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public CustomerDAO() {
    }

    public List<Customer> findBorrowerByWorkCasePreScreenId(long workCasePreScreenId) {
        log.info("findBorrowerByWorkCasePreScreenId ::: workCasePreScreenId : {}", workCasePreScreenId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCasePrescreen.id", workCasePreScreenId));
        criteria.add(Restrictions.eq("relation.id", RelationValue.BORROWER.value()));
        criteria.addOrder(Order.asc("id"));
        List<Customer> customerList = (List<Customer>) criteria.list();
        log.info("findBorrowerByWorkCasePreScreenId ::: size : {}", customerList.size());
        return customerList;
    }

    public List<Customer> findBorrowerByWorkCaseId(long workCaseId){
        log.info("findBorrowerByWorkCaseId ::: workCaseId : {}", workCaseId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.eq("relation.id", RelationValue.BORROWER.value()));
        criteria.addOrder(Order.asc("id"));
        List<Customer> customerList = (List<Customer>) criteria.list();
        log.info("findBorrowerByWorkCaseId ::: size : {}", customerList.size());
        return customerList;
    }

    //Function for AppHeader
    /*public List<Customer> getBorrowerByWorkCaseId(long workCaseId) {
        log.info("getBorrowerByWorkCaseId ::: workCaseId : {}", workCaseId);
        String query = "SELECT customer FROM Customer customer WHERE customer.workCase.id = :workCaseId AND relation.id = :relationId";
        List<Customer> customerList = (List<Customer>) getSession().createQuery(query)
                            .setParameter("workCaseId", workCaseId)
                            .setParameter("relationId", RelationValue.BORROWER.value())
                            .list();

        return customerList;
    }*/
    public List<Customer> getBorrowerByWorkCaseId(long workCaseId, long workCasePreScreenId){
        log.info("getBorrowerByWorkCaseId ::: workCaseId : {}, workCasePreScreenId : {}", workCaseId, workCasePreScreenId);
        Criteria criteria = createCriteria();
        if(workCaseId != 0){
            criteria.add(Restrictions.eq("workCase.id", workCaseId));
        } else if (workCasePreScreenId != 0){
            criteria.add(Restrictions.eq("workCasePrescreen.id", workCasePreScreenId));
        }
        criteria.add(Restrictions.eq("relation.id", RelationValue.BORROWER.value()));
        criteria.addOrder(Order.asc("id"));
        /*criteria.setFetchMode("workCase", FetchMode.SELECT);
        criteria.setFetchMode("workCasePrescreen", FetchMode.SELECT);
        criteria.setFetchMode("documentType.customerEntity", FetchMode.SELECT);
        criteria.setFetchMode("businessType", FetchMode.SELECT);
        criteria.setFetchMode("relation", FetchMode.SELECT);
        criteria.setFetchMode("reference.customerEntity", FetchMode.SELECT);
        criteria.setFetchMode("reference.borrowerType", FetchMode.SELECT);
        criteria.setFetchMode("kycLevel", FetchMode.SELECT);
        criteria.setFetchMode("mailingAddressType", FetchMode.SELECT);
        criteria.setFetchMode("sourceIncome", FetchMode.SELECT);
        criteria.setFetchMode("countryIncome", FetchMode.SELECT);
        criteria.setFetchMode("customerOblInfo", FetchMode.SELECT);
        criteria.setFetchMode("createBy", FetchMode.SELECT);
        criteria.setFetchMode("modifyBy", FetchMode.SELECT);
        criteria.setFetchMode("individual.customer", FetchMode.SELECT);
        criteria.setFetchMode("individual.customerAttorney", FetchMode.SELECT);
        criteria.setFetchMode("individual.citizenCountry", FetchMode.SELECT);
        criteria.setFetchMode("individual.nationality", FetchMode.SELECT);
        criteria.setFetchMode("individual.sndNationality", FetchMode.SELECT);
        criteria.setFetchMode("individual.race", FetchMode.SELECT);
        criteria.setFetchMode("individual.occupation", FetchMode.SELECT);
        criteria.setFetchMode("individual.education", FetchMode.SELECT);
        criteria.setFetchMode("individual.maritalStatus", FetchMode.SELECT);
        criteria.setFetchMode("individual.fatherTitle", FetchMode.SELECT);
        criteria.setFetchMode("individual.motherTitle", FetchMode.SELECT);
        criteria.setFetchMode("individual.modifyBy", FetchMode.SELECT);
        criteria.setFetchMode("individual.createBy", FetchMode.SELECT);
        criteria.setFetchMode("juristic.customer", FetchMode.SELECT);
        criteria.setFetchMode("ncb.customer", FetchMode.SELECT);
        criteria.setFetchMode("ncb.tdrCondition", FetchMode.SELECT);
        criteria.setFetchMode("ncb.createBy", FetchMode.SELECT);
        criteria.setFetchMode("ncb.modifyBy", FetchMode.SELECT);*/

        List<Customer> customerList = (List<Customer>) criteria.list();
        log.info("getBorrowerByWorkCaseId ::: size : {}", customerList.size());
        return customerList;
    }

    public List<Customer> findByWorkCasePreScreenId(long workCasePreScreenId) {
        log.info("findByWorkCasePreScreenId : {}", workCasePreScreenId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCasePrescreen.id", workCasePreScreenId));
        criteria.addOrder(Order.asc("id"));
        List<Customer> customerList = (List<Customer>) criteria.list();
        log.info("findByWorkCasePreScreenId ::: size : {}", customerList.size());
        return customerList;
    }

    public List<Customer> findCustomerByWorkCasePreScreenId(long workCasePreScreenId) {
        log.info("findCustomerByWorkCasePreScreenId : {}", workCasePreScreenId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCasePrescreen.id", workCasePreScreenId));
        criteria.add(Restrictions.eq("isSpouse", 0));
        criteria.addOrder(Order.asc("id"));
        List<Customer> customerList = (List<Customer>) criteria.list();
        log.info("findCustomerByWorkCasePreScreenId ::: size : {}", customerList.size());
        return customerList;
    }

    public List<Customer> findCustomerByWorkCaseId(long workCaseId) {
        log.info("findCustomerByWorkCaseId : {}", workCaseId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.eq("isSpouse", 0));
        criteria.addOrder(Order.asc("id"));
        List<Customer> customerList = (List<Customer>) criteria.list();
        log.info("findCustomerByWorkCaseId ::: size : {}", customerList.size());
        return customerList;
    }

    public Customer findSpouseById(long spouseId) {
        log.debug("findSpouseById ::: spouseId : {}", spouseId);
        Customer customer = new Customer();
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("id", spouseId));
        customer = (Customer) criteria.uniqueResult();

        return customer;
    }


    public Customer findByCitizenlId(String citizenId, long workCasePreScreenId) {
        log.info("findByCitizenlId : {}", citizenId);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCasePrescreen.id", workCasePreScreenId));
        criteria.add(Restrictions.eq("individual.citizenId", citizenId));
        Customer customer = (Customer) criteria.uniqueResult();

        return customer;
    }

    public Customer findByRegistrationId(String registrationId) {
        log.info("findByRegistrationId : {}", registrationId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("juristic.registrationId", registrationId));
        Customer customer = (Customer) criteria.uniqueResult();
        return customer;
    }

    public List<Customer> findByWorkCaseId(long workCaseId) {
        log.info("findByWorkCaseId : {}", workCaseId);
        Criteria criteria = createCriteria();
        criteria.addOrder(Order.asc("id"));
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        List<Customer> customerList = (List<Customer>) criteria.list();
        log.info("findByWorkCaseId ::: size : {}", customerList.size());
        return customerList;
    }

    public Customer findMainCustomerBySpouseId(long spouseId) {
        log.debug("findMainCustomerBySpouseId ::: spouseId : {}", spouseId);
        Customer customer = new Customer();
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("spouseId", spouseId));
        customer = (Customer) criteria.uniqueResult();

        return customer;
    }

    public List<Customer> findCustomerByCommitteeId(long committeeId) {
        log.debug("findCustomerByCommitteeId ::: committeeId : {}", committeeId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("juristicId", committeeId));
        criteria.addOrder(Order.asc("id"));
        List<Customer> customerList = (List<Customer>) criteria.list();
        log.info("findCustomerByCommitteeId ::: size : {}", customerList.size());
        return customerList;
    }

    public List<Customer> findGuarantorByWorkCaseId(long workCaseId) {
        log.info("findGuarantorByWorkCaseId : {}", workCaseId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.eq("relation.id", RelationValue.GUARANTOR.value()));
        criteria.addOrder(Order.asc("id"));
        List<Customer> customerList = (List<Customer>)criteria.list();
        log.info("findGuarantorByWorkCaseId ::: size : {}", customerList.size());
        return customerList;
    }

    public List<Customer> findCollateralOwnerUWByWorkCaseId(long workCaseId) {
        log.info("findCollateralOwnerUWByWorkCaseId : {}", workCaseId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.or(Restrictions.eq("relation.id", RelationValue.BORROWER.value()),Restrictions.eq("relation.id", RelationValue.GUARANTOR.value())));
        criteria.addOrder(Order.asc("id"));
        List<Customer> customerList = (List<Customer>)criteria.list();
        log.info("findCollateralOwnerUWByWorkCaseId ::: size : {}", customerList.size());
        return customerList;
    }

    public List<Customer> findCustomerBorrowerAndGuarantor(long workCaseId) {
        log.info("findCustomerBorrowerAndGuarantor : {}", workCaseId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.or(Restrictions.eq("relation.id", RelationValue.BORROWER.value()),Restrictions.eq("relation.id", RelationValue.GUARANTOR.value())));
        criteria.addOrder(Order.asc("id"));
        /*criteria.setFetchMode("workCase", FetchMode.SELECT);
        criteria.setFetchMode("workCasePrescreen", FetchMode.SELECT);
        criteria.setFetchMode("documentType.customerEntity", FetchMode.SELECT);
        criteria.setFetchMode("businessType", FetchMode.SELECT);
        criteria.setFetchMode("relation", FetchMode.SELECT);
        criteria.setFetchMode("reference.customerEntity", FetchMode.SELECT);
        criteria.setFetchMode("reference.borrowerType", FetchMode.SELECT);
        criteria.setFetchMode("kycLevel", FetchMode.SELECT);
        criteria.setFetchMode("mailingAddressType", FetchMode.SELECT);
        criteria.setFetchMode("sourceIncome", FetchMode.SELECT);
        criteria.setFetchMode("countryIncome", FetchMode.SELECT);
        criteria.setFetchMode("customerOblInfo", FetchMode.SELECT);
        criteria.setFetchMode("createBy", FetchMode.SELECT);
        criteria.setFetchMode("modifyBy", FetchMode.SELECT);
        criteria.setFetchMode("individual.customer", FetchMode.SELECT);
        criteria.setFetchMode("individual.customerAttorney", FetchMode.SELECT);
        criteria.setFetchMode("individual.citizenCountry", FetchMode.SELECT);
        criteria.setFetchMode("individual.nationality", FetchMode.SELECT);
        criteria.setFetchMode("individual.sndNationality", FetchMode.SELECT);
        criteria.setFetchMode("individual.race", FetchMode.SELECT);
        criteria.setFetchMode("individual.occupation", FetchMode.SELECT);
        criteria.setFetchMode("individual.education", FetchMode.SELECT);
        criteria.setFetchMode("individual.maritalStatus", FetchMode.SELECT);
        criteria.setFetchMode("individual.fatherTitle", FetchMode.SELECT);
        criteria.setFetchMode("individual.motherTitle", FetchMode.SELECT);
        criteria.setFetchMode("individual.modifyBy", FetchMode.SELECT);
        criteria.setFetchMode("individual.createBy", FetchMode.SELECT);
        criteria.setFetchMode("juristic.customer", FetchMode.SELECT);
        criteria.setFetchMode("ncb.customer", FetchMode.SELECT);
        criteria.setFetchMode("ncb.tdrCondition", FetchMode.SELECT);
        criteria.setFetchMode("ncb.createBy", FetchMode.SELECT);
        criteria.setFetchMode("ncb.modifyBy", FetchMode.SELECT);*/


        List<Customer> customerList = (List<Customer>)criteria.list();
        log.info("findCustomerBorrowerAndGuarantor ::: size : {}", customerList.size());
        return customerList;
    }

    @SuppressWarnings("unchecked")
	public List<Customer> findCustomerCanBePOA(long workCaseId) {
    	Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.eq("customerEntity.id",1)); //Individual only
        criteria.createAlias("relation", "r");
        criteria.add(Restrictions.eq("r.canBePOA",true));
        return criteria.list();
    }

	@SuppressWarnings("unchecked")
	public List<Customer> findCustomerCanBeAttorneyRight(long workCaseId) {
		Criteria criteria = createCriteria();
		criteria.add(Restrictions.eq("workCase.id", workCaseId));
		criteria.add(Restrictions.eq("customerEntity.id", 1)); // Individual only
		
		criteria.createAlias("relation", "r");
		criteria.add(Restrictions.eq("r.canBeAttorney", true));
		return criteria.list();
	}
}
