package com.basejava.webapp;

import java.io.File;
import java.io.IOException;

public class MainFile {
    public static void main(String[] args) throws IOException {
        File file = new File(".\\.gitignore");
        System.out.println(String.format("Absolute path  === %s", file.getAbsolutePath()));
        System.out.println(String.format("Canonical path === %s", file.getCanonicalPath()));
        System.out.println(String.format("Path           === %s", file.getPath()));
        System.out.println(String.format("Name           === %s", file.getName()));
        System.out.println(String.format("Parent         === %s", file.getParent()));
    }
}
