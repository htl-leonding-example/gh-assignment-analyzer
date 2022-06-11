package at.htl.ghanalyzer.boundary;

import at.htl.ghanalyzer.entity.Commit;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.quarkus.logging.Log;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.graphql.client.GraphQLClient;
import io.smallrye.graphql.client.Response;
import io.smallrye.graphql.client.dynamic.api.DynamicGraphQLClient;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Path("/playground")
@Produces(MediaType.APPLICATION_JSON)
public class CommitResourcePlayground {

    @Inject
    @GraphQLClient("github")
    DynamicGraphQLClient client;

    /*
     * Man kann anstelle des Injects, den client mittels einer Factory Methode instanzieren.
     * https://stackoverflow.com/questions/71500950/quarkus-graphql-client-with-keycloak
     */

    @GET
    @Path("/commit")
    @Produces(MediaType.APPLICATION_JSON)
    @Blocking
    public javax.ws.rs.core.Response getAllCommits() throws Exception {

        Log.info("entering getAllCommits");

        String graphqlQuery =
                        """
                        {
                          repository(owner: "2122-4ahif-nvs", name: "13-jetpack-compose-pathway-christophhandel") {
                            refs(query: "main", refPrefix: "refs/heads/", first: 1) {
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

        Response graphqlResponse = client.executeSync(graphqlQuery);

        //Response response = dynamicClient.executeSync(query);
        //return response.getObject(FilmConnection.class, "allFilms").getFilms();

        Log.info(graphqlResponse.toString());

        Log.info(graphqlResponse.getData());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(graphqlResponse.getData().toString());
        String prettyPrint = jsonNode.toPrettyString();
        Log.info(prettyPrint);

        // /repository/refs/nodes[0]/target/history/nodes
        JsonNode array = jsonNode.at("/repository/refs/nodes");
        Log.info("nodes ist array" + array.isArray());

        ArrayNode locatedArray = jsonNode
                .path("repository")
                .path("refs")
                .withArray("nodes");
        Log.info(locatedArray.get(0).toPrettyString());

        JsonNode historyNode = jsonNode.findPath("history");
        Log.info(historyNode.toPrettyString());
        ArrayNode commitsArray = historyNode.withArray("nodes");
        Log.info(commitsArray.toPrettyString());
        for (JsonNode commitNode : commitsArray) {
            Log.info(commitNode.get("committedDate").toString());
            Commit c = new Commit(
                    commitNode.get("message").asText("n/a"),
                    LocalDateTime.parse(
                            commitNode.get("committedDate").toString().replaceAll("\"", ""),
                            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                    )
            );
            Log.info(c);
        }

        return javax.ws.rs.core.Response.ok(jsonNode.at("/repository/refs").toString()).build();
    }
}
