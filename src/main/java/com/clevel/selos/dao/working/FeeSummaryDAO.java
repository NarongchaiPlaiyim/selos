package com.clevel.selos.dao.working;

import javax.inject.Inject;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.FeeSummary;

public class FeeSummaryDAO extends GenericDAO<FeeSummary,Long> {
	private static final long serialVersionUID = 8153408173594150455L;
	@Inject @SELOS
    private Logger log;

	public FeeSummary findByWorkCaseId(long workCaseId) {
		Criteria criteria = createCriteria();
		criteria.add(Restrictions.eq("workCase.id", workCaseId));
		FeeSummary feeSummary = (FeeSummary) criteria.uniqueResult();
		return feeSummary;
	}
}
