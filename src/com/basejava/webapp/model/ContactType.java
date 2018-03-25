package com.basejava.webapp.model;

public enum ContactType {
    MOBILE("Мобильный"),
    PHONE("Домашний тел."),
    SKYPE("Skype"),
    MAIL("Почта"),
    GITHUB("Профиль GitHub"),
    STACKOVERFLOW("Профиль Stackoverflow"),
    HOME_PAGE("Домашняя страница");

    private String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
