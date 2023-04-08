package org.o7planning.saeapplication.Modele;

public class Folder {
    private String title;
    private int image;

    public Folder(String title, int image) {
        this.title = title;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public int getImage() {
        return image;
    }
}
