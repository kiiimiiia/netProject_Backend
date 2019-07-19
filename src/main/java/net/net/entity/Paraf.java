package net.net.entity;

import org.springframework.web.bind.annotation.RequestHeader;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "paraf")
public class Paraf extends BaseEntity implements Serializable {

    @Column(name = "paraf")
    private String paraf;

    @Column(name = "sender")
    private long senderId;

    @Column(name = "case_id")
    private long caseId;

    @Column(name = "date")
    private long date;

    private String senderName;

    public Paraf() {
    }

    public Paraf(String paraf, long senderId, long caseId, long date) {
        this.paraf = paraf;
        this.senderId = senderId;
        this.caseId = caseId;
        this.date = date;
    }

    public String getParaf() {
        return paraf;
    }

    public void setParaf(String paraf) {
        this.paraf = paraf;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getCaseId() {
        return caseId;
    }

    public void setCaseId(long caseId) {
        this.caseId = caseId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
