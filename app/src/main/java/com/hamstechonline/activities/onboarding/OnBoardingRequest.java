package com.hamstechonline.activities.onboarding;

import com.google.gson.annotations.SerializedName;

public class OnBoardingRequest {
    @SerializedName("broadingpage")
    public OnBoardingResponse onBoardingResponse;

    public OnBoardingResponse getOnBoardingResponse() {
        return onBoardingResponse;
    }

    public void setOnBoardingResponse(OnBoardingResponse onBoardingResponse) {
        this.onBoardingResponse = onBoardingResponse;
    }
}
