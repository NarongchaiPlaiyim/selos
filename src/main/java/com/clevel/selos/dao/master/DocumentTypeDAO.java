package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.DocumentType;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class DocumentTypeDAO extends GenericDAO<DocumentType, Integer> {
    @Inject
    private Logger log;

    @Inject
    public DocumentTypeDAO() {

    }

    public List<DocumentType> findByCustomerEntityId(int customerEntityId) {
        log.info("findByCustomerEntityId. (customerEntityId: {})", customerEntityId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("customerEntity.id", customerEntityId));
        criteria.addOrder(Order.asc("id"));
        List<DocumentType> documentTypeList = criteria.list();
        log.info("findByCustomerEntityId. (result size: {})", documentTypeList.size());
        return documentTypeList;
    }

    public List<DocumentType> getDocumentTypeListPreScreen(int customerEntityId){
        log.debug("getDocumentTypeListPreScreen");
        Criteria criteria = createCriteria();
        if(customerEntityId != 0){
            criteria.add(Restrictions.eq("customerEntity.id", customerEntityId));
        }
        criteria.add(Restrictions.in("documentTypeCode", new String[]{"CI", "SC"}));
        criteria.addOrder(Order.asc("id"));
        List<DocumentType> documentTypeList = criteria.list();

        return documentTypeList;
    }
}
