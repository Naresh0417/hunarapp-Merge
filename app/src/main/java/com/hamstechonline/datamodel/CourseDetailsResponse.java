package com.hamstechonline.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hamstechonline.datamodel.homepage.SuccessStory;
import com.hamstechonline.datamodel.mycources.AccordionDatum;

import java.util.List;

public class CourseDetailsResponse {

    @SerializedName("status")
    private String status;
    @SerializedName("lessons")
    private List<LessonData> lessonData;
    @SerializedName("course_details")
    private CourseDetailsData courseDetails;
    @SerializedName("course_overview")
    @Expose
    private List<CourseOverview> courseOverview = null;
    @SerializedName("questions")
    @Expose
    private List<Question> questions = null;
    @SerializedName("answers")
    @Expose
    private List<Answer> answers = null;
    /*@SerializedName("faculties")
    @Expose
    private List<Faculty> faculties = null;*/
    @SerializedName("accordion_data")
    @Expose
    private List<AccordionDatum> accordionData = null;
    @SerializedName("certified_students")
    @Expose
    private String certifiedStudents;
    @SerializedName("hunar_students")
    @Expose
    private String hunarStudents;
    @SerializedName("success_stories")
    @Expose
    private List<SuccessStory> successStories = null;
    @SerializedName("footer_ribbon_image")
    @Expose
    private String footerRibbonImage;
    @SerializedName("expert_faculty")
    private ExpertFaculty expertFaculty;
    @SerializedName("testimonials")
    @Expose
    private List<Testimonials> testimonials = null;
    @SerializedName("email")
    @Expose
    private String email;

    String appname, page, apikey, courseId, language, lang, phone, notification_clicked;

    public CourseDetailsResponse(String appname, String page, String apikey, String courseId, String language,
                                 String lang, String phone, String notification_clicked) {
        this.appname = appname;
        this.page = page;
        this.apikey = apikey;
        this.courseId = courseId;
        this.language = language;
        this.lang = lang;
        this.phone = phone;
        this.notification_clicked = notification_clicked;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<LessonData> getLessonData() {
        return lessonData;
    }

    public void setLessonData(List<LessonData> lessonData) {
        this.lessonData = lessonData;
    }

    public CourseDetailsData getCourseDetails() {
        return courseDetails;
    }

    public void setCourseDetails(CourseDetailsData courseDetails) {
        this.courseDetails = courseDetails;
    }

    public List<CourseOverview> getCourseOverview() {
        return courseOverview;
    }

    public void setCourseOverview(List<CourseOverview> courseOverview) {
        this.courseOverview = courseOverview;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    /*public List<Faculty> getFaculties() {
        return faculties;
    }

    public void setFaculties(List<Faculty> faculties) {
        this.faculties = faculties;
    }*/

    public List<AccordionDatum> getAccordionData() {
        return accordionData;
    }

    public void setAccordionData(List<AccordionDatum> accordionData) {
        this.accordionData = accordionData;
    }

    public String getCertifiedStudents() {
        return certifiedStudents;
    }

    public void setCertifiedStudents(String certifiedStudents) {
        this.certifiedStudents = certifiedStudents;
    }

    public String getHunarStudents() {
        return hunarStudents;
    }

    public void setHunarStudents(String hunarStudents) {
        this.hunarStudents = hunarStudents;
    }

    public List<SuccessStory> getSuccessStories() {
        return successStories;
    }

    public void setSuccessStories(List<SuccessStory> successStories) {
        this.successStories = successStories;
    }

    public String getFooterRibbonImage() {
        return footerRibbonImage;
    }

    public void setFooterRibbonImage(String footerRibbonImage) {
        this.footerRibbonImage = footerRibbonImage;
    }

    public ExpertFaculty getExpertFaculty() {
        return expertFaculty;
    }

    public void setExpertFaculty(ExpertFaculty expertFaculty) {
        this.expertFaculty = expertFaculty;
    }

    public List<Testimonials> getTestimonials() {
        return testimonials;
    }

    public void setTestimonials(List<Testimonials> testimonials) {
        this.testimonials = testimonials;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}