package at.htl.ghanalyzer.boundary;

import at.htl.ghanalyzer.control.InitBean;
import at.htl.ghanalyzer.entity.Commit;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.quarkus.logging.Log;
import io.smallrye.graphql.client.GraphQLClient;
import io.smallrye.graphql.client.Response;
import io.smallrye.graphql.client.dynamic.api.DynamicGraphQLClient;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RequestScoped
public class GithubService {

    @Inject
    InitBean initBean;

    /**
     * https://smallrye.io/smallrye-graphql/2.0.0.RC4/dynamic-client-usage/
     * https://github.com/hantsy/quarkus-sandbox/blob/master/docs/graphql-client.md#httpclient
     */
    @Inject
    @GraphQLClient("github")
    DynamicGraphQLClient client;

    List<Commit> commits = new LinkedList<>();

    /**
     * @param owner      github-user or github-organisation
     * @param repository github-repository
     * @param branch     git branch ie main
     * @return List of branches when ok
     * null, when exception
     */
    public List<Commit> getCommitsPerRepositoryAndBranch(
            String owner,
            String repository,
            String branch
    ) {
        String graphqlQuery =
                """
                {
                  repository(owner: $owner, name: $repository) {
                    refs(query: $branch, refPrefix: "refs/heads/", first: 1) {
                      nodes {
                        target {
                          ... on Commit {
                            history(first: 100) {
                              nodes {
                                message
                                committedDate
                              }
                              pageInfo {
                                hasNextPage
                                endCursor
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
                """;

// TODO: Replacement im String Block im Rahmen von executeSync funktioniert nicht
//        Map<String, Object> graphqlParams = new HashMap<>();
//        graphqlParams.put("owner", owner);
//        graphqlParams.put("repository", repository);
//        graphqlParams.put("branch", branch);
        Response graphqlResponse = null;
        try {
            //graphqlResponse = client.executeSync(graphqlQuery, graphqlParams);
            graphqlQuery = graphqlQuery.replace("$repository", "\""+repository+"\"");
            graphqlQuery = graphqlQuery.replace("$branch", "\""+branch+"\"");
            graphqlQuery = graphqlQuery.replace("$owner", "\""+owner+"\"");
            Log.info(graphqlQuery);
            graphqlResponse = client.executeSync(graphqlQuery);
            Log.info(graphqlResponse.getErrors());
        } catch (Exception e) {
            Log.error(e.getMessage());
            return null;
        }

        Log.info(graphqlResponse.getData());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(graphqlResponse.getData().toString());
        } catch (JsonProcessingException e) {
            Log.error(e.getMessage());
            return null;
        }
        ArrayNode commitsArray = jsonNode
                .findPath("history")
                .withArray("nodes");

        for (JsonNode commitNode : commitsArray) {
            commits.add(
                    new Commit(
                            commitNode.get("message").asText("n/a"),
                            LocalDateTime.parse(
                                    commitNode.get("committedDate").toString().replaceAll("\"", ""),
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                            )
                    )
            );
        }

        return commits;
    }

    public List<String> getAllBranchesPerOwnerAndRepository(
            String owner,
            String repository
    ) {
        String graphqlQuery =
            """
            {
               repository(owner: "$owner", name: "$repository") {
                 refs(first: 10, refPrefix: "refs/heads/", after: "") {
                   totalCount
                   edges {
                     node {
                       name
                     }
                   }
                   pageInfo {
                     hasNextPage
                     endCursor
                   }
                 }
               }
             }
            """;

        Response graphqlResponse = null;
        try {
            graphqlQuery = graphqlQuery.replace("$owner", owner);
            graphqlQuery = graphqlQuery.replace("$repository", repository);
            Log.info(graphqlQuery);
            graphqlResponse = client.executeSync(graphqlQuery);
            Log.info(graphqlResponse.getErrors());

            Log.info(graphqlResponse.getTransportMeta());
            updateRateLimit(graphqlResponse.getTransportMeta());

        } catch (Exception e) {
            Log.error(e.getMessage());
            return null;
        }

        Log.info(graphqlResponse.getData());


        List<String> branches = new LinkedList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(graphqlResponse.getData().toString());
        } catch (JsonProcessingException e) {
            Log.error(e.getMessage());
            return null;
        }
        ArrayNode commitsArray = jsonNode
                .findPath("refs")
                .withArray("edges");

        for (JsonNode commitNode : commitsArray) {
              branches.add(commitNode.findPath("name").asText("n/a"));
        }

        return branches;


    }


    private void updateRateLimit(Map<String, List<String>> transportMeta) {

        initBean.setRateLimitQuota(Integer.parseInt(transportMeta.get("X-RateLimit-Limit").get(0)));
        initBean.setRateLimitRemaining(Integer.parseInt(transportMeta.get("X-RateLimit-Remaining").get(0)));
        initBean.setRateLimitReset(Integer.parseInt(transportMeta.get("X-RateLimit-Reset").get(0)));

    };

}
