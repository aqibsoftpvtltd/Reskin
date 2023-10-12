package com.kasa777.chat.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MediaFileModel implements Parcelable
{

    @SerializedName("fieldname")
    @Expose
    private String fieldname;
    @SerializedName("originalname")
    @Expose
    private String originalname;
    @SerializedName("encoding")
    @Expose
    private String encoding;
    @SerializedName("mimetype")
    @Expose
    private String mimetype;
    @SerializedName("destination")
    @Expose
    private String destination;
    @SerializedName("filename")
    @Expose
    private String filename;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("size")
    @Expose
    private Integer size;
    public final static Parcelable.Creator<MediaFileModel> CREATOR = new Creator<MediaFileModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public MediaFileModel createFromParcel(Parcel in) {
            return new MediaFileModel(in);
        }

        public MediaFileModel[] newArray(int size) {
            return (new MediaFileModel[size]);
        }

    }
            ;

    protected MediaFileModel(Parcel in) {
        this.fieldname = ((String) in.readValue((String.class.getClassLoader())));
        this.originalname = ((String) in.readValue((String.class.getClassLoader())));
        this.encoding = ((String) in.readValue((String.class.getClassLoader())));
        this.mimetype = ((String) in.readValue((String.class.getClassLoader())));
        this.destination = ((String) in.readValue((String.class.getClassLoader())));
        this.filename = ((String) in.readValue((String.class.getClassLoader())));
        this.path = ((String) in.readValue((String.class.getClassLoader())));
        this.size = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public MediaFileModel() {
    }

    public String getFieldname() {
        return fieldname;
    }

    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }

    public String getOriginalname() {
        return originalname;
    }

    public void setOriginalname(String originalname) {
        this.originalname = originalname;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(fieldname);
        dest.writeValue(originalname);
        dest.writeValue(encoding);
        dest.writeValue(mimetype);
        dest.writeValue(destination);
        dest.writeValue(filename);
        dest.writeValue(path);
        dest.writeValue(size);
    }

    public int describeContents() {
        return 0;
    }

}