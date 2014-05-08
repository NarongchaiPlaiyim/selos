package com.clevel.selos.integration.ncb.vaildation;


import com.clevel.selos.exception.ValidationException;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.NCCRSInputModel;
import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.NCCRSModel;
import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.NCRSInputModel;
import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.NCRSModel;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.ValidationMapping;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.ValidationUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;

public class ValidationImp implements Validation, Serializable {
    @Inject
    @SELOS
    Logger log;

    @Inject
    @ValidationMessage
    Message validationMsg;

    private final String required = ValidationMapping.NCB_DATA_REQUIRED;
    private final String invalid = ValidationMapping.NCB_FIELD_LENGTH_INVALID;

    @Inject
    public ValidationImp() {
    }

    @Override
    public void validation(NCRSInputModel inputModel) throws Exception {
        if (null == inputModel) {
            throw new ValidationException(required, validationMsg.get(required, "NCRSInputModel"));
        }
        log.debug("-- NCRSInputModel[{}]", inputModel.toString());
        if (!ValidationUtil.isValueInRange(1, 10, inputModel.getUserId())) {
            throw new ValidationException(invalid, validationMsg.get(invalid, "User ID"));
        }
        if (!ValidationUtil.isValueInRange(16, 16, inputModel.getAppRefNumber())) {
            throw new ValidationException(invalid, validationMsg.get(invalid, "AppNumber"));
        }
        /*if(!ValidationUtil.isValueInRange(20, 20, inputModel.getCANumber())){
            throw new ValidationException(invalid,  validationMsg.get(invalid, "CA Number"));
        }
        if(!ValidationUtil.isValueInRange(1, 10, inputModel.getReferenceTel())){
            throw new ValidationException(invalid,  validationMsg.get(invalid, "Reference Tel"));
        }
        if(!ValidationUtil.isNumeric(inputModel.getReferenceTel())){
            throw new ValidationException(required, validationMsg.get(required, "Reference Tel"));
        }    */
        ArrayList<NCRSModel> ncrsModelArrayList = inputModel.getNcrsModelArrayList();
        if (null == ncrsModelArrayList) {
            throw new ValidationException(required, validationMsg.get(required, "NCRS Model ArrayList"));
        }
        if (!ValidationUtil.isValueInRange(1, 99, ncrsModelArrayList)) {
            throw new ValidationException(invalid, validationMsg.get(invalid, "NCRS Model ArrayList"));
        }
        for (NCRSModel ncrsModel : ncrsModelArrayList) {
//            if (!ValidationUtil.isValueInRange(02, 02, ncrsModel.getTitleNameCode())) {
//                throw new ValidationException(invalid, validationMsg.get(invalid, "Title Name"));
//            }
            if (!ValidationUtil.isValueInRange(1, 30, ncrsModel.getFirstName())) {
                throw new ValidationException(invalid, validationMsg.get(invalid, "First Name"));
            }
            if (!ValidationUtil.isValueInRange(1, 50, ncrsModel.getLastName())) {
                throw new ValidationException(invalid, validationMsg.get(invalid, "Last Name"));
            }
            if (!ValidationUtil.isValueInRange(13, 20, ncrsModel.getCitizenId())) {
                throw new ValidationException(invalid, validationMsg.get(invalid, "Citizen ID"));
            }
            if (!ValidationUtil.isNumeric(ncrsModel.getCitizenId())) {
                throw new ValidationException(required, validationMsg.get(required, "Citizen ID"));
            }
            if (!ValidationUtil.isValueInRange(2, 2, ncrsModel.getIdType())) {
                throw new ValidationException(invalid, validationMsg.get(invalid, "ID Type"));
            }
            if (!ValidationUtil.isValueInRange(2, 2, ncrsModel.getCountryCode())) {
                throw new ValidationException(invalid, validationMsg.get(invalid, "Country Code"));
            }
        }
    }

    @Override
    public void validation(NCCRSInputModel inputModel) throws Exception {
        if (null == inputModel) {
            throw new ValidationException(required, validationMsg.get(required, "NCCRSInputModel"));
        }
        log.debug("-- NCCRSInputModel[{}]", inputModel.toString());
        if (!ValidationUtil.isValueInRange(1, 10, inputModel.getUserId())) {
            throw new ValidationException(invalid, validationMsg.get(invalid, "User ID"));
        }
        if (!ValidationUtil.isValueInRange(16, 16, inputModel.getAppRefNumber())) {
            throw new ValidationException(invalid, validationMsg.get(invalid, "AppNumber"));
        }
        /*if(!ValidationUtil.isValueInRange(20,20,inputModel.getCANumber())){
            throw new ValidationException(invalid,  validationMsg.get(invalid, "CA Number"));
        }
        if(!ValidationUtil.isValueInRange(9,10,inputModel.getReferenceTel())){
            throw new ValidationException(invalid,  validationMsg.get(invalid, "Reference Tel"));
        }
        if(!ValidationUtil.isNumeric(inputModel.getReferenceTel())){
            throw new ValidationException(required, validationMsg.get(required, "Reference Tel"));
        }   */
        ArrayList<NCCRSModel> nccrsModelArrayList = inputModel.getNccrsModelArrayList();
        if (null == nccrsModelArrayList) {
            throw new ValidationException(required, validationMsg.get(required, "NCCRS Model ArrayList"));
        }
        if (!ValidationUtil.isValueInRange(1, 99, nccrsModelArrayList)) {
            throw new ValidationException(invalid, validationMsg.get(invalid, "NCCRS Model ArrayList"));
        }
        for (NCCRSModel nccrsModel : nccrsModelArrayList) {
            if (!ValidationUtil.isValueInRange(7, 7, nccrsModel.getRegistType())) {
                throw new ValidationException(invalid, validationMsg.get(invalid, "Regist Type"));
            }
            if (!ValidationUtil.isValueInRange(1, 99, nccrsModel.getRegistId())) {
                throw new ValidationException(invalid, validationMsg.get(invalid, "Regist ID"));
            }
            if (!ValidationUtil.isValueInRange(1, 90, nccrsModel.getCompanyName())) {
                throw new ValidationException(invalid, validationMsg.get(invalid, "Company Name"));
            }
        }
    }
}
