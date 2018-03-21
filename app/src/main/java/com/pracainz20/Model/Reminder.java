package com.pracainz20.Model;

import java.util.Date;

/**
 * Created by Grzechu on 19.03.2018.
 */

public class Reminder {

    private String author_id;
    private String type_reminder;
    private String author_name;
    private String firstAuthorName;
    private String lastAuthorName;
    private Date create_dt;
    private String profileImage;

    public String getAuthor_id() {
        return author_id;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getFirstAuthorName() {
        return firstAuthorName;
    }

    public void setFirstAuthorName(String firstAuthorName) {
        this.firstAuthorName = firstAuthorName;
    }

    public String getLastAuthorName() {
        return lastAuthorName;
    }

    public void setLastAuthorName(String lastAuthorName) {
        this.lastAuthorName = lastAuthorName;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getType_reminder() {
        return type_reminder;
    }

    public void setType_reminder(String type_reminder) {
        this.type_reminder = type_reminder;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public Date getCreate_dt() {
        return create_dt;
    }

    public void setCreate_dt(Date create_dt) {
        this.create_dt = create_dt;
    }
}
