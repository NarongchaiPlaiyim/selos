package com.clevel.selos.integration.rlos.csi;

import com.clevel.selos.exception.RLOSInterfaceException;
import com.clevel.selos.integration.RLOS;
import com.clevel.selos.integration.rlos.csi.model.CSIData;
import com.clevel.selos.integration.rlos.csi.model.CSIInputData;
import com.clevel.selos.integration.rlos.csi.model.CSIResult;
import com.clevel.selos.integration.rlos.csi.module.DBExecute;
import com.clevel.selos.model.AccountInfoId;
import com.clevel.selos.model.AccountInfoName;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSIService implements Serializable {
    @Inject
    @RLOS
    Logger log;

    @Inject
    DBExecute dbExecute;

    @Inject
    public CSIService() {

    }

    public CSIResult getCSIData(String userId, CSIInputData csiInputData) throws Exception {
        log.debug("getCSIData service userId: {}, csiInputData: {}", userId, csiInputData.toString());
        List<CSIData> csiDataList = new ArrayList<CSIData>();
        List<CSIData> warningCodeFullMatched;
        List<CSIData> warningCodePartialMatched;
        CSIResult csiResult = null;
        try {
            Map<String, CSIData> fullyMatchedMap = null;
            Map<String, CSIData> partialMatchedMap = null;

            //get fully matched warning code
            if (csiInputData.getIdModelList() != null && csiInputData.getIdModelList().size() > 0) {
                fullyMatchedMap = new HashMap<String, CSIData>();
                for (AccountInfoId idModel : csiInputData.getIdModelList()) {
                    Map<String, CSIData> warningCodeMap = dbExecute.getWarningCodeListFullyMatched(idModel.getDocumentType(), idModel.getIdNumber());
                    if (warningCodeMap != null) {
                        for (String key : warningCodeMap.keySet()) {
                            fullyMatchedMap.put(key, warningCodeMap.get(key));
                        }
                    }
                }
            }

            //get partial matched warning code
            if (csiInputData.getNameModelList() != null && csiInputData.getNameModelList().size() > 0) {
                partialMatchedMap = new HashMap<String, CSIData>();
                for (AccountInfoName nameModel : csiInputData.getNameModelList()) {
                    String nameTh = null;
                    String nameEn = null;
                    if (!Util.isEmpty(nameModel.getNameTh()) && !Util.isEmpty(nameModel.getSurnameTh())) {
                        nameTh = nameModel.getNameTh().concat(" ").concat(nameModel.getSurnameTh());
                    }
                    if (!Util.isEmpty(nameModel.getNameEn()) && !Util.isEmpty(nameModel.getSurnameEn())) {
                        nameEn = nameModel.getNameEn().concat(" ").concat(nameModel.getSurnameEn());
                    }
                    Map<String, CSIData> warningCodeMap = dbExecute.getWarningCodeListPartialMatched(nameTh, nameEn);
                    if (warningCodeMap != null) {
                        for (String key : warningCodeMap.keySet()) {
                            partialMatchedMap.put(key, warningCodeMap.get(key));
                        }
                    }
                }
            }

            csiResult = new CSIResult();
            warningCodeFullMatched = new ArrayList<CSIData>();
            warningCodePartialMatched = new ArrayList<CSIData>();

            //check if partial is fully matched, remove from partial
            if (fullyMatchedMap != null && fullyMatchedMap.size() > 0) {
                for (String key : fullyMatchedMap.keySet()) {
                    //set fully code to list
                    warningCodeFullMatched.add(fullyMatchedMap.get(key));
                    //remove code from partial matched
                    if (partialMatchedMap != null && partialMatchedMap.containsKey(key)) {
                        partialMatchedMap.remove(key);
                    }
                }
            }

            if (partialMatchedMap != null && partialMatchedMap.size() > 0) {
                for (String key : partialMatchedMap.keySet()) {
                    //set remaining warning code to list
                    warningCodePartialMatched.add(partialMatchedMap.get(key));
                }
            }

            csiResult.setWarningCodeFullMatched(warningCodeFullMatched);
            csiResult.setWarningCodePartialMatched(warningCodePartialMatched);

        } catch (RLOSInterfaceException e){
            throw e;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return csiResult;
    }
}
