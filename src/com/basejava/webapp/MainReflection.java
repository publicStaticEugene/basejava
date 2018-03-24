package com.basejava.webapp;

import com.basejava.webapp.model.Resume;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainReflection {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Resume resume = new Resume("uuid01", "dummy");
        Class resumeClass = resume.getClass();
        Field field = resumeClass.getDeclaredFields()[0];
        Method method = resumeClass.getMethod("toString");
        String value = (String) method.invoke(resume);
        System.out.println(value);
    }
}
