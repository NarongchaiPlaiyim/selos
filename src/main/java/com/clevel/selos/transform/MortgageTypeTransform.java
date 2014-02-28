package com.clevel.selos.transform;

import com.clevel.selos.dao.master.MortgageTypeDAO;
import com.clevel.selos.model.db.master.MortgageType;
import com.clevel.selos.model.view.MortgageTypeView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class MortgageTypeTransform extends Transform {
    @Inject
    private MortgageTypeDAO mortgageTypeDAO;

    @Inject
    public MortgageTypeTransform() {
    }

    public MortgageType transformToModel(MortgageTypeView mortgageTypeView) {
        if (mortgageTypeView == null) {
            return null;
        }

        MortgageType mortgageType;
        if (mortgageTypeView.getId() != 0) {
            mortgageType = mortgageTypeDAO.findById(mortgageTypeView.getId());
        } else {
            mortgageType = new MortgageType();
        }
        mortgageType.setActive(mortgageTypeView.getActive());
        mortgageType.setMortgage(mortgageTypeView.getMortgage());
        mortgageType.setRedeem(mortgageTypeView.isRedeem());
        mortgageType.setMortgageFeeFlag(mortgageTypeView.isMortgageFeeFlag());
        mortgageType.setMortgageFlag(mortgageTypeView.isMortgageFlag());
        mortgageType.setPledgeFlag(mortgageTypeView.isPledgeFlag());
        mortgageType.setGuarantorFlag(mortgageTypeView.isGuarantorFlag());
        mortgageType.setTcgFlag(mortgageTypeView.isTcgFlag());
        mortgageType.setReferredFlag(mortgageTypeView.isReferredFlag());
        return mortgageType;
    }

    public List<MortgageType> transformToModel(List<MortgageTypeView> mortgageTypeViewList) {
        List<MortgageType> mortgageTypeList = new ArrayList<MortgageType>();
        if (mortgageTypeViewList == null) {
            return mortgageTypeList;
        }
        for (MortgageTypeView mortgageTypeView : mortgageTypeViewList) {
            mortgageTypeList.add(transformToModel(mortgageTypeView));
        }
        return mortgageTypeList;
    }

    public MortgageTypeView transformToView(MortgageType mortgageType) {
        MortgageTypeView mortgageTypeView = new MortgageTypeView();
        if (mortgageType == null) {
            return mortgageTypeView;
        }
        mortgageTypeView.setId(mortgageType.getId());
        mortgageTypeView.setActive(mortgageType.getActive());
        mortgageTypeView.setMortgage(mortgageType.getMortgage());
        mortgageTypeView.setRedeem(mortgageType.isRedeem());
        mortgageTypeView.setMortgageFeeFlag(mortgageType.isMortgageFeeFlag());
        mortgageTypeView.setMortgageFlag(mortgageType.isMortgageFlag());
        mortgageTypeView.setPledgeFlag(mortgageType.isPledgeFlag());
        mortgageTypeView.setGuarantorFlag(mortgageType.isGuarantorFlag());
        mortgageTypeView.setTcgFlag(mortgageType.isTcgFlag());
        mortgageTypeView.setReferredFlag(mortgageType.isReferredFlag());
        return mortgageTypeView;
    }

    public List<MortgageTypeView> transformToView(List<MortgageType> mortgageTypeList) {
        List<MortgageTypeView> mortgageTypeViewList = new ArrayList<MortgageTypeView>();
        if (mortgageTypeList == null) {
            return mortgageTypeViewList;
        }
        for (MortgageType mortgageType : mortgageTypeList) {
            mortgageTypeViewList.add(transformToView(mortgageType));
        }
        return mortgageTypeViewList;
    }
}
