package com.clevel.selos.integration.ncb.vaildation;


import com.clevel.selos.exception.ValidationException;
import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.NCCRSInputModel;
import com.clevel.selos.integration.ncb.ncrs.models.request.TUEFEnquiryIdModel;
import com.clevel.selos.integration.ncb.ncrs.models.request.TUEFEnquiryNameModel;
import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.NCRSModel;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.ValidationUtil;

import javax.inject.Inject;
import java.io.Serializable;

public class ValidationImp implements /*Validation, */Serializable {
    @Inject
    @ValidationMessage
    Message message;

    @Inject
    public ValidationImp() {
    }
/*
    @Override
    public void validation(NCRSModel model) throws Exception {
        if (null==model){
            throw new ValidationException(message.get("101"));
        }
        if(ValidationUtil.isNull(model.getMemberref()) && ValidationUtil.isGreaterThan(25, model.getMemberref())){
            throw new ValidationException("Length of memberref is more than 25");
        }

        String enqPurpose = null;
        if(!ValidationUtil.isValueInRange(2,2,model.getEnqpurpose())){
            throw new ValidationException(message.get("104"));
        } else {
            enqPurpose = model.getEnqpurpose();
            boolean flag = true;
            for (int i=1;i<=2;i++) {
                if (!enqPurpose.equals("0" + i)) {
                    continue;
                } else {
                    flag = false;
                    return;
                }
            }
            if(flag){
                throw new ValidationException(message.get("105"));
                //Values are 01 new application or 02 review credit
            }
        }

        //if(!ValidationUtil.isNull(enqamount) && ValidationUtil.isGreaterThan(9, enqamount))throw new ValidationException("Length of enqamount is more than 9");

        if(!"Y".equals(model.getEnqpurpose())){
            throw new ValidationException(message.get("107"));
        }

//        if(!ValidationUtil.isValueInRange(1, 2, model.getNameList())){
//            //Name can't be more than 2 names
//        } else {
//            ArrayList<TUEFEnquiryNameModel> arrayList = model.getNameList();
//            for(TUEFEnquiryNameModel nameModel : arrayList){
//                validation(nameModel);
//            }
//        }
//
//        if(!ValidationUtil.isValueInRange(1, 4, model.getIdList())){
//            //ID can't be more than 4 ids
//        } else {
//            ArrayList<TUEFEnquiryIdModel> arrayList = model.getIdList();
//            for(TUEFEnquiryIdModel idModel : arrayList){
//                validation(idModel);
//            }
//        }



    }

    @Override
    public void validation(TUEFEnquiryNameModel model) throws Exception {

        if(!ValidationUtil.isValueInRange(1,50,model.getFamilyname())){
            //Two character minimum
        }
        if(!ValidationUtil.isValueInRange(1,30,model.getFirstname())){
            //Two character minimum
        }
        if(!ValidationUtil.isNull(model.getMiddlename()) && ValidationUtil.isGreaterThan(26, model.getMiddlename())){
            // can null but if not null length of middlename <= 26
        }
        if(!ValidationUtil.isValueInRange(8,8,model.getDateofbirth())||!ValidationUtil.isInteger(model.getDateofbirth())){
            //
        }

    }

    @Override
    public void validation(TUEFEnquiryIdModel model) throws Exception {
        String idType = null;
        if(!ValidationUtil.isValueInRange(2,2,model.getIdtype())){
            //Two character minimum
        } else {
            idType = model.getIdtype();
            boolean flag = true;
            for (int i=0;i<=9;i++) {
                if (!idType.equals("0" + i)) {
                    continue;
                } else {
                    flag = false;
                    return;
                }
            }
            if(flag){
                //Values are 01-09
            }
        }
        if("01".equals(idType)){
            model.setIssuecountry("TH");
        } else {
            model.setIssuecountry("");
        }

        if(!ValidationUtil.isNull(model.getIdnumber()) && ValidationUtil.isGreaterThan(20, model.getIdnumber())){
            //not null and 20 character
        }
        if("01".equals(idType)&&!ValidationUtil.isValueInRange(13,13,model.getIdnumber())){
            //The length of citizen id is 13
        }

    }

    @Override
    public void validation(NCCRSInputModel model) throws Exception {
        if (null==model){
            throw new ValidationException(message.get("101"));
        }
        String registType = null;
        if(!ValidationUtil.isValueInRange(7,7,model.getRegistType())){
            throw new ValidationException(message.get("151"));
        } else {
            registType = model.getRegistType();
            boolean flag = true;
            for (int i=1;i<=5;i++) {
                if (!registType.equals("114000" + i)) {
                    continue;
                } else {
                    flag = false;
                    return;
                }
            }
            if(flag){
                throw new ValidationException(message.get("152"));

                 * 11400001 Company limited
                 * 11400002 Limited partnership
                 * 11400003 Registered ordinary partnership
                 * 11400004 Public company limited
                 * 11400005 Foreign registration id or others

            }
        }

        if (!ValidationUtil.isValueInRange(1, 90 ,model.getCompanyName())){
            //Company
        }

        String inqPurose = null;
        if(!ValidationUtil.isValueInRange(7,7,model.getInqPurose())){
            throw new ValidationException(message.get("151"));
        } else {
            inqPurose = model.getInqPurose();
            boolean flag = true;
            for (int i=1;i<=2;i++) {
                if (!inqPurose.equals("117000" + i)) {
                    continue;
                } else {
                    flag = false;
                    return;
                }
            }
            if(flag){
                throw new ValidationException(message.get("153"));
                //Values are 1170001 New credit approve - 1170002 Credit review
            }
        }

        String productType = null;
        if(!ValidationUtil.isValueInRange(7,7,model.getProductType())){
            throw new ValidationException(message.get("151"));
        } else {
            productType = model.getProductType();
            boolean flag = true;
            for (int i=1;i<=7;i=7) {
                if (!productType.equals("203000" + i)) {
                    continue;
                } else {
                    flag = false;
                    return;
                }
            }
            if(flag){
                throw new ValidationException(message.get("154"));
                //Value is 2030001 Credit Report - 2030007 Credit Report and Scoring
            }
        }

        if(!ValidationUtil.isValueInRange(1,20,model.getMemberRef())){
            throw new ValidationException(message.get("155"));
            //1-20 character
        }

        if(!ValidationUtil.isValueInRange(1,1,model.getConfirmConsent())){
            throw new ValidationException(message.get("156"));
            //Y
        } else {
            model.setConfirmConsent("Y");
        }


        String language = null;
        if(!ValidationUtil.isValueInRange(7,7,model.getLanguage())){
            throw new ValidationException(message.get("151"));
        } else {
            language = model.getLanguage();
            boolean flag = true;
            for (int i=1;i<=2;i++) {
                if (!language.equals("206000" + i)) {
                    continue;
                } else {
                    flag = false;
                    return;
                }
            }
            if(flag){
                throw new ValidationException(message.get("157"));
                //Value is 2060001 ENG - 2060002 THAI
            }
        }

        if(!ValidationUtil.isValueInRange(1,1,model.getHistoricalBalanceReport())){
            throw new ValidationException(message.get("156"));
            //Y
        } else {
            model.setHistoricalBalanceReport("Y");
        }

    }  */
}
