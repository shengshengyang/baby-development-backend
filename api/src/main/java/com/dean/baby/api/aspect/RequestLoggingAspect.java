package com.dean.baby.api.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.argument.StructuredArguments;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * API 請求日誌切面
 * 自動記錄所有 Controller 的請求和回應
 */
@Aspect
@Component
@Slf4j
public class RequestLoggingAspect {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 定義切點：攔截所有 Controller
     */
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerPointcut() {
    }

    /**
     * 環繞通知：記錄請求和回應
     */
    @Around("controllerPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 生成請求 ID
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);

        HttpServletRequest request = getCurrentRequest();
        long startTime = System.currentTimeMillis();

        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String uri = request != null ? request.getRequestURI() : "unknown";
        String method = request != null ? request.getMethod() : "unknown";
        String clientIp = getClientIp(request);

        try {
            // 記錄請求
            log.info("API_REQUEST - {} {} from {}",
                    method,
                    uri,
                    clientIp,
                    StructuredArguments.keyValue("requestId", requestId),
                    StructuredArguments.keyValue("method", methodName),
                    StructuredArguments.keyValue("controller", className),
                    StructuredArguments.keyValue("clientIp", clientIp),
                    StructuredArguments.keyValue("userAgent", request != null ? request.getHeader("User-Agent") : "unknown")
            );

            // 記錄請求參數
            if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
                String params = Arrays.stream(joinPoint.getArgs())
                        .filter(arg -> arg != null)
                        .map(arg -> {
                            try {
                                // 避免記錄敏感資訊如密碼
                                String json = objectMapper.writeValueAsString(arg);
                                return json.replaceAll("\"password\"\\s*:\\s*\"[^\"]*\"", "\"password\":\"***\"");
                            } catch (Exception e) {
                                return arg.toString();
                            }
                        })
                        .collect(Collectors.joining(", "));

                log.debug("REQUEST_PARAMS - [{}]",
                        params,
                        StructuredArguments.keyValue("requestId", requestId)
                );
            }

            // 執行方法
            Object result = joinPoint.proceed();

            // 計算執行時間
            long executionTime = System.currentTimeMillis() - startTime;

            // 記錄回應
            log.info("API_RESPONSE - {} {} completed in {}ms",
                    method,
                    uri,
                    executionTime,
                    StructuredArguments.keyValue("requestId", requestId),
                    StructuredArguments.keyValue("executionTime", executionTime),
                    StructuredArguments.keyValue("status", "SUCCESS")
            );

            return result;

        } catch (Exception e) {
            // 計算執行時間
            long executionTime = System.currentTimeMillis() - startTime;

            // 記錄錯誤
            log.error("API_ERROR - {} {} failed after {}ms: {}",
                    method,
                    uri,
                    executionTime,
                    e.getMessage(),
                    StructuredArguments.keyValue("requestId", requestId),
                    StructuredArguments.keyValue("executionTime", executionTime),
                    StructuredArguments.keyValue("status", "ERROR"),
                    StructuredArguments.keyValue("errorType", e.getClass().getSimpleName())
            );

            throw e;
        } finally {
            MDC.remove("requestId");
        }
    }

    /**
     * 獲取當前請求
     */
    private HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }

    /**
     * 獲取客戶端 IP
     */
    private String getClientIp(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 處理多個 IP 的情況（X-Forwarded-For 可能包含多個 IP）
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip;
    }
}
