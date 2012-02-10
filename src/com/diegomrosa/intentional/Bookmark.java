package com.diegomrosa.intentional;

import android.content.Intent;
import android.net.Uri;
import android.text.format.DateFormat;

import java.util.Date;

public class Bookmark {
    private Long id;
    private String name;
    private Date creationDate;
    private Intent intent;

    public Bookmark(String name, Date creationDate, Intent intent) {
        this(null, name, creationDate, intent);
    }

    public Bookmark(Long id, String name, Date creationDate, Intent intent) {
        this.id = id;
        this.name = name;
        this.creationDate = creationDate;
        this.intent = intent;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Intent getIntent() {
        return intent;
    }

    public String getDateString() {
        return DateFormat.format("yyyy-MM-dd hh:mm", creationDate).toString();
    }

    public String getDataString() {
        return Intents.getDataStreamOrText(intent).toString();
    }

    @Override
    public String toString() {
        if (name != null) {
            return name;
        }
        if (intent.getComponent() != null) {
            return intent.getComponent().getClassName();
        }
        return intent.getAction();
    }
}
