package com.basejava.webapp.util;

import com.basejava.webapp.model.ListSection;
import com.basejava.webapp.model.Section;
import com.basejava.webapp.model.TextSection;

public class Test {
    public static void main(String[] args) {
        Section section = new TextSection();
        System.out.println(section instanceof Section);
        System.out.println(section instanceof TextSection);
        System.out.println(section instanceof ListSection);
    }
}
