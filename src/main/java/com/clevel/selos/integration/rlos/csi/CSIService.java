package com.clevel.selos.integration.rlos.csi;

import com.clevel.selos.integration.RLOS;
import com.clevel.selos.integration.rlos.csi.model.*;
import com.clevel.selos.integration.rlos.csi.module.DBExecute;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;

public class CSIService implements Serializable{
    @Inject
    @RLOS
    Logger log;

    @Inject
    DBExecute dbExecute;

    public CSIResult getCSIData(String userId,CSIInputData csiInputData) throws Exception{
        log.debug("getCSIData service userId: {}, csiInputData: {}", userId,csiInputData.toString());
        List<CSIData> csiDataList = new ArrayList<CSIData>();
        List<String> warningCodeFullMatched;
        List<String> warningCodePartialMatched;
        CSIResult csiResult = null;
        try{
            Map<String, String> fullyMatchedMap = null;
            Map<String, String> partialMatchedMap = null;

            //get fully matched warning code
            if(csiInputData.getIdModelList()!=null && csiInputData.getIdModelList().size()>0){
                fullyMatchedMap = new HashMap<String, String>();
                for(IdModel idModel: csiInputData.getIdModelList()){
                    Map<String,String> warningCodeMap = dbExecute.getWarningCodeListFullyMatched(idModel.getDocumentType(),idModel.getIdNumber());
                    if(warningCodeMap!=null){
                        for (String key: warningCodeMap.keySet()) {
                            fullyMatchedMap.put(key,warningCodeMap.get(key));
                        }
                    }
                }
            }

            //get partial matched warning code
            if(csiInputData.getNameModelList()!=null && csiInputData.getNameModelList().size()>0){
                partialMatchedMap = new HashMap<String, String>();
                for(NameModel nameModel: csiInputData.getNameModelList()){
                    String nameTh = null;
                    String nameEn = null;
                    if(!Util.isEmpty(nameModel.getNameTh()) && !Util.isEmpty(nameModel.getSurnameTh())){
                        nameTh = nameModel.getNameTh().concat(" ").concat(nameModel.getSurnameTh());
                    }
                    if(!Util.isEmpty(nameModel.getNameEn()) && !Util.isEmpty(nameModel.getSurnameEn())){
                        nameEn = nameModel.getNameEn().concat(" ").concat(nameModel.getSurnameEn());
                    }
                    Map<String,String> warningCodeMap = dbExecute.getWarningCodeListPartialMatched(nameTh,nameEn);
                    if(warningCodeMap!=null){
                        for (String key: warningCodeMap.keySet()) {
                            partialMatchedMap.put(key,warningCodeMap.get(key));
                        }
                    }
                }
            }

            csiResult = new CSIResult();
            warningCodeFullMatched = new ArrayList<String>();
            warningCodePartialMatched = new ArrayList<String>();

            //check if partial is fully matched, remove from partial
            if(fullyMatchedMap!=null && fullyMatchedMap.size()>0){
                for (String key: fullyMatchedMap.keySet()) {
                    //set fully code to list
                    warningCodeFullMatched.add(key);
                    //remove code from partial matched
                    if(partialMatchedMap!=null && partialMatchedMap.containsKey(key)){
                        partialMatchedMap.remove(key);
                    }
                }
            }

            if(partialMatchedMap!=null && partialMatchedMap.size()>0){
                for (String key: partialMatchedMap.keySet()) {
                    //set remaining warning code to list
                    warningCodePartialMatched.add(key);
                }
            }

            csiResult.setWarningCodeFullMatched(warningCodeFullMatched);
            csiResult.setWarningCodePartialMatched(warningCodePartialMatched);

        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return csiResult;
    }
}
