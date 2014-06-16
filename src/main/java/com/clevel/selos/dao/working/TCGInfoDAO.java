package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.TCGInfo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class TCGInfoDAO extends GenericDAO<TCGInfo, Long> {
    private static final long serialVersionUID = -654717352875714578L;

	public TCGInfoDAO() {
    }

    public TCGInfo findByWorkCaseId(long workCase) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCase));
        TCGInfo tcg = (TCGInfo) criteria.uniqueResult();
        return tcg;
    }
}
