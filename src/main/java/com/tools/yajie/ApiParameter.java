package com.tools.yajie;

/**
 * Swagger/OpenAPI文档中的API参数模型
 */
public class ApiParameter {

    private String name;
    private String in;
    private boolean required;
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIn() {
        return in;
    }

    public void setIn(String in) {
        this.in = in;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ApiParameter{" +
                "name='" + name + '\'' +
                ", in='" + in + '\'' +
                ", required=" + required +
                ", type='" + type + '\'' +
                '}';
    }
}
