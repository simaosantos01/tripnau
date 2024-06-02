package com.desofs.backend.interceptors;

import com.desofs.backend.services.RateService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.servlet.HandlerInterceptor;


@RequiredArgsConstructor
public class RateLimitingInterceptor implements HandlerInterceptor {

    private final JwtDecoder jwtDecoder;
    private final RateService rateService;
    private final int REQUEST_LIMIT = 60;
    private final int TOO_MANY_REQUESTS_STATUS = 429;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String rawToken = request.getHeader("Authorization");
        if(rawToken != null) {
            Jwt token = jwtDecoder.decode(rawToken.substring(7));
            String user = token.getClaim("email");
            int rate = rateService.getUserRate(user);

            if(rate < REQUEST_LIMIT) {
                rateService.increaseRate(user);
                return true;
            }

            // This is the status for Too Many Requests, for some reason HttpServletResponse doesn't have it?
            response.setStatus(TOO_MANY_REQUESTS_STATUS);
            return false;
        }
        return true;
    }
}
