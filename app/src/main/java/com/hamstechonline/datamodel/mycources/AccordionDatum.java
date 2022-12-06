package com.hamstechonline.datamodel.mycources;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AccordionDatum {
    @SerializedName("heading")
    @Expose
    private String heading;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("content")
    @Expose
    private String content;
    /*@SerializedName("list")
    @Expose
    private List<Listsyllabus> list = null;*/

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /*public List<Listsyllabus> getList() {
        return list;
    }

    public void setList(List<Listsyllabus> list) {
        this.list = list;
    }*/
}
