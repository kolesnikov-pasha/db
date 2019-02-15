package com.example.user.homework;

public class FileModel {
    String ref;
    String name;
    String uri;

    public FileModel(String ref, String name, String uri) {
        this.ref = ref;
        this.name = name;
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public FileModel() {
    }

    public String getRef() {
        return ref;
    }

    public String getName() {
        return name;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public void setName(String name) {
        this.name = name;
    }

}
