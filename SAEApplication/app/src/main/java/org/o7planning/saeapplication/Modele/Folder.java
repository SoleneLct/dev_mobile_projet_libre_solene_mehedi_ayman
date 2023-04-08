package org.o7planning.saeapplication.Modele;

public class Folder {

    private int id;
    private String title;
    private int userId;
    public Folder(int id) {
        this.id = id;
    }
    public Folder(String title) {
        this.title = title;
    }
    public Folder(int id, String title) {
        this.id = id;
        this.title = title;
    }
    public Folder(int id, String title,int userId) {
        this.id = id;
        this.title = title;
        this.userId = userId;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }
}
