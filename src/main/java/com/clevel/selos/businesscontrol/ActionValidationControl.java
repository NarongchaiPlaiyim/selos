package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.MandateFieldConditionDAO;
import com.clevel.selos.dao.master.MandateFieldDAO;
import com.clevel.selos.dao.master.MandateFieldStepActionDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.ConditionType;
import com.clevel.selos.model.UserSysParameterKey;
import com.clevel.selos.model.db.master.MandateField;
import com.clevel.selos.model.db.master.MandateFieldCondition;
import com.clevel.selos.model.db.master.MandateFieldConditionDetail;
import com.clevel.selos.model.db.master.MandateFieldStepAction;
import com.clevel.selos.model.view.ActionValidationResult;
import com.clevel.selos.model.view.MandateFieldMessageView;
import com.clevel.selos.model.view.MandateFieldView;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.transform.MandateFieldTransform;
import com.clevel.selos.util.Util;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class ActionValidationControl extends BusinessControl{

    @Inject
    @SELOS
    Logger logger;

    @Inject
    private MandateFieldDAO mandateFieldDAO;

    @Inject
    MandateFieldStepActionDAO mandateFieldStepActionDAO;

    @Inject
    MandateFieldConditionDAO mandateFieldConditionDAO;

    @Inject
    @ValidationMessage
    Message validationMsg;

    @Inject
    MandateFieldTransform mandateFieldTransform;

    private final static String ACTION_DATA_REQUIRED = "1151";
    private final static String ACTION_DATA_INCORRECT = "1152";
    private final static String ACTION_DATA_MIN_INCORRECT = "1153";
    private final static String ACTION_DATA_MAX_INCORRECT = "1154";
    private final static String ACTION_DATA_MIN_MAX_INCORRECT = "1155";
    private final static String ACTION_DATA_MATCHED_INCORRECT = "1156";
    private final static String ACTION_DATA_NOT_MATCHED_INCORRECT = "1157";
    private final static String CONDITION_AND = "1158";
    private final static String CONDITION_OR = "1159";
    private final static String CONDITION_DEPEND_FALSE = "1160";
    private final static String CONDITION_DEPEND_TRUE = "1161";

    private Map<String, List<MandateFieldView>> mandateFieldViewMap;
    private Map<String, MandateFieldView> listMandateFieldViewMap;
    private ActionValidationResult actionValidationResult;


    public int loadActionValidation(long stepId, long actionId){
        logger.info("-- begin loadActionValidation stepId {}: actionId {}", stepId, actionId);
        mandateFieldViewMap = new HashMap<String, List<MandateFieldView>>();
        listMandateFieldViewMap = new HashMap<String, MandateFieldView>();
        List<MandateFieldStepAction> mandateFieldStepActionList = mandateFieldStepActionDAO.findByAction(stepId, actionId);
        logger.info("-- update Mandate Field {}", mandateFieldStepActionList);

        for(MandateFieldStepAction mandateFieldStepAction : mandateFieldStepActionList){
            MandateField mandateField = mandateFieldStepAction.getMandateField();
            if(mandateField != null){
                if(ArrayList.class.getName().equals(mandateField.getClassName())){
                    listMandateFieldViewMap.put(mandateField.getClassName(), mandateFieldTransform.transformToView(mandateField));
                    logger.info("listMandateFieldViewMap {}", listMandateFieldViewMap);
                } else {
                    MandateFieldView mandateFieldView = mandateFieldTransform.transformToView(mandateFieldStepAction.getMandateField());
                    List<MandateFieldView> mandateFieldViewList = mandateFieldViewMap.get(mandateFieldView.getClassName());
                    if(mandateFieldViewList == null)
                        mandateFieldViewList = new ArrayList<MandateFieldView>();
                    mandateFieldViewList.add(mandateFieldView);
                    mandateFieldViewMap.put(mandateFieldView.getClassName(), mandateFieldViewList);
                    logger.info("mandateFieldView {}", mandateFieldView);
                }
            }
        }
        actionValidationResult = new ActionValidationResult();
        List<MandateFieldMessageView> mandateFieldMessageViewList = new ArrayList<MandateFieldMessageView>();
        actionValidationResult.setMandateFieldMessageViewList(mandateFieldMessageViewList);
        return mandateFieldViewMap.size();
    }

    public boolean validate(Object object){
        if(object != null){
            Class objectClass = object.getClass();

            logger.info("Object Class Name {}", objectClass.getName());
            if(objectClass.getName().equals(ArrayList.class.getName())){
                List<?> objectList = (ArrayList<?>)object;
                TypeVariable typeVariable[] = objectList.getClass().getTypeParameters();
                for(int i=0; i < typeVariable.length; i++){
                    logger.info("type variable {}", typeVariable[i].getName());
                }
                logger.info("It is arrayList Type");
                ArrayList arrayList = (ArrayList<?>)object;
                if(arrayList != null && arrayList.size() > 0){
                    for(Object objectInArray : arrayList){
                        logger.info("objectInArray is a class of {}", objectInArray.getClass().getName());
                        validateActualObj(objectInArray);
                    }
                }
            } else {
                validateActualObj(object);

            }
        }
        return true;
    }

    private void validateActualObj(Object object){
        logger.info("-- begin validateActualObj");
        Class objectClass = object.getClass();
        Type type = objectClass.getGenericSuperclass();
        logger.info("Type of {}, object Class Name {}", type, objectClass.getName());
        String _compareClassName = type.toString();
        if(_compareClassName.endsWith(Object.class.getName())){
            _compareClassName = objectClass.getName();
        }

        logger.info("compare class name {}", _compareClassName);
        String className = null;
        for(String key : mandateFieldViewMap.keySet()){
            if(_compareClassName.endsWith(key)){
                logger.info("find class Name {}", key);
                className = key;
            }
        }

        List<MandateFieldView> mandateFieldViewList = mandateFieldViewMap.get(className);
        if(mandateFieldViewList != null){

            Map<Long, ValidationResult> validationResultMap = new HashMap<Long, ValidationResult>();

            for(MandateFieldView mandateFieldView : mandateFieldViewList){
                Field field = null;
                try{
                    field = objectClass.getDeclaredField(mandateFieldView.getFieldName());
                }catch (NoSuchFieldException ex){
                    logger.debug("Cannot find field from original class {}", mandateFieldView.getFieldName());
                    try{
                        if(objectClass.getSuperclass() != null){
                            field = objectClass.getSuperclass().getDeclaredField(mandateFieldView.getFieldName());
                        }
                    } catch (NoSuchFieldException sex){
                        logger.debug("Also not found field in super class {}", mandateFieldView.getFieldName());
                    }
                }

                if(field != null) {
                    field.setAccessible(true);
                    logger.info("loop for mandateFieldView name {}", mandateFieldView);
                    if(field.getName().startsWith(mandateFieldView.getFieldName())){
                        logger.info("found name matched field: {}, mandateFieldView: {}", field.getName(), mandateFieldView.getFieldName());
                        mandateFieldView.setChecked(Boolean.TRUE);
                        ValidationResult validationResult = null;

                        try{
                            Object _fieldObj = field.get(object);
                            if(_fieldObj == null){
                                validationResult = validateNullObject(mandateFieldView);
                            } else {
                                Class _fieldObjClass = _fieldObj.getClass();
                                if(_fieldObjClass.equals(Integer.class.getName())){
                                    validationResult = validate(mandateFieldView, (Integer)_fieldObj);
                                } else if(_fieldObjClass.equals(Long.class.getName())){
                                    validationResult = validate(mandateFieldView, (Long)_fieldObj);
                                } else if(_fieldObjClass.equals(BigDecimal.class.getName())){
                                    validationResult = validate(mandateFieldView, (BigDecimal)_fieldObj);
                                }
                            }
                        } catch (IllegalAccessException iae){
                            logger.debug("Cannot access Field to validate Field {}", field.getName());
                            validationResult = getValidationResult(mandateFieldView, validationMsg.get(ACTION_DATA_REQUIRED, mandateFieldView.getFieldDesc()));
                        }

                        if(validationResult != null){
                            validationResultMap.put(mandateFieldView.getId(), validationResult);
                        }
                    }
                }
            }
            //Consolidate Condition Validation;
            checkCondition(className, validationResultMap);
        } else {
            logger.info("No validation configure for class {}", _compareClassName);
        }
    }

    private void checkCondition(String className, Map<Long, ValidationResult> validationResultMap){
        List<MandateFieldCondition> mandateFieldConditionList = mandateFieldConditionDAO.findByClassName(className);
        if(mandateFieldConditionList != null && mandateFieldConditionList.size() > 0){
            for(MandateFieldCondition condition : mandateFieldConditionList){
                logger.info("There is condtion setting");
                List<MandateFieldConditionDetail> conditionDetailList = condition.getMandateFieldConditionDetailList();

                StringBuilder stringBuilder = new StringBuilder();

                boolean isPassCondition = Boolean.TRUE;
                if(condition.getConditionType().equals(ConditionType.AND)){
                    for(MandateFieldConditionDetail conditionDetail : conditionDetailList){
                        ValidationResult validationResult = validationResultMap.get(conditionDetail.getMandateField().getId());
                        if(!validationResult.isPass)
                            isPassCondition = Boolean.FALSE;
                        if(stringBuilder.length() > 0){
                            stringBuilder.append(" ").append(ConditionType.AND).append(" ").append(validationResult.message);
                        }
                    }
                } else if(condition.getConditionType().equals(ConditionType.OR)){
                    isPassCondition = Boolean.FALSE;
                    for(MandateFieldConditionDetail conditionDetail : conditionDetailList){
                        ValidationResult validationResult = validationResultMap.get(conditionDetail.getMandateField().getId());
                        if(validationResult.isPass)
                            isPassCondition = Boolean.TRUE;
                        if(stringBuilder.length() > 0){
                            stringBuilder.append(" ").append(ConditionType.AND).append(" ").append(validationResult.message);
                        }
                    }
                }

                if(!isPassCondition){
                    addMandateFieldMessageView(condition, stringBuilder.toString());

                }
            }
        }
        for(ValidationResult validationResult : validationResultMap.values()){
            if(!validationResult.isPass){
                logger.info("add mandate field result {}", validationResult);
                addMandateFieldMessageView(validationResult);
            }
        }

    }

    private ValidationResult validate(MandateFieldView mandateFieldView, Integer _fieldObj){
        logger.info("validate mandateFieldView:{}, Integer: {}", mandateFieldView, _fieldObj);
        Boolean isPassMin = null, isPassMax = null, isPassMatched = null, isPassNotMatched = null;
        if(!Util.isEmpty(mandateFieldView.getMinValue())){
            isPassMin = (_fieldObj >= Integer.parseInt(mandateFieldView.getMinValue()));
        }
        if(!Util.isEmpty(mandateFieldView.getMaxValue())){
            isPassMax = (_fieldObj <= Integer.parseInt(mandateFieldView.getMaxValue()));
        }
        if(!Util.isEmpty(mandateFieldView.getMatchedValue())){
            if(mandateFieldView.getMatchedValue().equals(UserSysParameterKey.STATIC_EMPTY.toString())){
                isPassMatched = (_fieldObj == null);
            } else {
                isPassMatched = (_fieldObj == Integer.parseInt(mandateFieldView.getMatchedValue()));
            }
        }
        if(!Util.isEmpty(mandateFieldView.getNotMatchedValue())){
            if(mandateFieldView.getNotMatchedValue().equals(UserSysParameterKey.STATIC_EMPTY.toString())){
                isPassNotMatched = !(_fieldObj == null);
            } else {
                isPassNotMatched = !(_fieldObj == Integer.parseInt(mandateFieldView.getNotMatchedValue()));
            }
        }
        logger.info("validate mandateFieldView:{}, Integer: {}", mandateFieldView, _fieldObj);
        return getValidationResult(mandateFieldView, isPassMin, isPassMax, isPassMatched, isPassNotMatched);
    }

    private ValidationResult validate(MandateFieldView mandateFieldView, Long _fieldObj){
        logger.info("validateNullObject mandateFieldView:{}, Long: {}", mandateFieldView, _fieldObj);
        Boolean isPassMin = null, isPassMax = null, isPassMatched = null, isPassNotMatched = null;
        if(!Util.isEmpty(mandateFieldView.getMinValue())){
            isPassMin = (_fieldObj >= Long.parseLong(mandateFieldView.getMinValue()));
        }
        if(!Util.isEmpty(mandateFieldView.getMaxValue())){
            isPassMax = (_fieldObj <= Long.parseLong(mandateFieldView.getMaxValue()));
        }
        if(!Util.isEmpty(mandateFieldView.getMatchedValue())){
            if(mandateFieldView.getMatchedValue().equals(UserSysParameterKey.STATIC_EMPTY.key())){
                isPassMatched = (_fieldObj == null);
            } else {
                isPassMatched = (_fieldObj == Long.parseLong(mandateFieldView.getMatchedValue()));
            }
        }
        if(!Util.isEmpty(mandateFieldView.getNotMatchedValue())){
            if(mandateFieldView.getNotMatchedValue().equals(UserSysParameterKey.STATIC_EMPTY.key())){
                isPassNotMatched = !(_fieldObj == null);
            } else {
                isPassNotMatched = !(_fieldObj == Long.parseLong(mandateFieldView.getNotMatchedValue()));
            }
        }
        logger.info("validateNullObject mandateFieldView:{}, Long: {}", mandateFieldView, _fieldObj);
        return getValidationResult(mandateFieldView, isPassMin, isPassMax, isPassMatched, isPassNotMatched);
    }

    private ValidationResult validate(MandateFieldView mandateFieldView, BigDecimal _fieldObj){
        logger.info("validateNullObject mandateFieldView:{}, BigDecimal: {}", mandateFieldView, _fieldObj);
        Boolean isPassMin = null, isPassMax = null, isPassMatched = null, isPassNotMatched = null;
        if(!Util.isEmpty(mandateFieldView.getMinValue())){
            BigDecimal _compareDecimal = new BigDecimal(mandateFieldView.getMinValue());
            isPassMin = (_fieldObj.compareTo(_compareDecimal) >= 0);
        }
        if(!Util.isEmpty(mandateFieldView.getMaxValue())){
            BigDecimal _compareDecimal = new BigDecimal(mandateFieldView.getMaxValue());
            isPassMax = (_fieldObj.compareTo(_compareDecimal) <= 0);
        }
        if(!Util.isEmpty(mandateFieldView.getMatchedValue())){
            if(mandateFieldView.getMatchedValue().equals(UserSysParameterKey.STATIC_EMPTY.key())){
                isPassMatched = (_fieldObj == null);
            } else {
                BigDecimal _compareDecimal = new BigDecimal(mandateFieldView.getMatchedValue());
                isPassMatched = (_fieldObj.compareTo(_compareDecimal) == 0);
            }
        }
        if(!Util.isEmpty(mandateFieldView.getNotMatchedValue())){
            if(mandateFieldView.getNotMatchedValue().equals(UserSysParameterKey.STATIC_EMPTY.key())){
                isPassNotMatched = !(_fieldObj == null);
            } else {
                BigDecimal bigDecimal = new BigDecimal(mandateFieldView.getNotMatchedValue());
                isPassNotMatched = !(_fieldObj.compareTo(bigDecimal) == 0);
            }
        }
        logger.info("validate result:{}, {}, {}, {}", isPassMin, isPassMax, isPassMatched, isPassNotMatched);
        return getValidationResult(mandateFieldView, isPassMin, isPassMax, isPassMatched, isPassNotMatched);
    }

    private ValidationResult validateNullObject(MandateFieldView mandateFieldView){
        logger.info("validateNullObject {}", mandateFieldView);
        Boolean isPassMin = null, isPassMax = null, isPassMatched = null, isPassNotMatched = null;
        if(!Util.isEmpty(mandateFieldView.getMinValue())){
            isPassMin = false;
        }
        if(!Util.isEmpty(mandateFieldView.getMaxValue())){
            isPassMax = false;
        }
        if(!Util.isEmpty(mandateFieldView.getMatchedValue())){
            if(mandateFieldView.getMatchedValue().equals(UserSysParameterKey.STATIC_EMPTY.key())){
                isPassMatched = true;
            } else {
                isPassMatched = false;
            }
        }
        if(!Util.isEmpty(mandateFieldView.getNotMatchedValue())){
            if(mandateFieldView.getNotMatchedValue().equals(UserSysParameterKey.STATIC_EMPTY.key())){
                isPassNotMatched = false;
            } else {
                isPassNotMatched = true;
            }
        }
        logger.info("validateNullObject result:{}, {}, {}, {}", isPassMin, isPassMax, isPassMatched, isPassNotMatched);
        return getValidationResult(mandateFieldView, isPassMin, isPassMax, isPassMatched, isPassNotMatched);
    }

    private ValidationResult getValidationResult(MandateFieldView mandateFieldView, Boolean isPassMin, Boolean isPassMax, Boolean isPassMatched, Boolean isPassNotMatched){
        logger.info("getValidationResult isPassMin:{}, isPassMax:{}, isPassMatched:{}, isPassNotMatched:{}", isPassMin, isPassMax, isPassMatched, isPassNotMatched);
        ValidationResult validationResult = new ValidationResult();
        StringBuilder stringBuilder = new StringBuilder();
        if(isPassMin != null && isPassMax != null){
            if(!isPassMin || !isPassMax)
                stringBuilder.append(validationMsg.get(ACTION_DATA_MIN_MAX_INCORRECT, mandateFieldView.getFieldDesc(), mandateFieldView.getMinValue(), mandateFieldView.getMaxValue()));
        }
        else if(isPassMin != null && !isPassMin) {
            stringBuilder.append(validationMsg.get(ACTION_DATA_MIN_INCORRECT, mandateFieldView.getFieldDesc(), mandateFieldView.getMinValue()));
        } else if(isPassMax != null && !isPassMax){
            stringBuilder.append(validationMsg.get(ACTION_DATA_MAX_INCORRECT, mandateFieldView.getFieldDesc(), mandateFieldView.getMaxValue()));
        }

        if(isPassMatched != null && !isPassMatched){
            stringBuilder.append(stringBuilder.length() > 0?",":"");
            stringBuilder.append(validationMsg.get(ACTION_DATA_MATCHED_INCORRECT, mandateFieldView.getFieldDesc(), mandateFieldView.getMatchedValue()));
        }

        if(isPassNotMatched != null && !isPassNotMatched){
            stringBuilder.append(stringBuilder.length() > 0?",":"");
            stringBuilder.append(validationMsg.get(ACTION_DATA_NOT_MATCHED_INCORRECT, mandateFieldView.getFieldDesc(), mandateFieldView.getNotMatchedValue()));
        }

        if(stringBuilder.length() > 0){
            validationResult.isPass = Boolean.FALSE;
            validationResult.message = stringBuilder.toString();
        } else {
            validationResult.isPass = Boolean.TRUE;
        }
        validationResult.mandateFieldView = mandateFieldView;
        logger.info("getValidationResult return {}", validationResult);
        return validationResult;
    }

    private ValidationResult getValidationResult(MandateFieldView mandateFieldView, String message){
        ValidationResult validationResult = new ValidationResult();
        if(message != null && message.length() > 0){
            validationResult.isPass = Boolean.FALSE;
            validationResult.message = message;
        } else {
            validationResult.isPass = Boolean.TRUE;
        }
        validationResult.mandateFieldView = mandateFieldView;
        return validationResult;
    }

    private void addMandateFieldMessageView(ValidationResult validationResult){
        MandateFieldView mandateFieldView = validationResult.mandateFieldView;
        MandateFieldMessageView mandateFieldMessageView = new MandateFieldMessageView();
        mandateFieldMessageView.setFieldName(mandateFieldView.getFieldName());
        mandateFieldMessageView.setFieldDesc(mandateFieldView.getFieldDesc());
        mandateFieldMessageView.setMessage(validationResult.message);
        mandateFieldMessageView.setPageName(mandateFieldView.getPage());
        actionValidationResult.getMandateFieldMessageViewList().add(mandateFieldMessageView);
    }

    private void addMandateFieldMessageView(MandateFieldCondition condition, String message){
        MandateFieldMessageView mandateFieldMessageView = new MandateFieldMessageView();
        mandateFieldMessageView.setFieldName("Condition " + condition.getConditionType().name());
        mandateFieldMessageView.setFieldDesc(condition.getConditionDesc());
        mandateFieldMessageView.setMessage(message);

        actionValidationResult.getMandateFieldMessageViewList().add(mandateFieldMessageView);
    }

    public ActionValidationResult getFinalValidationResult(){
        if(actionValidationResult.getMandateFieldMessageViewList().size() > 0){
            actionValidationResult.setActionResult(ActionResult.FAILED);

        } else {
            actionValidationResult.setActionResult(ActionResult.SUCCESS);
        }
        return actionValidationResult;
    }

    private class ValidationResult{
        Boolean isPass = null;
        String message = null;
        MandateFieldView mandateFieldView = null;

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                    .append("isPass", isPass)
                    .append("message", message)
                    .append("mandateFieldView", mandateFieldView)
                    .toString();
        }
    }


}
