package org.unibl.etf.fitsocial.entity;

public enum FileType {
    IMAGE("IMAGE"),
    VIDEO("VIDEO"),
    DOCUMENT("DOCUMENT"),
    PROFILE("PROFILE"),;

    private final String folderName;
    FileType(String folderName) { this.folderName = folderName; }
    public String getFolderName() { return folderName; }
}
