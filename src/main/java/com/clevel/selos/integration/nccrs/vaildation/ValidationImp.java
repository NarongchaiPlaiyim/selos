package com.clevel.selos.integration.nccrs.vaildation;


import com.clevel.selos.exception.ValidationException;
import com.clevel.selos.integration.nccrs.service.NCCRSModel;
import com.clevel.selos.integration.ncrs.models.request.TUEFEnquiryIdModel;
import com.clevel.selos.integration.ncrs.models.request.TUEFEnquiryNameModel;
import com.clevel.selos.integration.ncrs.service.NCRSModel;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.ValidationUtil;

import javax.inject.Inject;
import java.io.Serializable;

public class ValidationImp implements Validation, Serializable {
    @Inject
    @ValidationMessage
    Message message;

    @Inject
    public ValidationImp() {
    }

    @Override
    public void validation(NCCRSModel model) throws Exception {
        if (null==model){
            throw new ValidationException(message.get("101"));
        }
        String registType = null;
        if(!ValidationUtil.isValueInRange(7,7,model.getRegistType())){
            //7 character only
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
                //Values are 1140000-1140005
                /**
                 * 11400001 Company limited
                 * 11400002 Limited partnership
                 * 11400003 Registered ordinary partnership
                 * 11400004 Public company limited
                 * 11400005 Foreign registration id or others
                 */
            }
        }

        if (!ValidationUtil.isValueInRange(1, 90 ,model.getCompanyName())){
            //Company
        }

        String inqPurose = null;
        if(!ValidationUtil.isValueInRange(7,7,model.getInqPurose())){
            //7 character only
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
                //Values are 1170001 New credit approve - 1170002 Credit review
            }
        }

        String productType = null;
        if(!ValidationUtil.isValueInRange(7,7,model.getProductType())){
            //7 character only
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
                //Value is 2030001 Credit Report - 2030007 Credit Report and Scoring
            }
        }

        if(!ValidationUtil.isValueInRange(1,20,model.getMemberRef())){
            //1-20 character
        }

        if(!ValidationUtil.isValueInRange(1,1,model.getConfirmConsent())){
            //Y
        } else {
            model.setConfirmConsent("Y");
        }


        String language = null;
        if(!ValidationUtil.isValueInRange(7,7,model.getLanguage())){
            //7 character only
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
                //Value is 2060001 ENG - 2060002 THAI
            }
        }

        if(!ValidationUtil.isValueInRange(1,1,model.getHistoricalBalanceReport())){
            //Y
        } else {
            model.setHistoricalBalanceReport("Y");
        }

    }
}
