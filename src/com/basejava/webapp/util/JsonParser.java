package com.basejava.webapp.util;

import com.basejava.webapp.model.Section;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Reader;
import java.io.Writer;

public class JsonParser {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Section.class, new JsonSectionAdapter<>())
            .create();

    public static <T> void write(T object, Writer writer) {
        gson.toJson(object, writer);
    }

    public static <T> T read(Reader reader, Class<T> classOfT) {
        return gson.fromJson(reader, classOfT);
    }
}
