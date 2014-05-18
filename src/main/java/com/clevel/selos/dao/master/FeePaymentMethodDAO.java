package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.FeePaymentMethod;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class FeePaymentMethodDAO extends GenericDAO<FeePaymentMethod,Integer> {
	private static final long serialVersionUID = -9092627334977116360L;

    @Inject
    @SELOS
    private Logger logger;

    public FeePaymentMethod findByBRMSCode(String paymentMethod){
        logger.debug("findByBRMSCode {}", paymentMethod);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("brmsCode", paymentMethod));
        FeePaymentMethod feePaymentMethod = (FeePaymentMethod) criteria.uniqueResult();
        logger.debug("retrun feePaymentThod {}", feePaymentMethod);
        return feePaymentMethod;
    }
}
