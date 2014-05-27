package com.clevel.selos.model.view;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class MandateFieldClassSAAdminView extends MandateFieldClassView implements Serializable{

    protected List<MandateFieldView> mandateFieldViewList;
    protected List<MandateFieldConditionView> mandateFieldConditionViewList;
    private boolean needUpdate = false;
    private boolean classRequired = false;

    public List<MandateFieldView> getMandateFieldViewList() {
        return mandateFieldViewList;
    }

    public void setMandateFieldViewList(List<MandateFieldView> mandateFieldViewList) {
        this.mandateFieldViewList = mandateFieldViewList;
    }

    public List<MandateFieldConditionView> getMandateFieldConditionViewList() {
        return mandateFieldConditionViewList;
    }

    public void setMandateFieldConditionViewList(List<MandateFieldConditionView> mandateFieldConditionViewList) {
        this.mandateFieldConditionViewList = mandateFieldConditionViewList;
    }

    public boolean isNeedUpdate() {
        return needUpdate;
    }

    public void setNeedUpdate(boolean needUpdate) {
        this.needUpdate = needUpdate;
    }

    public boolean isClassRequired() {
        return classRequired;
    }

    public void setClassRequired(boolean classRequired) {
        this.classRequired = classRequired;
    }

    public void updateValues(MandateFieldClassSAAdminView view){
        id = view.id;
        className = view.className;
        classDescription = view.classDescription;
        classRequired = view.classRequired;

        pageName = view.pageName;
        active = view.active;
        mandateFieldViewList = view.mandateFieldViewList;
        mandateFieldConditionViewList = view.mandateFieldConditionViewList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append(super.toString())
                .append("mandateFieldViewList", mandateFieldViewList)
                .toString();
    }
}
