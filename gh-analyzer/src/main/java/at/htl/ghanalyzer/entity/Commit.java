package at.htl.ghanalyzer.entity;

import java.time.LocalDateTime;

public class Commit {

    private String comment;
    private LocalDateTime commitDateTime;

    public Commit() {
    }

    public Commit(String comment, LocalDateTime commitDateTime) {
        this.comment = comment;
        this.commitDateTime = commitDateTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCommitDateTime() {
        return commitDateTime;
    }

    public void setCommitDateTime(LocalDateTime commitDateTime) {
        this.commitDateTime = commitDateTime;
    }

    @Override
    public String toString() {
        return "Commit{" +
                "comment='" + comment + '\'' +
                ", commitDate='" + commitDateTime + '\'' +
                '}';
    }
}
