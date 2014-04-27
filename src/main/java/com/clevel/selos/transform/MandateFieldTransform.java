package com.clevel.selos.transform;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.UserSysParameterKey;
import com.clevel.selos.model.db.master.MandateField;
import com.clevel.selos.model.db.master.MandateFieldClass;
import com.clevel.selos.model.db.master.MandateFieldCondition;
import com.clevel.selos.model.db.master.MandateFieldConditionDetail;
import com.clevel.selos.model.view.MandateFieldClassView;
import com.clevel.selos.model.view.MandateFieldConditionDetailView;
import com.clevel.selos.model.view.MandateFieldConditionView;
import com.clevel.selos.model.view.MandateFieldView;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class MandateFieldTransform extends Transform{

    @Inject
    @SELOS
    Logger logger;

    @Inject
    private ActionTransform actionTransform;
    @Inject
    private StepTransform stepTransform;

    @Inject
    public MandateFieldTransform(){

    }

    public MandateFieldView transformToView(MandateField mandateField){
        logger.debug("-- begin transformToView mandateField{}", mandateField);
        if(mandateField == null)
            return null;

        MandateFieldView mandateFieldView = new MandateFieldView();
        mandateFieldView.setId(mandateField.getId());
        mandateFieldView.setMandateFieldClassView(transformToView(mandateField.getMandateFieldClass()));
        mandateFieldView.setFieldName(mandateField.getFieldName());
        mandateFieldView.setFieldDesc(mandateField.getFieldDescription());
        mandateFieldView.setPage(mandateField.getPage());
        mandateFieldView.setMinValue(mandateField.getMinValue());
        mandateFieldView.setMaxValue(mandateField.getMaxValue());
        mandateFieldView.setMatchedValue(mandateField.getMatchedValue());
        if(UserSysParameterKey.STATIC_EMPTY.key().equals(mandateField.getMatchedValue())){
            mandateFieldView.setMatchedEmpty(RadioValue.YES.value());
        } else {
            if(mandateField.getMatchedValue() == null || "".equals(mandateField.getMatchedValue())){
                mandateFieldView.setMatchedEmpty(RadioValue.NOT_SELECTED.value());
            } else {
                mandateFieldView.setMatchedEmpty(RadioValue.NO.value());
            }
        }
        mandateFieldView.setNotMatchedValue(mandateField.getNotMatchedValue());
        if(UserSysParameterKey.STATIC_EMPTY.key().equals(mandateField.getNotMatchedValue())){
            mandateFieldView.setNotMatchedEmpty(RadioValue.YES.value());
        } else {
            if(mandateField.getNotMatchedValue() == null || "".equals(mandateField.getNotMatchedValue()))
                mandateFieldView.setNotMatchedEmpty(RadioValue.NOT_SELECTED.value());
            else
                mandateFieldView.setNotMatchedEmpty(RadioValue.NO.value());
        }
        mandateFieldView.setMandateFieldType(mandateField.getMandateFieldType());
        logger.debug("-- end transformToView return mandateFieldView: {}", mandateFieldView);
        return mandateFieldView;
    }

    public MandateFieldClassView transformToView(MandateFieldClass mandateFieldClass){
        logger.debug("-- being transformToView(MandateFieldClass : {})", mandateFieldClass);
        if(mandateFieldClass == null)
            return null;

        MandateFieldClassView mandateFieldClassView = new MandateFieldClassView();
        mandateFieldClassView.setId(mandateFieldClass.getId());
        mandateFieldClassView.setClassName(mandateFieldClass.getClassName());
        mandateFieldClassView.setPageName(mandateFieldClass.getPageName());
        mandateFieldClassView.setClassDescription(mandateFieldClass.getClassDescription());

        logger.debug("-- end transformToView return MandateFieldClassView: {}", mandateFieldClassView);
        return mandateFieldClassView;
    }

    public MandateFieldConditionView transformToView(MandateFieldCondition mandateFieldCondition){
        logger.debug("-- begin transformToView(MandateFieldCondition : {})", mandateFieldCondition);

        if(mandateFieldCondition == null)
            return null;

        MandateFieldConditionView mandateFieldConditionView = new MandateFieldConditionView();
        mandateFieldConditionView.setId(mandateFieldCondition.getId());
        mandateFieldConditionView.setClassName(mandateFieldCondition.getClassName());
        mandateFieldConditionView.setConditionDesc(mandateFieldCondition.getConditionDesc());
        mandateFieldConditionView.setDependCondition(transformToView(mandateFieldCondition.getDependCondition()));
        mandateFieldConditionView.setDependType(mandateFieldCondition.getDependType());
        mandateFieldConditionView.setMandateConditionType(mandateFieldCondition.getMandateConditionType());

        List<MandateFieldConditionDetailView> conditionDetailViewList = new ArrayList<MandateFieldConditionDetailView>();
        for(MandateFieldConditionDetail conditionDetail : mandateFieldCondition.getMandateFieldConditionDetailList()){
            if(conditionDetail != null)
                conditionDetailViewList.add(transformToView(conditionDetail));
        }

        logger.debug("-- end transformToView()");
        return mandateFieldConditionView;
    }

    public MandateFieldConditionDetailView transformToView(MandateFieldConditionDetail conditionDetail){
        logger.debug("-- begin transformToView(MandateFieldConditionDetail: {})", conditionDetail);

        if(conditionDetail == null)
            return null;
        MandateFieldConditionDetailView mandateFieldConditionDetailView = new MandateFieldConditionDetailView();
        mandateFieldConditionDetailView.setId(conditionDetail.getId());
        mandateFieldConditionDetailView.setMandateFieldView(transformToView(conditionDetail.getMandateField()));
        mandateFieldConditionDetailView.setBaseValue(conditionDetail.getBaseValue());

        logger.debug("-- end transformToView return ", mandateFieldConditionDetailView);
        return mandateFieldConditionDetailView;
    }
}
