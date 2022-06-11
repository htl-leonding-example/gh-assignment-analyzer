package at.htl.ghanalyzer.boundary;

import io.quarkus.logging.Log;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("branches")
public class BranchResource {

    @Inject
    GithubService githubService;

    @GET
    public Response getAllBranches(
            @QueryParam("owner") String owner,
            @QueryParam("repository") String repository
    ) {

        Log.infof("owner: %s", owner);
        Log.infof("repository: %s", repository);

        return Response.ok(
                githubService.getAllBranchesPerOwnerAndRepository(owner, repository)
        ).build();


    }
}
