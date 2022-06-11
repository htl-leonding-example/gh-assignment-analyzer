package at.htl.ghanalyzer.control;

import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

/**
 * x-ratelimit-limit	The maximum number of requests you're permitted to make per hour.
 * x-ratelimit-remaining	The number of requests remaining in the current rate limit window.
 * x-ratelimit-reset	The time at which the current rate limit window resets in UTC epoch seconds.
 */
@ApplicationScoped
public class InitBean {

    // https://docs.github.com/en/rest/overview/resources-in-the-rest-api#rate-limit-http-headers
    private int rateLimitQuota = -1;
    private int rateLimitRemaining = -1;
    private int rateLimitReset = -1;

    void init(@Observes StartupEvent event) {
        Log.info("it works");
    }

    //region getter and setter
    public int getRateLimitQuota() {
        return rateLimitQuota;
    }

    public void setRateLimitQuota(int rateLimitQuota) {
        this.rateLimitQuota = rateLimitQuota;
    }

    public int getRateLimitRemaining() {
        return rateLimitRemaining;
    }

    public void setRateLimitRemaining(int rateLimitRemaining) {
        this.rateLimitRemaining = rateLimitRemaining;
    }

    public int getRateLimitReset() {
        return rateLimitReset;
    }

    public void setRateLimitReset(int rateLimitReset) {
        this.rateLimitReset = rateLimitReset;
    }
    //endregion
}
