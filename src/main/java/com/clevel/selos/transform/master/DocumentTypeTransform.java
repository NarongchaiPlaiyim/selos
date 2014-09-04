package com.clevel.selos.transform.master;

import com.clevel.selos.dao.master.DocumentTypeDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.DocumentType;
import com.clevel.selos.model.view.master.DocumentTypeView;
import com.clevel.selos.transform.Transform;
import org.slf4j.Logger;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DocumentTypeTransform extends Transform {
    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private DocumentTypeDAO documentTypeDAO;

    @Inject
    public DocumentTypeTransform(){}

    public SelectItem transformToSelectItem(DocumentTypeView documentTypeView){
        if(documentTypeView == null)
            return null;
        SelectItem selectItem = new SelectItem();
        selectItem.setLabel(documentTypeView.getDescription());
        selectItem.setValue(documentTypeView.getId());
        return selectItem;
    }

    public DocumentTypeView transformToView(DocumentType documentType){
        DocumentTypeView documentTypeView = new DocumentTypeView();
        if(documentType != null) {
            documentTypeView.setId(documentType.getId());
            documentTypeView.setActive(documentType.getActive());
            documentTypeView.setComsName(documentType.getComsName());
            documentTypeView.setCustomerEntityId(documentType.getCustomerEntity() == null ? 0 : documentType.getCustomerEntity().getId());
            documentTypeView.setDescription(documentType.getDescription());
            documentTypeView.setDocumentTypeCode(documentType.getDocumentTypeCode());
        }
        return documentTypeView;
    }

    public Map<Integer, DocumentTypeView> transformToCache(List<DocumentType> documentTypeList){
        if(documentTypeList == null || documentTypeList.size() == 0)
            return null;
        Map<Integer, DocumentTypeView> _tmpMap = new ConcurrentHashMap<Integer, DocumentTypeView>();
        for(DocumentType documentType : documentTypeList){
            DocumentTypeView documentTypeView = transformToView(documentType);
            _tmpMap.put(documentTypeView.getId(), documentTypeView);
        }
        return _tmpMap;
    }
}
