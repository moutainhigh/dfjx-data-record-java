package com.datarecord.webapp.submittedBy.bean;

public class Originss {

    private String id;

    private String parentId;

    private String label;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "Origin{" +
                "id='" + id + '\'' +
                ", parentId='" + parentId + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
