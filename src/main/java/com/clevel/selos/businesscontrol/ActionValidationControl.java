package com.clevel.selos.businesscontrol;

import com.clevel.selos.businesscontrol.master.MandateFieldControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.model.view.master.*;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.transform.master.MandateFieldTransform;
import com.clevel.selos.util.Util;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.lang.reflect.Field;
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
    MandateFieldClassStepActionDAO mandateFieldClassStepActionDAO;

    @Inject
    MandateFieldControl mandateFieldControl;

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
    private Map<String, MandateFieldClassView> listMandateClassViewMap;
    private MandateFieldValidationResult mandateFieldValidationResult;

    private Map<Long, ConditionResult> conditionResultMap;
    private Map<Long, ConditionResult> dependedConditionMap;

    private Map<Long, ValidationResult> validationResultMap;
    private Map<Long, ValidationResult> dependedValidationMap;

    public int loadActionValidation(long stepId, long actionId){
        logger.info("-- begin loadActionValidation stepId {}: actionId {}", stepId, actionId);
        mandateFieldViewMap = new HashMap<String, List<MandateFieldView>>();
        listMandateFieldViewMap = new HashMap<String, MandateFieldView>();
        listMandateClassViewMap = new HashMap<String, MandateFieldClassView>();
        List<MandateFieldStepAction> mandateFieldStepActionList = mandateFieldStepActionDAO.findByAction(stepId, actionId);
        logger.info("-- update Mandate Field {}", mandateFieldStepActionList);

        for(MandateFieldStepAction mandateFieldStepAction : mandateFieldStepActionList){
            MandateField mandateField = mandateFieldStepAction.getMandateField();
            if(mandateField != null){
                if(ArrayList.class.getName().equals(mandateField.getMandateFieldClass().getClassName())){
                    listMandateFieldViewMap.put(mandateField.getFieldName(), mandateFieldTransform.transformToView(mandateField));
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

        List<MandateFieldClassStepAction> classStepActionList = mandateFieldClassStepActionDAO.findByStepAction(stepId, actionId);
        for(MandateFieldClassStepAction classStepAction : classStepActionList){
            listMandateClassViewMap.put(classStepAction.getMandateFieldClass().getClassName(), mandateFieldTransform.transformToView(classStepAction.getMandateFieldClass()));
        }
        mandateFieldValidationResult = new MandateFieldValidationResult();
        List<MandateFieldMessageView> mandateFieldMessageViewList = new ArrayList<MandateFieldMessageView>();
        mandateFieldValidationResult.setMandateFieldMessageViewList(mandateFieldMessageViewList);
        return mandateFieldViewMap.size();
    }

    public void validate(Object object, Class parameterizedClass){
        logger.info("-- begin valide(object:{}, paramenterizedName:{}", object, parameterizedClass.getName());
        String parameterizedName = parameterizedClass.getName();

        if(parameterizedName != null){
            //MandateFieldView mandateFieldView = listMandateFieldViewMap.get(parameterizedName);
            MandateFieldClassView mandateFieldClassView = listMandateClassViewMap.get(parameterizedName);
            if(mandateFieldClassView != null){
                if(object == null){
                    addMandateFieldMessageView(mandateFieldClassView, validationMsg.get(ACTION_DATA_REQUIRED, mandateFieldClassView.getClassDescription()));
                    return;
                } else {
                    Class objectClass = object.getClass();
                    if(objectClass.getName().equals(ArrayList.class.getName())){
                        ArrayList arrayList = (ArrayList<?>)object;
                        if(arrayList.size() == 0){
                            addMandateFieldMessageView(mandateFieldClassView, validationMsg.get(ACTION_DATA_REQUIRED, mandateFieldClassView.getClassDescription()));
                        }
                    }

                }
            }
        }

        if(object != null){
            Class objectClass = object.getClass();
            logger.info("Object Class Name {}", objectClass.getName());
            if(objectClass.getName().equals(ArrayList.class.getName())){
                ArrayList arrayList = (ArrayList<?>)object;
                if(arrayList != null && arrayList.size() > 0){
                    for(Object objectInArray : arrayList){
                        logger.info("objectInArray is a class of {}", objectInArray.getClass().getName());
                        validationResultMap = new HashMap<Long, ValidationResult>();
                        dependedValidationMap = new HashMap<Long, ValidationResult>();

                        conditionResultMap = new HashMap<Long, ConditionResult>();
                        dependedConditionMap = new HashMap<Long, ConditionResult>();

                        validate(objectInArray);
                    }
                }
            } else {
                validationResultMap = new HashMap<Long, ValidationResult>();
                dependedValidationMap = new HashMap<Long, ValidationResult>();

                conditionResultMap = new HashMap<Long, ConditionResult>();
                dependedConditionMap = new HashMap<Long, ConditionResult>();

                validate(object);
            }
        }
        logger.info("-- end validate()");
    }

    private void validate(Object object){
        logger.info("-- begin validate object: {}", object);
        if(object != null){
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
        }
        logger.info("-- end validate");
    }

    private ValidationResult validateField(Object object, Class objectClass, MandateFieldView mandateFieldView){
        logger.info("-- begin validateField object: {}, Class: {}, MandateFieldView: {}", object, objectClass.getName(), mandateFieldView);
        Field field = null;
        try{
            field = objectClass.getDeclaredField(mandateFieldView.getFieldName());
        }catch (NoSuchFieldException ex){
            logger.info("Cannot find field from original class {}", mandateFieldView.getFieldName());
            try{
                if(objectClass.getSuperclass() != null){
                    field = objectClass.getSuperclass().getDeclaredField(mandateFieldView.getFieldName());
                }
            } catch (NoSuchFieldException sex){
                logger.info("Also not found field in super class {}", mandateFieldView.getFieldName());
            }
        }
        try{
            if(field != null) {
                field.setAccessible(true);
                Object _fieldObj = field.get(object);

                if(field.getName().startsWith(mandateFieldView.getFieldName())){
                logger.info("found name matched field: {}, {}", field.getName(), mandateFieldView.getFieldName());

                    if(_fieldObj == null){
                        return validateNullObject(mandateFieldView);
                    } else {
                        Class _fieldObjClass = _fieldObj.getClass();
                        logger.debug("__fieldObjClass: {}", _fieldObj.getClass());
                        if(_fieldObjClass.isEnum()){
                            logger.info("----- object is enum -------", _fieldObj.getClass());
                        } else if(_fieldObjClass.equals(Integer.class.getName())){
                            return validate(mandateFieldView, (Integer)_fieldObj);
                        } else if(_fieldObjClass.equals(Long.class.getName())){
                            return validate(mandateFieldView, (Long)_fieldObj);
                        } else if(_fieldObjClass.equals(Number.class.getName())){
                                return validate(mandateFieldView, (Long)_fieldObj);
                        } else if(_fieldObjClass.equals(BigDecimal.class.getName())){
                            return validate(mandateFieldView, (BigDecimal)_fieldObj);
                        } else if(_fieldObjClass.equals(Boolean.class.getName())){
                            return validate(mandateFieldView, (Boolean)_fieldObj);
                        } else if(_fieldObjClass.equals(String.class.getName())){
                            return validate(mandateFieldView, (String)_fieldObj);
                        } else if(!_fieldObjClass.isPrimitive()){
                            if(_fieldObjClass.getName().equals(ArrayList.class.getName())){
                                return validate(mandateFieldView, (ArrayList<?>)_fieldObj);
                            } else {
                                if(_fieldObjClass.getName().contains(".master.")){
                                    logger.info("check for field which is a master table");
                                    validateMaster(mandateFieldView, _fieldObj);
                                } else {
                                    logger.info("check for field is not an primitive and expected value");
                                    validate(_fieldObj);
                                }
                            }
                        }
                    }
                } else if(field.getType().getName().contains("db.working.")){
                    logger.info("check for field is not an primitive and expected value");
                    validate(_fieldObj);
                }
            }
        } catch (IllegalAccessException iae){
            logger.info("Cannot access Field to validate Field {}", field.getName());
            return getValidationResult(mandateFieldView, validationMsg.get(ACTION_DATA_REQUIRED, mandateFieldView.getFieldDesc()));
        }
        return null;
    }

    private void checkCondition(Object object){
        logger.info("-- begin checkCondition object: {}, ", object);

        String className = object.getClass().getName();

        MandateFieldClassView mandateFieldClass = mandateFieldTransform.transformToView(mandateFieldClassDAO.findByClassName(className));
        List<MandateFieldConditionView> mandateFieldConditionViewList = mandateFieldControl.getMandateConditionList(mandateFieldClass);

        if(mandateFieldConditionViewList != null && mandateFieldConditionViewList.size() > 0){
            for(MandateFieldConditionView conditionView : mandateFieldConditionViewList){
                checkConditionAndDependCondition(object, conditionView);
            }

            List<ConditionResult> _failedConResultList = new ArrayList<ConditionResult>();
            for(ConditionResult conditionResult : conditionResultMap.values()){
                if(combineConditionResult(conditionResult)){
                    _failedConResultList.add(conditionResult);
                }
            }

            for(ConditionResult _failedConditionResult : _failedConResultList) {
                if(dependedConditionMap.get(_failedConditionResult.id) == null)
                    addMandateFieldMessageView(_failedConditionResult.conditionView, _failedConditionResult.message);
            }
        }
        for(ValidationResult validationResult : validationResultMap.values()){
            if(!validationResult.isPass && (dependedValidationMap.get(validationResult.mandateFieldView.getId()) == null)){
                logger.info("add mandate field result {}", validationResult);
                addMandateFieldMessageView(validationResult);
            }
        }
    }

    private void checkConditionAndDependCondition(Object object, MandateFieldConditionView conditionView){
        logger.info("-- begin checkConditionAndDependCondition: object: {}", object);
        logger.info("-- MandateFieldConditionView: {}", conditionView);

        checkCondition(object, conditionView);

        if(conditionView.getDependConType() == MandateDependConType.EXTERNAL){
            logger.info("depend Condition is External Class: {}", conditionView.getDependConType());
            MandateFieldConditionView externalDependCondition = conditionView.getDependCondition();
            MandateFieldClassView extDependConClassView = externalDependCondition.getMandateFieldClassView();
            try{
                Class objectClazz = object.getClass();
                Field[] fields = objectClazz.getFields();
                for(Field field : fields){
                    field.setAccessible(true);
                    logger.info("Field.getType: {}", field.getType().getName());
                    if(field.getType().getName().equals(extDependConClassView.getClassName())){
                        Object dependObject = field.get(object);
                        logger.info("Loop for checking depend condition: {}", object);
                        checkConditionAndDependCondition(dependObject, externalDependCondition);
                    }
                }
            } catch (IllegalAccessException iaex){
                ConditionResult conditionResult = new ConditionResult();
                conditionResult.isPass = false;
                conditionResult.id = conditionView.getId();
                conditionResult.message = "cannot access field";
                conditionResult.conditionView = conditionView;
                conditionResultMap.put(conditionResult.id, conditionResult);
            }
        }
    }

    private boolean combineConditionResult(ConditionResult conditionResult){
        logger.info("-- begin combineConditionResult :: {}", conditionResult);

        MandateFieldConditionView conditionView = conditionResult.conditionView;

        if(conditionView.getDependType().equals(MandateDependType.DEPEND_TRUE)){
            ConditionResult dependResult = conditionResultMap.get(conditionView.getDependCondition().getId());
            dependedConditionMap.put(dependResult.id, dependResult);
            if(combineConditionResult(dependResult)){
                return conditionResult.isPass;
            } else {
                //When DEPEND_TRUE the condition will not be checked.
                return true;
            }
        } else if(conditionView.getDependType().equals(MandateDependType.DEPEND_FALSE)){
            ConditionResult dependResult = conditionResultMap.get(conditionView.getDependCondition().getId());
            dependedConditionMap.put(dependResult.id, dependResult);
            if(!combineConditionResult(dependResult)){
                return conditionResult.isPass;
            } else {
                return true;
            }
        } else {
            return conditionResult.isPass;
        }
    }

    private boolean checkCondition(Object object, MandateFieldConditionView conditionView){
        logger.info("-- begin checkCondition object:{}, mandateFieldCondition: {}", object, conditionView);
        logger.info("with mandateFieldCondition: {}", conditionView);
        logger.info("current validation result {}", validationResultMap);
        logger.info("current condition result {}", conditionResultMap);

        List<MandateFieldConditionDetailView> conditionDetailViewList = conditionView.getConditionDetailViewList();
        boolean isPassCondition = Boolean.TRUE;
        StringBuilder stringBuilder = new StringBuilder();

        if(conditionView.getMandateConditionType().equals(MandateConditionType.AND)){
            for(MandateFieldConditionDetailView conditionDetailView : conditionDetailViewList){
                ValidationResult validationResult = validationResultMap.get(conditionDetailView.getMandateFieldView().getId());
                if(!validationResult.isPass)
                    isPassCondition = Boolean.FALSE;
                if(stringBuilder.length() > 0){
                    stringBuilder.append(" ").append(MandateConditionType.AND).append(" ").append(validationResult.message);
                } else {
                    stringBuilder.append(validationResult.message);
                }
                dependedValidationMap.put(validationResult.mandateFieldView.getId(), validationResult);
            }
        } else if(conditionView.getMandateConditionType().equals(MandateConditionType.OR)){
            isPassCondition = Boolean.FALSE;
            for(MandateFieldConditionDetailView conditionDetailView : conditionDetailViewList){
                ValidationResult validationResult = validationResultMap.get(conditionDetailView.getMandateFieldView().getId());
                if(validationResult.isPass)
                    isPassCondition = Boolean.TRUE;
                if(stringBuilder.length() > 0){
                    stringBuilder.append(" ").append(MandateConditionType.OR).append(" ").append(validationResult.message);
                } else {
                    stringBuilder.append(validationResult.message);
                }
                dependedValidationMap.put(validationResult.mandateFieldView.getId(), validationResult);
            }
        } else if(conditionView.getMandateConditionType().equals(MandateConditionType.BASE)){
            for(MandateFieldConditionDetailView conditionDetailView : conditionDetailViewList){
                MandateFieldView mandateFieldView = conditionDetailView.getMandateFieldView();
                if(conditionDetailView.getBaseValue() == null){
                    ValidationResult validationResult = validationResultMap.get(mandateFieldView.getId());
                    if(validationResult != null) {
                        isPassCondition = validationResult.isPass;
                        stringBuilder.append(validationResult.message);
                        dependedValidationMap.put(validationResult.mandateFieldView.getId(), validationResult);
                    }
                } else {
                    if(object != null) {

                        try{
                            Field field = object.getClass().getField(mandateFieldView.getFieldName());
                            field.setAccessible(true);
                            Object _fieldObj = field.get(object);
                            if(_fieldObj == null){
                                isPassCondition = Boolean.FALSE;
                                stringBuilder.append(validationMsg.get(ACTION_DATA_REQUIRED, mandateFieldView.getFieldName()));
                            } else {
                                logger.info("check condition value: {}", _fieldObj);
                                if(conditionDetailView.getBaseValue() != null && conditionDetailView.getBaseValue().contains(":")){
                                    isPassCondition = isContainValue(conditionDetailView.getBaseValue(), _fieldObj);
                                } else {
                                    if(Integer.class.getName().equals(_fieldObj.getClass().getName())){
                                        if((Integer)_fieldObj != Integer.parseInt(conditionDetailView.getBaseValue())){
                                            isPassCondition = Boolean.TRUE;
                                        } else {
                                            isPassCondition = Boolean.FALSE;
                                        }
                                    } else if(Long.class.getName().equals(_fieldObj.getClass().getName())){
                                        if((Long)_fieldObj != Long.parseLong(conditionDetailView.getBaseValue())){
                                            isPassCondition = Boolean.TRUE;
                                        } else {
                                            isPassCondition = Boolean.FALSE;
                                        }
                                    } else if(BigDecimal.class.getName().equals(_fieldObj.getClass().getName())){
                                        BigDecimal baseValue = new BigDecimal(conditionDetailView.getBaseValue());
                                        if(baseValue.compareTo((BigDecimal)_fieldObj) == 0){
                                            isPassCondition = Boolean.TRUE;
                                        } else {
                                            isPassCondition = Boolean.FALSE;
                                        }

                                    } else if(Boolean.class.getName().equals(_fieldObj.getClass().getName())){
                                        Boolean baseValue = Boolean.valueOf(conditionDetailView.getBaseValue());
                                        if(_fieldObj.equals(baseValue)){
                                            isPassCondition = Boolean.TRUE;
                                        } else {
                                            isPassCondition = Boolean.FALSE;
                                        }
                                    }
                                }
                                //Set message for every condition check
                                stringBuilder.append(validationMsg.get(CONDITION_BASE, conditionView.getConditionDesc()));

                            }
                        } catch (NoSuchFieldException nse){
                            stringBuilder.append(validationMsg.get(ACTION_DATA_REQUIRED, mandateFieldView.getFieldName()));
                            logger.info("cannot find field {}", mandateFieldView.getFieldName());
                        } catch (IllegalAccessException nse){
                            isPassCondition = Boolean.FALSE;
                            stringBuilder.append(validationMsg.get(ACTION_DATA_REQUIRED, mandateFieldView.getFieldName()));
                            logger.info("cannot access field {}", mandateFieldView.getFieldName());
                        }
                    }
                }
            }
        }

        ConditionResult conditionResult = new ConditionResult();
        conditionResult.isPass = isPassCondition;
        conditionResult.id = conditionView.getId();
        conditionResult.message = stringBuilder.toString();
        conditionResult.conditionView = conditionView;

        conditionResultMap.put(conditionResult.id, conditionResult);
        logger.info("Condition Result Map: {}", conditionResultMap);
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
                isPassNotMatched = (_fieldObj != null);
            } else {
                isPassNotMatched = (_fieldObj != Boolean.parseBoolean(mandateFieldView.getNotMatchedValue()));
            }
        }
        logger.info("validate isPassMin:{}, isPassMax: {}, isPassMatched: {}, isPassNotMatched: {}", isPassMin, isPassMax, isPassMatched, isPassNotMatched);
        return getValidationResult(mandateFieldView, isPassMin, isPassMax, isPassMatched, isPassNotMatched);
    }

    private ValidationResult validate(MandateFieldView mandateFieldView, String _fieldObj){
        logger.info("validate mandateFieldView:{}, String: {}", mandateFieldView, _fieldObj);
        Boolean isPassMin = null, isPassMax = null, isPassMatched = null, isPassNotMatched = null;
        if(!Util.isEmpty(mandateFieldView.getMinValue())){
            isPassMin = (_fieldObj.compareTo(mandateFieldView.getMinValue()) >= 0);
        }
        if(!Util.isEmpty(mandateFieldView.getMaxValue())){
            isPassMax = (_fieldObj.compareTo(mandateFieldView.getMinValue()) <= 0);
        }
        if(!Util.isEmpty(mandateFieldView.getMatchedValue())){
            if(mandateFieldView.getMatchedValue().equals(UserSysParameterKey.STATIC_EMPTY.key())){
                isPassMatched = (_fieldObj == null || "".equals(_fieldObj));
            } else {
                isPassMatched = (_fieldObj.equals(mandateFieldView.getMatchedValue()));
            }
        }
        if(!Util.isEmpty(mandateFieldView.getNotMatchedValue())){
            if(mandateFieldView.getNotMatchedValue().equals(UserSysParameterKey.STATIC_EMPTY.key())){
                isPassNotMatched = !(_fieldObj == null || "".equals(_fieldObj));
            } else {
                isPassNotMatched = (_fieldObj.equals(mandateFieldView.getMatchedValue()));
            }
        }
        logger.info("validate result:{}, {}, {}, {}", isPassMin, isPassMax, isPassMatched, isPassNotMatched);
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

    private ValidationResult validateMaster(MandateFieldView mandateFieldView, Object _fieldObj){
        logger.info("validateMaster mandateFieldView:{}, Integer: {}", mandateFieldView, _fieldObj);
        Boolean isPassMin = null, isPassMax = null, isPassMatched = null, isPassNotMatched = null;

        if(!Util.isEmpty(mandateFieldView.getMatchedValue())){
            if(mandateFieldView.getMatchedValue().equals(UserSysParameterKey.STATIC_EMPTY.toString())){
                isPassMatched = (_fieldObj == null);
            } else {
                isPassMatched = isContainValue(mandateFieldView.getMatchedValue(), _fieldObj);

            }
        }
        if(!Util.isEmpty(mandateFieldView.getNotMatchedValue())){
            if(mandateFieldView.getNotMatchedValue().equals(UserSysParameterKey.STATIC_EMPTY.key())){
                isPassNotMatched = (_fieldObj != null);
            } else {
                isPassNotMatched = !isContainValue(mandateFieldView.getNotMatchedValue(), _fieldObj);
            }
        }

        logger.info("validateMaster result:{}, {}, {}, {}", isPassMin, isPassMax, isPassMatched, isPassNotMatched);
        return getValidationResult(mandateFieldView, isPassMin, isPassMax, isPassMatched, isPassNotMatched);
    }

    private boolean isContainValue(String _compValue, Object _fieldObj){
        boolean isContain = false;
        if(_compValue != null){
            if(_compValue.contains(":")){
                String[] splits = _compValue.split(":");
                try{
                    Field subField = _fieldObj.getClass().getField(splits[0]);
                    subField.setAccessible(true);
                    Object _subFieldObj = subField.get(_fieldObj);
                    if(_subFieldObj == null){
                        if(_subFieldObj == null || "".equals(_subFieldObj)){
                            isContain = true;
                        }
                    } else {
                        Class _subClass = _subFieldObj.getClass();
                        if(Integer.class.getName().equals(_subClass.getName())){
                            isContain = ((Integer)_subFieldObj == Integer.parseInt(splits[1]));
                        } else if(Long.class.getName().equals(_subClass.getName())) {
                            isContain = ((Long)_subFieldObj == Long.parseLong(splits[1]));
                        } else if(Boolean.class.getName().equals(_subClass.getName())){
                            isContain = ((Boolean)_subFieldObj == Boolean.parseBoolean(splits[1]));
                        } else if(String.class.getName().equals(_subClass.getName())){
                            isContain = (splits[1].equals(_subFieldObj));
                        }
                    }
                }catch (Exception ex){
                    logger.error("Cannot Compare Value", ex);
                }
            }
        }
        return isContain;
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
        mandateFieldValidationResult.getMandateFieldMessageViewList().add(mandateFieldMessageView);
    }

    private void addMandateFieldMessageView(MandateFieldConditionView conditionView, String message){
        MandateFieldMessageView mandateFieldMessageView = new MandateFieldMessageView();
        mandateFieldMessageView.setFieldName("Condition " + conditionView.getMandateConditionType().name());
        mandateFieldMessageView.setFieldDesc(conditionView.getConditionDesc());
        mandateFieldMessageView.setMessage(message);
        mandateFieldValidationResult.getMandateFieldMessageViewList().add(mandateFieldMessageView);
    }

    private void addMandateFieldMessageView(MandateFieldClassView mandateFieldClassView, String message){
        MandateFieldMessageView mandateFieldMessageView = new MandateFieldMessageView();
        mandateFieldMessageView.setFieldName("Class " + mandateFieldClassView.getClassName());
        mandateFieldMessageView.setPageName(mandateFieldClassView.getPageName());
        mandateFieldMessageView.setFieldDesc(mandateFieldClassView.getClassDescription());
        mandateFieldMessageView.setMessage(message);
        mandateFieldValidationResult.getMandateFieldMessageViewList().add(mandateFieldMessageView);
    }

    public MandateFieldValidationResult getFinalValidationResult(){
        if(mandateFieldValidationResult.getMandateFieldMessageViewList().size() > 0){
            mandateFieldValidationResult.setActionResult(ActionResult.FAILED);

        } else {
            mandateFieldValidationResult.setActionResult(ActionResult.SUCCESS);
        }
        return mandateFieldValidationResult;
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
        MandateFieldConditionView conditionView = null;

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                    .append("id", id)
                    .append("isPass", isPass)
                    .append("message", message)
                    .append("conditionView", conditionView)
                    .toString();
        }
    }

}
