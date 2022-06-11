package at.htl.ghanalyzer.entity;

import io.smallrye.graphql.client.typesafe.api.GraphQLClientApi;

@GraphQLClientApi(configKey = "github")
public interface GithubClientApi {

    CommitConnection allCommits();

}
