package com.clevel.selos.model.db.relation;

import com.clevel.selos.model.db.master.StaticDocument;
import com.clevel.selos.model.db.master.Step;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "rel_static_doc_step")
public class StaticDocStep {
    @Id
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "static_document_id")
    private StaticDocument document;

    @OneToMany
    @JoinColumn(name = "step_id")
    private List<Step> stepList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public StaticDocument getDocument() {
        return document;
    }

    public void setDocument(StaticDocument document) {
        this.document = document;
    }

    public List<Step> getStepList() {
        return stepList;
    }

    public void setStepList(List<Step> stepList) {
        this.stepList = stepList;
    }
}
