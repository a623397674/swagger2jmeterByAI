package com.tools.yajie;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger/OpenAPI文档中的API路径模型
 */
public class ApiPath {

    private String path;
    private String method;
    private String summary;
    private String description;
    private List<String> tags;
    private List<ApiParameter> parameters;
    private String requestBodySchema;
    private String requestExample;

    public ApiPath() {
        this.tags = new ArrayList<>();
        this.parameters = new ArrayList<>();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<ApiParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<ApiParameter> parameters) {
        this.parameters = parameters;
    }

    public String getRequestBodySchema() {
        return requestBodySchema;
    }

    public void setRequestBodySchema(String requestBodySchema) {
        this.requestBodySchema = requestBodySchema;
    }

    public String getRequestExample() {
        return requestExample;
    }

    public void setRequestExample(String requestExample) {
        this.requestExample = requestExample;
    }

    @Override
    public String toString() {
        return "ApiPath{" +
                "path='" + path + '\'' +
                ", method='" + method + '\'' +
                ", summary='" + summary + '\'' +
                ", tags=" + tags +
                '}';
    }
}
