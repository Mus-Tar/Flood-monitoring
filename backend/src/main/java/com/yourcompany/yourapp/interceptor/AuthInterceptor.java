package com.yourcompany.yourapp.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourcompany.yourapp.annotation.RequireRole;
import com.yourcompany.yourapp.security.AuthContext;
import com.yourcompany.yourapp.security.AuthUser;
import com.yourcompany.yourapp.security.JwtTokenService;
import io.jsonwebtoken.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtTokenService jwtTokenService;
    private final ObjectMapper objectMapper;

    public AuthInterceptor(JwtTokenService jwtTokenService, ObjectMapper objectMapper) {
        this.jwtTokenService = jwtTokenService;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod()) || !(handler instanceof HandlerMethod)) {
            return true;
        }

        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            writeError(response, HttpServletResponse.SC_UNAUTHORIZED, "未登录或令牌缺失");
            return false;
        }

        String token = authorization.substring("Bearer ".length()).trim();
        if (token.isEmpty()) {
            writeError(response, HttpServletResponse.SC_UNAUTHORIZED, "未登录或令牌缺失");
            return false;
        }

        try {
            AuthUser authUser = jwtTokenService.parseAccessToken(token);
            AuthContext.set(authUser);

            HandlerMethod handlerMethod = (HandlerMethod) handler;
            if (!hasRequiredRole(handlerMethod, authUser)) {
                writeError(response, HttpServletResponse.SC_FORBIDDEN, "无权限访问该接口");
                return false;
            }
            return true;
        } catch (JwtException ex) {
            writeError(response, HttpServletResponse.SC_UNAUTHORIZED, "令牌无效或已过期");
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        AuthContext.clear();
    }

    private boolean hasRequiredRole(HandlerMethod handlerMethod, AuthUser authUser) {
        RequireRole requireRole = handlerMethod.getMethodAnnotation(RequireRole.class);
        if (requireRole == null) {
            requireRole = handlerMethod.getBeanType().getAnnotation(RequireRole.class);
        }
        if (requireRole == null) {
            return true;
        }
        return Arrays.asList(requireRole.value()).contains(authUser.getRole());
    }

    private void writeError(HttpServletResponse response, int status, String message) throws Exception {
        response.setStatus(status);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> body = new HashMap<>();
        body.put("code", status == HttpServletResponse.SC_FORBIDDEN ? 403 : 401);
        body.put("msg", message);
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
