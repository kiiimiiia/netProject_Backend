package net.net.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "cases")
public class Cases extends BaseEntity implements Serializable {

    public static enum TYPE {
        COMPLAINT(1),
        CRITISM(2),
        SUGGESTION(3),
        REQUEST(4);

        private final int value;

        TYPE(final int newValue) {
            value = newValue;
        }

        public int getValue() {
            return value;
        }
    }

    @Column(name = "sender_user")
    private long senderId;

    @Column(name = "time")
    private Date time;

    @Column(name = "subject")
    private String subject;

    @Column(name = "text")
    private String text;

    @Column(name = "file")
    private String fileName;

    @Column(name = "receiver")
    private long receiverId;

    @Column(name = "status")
    private long status;

    @Column(name = "type")
    private String type;



    @Column(name = "Satisfaction")
    private int satisfaction;

    private String reciverName;

    private String senderName;

//    @Column(name = "paraf")
//    private String paraf;

    public Cases() {
    }

//    public Cases(long senderId, Date time, String subject, String text, String fileContent, long receiverId, long status) {
//        this.senderId = senderId;
//        this.time = time;
//        this.subject = subject;
//        this.text = text;
//        this.fileContent = fileContent;
//        this.receiverId = receiverId;
//        this.status = status;
//    }


    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReciverName() {
        return reciverName;
    }

    public void setReciverName(String reciverName) {
        this.reciverName = reciverName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
    public int getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(int satisfaction) {
        this.satisfaction = satisfaction;
    }

    //    public String getParaf() {
//        return paraf;
//    }
//
//    public void setParaf(String paraf) {
//        this.paraf = paraf;
//    }
}
