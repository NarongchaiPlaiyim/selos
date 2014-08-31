package com.clevel.selos.transform.master;

import com.clevel.selos.dao.master.RequestTypeDAO;
import com.clevel.selos.model.db.master.RequestType;
import com.clevel.selos.model.view.master.RequestTypeView;
import com.clevel.selos.transform.Transform;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RequestTypeTransform extends Transform {

    @Inject
    private RequestTypeDAO requestTypeDAO;

    @Inject
    public RequestTypeTransform() {}

    public RequestType transformToModel(RequestTypeView requestTypeView){
        if(requestTypeView == null)
            return null;

        RequestType requestType = new RequestType();
        if(requestTypeView.getId() != 0){
            requestType = requestTypeDAO.findById(requestTypeView.getId());

        }
        requestType.setName(requestTypeView.getName());
        requestType.setActive(requestTypeView.getActive());
        return requestType;
    }

    public RequestTypeView transformToView(RequestType requestType){
        if(requestType == null)
            return null;
        RequestTypeView requestTypeView = new RequestTypeView();
        requestTypeView.setId(requestType.getId());
        requestTypeView.setName(requestType.getName());
        requestTypeView.setActive(requestType.getActive());
        return requestTypeView;
    }

    public Map<Integer, RequestTypeView> transformToCache(List<RequestType> requestTypeList){
        if(requestTypeList == null || requestTypeList.size() == 0)
            return null;
        Map<Integer, RequestTypeView> _tmpMap = new ConcurrentHashMap<Integer, RequestTypeView>();
        for(RequestType requestType : requestTypeList){
            RequestTypeView requestTypeView = transformToView(requestType);
            _tmpMap.put(requestTypeView.getId(), requestTypeView);
        }
        return _tmpMap;
    }

}
