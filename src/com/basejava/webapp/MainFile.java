package com.basejava.webapp;

import java.io.File;
import java.util.Objects;

public class MainFile {
    private static StringBuilder indent = new StringBuilder();
    private static final String tab = "     ";

    public static void main(String[] args) {
        File dir = new File(".");
//        System.out.println(String.format("Absolute path  === %s", file.getAbsolutePath()));
//        System.out.println(String.format("Canonical path === %s", file.getCanonicalPath()));
//        System.out.println(String.format("Path           === %s", file.getPath()));
//        System.out.println(String.format("Name           === %s", file.getName()));
//        System.out.println(String.format("Parent         === %s", file.getParent()));
        printFiles(dir);
    }

    public static void printFiles(File dir) {
        Objects.requireNonNull(dir);
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException(dir + "is not directory");
        }
        File[] files = dir.listFiles();
        for (File entity : files) {
            if (entity.isDirectory()) {
                System.out.println(indent + "-->:" + entity.getName());
                indent.append(tab);
                printFiles(entity);
            } else {
                System.out.println(indent + "--:" + entity.getName());
            }
        }
        MainFile.indent.delete(0, tab.length());
    }
}
