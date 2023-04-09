package org.o7planning.saeapplication.Modele;

public class Folder {

    private int id;
    private String title;
    private int userId;

    public Folder(String title,int userId) {
        this.title = title;
        this.userId = userId;
    }
    public Folder(int id, String title,int userId) {
        this.id = id;
        this.title = title;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public int getUserId() {
        return userId;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
}
