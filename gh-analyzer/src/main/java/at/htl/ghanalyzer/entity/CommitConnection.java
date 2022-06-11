package at.htl.ghanalyzer.entity;

import java.util.List;

public class CommitConnection {

    private List<Commit> commits;

    public List<Commit> getCommits() {
        return commits;
    }

    public void setCommits(List<Commit> commits) {
        this.commits = commits;
    }
}
