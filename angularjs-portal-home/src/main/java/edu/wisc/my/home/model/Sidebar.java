package edu.wisc.my.home.model;

public class Sidebar {
    
    private String title;
    private String hyperlink;
    private String target;
    private String faIcon;
    private String displayFlag;
    private Double importance;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getHyperlink() {
        return hyperlink;
    }
    public void setHyperlink(String hyperlink) {
        this.hyperlink = hyperlink;
    }
    public String getTarget() {
        return target;
    }
    public void setTarget(String target) {
        this.target = target;
    }
    public String getFaIcon() {
        return faIcon;
    }
    public void setFaIcon(String faIcon) {
        this.faIcon = faIcon;
    }
    public String getDisplayFlag() {
        return displayFlag;
    }
    public void setDisplayFlag(String displayFlag) {
        this.displayFlag = displayFlag;
    }
    public Double getImportance() {
        return importance;
    }
    public void setImportance(Double importance) {
        this.importance = importance;
    }
    
}
