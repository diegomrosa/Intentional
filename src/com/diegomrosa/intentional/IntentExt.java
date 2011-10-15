package com.diegomrosa.intentional;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class IntentExt implements Parcelable {
    private Intent originalIntent;
    private Intent updatedIntent;
    private String inferredFileName;
    private String inferredMimeType;

    public IntentExt(Intent originalIntent) {
        this.originalIntent = originalIntent;
        initUpdatedIntent();
    }

    public void reset() {
        initUpdatedIntent();
    }

    private void initUpdatedIntent() {
        updatedIntent = new Intent(originalIntent);
        if (updatedIntent.getComponent() != null) {
            updatedIntent.setComponent(null);
        }
    }

    public Intent getOriginalIntent() {
        return originalIntent;
    }

    public Intent getUpdatedIntent() {
        return updatedIntent;
    }

    public Intent getShareIntent() {
        return new Intent(Intent.ACTION_SEND, updatedIntent.getData());
    }

    public Uri getData() {
        return updatedIntent.getData();
    }

    public Uri getDataStream() {
        Uri dataStream = updatedIntent.getData();

        if (dataStream != null) {
            return dataStream;
        }
        dataStream = updatedIntent.getParcelableExtra(Intent.EXTRA_STREAM);
        return dataStream;
    }

    public String getType() {
        return updatedIntent.getType();
    }

    public String getAction() {
        return updatedIntent.getAction();
    }

    public Set<String> getCategories() {
        return updatedIntent.getCategories();
    }

    public Bundle getExtras() {
        return updatedIntent.getExtras();
    }

    public int getFlags() {
        return updatedIntent.getFlags();
    }

    public ComponentName getComponent() {
        return updatedIntent.getComponent();
    }

    public String getInferredFileName() {
        return inferredFileName;
    }

    public void setInferredFileName(String inferredFileName) {
        this.inferredFileName = inferredFileName;
    }

    public String getInferredMimeType() {
        return inferredMimeType;
    }

    public void setInferredMimeType(String inferredMimeType) {
        this.inferredMimeType = inferredMimeType;
    }


    // Implements the Parcelable interface:

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(this.originalIntent, 0);
        out.writeParcelable(this.updatedIntent, 0);
        out.writeString(this.inferredFileName);
        out.writeString(this.inferredMimeType);
    }

    public static final Creator<IntentExt> CREATOR = new Creator<IntentExt>() {
        public IntentExt createFromParcel(Parcel in) {
            return new IntentExt(in);
        }

        public IntentExt[] newArray(int size) {
            return new IntentExt[size];
        }
    };

    public IntentExt(Parcel in) {
        this.originalIntent = in.readParcelable(null);
        this.updatedIntent = in.readParcelable(null);
        this.inferredFileName = in.readString();
        this.inferredMimeType = in.readString();
    }
}
