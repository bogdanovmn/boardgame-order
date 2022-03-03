package com.github.bogdanovmn.boardgameorder.web.app;

public class MenuItem {
    private final String id;
    private final String url;
    private final String title;
    private final boolean admin;
    private boolean selected = false;

    public MenuItem(String id, String url, String title) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.admin = url.startsWith("/admin/");
    }

    public void select() {
        this.selected = true;
    }

    public boolean is(String current) {
        return this.id.equals(current);
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public boolean isSelected() {
        return selected;
    }

    public boolean isAdmin() {
        return admin;
    }
}

