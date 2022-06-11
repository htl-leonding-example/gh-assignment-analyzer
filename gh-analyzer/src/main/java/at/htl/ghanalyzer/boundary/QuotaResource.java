package at.htl.ghanalyzer.boundary;

import at.htl.ghanalyzer.control.InitBean;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

@Path("quota")
public class QuotaResource {

    @Inject
    InitBean initBean;

    @Inject
    GithubService githubService;

    @GET
    public Response getQuota() {
        if (initBean.getRateLimitQuota() == -1) {
            githubService.getAllBranchesPerOwnerAndRepository("htl-leonding", "jetpack-compose-pathway");
        }

        Map<String, Object> quota = new HashMap<>();
        quota.put("x-ratelimit-limit", initBean.getRateLimitQuota());
        quota.put("x-ratelimit-remaining", initBean.getRateLimitRemaining());
        quota.put("x-ratelimit-reset", LocalDateTime.ofEpochSecond(
                initBean.getRateLimitReset(),
                0,
                ZoneId.of("Europe/Vienna")
                        .getRules()
                        .getOffset(LocalDateTime.now())
        ));

        return Response.ok(quota).build();
    }

}
