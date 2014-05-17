package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mst_mandate_fields_class")
public class MandateFieldClass implements Serializable{

    @Id
    @SequenceGenerator(name = "SEQ_MST_MAN_FIELD_CLASS", sequenceName = "SEQ_MST_MAN_FIELD_CLASS", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MST_MAN_FIELD_CLASS")
    private long id;

    @Column(name = "class_name", length = 150)
    private String className;

    @Column(name = "class_desc", length = 150)
    private String classDescription;

    @Column(name = "page_name", length = 100)
    private String pageName;

    @Column(name = "active", length = 1, columnDefinition = "int default 1")
    private boolean active;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassDescription() {
        return classDescription;
    }

    public void setClassDescription(String classDescription) {
        this.classDescription = classDescription;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("className", className)
                .append("classDescription", classDescription)
                .append("pageName", pageName)
                .append("active", active)
                .toString();
    }
}
