package com.clevel.selos.model.db.ext.map;

import com.clevel.selos.model.db.master.Title;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "map_rm_title")
public class RMTitle implements Serializable {
    @Id
    @Column(name = "id")
    private long id;
    @Column(name = "rm_title")
    private String rmTitle;
    @OneToOne
    @JoinColumn(name = "mst_title_id")
    private Title title;
    @Column(name = "default_flag")
    private int defaultFlag;

    public RMTitle() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRmTitle() {
        return rmTitle;
    }

    public void setRmTitle(String rmTitle) {
        this.rmTitle = rmTitle;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public int getDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(int defaultFlag) {
        this.defaultFlag = defaultFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("rmTitle", rmTitle)
                .append("title", title)
                .append("defaultFlag", defaultFlag)
                .toString();
    }
}
