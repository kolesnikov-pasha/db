package com.example.user.homework;

import android.support.annotation.NonNull;

public class Hometask{
    String task, attach;
    Boolean isAttach = false;

    public String getAttach() {
        return attach;
    }

    public Boolean getIsAttach(){
        return isAttach;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public void setAttach(Boolean attach) {
        isAttach = attach;
    }

    public Hometask() {}

    public Hometask(String task, String attach) {
        this.task = task;
        this.attach = attach;
        if (attach.isEmpty()){
            isAttach = false;
        }
        else{
            isAttach = true;
        }
    }
}
