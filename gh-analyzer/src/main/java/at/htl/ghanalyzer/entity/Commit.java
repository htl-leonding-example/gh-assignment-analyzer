package at.htl.ghanalyzer.entity;

import java.time.LocalDateTime;

public class Commit {

    private String owner;

    private String repository;

    private String branch;

    private String oid;

    private String message;

    private String messageBody;
    private LocalDateTime commitDateTime;

    private LocalDateTime pushedDate;

    private String authorName;

    private String authorEmail;
    public Commit() {
    }

    public Commit(String message, LocalDateTime commitDateTime) {
        this.message = message;
        this.commitDateTime = commitDateTime;
    }

    public Commit(
            String owner,
            String repository,
            String branch,
            String oid,
            String message,
            String messageBody,
            LocalDateTime commitDateTime,
            LocalDateTime pushedDate,
            String authorName,
            String authorEmail) {
        this.owner = owner;
        this.repository = repository;
        this.branch = branch;
        this.oid = oid;
        this.message = message;
        this.messageBody = messageBody;
        this.commitDateTime = commitDateTime;
        this.pushedDate = pushedDate;
        this.authorName = authorName;
        this.authorEmail = authorEmail;
    }

    //region getter and setter
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCommitDateTime() {
        return commitDateTime;
    }

    public void setCommitDateTime(LocalDateTime commitDateTime) {
        this.commitDateTime = commitDateTime;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public LocalDateTime getPushedDate() {
        return pushedDate;
    }

    public void setPushedDate(LocalDateTime pushedDate) {
        this.pushedDate = pushedDate;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }
    //endregion

    @Override
    public String toString() {
        return "Commit{" +
                "comment='" + message + '\'' +
                ", commitDate='" + commitDateTime + '\'' +
                '}';
    }

}
