package com.hamstechonline.activities.lesson;

import com.google.gson.annotations.SerializedName;

public class LessonData {
    @SerializedName("detail")
    private LessonDetail lessonDetail;

    String language;
    int lessonId;

    public LessonData(int lessonId, String language){
        this.lessonId = lessonId;
        this.language = language;
    }

    public LessonDetail getLessonDetail() {
        return lessonDetail;
    }

    public void setLessonDetail(LessonDetail lessonDetail) {
        this.lessonDetail = lessonDetail;
    }
}
