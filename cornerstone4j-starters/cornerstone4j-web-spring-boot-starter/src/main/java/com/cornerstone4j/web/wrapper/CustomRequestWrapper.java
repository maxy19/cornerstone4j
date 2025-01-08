package com.cornerstone4j.web.wrapper;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.ObjectUtils;

import java.util.*;


public class CustomRequestWrapper extends ContentCachingRequestWrapper {

    private final Map<String, String> headerMap = new HashMap();

    private final Map<String, String[]> paramMap = new HashMap();

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request the {@link HttpServletRequest} to be wrapped.
     * @throws IllegalArgumentException if the request is null
     */
    public CustomRequestWrapper(HttpServletRequest request) {
        super(request);
    }


    /**
     * 添加标题
     *
     * @param name  名字
     * @param value 值
     * @return {@link CustomRequestWrapper}
     */
    public CustomRequestWrapper addHeader(String name, String value) {
        headerMap.putIfAbsent(name, value);
        return this;
    }

    /**
     * 添加参数
     *
     * @param name  名字
     * @param value 值
     * @return {@link CustomRequestWrapper}
     */
    public CustomRequestWrapper addParameter(String name, Object value) {
        if (value != null) {
            if (value instanceof String[]) {
                paramMap.putIfAbsent(name, (String[]) value);
            } else if (value instanceof String) {
                paramMap.putIfAbsent(name, new String[]{(String) value});
            } else {
                paramMap.putIfAbsent(name, new String[]{String.valueOf(value)});
            }
        }
        return this;
    }


    @Override
    public String getHeader(String name) {
        String headerValue = super.getHeader(name);
        if (headerMap.containsKey(name)) {
            headerValue = headerMap.get(name);
        }
        return headerValue;
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        List<String> names = Collections.list(super.getHeaderNames());
        names.addAll(headerMap.keySet());
        return Collections.enumeration(names);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        List<String> values = Collections.list(super.getHeaders(name));
        if (headerMap.containsKey(name)) {
            values.add(headerMap.get(name));
        }
        return Collections.enumeration(values);
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if (paramMap.containsKey(name)) {
            String[] values = paramMap.get(name);
            if (ObjectUtils.isEmpty(values)) {
                return null;
            }
            return values[0];
        }
        return value;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        paramMap.putAll(super.getParameterMap());
        return paramMap;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        List<String> names = Collections.list(super.getParameterNames());
        names.addAll(paramMap.keySet());
        return Collections.enumeration(names);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] parameterValues = super.getParameterValues(name);
        if (paramMap.containsKey(name)) {
            parameterValues = paramMap.get(name);
        }
        return parameterValues;
    }
}
