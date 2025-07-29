package portal.forasbackend.filter;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import portal.forasbackend.core.config.RateLimitingConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RateLimitingFilter implements Filter {

    private final RateLimitingConfig rateLimitingConfig;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String key = getKey(httpRequest);
        Bucket bucket = resolveBucket(httpRequest.getRequestURI(), key);

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            httpResponse.addHeader("X-Rate-Limit-Remaining",
                    String.valueOf(probe.getRemainingTokens()));
            chain.doFilter(request, response);
        } else {
            long waitForRefill = TimeUnit.NANOSECONDS.toSeconds(probe.getNanosToWaitForRefill());
            httpResponse.addHeader("X-Rate-Limit-Retry-After-Seconds",
                    String.valueOf(waitForRefill));
            httpResponse.sendError(HttpStatus.TOO_MANY_REQUESTS.value(),
                    "Too many requests");
        }
    }

    private String getKey(HttpServletRequest request) {
        // Use IP address as key, consider using user ID for authenticated requests
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private Bucket resolveBucket(String uri, String key) {
        if (uri.contains("/auth/")) {
            return rateLimitingConfig.resolveAuthBucket(key);
        }
        return rateLimitingConfig.resolveBucket(key);
    }
}