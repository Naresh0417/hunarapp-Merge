package com.hamstechonline.activities.splash;

import com.google.gson.annotations.SerializedName;

public class VersionRequest {

    String apikey;

    @SerializedName("status")
    private JsonStatus versionResponse;

    public VersionRequest(String apikey){
        this.apikey = apikey;
    }

    public JsonStatus getVersionResponse() {
        return versionResponse;
    }

    public void setVersionResponse(JsonStatus versionResponse) {
        this.versionResponse = versionResponse;
    }
}
