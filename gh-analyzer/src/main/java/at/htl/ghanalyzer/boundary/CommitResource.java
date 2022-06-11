package at.htl.ghanalyzer.boundary;

import io.quarkus.logging.Log;
import io.smallrye.common.annotation.Blocking;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class CommitResource {

    @ApplicationPath("/api")
    public static class RestConfig extends Application {
    }

    @Inject
    GithubService githubService;

    @GET
    @Path("/commits")
    @Produces(MediaType.APPLICATION_JSON)
    @Blocking
    public Response getAllCommits(
            @QueryParam("owner") String owner,
            @QueryParam("repository") String repository,
            @QueryParam("branch") String branch
    ) {

        Log.infof("owner: %s", owner);
        Log.infof("repository: %s", repository);
        Log.infof("branch: %s", branch);

        return Response.ok(
                githubService.getCommitsPerRepositoryAndBranch(owner, repository, branch)
        ).build();
    }
}

