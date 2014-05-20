package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.MandateFieldClassDAO;
import com.clevel.selos.dao.master.MandateFieldConditionDAO;
import com.clevel.selos.dao.master.MandateFieldDAO;
import com.clevel.selos.dao.master.MandateFieldStepActionDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.master.*;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
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
    private MandateFieldClassDAO mandateFieldClassDAO;

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
    private final static String CONDITION_BASE = "1162";

    private Map<String, List<MandateFieldView>> mandateFieldViewMap;
    private Map<String, MandateFieldView> listMandateFieldViewMap;
    private ActionValidationResult actionValidationResult;

    private Map<Long, ConditionResult> conditionResultMap;
    private Map<Long, MandateFieldCondition> dependedConditionMap;
    private Map<Long, ValidationResult> validationResultMap;
    private Map<Long, ValidationResult> dependedValidationMap;

    public int loadActionValidation(long stepId, long actionId){
        logger.info("-- begin loadActionValidation stepId {}: actionId {}", stepId, actionId);
        mandateFieldViewMap = new HashMap<String, List<MandateFieldView>>();
        listMandateFieldViewMap = new HashMap<String, MandateFieldView>();
        List<MandateFieldStepAction> mandateFieldStepActionList = mandateFieldStepActionDAO.findByAction(stepId, actionId);
        logger.info("-- update Mandate Field {}", mandateFieldStepActionList);

        for(MandateFieldStepAction mandateFieldStepAction : mandateFieldStepActionList){
            MandateField mandateField = mandateFieldStepAction.getMandateField();
            if(mandateField != null){
                if(ArrayList.class.getName().equals(mandateField.getMandateFieldClass().getClassName())){
                    listMandateFieldViewMap.put(mandateField.getParameterizedName(), mandateFieldTransform.transformToView(mandateField));
                    logger.info("listMandateFieldViewMap {}", listMandateFieldViewMap);
                } else {
                    MandateFieldView mandateFieldView = mandateFieldTransform.transformToView(mandateFieldStepAction.getMandateField());
                    List<MandateFieldView> mandateFieldViewList = mandateFieldViewMap.get(mandateFieldView.getMandateFieldClassView().getClassName());
                    if(mandateFieldViewList == null)
                        mandateFieldViewList = new ArrayList<MandateFieldView>();
                    mandateFieldViewList.add(mandateFieldView);
                    mandateFieldViewMap.put(mandateFieldView.getMandateFieldClassView().getClassName(), mandateFieldViewList);
                    logger.info("mandateFieldView {}", mandateFieldView);
                }
            }
        }
        actionValidationResult = new ActionValidationResult();
        List<MandateFieldMessageView> mandateFieldMessageViewList = new ArrayList<MandateFieldMessageView>();
        actionValidationResult.setMandateFieldMessageViewList(mandateFieldMessageViewList);
        return mandateFieldViewMap.size();
    }

    public void validate(Object object, Class parameterizedClass){
        logger.debug("-- begin valide(object:{}, paramenterizedName:{}", object, parameterizedClass.getName());
        String parameterizedName = parameterizedClass.getName();

        if(object != null){
            Class objectClass = object.getClass();

            logger.info("Object Class Name {}", objectClass.getName());
            if(objectClass.getName().equals(ArrayList.class.getName())){
                if(parameterizedName != null){
                    MandateFieldView mandateFieldView = listMandateFieldViewMap.get(parameterizedName);
                    if(mandateFieldView != null) {
                        if(mandateFieldView.getMandateFieldType() == MandateFieldType.FIELD_TYPE){
                            addMandateFieldMessageView(validateField(object, objectClass, mandateFieldView));
                        } else if(mandateFieldView.getMandateFieldType() == MandateFieldType.METHOD_TYPE) {
                            addMandateFieldMessageView(validateMethod(object, objectClass, mandateFieldView));
                        }
                    }
                    ArrayList arrayList = (ArrayList<?>)object;
                    if(arrayList != null && arrayList.size() > 0){
                        for(Object objectInArray : arrayList){
                            logger.info("objectInArray is a class of {}", objectInArray.getClass().getName());
                            validate(objectInArray);
                        }
                    }
                }
            } else {
                validate(object);
            }
        }
        logger.debug("-- end validate()");
    }

    public void validate(Object object){
        logger.info("-- begin validate object: {}", object);
        validationResultMap = new HashMap<Long, ValidationResult>();
        dependedValidationMap = new HashMap<Long, ValidationResult>();
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
            for(MandateFieldView mandateFieldView : mandateFieldViewList){
                ValidationResult validationResult = validateField(object, objectClass, mandateFieldView);
                if(validationResult != null){
                    validationResultMap.put(mandateFieldView.getId(), validationResult);
                }
            }
            //Consolidate Condition Validation;
            checkCondition(object);
        } else {
            logger.info("No validation configure for class {}", _compareClassName);
        }
        logger.debug("-- end validate");
    }

    private ValidationResult validateField(Object object, Class objectClass, MandateFieldView mandateFieldView){
        logger.debug("-- begin validateField object: {}, Class: {}, MandateFieldView: {}", object, objectClass.getName(), mandateFieldView);
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
            if(field.getName().startsWith(mandateFieldView.getFieldName())){
                logger.info("found name matched field: {}, {}", field.getName(), mandateFieldView.getFieldName());

                try{
                    Object _fieldObj = field.get(object);
                    if(_fieldObj == null){
                        return validateNullObject(mandateFieldView);
                    } else {
                        Class _fieldObjClass = _fieldObj.getClass();
                        if(_fieldObjClass.isEnum()){

                        } else if(_fieldObjClass.equals(Integer.class.getName())){
                            return validate(mandateFieldView, (Integer)_fieldObj);
                        } else if(_fieldObjClass.equals(Long.class.getName())){
                            return validate(mandateFieldView, (Long)_fieldObj);
                        } else if(_fieldObjClass.equals(BigDecimal.class.getName())){
                            return validate(mandateFieldView, (BigDecimal)_fieldObj);
                        } else if(_fieldObjClass.equals(Boolean.class.getName())){
                            return validate(mandateFieldView, (Boolean)_fieldObj);
                        } else if(!_fieldObjClass.isPrimitive()){
                            if(_fieldObjClass.getName().equals(ArrayList.class.getName())){
                                return validate(mandateFieldView, (ArrayList<?>)_fieldObj);
                            } else {
                                logger.debug("check for field is not an primitive and expected value");
                                validate(_fieldObj);
                            }
                        }
                    }
                } catch (IllegalAccessException iae){
                    logger.debug("Cannot access Field to validate Field {}", field.getName());
                    return getValidationResult(mandateFieldView, validationMsg.get(ACTION_DATA_REQUIRED, mandateFieldView.getFieldDesc()));
                }
            }
        }
        return null;
    }

    private ValidationResult validateMethod(Object object, Class objectClass, MandateFieldView mandateFieldView){
        logger.debug("-- begin validateMethod object: {}, Class: {}, MandateFieldView: {}", object, objectClass.getName(), mandateFieldView);
        Method method = null;
        try{
            method = objectClass.getDeclaredMethod(mandateFieldView.getFieldName());
        }catch (NoSuchMethodException ex){
            logger.debug("Cannot find method from original class {}", mandateFieldView.getFieldName());
            try{
                if(objectClass.getSuperclass() != null){
                    method = objectClass.getSuperclass().getDeclaredMethod(mandateFieldView.getFieldName());
                }
            } catch (NoSuchMethodException sex){
                logger.debug("Also not found method in super class {}", mandateFieldView.getFieldName());
            }
        }

        if(method != null){
            method.setAccessible(true);
            if(method.getName().startsWith(mandateFieldView.getFieldName())){
                logger.info("found name matched field: {}, {}", method.getName(), mandateFieldView.getFieldName());

                try{
                    Object _methodReturn = method.invoke(object);
                    if(_methodReturn == null){
                        return validateNullObject(mandateFieldView);
                    } else {
                        Class _fieldObjClass = _methodReturn.getClass();
                        if(_fieldObjClass.equals(Integer.class.getName())){
                            return validate(mandateFieldView, (Integer)_methodReturn);
                        } else if(_fieldObjClass.equals(Long.class.getName())){
                            return validate(mandateFieldView, (Long)_methodReturn);
                        } else if(_fieldObjClass.equals(BigDecimal.class.getName())){
                            return validate(mandateFieldView, (BigDecimal)_methodReturn);
                        }
                    }
                } catch (IllegalAccessException iae){
                    logger.debug("Cannot access Field to validate Field {}", method.getName());
                    return getValidationResult(mandateFieldView, validationMsg.get(ACTION_DATA_REQUIRED, mandateFieldView.getFieldDesc()));
                }  catch (IllegalArgumentException iae){
                    logger.debug("Cannot access Field to validate Field {}", method.getName());
                    return getValidationResult(mandateFieldView, validationMsg.get(ACTION_DATA_REQUIRED, mandateFieldView.getFieldDesc()));
                }  catch (InvocationTargetException iae){
                    logger.debug("Cannot access Field to validate Field {}", method.getName());
                    return getValidationResult(mandateFieldView, validationMsg.get(ACTION_DATA_REQUIRED, mandateFieldView.getFieldDesc()));
                }
            }
        }

        return null;
    }

    private void checkCondition(Object object){
        logger.debug("-- begin checkCondition object{}, ");
        conditionResultMap = new HashMap<Long, ConditionResult>();
        dependedConditionMap = new HashMap<Long, MandateFieldCondition>();

        String className = object.getClass().getName();

        MandateFieldClass mandateFieldClass = mandateFieldClassDAO.findByClassName(className);
        List<MandateFieldCondition> mandateFieldConditionList = mandateFieldConditionDAO.findByClass(mandateFieldClass);
        if(mandateFieldConditionList != null && mandateFieldConditionList.size() > 0){
            for(MandateFieldCondition condition : mandateFieldConditionList){
                logger.info("There is condition setting");
                if(conditionResultMap.get(condition.getId()) != null){
                    continue;
                } else {
                    checkCondition(object, condition);
                }
            }

            for(ConditionResult conditionResult : conditionResultMap.values()){
                if(dependedConditionMap.get(conditionResult.id) != null)
                    addMandateFieldMessageView(conditionResult.mandateFieldCondition, conditionResult.message);
            }
        }
        for(ValidationResult validationResult : validationResultMap.values()){
            if(!validationResult.isPass && (dependedValidationMap.get(validationResult.mandateFieldView.getId()) == null)){
                logger.info("add mandate field result {}", validationResult);
                addMandateFieldMessageView(validationResult);
            }
        }
    }

    private boolean checkCondition(Object object, long conditionId){
        MandateFieldCondition condition = mandateFieldConditionDAO.findById(conditionId);
        return checkCondition(object, condition);
    }

    private boolean checkCondition(Object object, MandateFieldCondition condition){
        logger.debug("-- begin checkCondition object:{}, mandateFieldCondition: {}, validationResultMap: {}", object, condition, validationResultMap);

        List<MandateFieldConditionDetail> conditionDetailList = condition.getMandateFieldConditionDetailList();
        boolean isPassCondition = Boolean.TRUE;
        StringBuilder stringBuilder = new StringBuilder();

        boolean dependResult = true;

        if(!condition.getDependType().equals(MandateDependType.NO_DEPENDENCY)){
            ConditionResult conditionResult = conditionResultMap.get(condition.getId());
            if(conditionResult != null){
                if(condition.getDependType() == MandateDependType.DEPEND_TRUE){
                    if(conditionResult.isPass)
                        dependResult = true;
                } else if(condition.getDependType() == MandateDependType.DEPEND_FALSE){
                    if(!conditionResult.isPass)
                        dependResult = true;
                }
            } else {
                checkCondition(object, condition.getDependCondition());
            }
        }

        if(dependResult) {
            if(condition.getMandateConditionType().equals(MandateConditionType.AND)){
                for(MandateFieldConditionDetail conditionDetail : conditionDetailList){
                    ValidationResult validationResult = validationResultMap.get(conditionDetail.getMandateField().getId());
                    if(!validationResult.isPass)
                        isPassCondition = Boolean.FALSE;
                    if(stringBuilder.length() > 0){
                        stringBuilder.append(" ").append(MandateConditionType.AND).append(" ").append(validationResult.message);
                    }
                    dependedValidationMap.put(validationResult.mandateFieldView.getId(), validationResult);
                }
            } else if(condition.getMandateConditionType().equals(MandateConditionType.OR)){
                isPassCondition = Boolean.FALSE;
                for(MandateFieldConditionDetail conditionDetail : conditionDetailList){
                    ValidationResult validationResult = validationResultMap.get(conditionDetail.getMandateField().getId());
                    if(validationResult.isPass)
                        isPassCondition = Boolean.TRUE;
                    if(stringBuilder.length() > 0){
                        stringBuilder.append(" ").append(MandateConditionType.AND).append(" ").append(validationResult.message);
                    }
                    dependedValidationMap.put(validationResult.mandateFieldView.getId(), validationResult);
                }
            } else if(condition.getMandateConditionType().equals(MandateConditionType.BASE)){
                for(MandateFieldConditionDetail conditionDetail : conditionDetailList){
                    MandateField mandateField = conditionDetail.getMandateField();

                    if(conditionDetail.getBaseValue() == null){
                        ValidationResult validationResult = validationResultMap.get(mandateField.getId());
                        if(validationResult != null) {
                            isPassCondition = validationResult.isPass;
                            stringBuilder.append(validationResult.message);
                            dependedValidationMap.put(validationResult.mandateFieldView.getId(), validationResult);
                        }
                    } else {
                        if(object != null) {
                            try{
                                Field field = object.getClass().getField(mandateField.getFieldName());
                                field.setAccessible(true);
                                Object _fieldObj = field.get(object);
                                if(_fieldObj == null){
                                    isPassCondition = Boolean.FALSE;
                                    stringBuilder.append(validationMsg.get(ACTION_DATA_REQUIRED, mandateField.getFieldName()));
                                } else {

                                    if(Integer.class.getName().equals(_fieldObj.getClass().getName())){
                                        if((Integer)_fieldObj != Integer.parseInt(conditionDetail.getBaseValue())){
                                            isPassCondition = Boolean.TRUE;
                                        } else {
                                            isPassCondition = Boolean.FALSE;
                                        }
                                    } else if(Long.class.getName().equals(_fieldObj.getClass().getName())){
                                        if((Long)_fieldObj != Long.parseLong(conditionDetail.getBaseValue())){
                                            isPassCondition = Boolean.TRUE;
                                        } else {
                                            isPassCondition = Boolean.FALSE;
                                        }
                                    } else if(BigDecimal.class.getName().equals(_fieldObj.getClass().getName())){
                                        BigDecimal baseValue = new BigDecimal(conditionDetail.getBaseValue());
                                        if(baseValue.compareTo((BigDecimal)_fieldObj) == 0){
                                            isPassCondition = Boolean.TRUE;
                                        } else {
                                            isPassCondition = Boolean.FALSE;
                                        }

                                    } else if(Boolean.class.getName().equals(_fieldObj.getClass().getName())){
                                        Boolean baseValue = Boolean.valueOf(conditionDetail.getBaseValue());
                                        if(_fieldObj.equals(baseValue)){
                                            isPassCondition = Boolean.TRUE;
                                        } else {
                                            isPassCondition = Boolean.FALSE;
                                        }
                                    } else {
                                        if(!_fieldObj.getClass().isPrimitive()){
                                            Field idField = _fieldObj.getClass().getField("id");
                                            idField.setAccessible(true);
                                            Object idObject = idField.get(_fieldObj);
                                            if((Long)idObject == Long.parseLong(conditionDetail.getBaseValue())){
                                                isPassCondition = true;
                                            } else {
                                                isPassCondition = Boolean.FALSE;
                                            }
                                        }
                                    }
                                    if(!isPassCondition){
                                        stringBuilder.append(validationMsg.get(CONDITION_BASE, condition.getConditionDesc()));
                                    }
                                }
                            } catch (NoSuchFieldException nse){
                                stringBuilder.append(validationMsg.get(ACTION_DATA_REQUIRED, mandateField.getFieldName()));
                                logger.debug("cannot find field {}", mandateField.getFieldName());
                            } catch (IllegalAccessException nse){
                                isPassCondition = Boolean.FALSE;
                                stringBuilder.append(validationMsg.get(ACTION_DATA_REQUIRED, mandateField.getFieldName()));
                                logger.debug("cannot access field {}", mandateField.getFieldName());
                            }
                        }

                    }
                }
            }
        }

        ConditionResult conditionResult = new ConditionResult();
        conditionResult.isPass = isPassCondition;
        conditionResult.id = condition.getId();
        conditionResult.message = stringBuilder.toString();
        conditionResult.mandateFieldCondition = condition;
        conditionResultMap.put(conditionResult.id, conditionResult);
        return true;
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
        logger.info("validate isPassMin:{}, isPassMax: {}, isPassMatched: {}, isPassNotMatched: {}", isPassMin, isPassMax, isPassMatched, isPassNotMatched);
        return getValidationResult(mandateFieldView, isPassMin, isPassMax, isPassMatched, isPassNotMatched);
    }

    private ValidationResult validate(MandateFieldView mandateFieldView, Long _fieldObj){
        logger.info("validate mandateFieldView:{}, Long: {}", mandateFieldView, _fieldObj);
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
        logger.info("validate isPassMin:{}, isPassMax: {}, isPassMatched: {}, isPassNotMatched: {}", isPassMin, isPassMax, isPassMatched, isPassNotMatched);
        return getValidationResult(mandateFieldView, isPassMin, isPassMax, isPassMatched, isPassNotMatched);
    }

    private ValidationResult validate(MandateFieldView mandateFieldView, BigDecimal _fieldObj){
        logger.info("validate mandateFieldView:{}, BigDecimal: {}", mandateFieldView, _fieldObj);
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

    private ValidationResult validate(MandateFieldView mandateFieldView, Boolean _fieldObj){
        logger.info("validateNullObject mandateFieldView:{}, Long: {}", mandateFieldView, _fieldObj);
        Boolean isPassMin = null, isPassMax = null, isPassMatched = null, isPassNotMatched = null;
        if(!Util.isEmpty(mandateFieldView.getMatchedValue())){
            if(mandateFieldView.getMatchedValue().equals(UserSysParameterKey.STATIC_EMPTY.key())){
                isPassMatched = (_fieldObj == null);
            } else {
                isPassMatched = (_fieldObj == Boolean.parseBoolean(mandateFieldView.getMatchedValue()));
            }
        }
        if(!Util.isEmpty(mandateFieldView.getNotMatchedValue())){
            if(mandateFieldView.getNotMatchedValue().equals(UserSysParameterKey.STATIC_EMPTY.key())){
                isPassNotMatched = !(_fieldObj == null);
            } else {
                isPassNotMatched = !(_fieldObj == Boolean.parseBoolean(mandateFieldView.getNotMatchedValue()));
            }
        }
        logger.info("validate isPassMin:{}, isPassMax: {}, isPassMatched: {}, isPassNotMatched: {}", isPassMin, isPassMax, isPassMatched, isPassNotMatched);
        return getValidationResult(mandateFieldView, isPassMin, isPassMax, isPassMatched, isPassNotMatched);
    }

    private ValidationResult validate(MandateFieldView mandateFieldView, ArrayList<?> _fieldObj){
        logger.info("validateNullObject mandateFieldView:{}, Long: {}", mandateFieldView, _fieldObj);
        Boolean isPassMin = null, isPassMax = null, isPassMatched = null, isPassNotMatched = null;
        if(!Util.isEmpty(mandateFieldView.getMatchedValue())){
            if(mandateFieldView.getMatchedValue().equals(UserSysParameterKey.STATIC_EMPTY.key())){
                isPassMatched = (_fieldObj == null);
            }
        }
        if(!Util.isEmpty(mandateFieldView.getNotMatchedValue())){
            if(mandateFieldView.getNotMatchedValue().equals(UserSysParameterKey.STATIC_EMPTY.key())){
                if(_fieldObj != null && _fieldObj.size() > 0){
                    isPassMatched = true;
                } else {
                    isPassNotMatched = false;
                }
            }
        }
        logger.info("validate isPassMin:{}, isPassMax: {}, isPassMatched: {}, isPassNotMatched: {}", isPassMin, isPassMax, isPassMatched, isPassNotMatched);
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
        mandateFieldMessageView.setFieldName("Condition " + condition.getMandateConditionType().name());
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

    private class ConditionResult{
        Long id = null;
        Boolean isPass = null;
        String message = null;
        MandateFieldCondition mandateFieldCondition = null;

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                    .append("isPass", isPass)
                    .append("message", message)
                    .toString();
        }
    }


}
