package com.clevel.selos.transform.master;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Title;
import com.clevel.selos.model.view.master.TitleView;
import com.clevel.selos.transform.Transform;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TitleTransform extends Transform {
    @Inject
    @SELOS
    private Logger logger;

    @Inject
    public TitleTransform(){}

    public TitleView transformToView(Title title){
        TitleView titleView = new TitleView();
        if(title != null){
            titleView.setId(title.getId());
            titleView.setCode(title.getCode());
            titleView.setCustomerEntityId(title.getCustomerEntity() == null ? 0 : title.getCustomerEntity().getId());
            titleView.setTitleEn(title.getTitleEn());
            titleView.setTitleTh(title.getTitleTh());
            titleView.setActive(title.getActive());
        }
        return titleView;
    }

    public Map<Integer, TitleView> transformToCache(List<Title> titleList){
        if(titleList == null || titleList.size() == 0)
            return null;

        Map<Integer, TitleView> _tmpMap = new ConcurrentHashMap<Integer, TitleView>();
        for(Title title : titleList){
            TitleView titleView = transformToView(title);
            _tmpMap.put(titleView.getId(), titleView);
        }
        return _tmpMap;
    }
}
