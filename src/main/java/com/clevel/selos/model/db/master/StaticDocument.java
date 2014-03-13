package com.clevel.selos.model.db.master;

import com.clevel.selos.model.DocLevel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mst_static_document")
public class StaticDocument implements Serializable{
    @Id
    @Column(name = "id")
    private long id;
    @Column(name = "name", length = 100)
    private String name;
    @Column(name = "code", length = 4)
    private String code;
    @Column(name = "description", length = 100)
    private String description;
    @Column(name = "doc_level", length = 1)
    @Enumerated(EnumType.ORDINAL)
    private DocLevel docLevel;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DocLevel getDocLevel() {
        return docLevel;
    }

    public void setDocLevel(DocLevel docLevel) {
        this.docLevel = docLevel;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("name", name)
                .append("code", code)
                .append("description", description)
                .append("docLevel", docLevel)
                .toString();
    }
}
