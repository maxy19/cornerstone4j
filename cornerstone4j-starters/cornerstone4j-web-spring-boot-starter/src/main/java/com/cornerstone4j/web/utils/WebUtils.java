package com.cornerstone4j.web.utils;

import cn.hutool.core.util.StrUtil;
import com.cornerstone4j.core.constant.StringPoolConstant;
import com.cornerstone4j.web.wrapper.CustomRequestWrapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.util.Strings;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;


@UtilityClass
public class WebUtils extends org.springframework.web.util.WebUtils {

    private final String HTTP_RULE = "^http(s)?://.*";

    private final Pattern PATTERN = Pattern.compile(HTTP_RULE);

    /**
     * getRequest
     *
     * <p>
     * get jakarta.servlet.http.HttpServletRequest instance
     * </p>
     *
     * @return jakarta.servlet.http.HttpServletRequest
     * @since 2020/4/13 17:32
     */
    public HttpServletRequest getRequest() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .map(x -> (ServletRequestAttributes) x)
                .map(ServletRequestAttributes::getRequest)
                .orElse(null);
    }


    public String getRequestUrl() {
        return Optional.ofNullable(WebUtils.getRequest())
                .map(HttpServletRequest::getServletPath)
                .orElse(null);
    }

    /**
     * getRequestMethod
     *
     * @return java.lang.String
     * @since 2021/8/27 13:59
     */
    public String getRequestMethod() {
        return Optional.ofNullable(WebUtils.getRequest()).map(HttpServletRequest::getMethod).orElse(null);
    }


    /**
     * getResponse
     *
     * <p>
     * get jakarta.servlet.http.HttpServletResponse instance
     * </p>
     *
     * @return jakarta.servlet.http.HttpServletResponse
     * @since 2020/4/13 17:31
     */
    public HttpServletResponse getResponse() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .map(x -> (ServletRequestAttributes) x)
                .map(ServletRequestAttributes::getResponse)
                .orElse(null);
    }


    /**
     * getRequestHeaders
     *
     * @param request http request instance
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @since 2020/4/13 17:31
     */
    public Map<String, String> getRequestHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap();
        if (ObjectUtils.isEmpty(request)) {
            return headers;
        }
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String headerName = enumeration.nextElement();
            String headerValue = request.getHeader(headerName);
            headers.put(headerName, headerValue);
        }
        return headers;
    }

    /**
     * getResponseHeaders
     *
     * @param response http response instance
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @since 2020/4/13 17:17
     */
    public Map<String, String> getResponseHeaders(HttpServletResponse response) {
        Map<String, String> headers = new HashMap();
        if (ObjectUtils.isEmpty(response)) {
            return headers;
        }
        for (String headerName : response.getHeaderNames()) {
            String headerValue = response.getHeader(headerName);
            headers.put(headerName, headerValue);
        }
        return headers;
    }

    /**
     * 添加标题
     *
     * @param headerName  标题名称
     * @param headerValue 头值
     */
    public static void addHeader(String headerName, String headerValue) {
        HttpServletRequest request = WebUtils.getRequest();
        CustomRequestWrapper wrapper = new CustomRequestWrapper(request);
        wrapper.addHeader(headerName, headerValue);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(wrapper));
    }

    /**
     * get target value from parameterMap, if not found will throw {@link IllegalArgumentException}.
     *
     * @param req {@link HttpServletRequest}
     * @param key key
     * @return value
     */
    public static String required(final HttpServletRequest req, final String key) {
        String value = req.getParameter(key);
        if (StrUtil.isEmpty(value)) {
            throw new IllegalArgumentException("Param '" + key + "' is required.");
        }
        String encoding = req.getParameter("encoding");
        return resolveValue(value, encoding);
    }

    /**
     * get target value from parameterMap, if not found will return default value.
     *
     * @param req          {@link HttpServletRequest}
     * @param key          key
     * @param defaultValue default value
     * @return value
     */
    public static String optional(final HttpServletRequest req, final String key, final String defaultValue) {
        if (!req.getParameterMap().containsKey(key) || req.getParameterMap().get(key)[0] == null) {
            return defaultValue;
        }
        String value = req.getParameter(key);
        if (StrUtil.isBlank(value)) {
            return defaultValue;
        }
        String encoding = req.getParameter("encoding");
        return resolveValue(value, encoding);
    }

    /**
     * decode target value.
     *
     * @param value    value
     * @param encoding encode
     * @return Decoded data
     */
    private static String resolveValue(String value, String encoding) {
        if (StrUtil.isBlank(encoding)) {
            encoding = StandardCharsets.UTF_8.name();
        }
        try {
            value = new String(value.getBytes(StandardCharsets.UTF_8), encoding);
        } catch (UnsupportedEncodingException ignore) {
        }
        return value.trim();
    }


    /**
     * get accept encode from request.
     *
     * @param req {@link HttpServletRequest}
     * @return accept encode
     */
    public static String getAcceptEncoding(HttpServletRequest req) {
        String encode = StrUtil.emptyToDefault(req.getHeader("Accept-Charset"), StandardCharsets.UTF_8.name());
        encode = encode.contains(",") ? encode.substring(0, encode.indexOf(",")) : encode;
        return encode.contains(";") ? encode.substring(0, encode.indexOf(";")) : encode;
    }

    /**
     * 获取用户代理
     *
     * @return {@link String}
     */
    public static String getUserAgent() {
        return WebUtils.getUserAgent(WebUtils.getRequest());
    }

    /**
     * Returns the value of the request header "user-agent" as a <code>String</code>.
     *
     * @param request HttpServletRequest
     * @return the value of the request header "user-agent", or the value of the request header "client-version" if the
     * request does not have a header of "user-agent".
     */
    public static String getUserAgent(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (StrUtil.isBlank(userAgent)) {
            userAgent = StrUtil
                    .emptyToDefault(request.getHeader("Client-Version"), Strings.EMPTY);
        }
        return userAgent;
    }

    /**
     * IPv4 地址校验
     *
     * @param ip ip地址
     * @return Boolean
     * @since 2020/5/19 17:46
     */
    public Boolean isIpAddress(String ip) {
        String regex = "(((2[0-4]\\d)|(25[0-5]))|(1\\d{2})|([1-9]\\d)|(\\d))[.](((2[0-4]\\d)|(25[0-5]))|(1\\d{2})|([1-9]\\d)|(\\d))[.](((2[0-4]\\d)|(25[0-5]))|(1\\d{2})|([1-9]\\d)|(\\d))[.](((2[0-4]\\d)|(25[0-5]))|(1\\d{2})|([1-9]\\d)|(\\d))";
        return Pattern.compile(regex).matcher(ip).matches();
    }


    private final String[] IP_HEADER_NAMES = new String[]{
            "x-forwarded-for",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"
    };

    private final Predicate<String> IP_PREDICATE = (ip) -> StrUtil.isBlank(ip) || StringPoolConstant.UNKNOWN.equalsIgnoreCase(ip);


    /**
     * 获取ip
     *
     * @return {@link String}
     */
    public String getIpAddr() {
        return WebUtils.getIpAddr(WebUtils.getRequest());
    }

    /**
     * 获取ip
     *
     * @param request HttpServletRequest
     * @return {@code String}
     * @since 2020/5/19 17:43
     */
    public String getIpAddr(@Nullable HttpServletRequest request) {
        if (request == null) {
            return StringPoolConstant.EMPTY;
        }
        String ip = null;
        for (String ipHeader : IP_HEADER_NAMES) {
            ip = request.getHeader(ipHeader);
            if (!IP_PREDICATE.test(ip)) {
                break;
            }
        }
        if (IP_PREDICATE.test(ip)) {
            ip = request.getRemoteAddr();
            if (StringPoolConstant.LOCALHOST.equals(ip) || StringPoolConstant.LOCALHOST_IPV6.equals(ip)) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ip = Optional.ofNullable(inet).map(InetAddress::getHostAddress).orElse(StringPoolConstant.UNKNOWN);
            }
        }

        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        return StrUtil.isBlank(ip) ? null : StringUtils.split(ip, StringPoolConstant.COMMA)[0];
    }

    /**
     * 获取本机网卡地址
     *
     * @return macAddress
     * @since 2020/5/19 17:46
     */
    public String getLocalMac() {
        try {
            InetAddress ia = InetAddress.getLocalHost();
            // 获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。
            byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
            // 下面代码是把mac地址拼装成String
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    sb.append("-");
                }
                // mac[i] & 0xFF 是为了把byte转化为正整数
                String s = Integer.toHexString(mac[i] & 0xFF);
                sb.append(s.length() == 1 ? 0 + s : s);
            }
            // 把字符串所有小写字母改为大写成为正规的mac地址并返回
            return sb.toString().toUpperCase().replaceAll("-", "");
        } catch (Exception e) {
            throw new IllegalStateException("getLocalMAC error");
        }
    }


    /**
     * http(s)校验, 是否为http(s)请求
     *
     * @param url 请求url
     * @return true/false
     */
    public Boolean isHttp(String url) {
        return PATTERN.matcher(url).matches();
    }


}
