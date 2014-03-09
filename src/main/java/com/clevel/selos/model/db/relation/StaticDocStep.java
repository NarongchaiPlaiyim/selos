package com.clevel.selos.model.db.relation;

import com.clevel.selos.model.db.master.StaticDocument;
import com.clevel.selos.model.db.master.Step;

import javax.persistence.*;

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
    private Step step;

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

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }
}
