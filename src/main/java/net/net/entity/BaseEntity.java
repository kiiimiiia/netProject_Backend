package net.net.entity;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    @JsonProperty("id")
    protected long id;
    @JsonProperty("created_at")
    protected Date created;
    @JsonProperty("updated_at")
    protected Date updated;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @PrePersist
    protected void prePersist() {
        if (created == null) {
            created = new Date();
        }
    }

    @PreUpdate
    protected void preUpdate() {
        if (updated == null) {
            updated = new Date();
        }
    }

    @PreRemove
    protected void preRemove() {
    }

    @Override
    public String toString() {
        return String.format("%s, %d", getClass().getSimpleName(), getId());
    }

}



