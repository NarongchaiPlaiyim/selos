package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class NewCollSubRelatedView implements Serializable {
    private long id;
    private NewCollateralSubView relatedWith;
    private NewCollateralSubView newCollateralSubView;

    public NewCollSubRelatedView() {
        reset();
    }

    public void reset() {
        relatedWith = new NewCollateralSubView();
        newCollateralSubView = new NewCollateralSubView();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public NewCollateralSubView getRelatedWith() {
        return relatedWith;
    }

    public void setRelatedWith(NewCollateralSubView relatedWith) {
        this.relatedWith = relatedWith;
    }

    public NewCollateralSubView getNewCollateralSubView() {
        return newCollateralSubView;
    }

    public void setNewCollateralSubView(NewCollateralSubView newCollateralSubView) {
        this.newCollateralSubView = newCollateralSubView;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("relatedWith", relatedWith)
                .append("newCollateralSubView", newCollateralSubView)
                .toString();
    }
}
