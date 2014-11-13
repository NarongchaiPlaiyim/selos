package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.RelationValue;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.view.CustomerBasicView;
import com.clevel.selos.model.view.CustomerInfoSimpleView;
import com.clevel.selos.util.Util;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CustomerDAO extends GenericDAO<Customer, Long> {
    @Inject
    @SELOS
    Logger log;
    //@PersistenceContext
    //protected EntityManager em;
    @Inject
    public CustomerDAO() {
    }

    /*public List<CustomerBasicView> getCustomerForHeader(long workCasePreScreenId, long workCaseId){
        log.info("getCustomerForHeader ::: workCasePreScreenId : {}, workCaseId : {}", workCasePreScreenId, workCaseId);

        String queryStr = "SELECT customer.id, customer.customerentity_id, title.title_th, customer.name_th, customer.lastname_th, customer.tmb_customer_id, individual.citizen_id, juristic.registration_id ";
        queryStr = queryStr.concat("from wrk_customer customer ");
        queryStr = queryStr.concat("left outer join mst_title title on customer.title_id = title.id ");
        queryStr = queryStr.concat("left outer join wrk_individual individual on customer.id = individual.customer_id ");
        queryStr = queryStr.concat("left outer join wrk_juristic juristic on customer.id = juristic.customer_id ");

        if(workCaseId != 0) {
            queryStr = queryStr.concat("where customer.workcase_id = :param1 ");
        }else if (workCasePreScreenId != 0){
            queryStr = queryStr.concat("where customer.workcase_prescreen_id = :param1 ");
        }

        queryStr = queryStr.concat("and customer.relation_id = 1");

        Query query = em.createNativeQuery(queryStr);
        if(workCaseId != 0) {
            query.setParameter("param1", workCaseId);
        }else if(workCasePreScreenId != 0){
            query.setParameter("param1", workCasePreScreenId);
        }

        List<Object[]> tempObject = query.getResultList();

        CustomerBasicView customerBasicView;
        List<CustomerBasicView> customerBasicViewList = new ArrayList<CustomerBasicView>();
        for(Object[] row : tempObject){
            log.debug("row : {}", row);
            customerBasicView = new CustomerBasicView();
            customerBasicView.setId(Util.parseLong(row[0], 0));
            customerBasicView.setCustomerEntityId(Util.parseInt(row[1], 0));
            customerBasicView.setTitleTh(Util.parseString(row[2], ""));
            customerBasicView.setFirstNameTh(Util.parseString(row[3], ""));
            customerBasicView.setLastNameEm(Util.parseString(row[4], ""));
            customerBasicView.setTmbCustomerId(Util.parseString(row[5], ""));
            customerBasicView.setCitizenId(Util.parseString(row[6], ""));
            customerBasicView.setRegistrationId(Util.parseString(row[7], ""));
            log.debug("customerBasicView : {}", customerBasicView);
            customerBasicViewList.add(customerBasicView);
        }
        log.debug("customerBasicViewList : {}", customerBasicViewList);

        return customerBasicViewList;
    }*/

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
        Customer customer;
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
