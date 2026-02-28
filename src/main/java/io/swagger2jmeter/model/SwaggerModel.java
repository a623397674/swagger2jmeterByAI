package io.swagger2jmeter.model;

import java.util.ArrayList;
import java.util.List;

public class SwaggerModel {

    private List<Tag> tags;
    private List<ApiPath> paths;
    private String serverUrl;
    private String contactName;

    public SwaggerModel() {
        this.tags = new ArrayList<>();
        this.paths = new ArrayList<>();
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<ApiPath> getPaths() {
        return paths;
    }

    public void setPaths(List<ApiPath> paths) {
        this.paths = paths;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
}
