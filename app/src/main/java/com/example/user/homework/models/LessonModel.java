package com.example.user.homework.models;

import android.support.annotation.NonNull;

public class LessonModel implements Comparable<LessonModel>{
    private Integer number;
    private String lesson, homework;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getLesson() {
        return lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public String getHomework() {
        return homework;
    }

    public void setHomework(String homework) {
        this.homework = homework;
    }

    @Override
    public int compareTo(@NonNull LessonModel lesson) {
        return number - lesson.number;
    }
}
