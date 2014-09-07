package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.DocumentTypeDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.DocumentType;
import com.clevel.selos.model.view.master.DocumentTypeView;
import com.clevel.selos.transform.master.DocumentTypeTransform;
import org.slf4j.Logger;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DocumentTypeControl extends BusinessControl {

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private ApplicationCacheLoader cacheLoader;

    @Inject
    private DocumentTypeTransform documentTypeTransform;

    @Inject
    private DocumentTypeDAO documentTypeDAO;

    @Inject
    public DocumentTypeControl(){}

    public List<SelectItem> getDocumentTypeByCustomerEntity(int customerEntityId){
        logger.debug("-- getDocumentTypeByCustomerEntity, customerEntityId: {}", customerEntityId);
        Map<Integer, DocumentTypeView> _tmpMap = getInternalCacheMap();
        List<SelectItem> documentTypeList = new ArrayList<SelectItem>();
        for(DocumentTypeView documentTypeView : _tmpMap.values()){
            if(documentTypeView.getCustomerEntityId() == customerEntityId) {
                SelectItem selectItem = documentTypeTransform.transformToSelectItem(documentTypeView);
                documentTypeList.add(selectItem);
            }
        }
        logger.debug("getDocumentTypeByCustomerEntity return DocumentTypeView size: {}", documentTypeList.size());
        return documentTypeList;
    }

    private Map<Integer, DocumentTypeView> loadData(){
        logger.debug("-- begin loadData --");
        List<DocumentType> documentTypeList = documentTypeDAO.findAll();
        Map<Integer, DocumentTypeView> _tmpMap = documentTypeTransform.transformToCache(documentTypeList);
        if(_tmpMap == null || _tmpMap.size() == 0) {
            logger.debug("return empty DocumentTypeView");
            return new ConcurrentHashMap<Integer, DocumentTypeView>();
        } else {
            cacheLoader.setCacheMap(DocumentType.class.getName(), _tmpMap);
            logger.debug("loadData return DocumentTypeView size: {}", _tmpMap.size());
            return _tmpMap;
        }
    }

    private Map<Integer, DocumentTypeView> getInternalCacheMap(){
        logger.debug("-- getInternalCacheMap");
        Map<Integer, DocumentTypeView> _tmpMap = cacheLoader.getCacheMap(DocumentType.class.getName());
        if(_tmpMap == null || _tmpMap.size() == 0){
            logger.debug("DocumentTypeView is null or empty, reload from DB");
            _tmpMap = loadData();
        }
        return _tmpMap;
    }

}
