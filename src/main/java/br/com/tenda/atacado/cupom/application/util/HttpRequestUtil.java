package br.com.tenda.atacado.cupom.application.util;


import javax.servlet.http.HttpServletRequest;

public final class HttpRequestUtil {

    private HttpRequestUtil() {
    }
    public static String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr() != null ? request.getRemoteAddr() : "";
    }
}
