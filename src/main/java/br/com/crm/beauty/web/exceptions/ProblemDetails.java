package br.com.crm.beauty.web.exceptions;

import java.util.Map;

public class ProblemDetails {
    private String type;
    private String title;
    private int status;
    private String detail;
    private String instance;
    private Map<String, String[]> errors;
    private String traceId;

    public ProblemDetails() {
    }

    public ProblemDetails(String type, String title, int status, String detail, String instance, String traceId,
            Map<String, String[]> errors) {
        this.type = type;
        this.title = title;
        this.status = status;
        this.detail = detail;
        this.instance = instance;
        this.traceId = traceId;
        this.errors = errors;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public int getStatus() {
        return status;
    }

    public String getDetail() {
        return detail;
    }

    public String getInstance() {
        return instance;
    }

    public String getTraceId() {
        return traceId;
    }

    public Map<String, String[]> getErrors() {
        return errors;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public void setErrors(Map<String, String[]> errors) {
        this.errors = errors;
    }
}