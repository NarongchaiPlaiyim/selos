package com.clevel.selos.transform.master;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Usages;
import com.clevel.selos.model.view.master.UsagesView;
import com.clevel.selos.transform.Transform;
import org.slf4j.Logger;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UsagesTransform extends Transform {
    @SELOS
    @Inject
    private Logger log;

    @Inject
    public UsagesTransform() {
    }

    public UsagesView transformToView(Usages usages) {
        UsagesView usagesView = new UsagesView();
        if (usages == null) {
            log.debug("transformToView() usages is null!");
            return usagesView;
        }

        usagesView.setId(usages.getId());
        usagesView.setDescription(usages.getDescription());
        usagesView.setCode(usages.getCode());
        usagesView.setActive(usages.getActive());
        return usagesView;
    }

    public SelectItem transformToSelectItem(UsagesView usagesView){
        SelectItem selectItem = new SelectItem();
        if(usagesView != null) {

        }
        return selectItem;
    }

    public List<UsagesView> transformToViewList(List<Usages> usageses) {
        List<UsagesView> usagesViews = new ArrayList<UsagesView>();
        if (usageses == null) {
            log.debug("transformToViewList() relations is null!");
            return usagesViews;
        }

        for (Usages usages : usageses) {
            usagesViews.add(transformToView(usages));
        }
        return usagesViews;
    }
}
