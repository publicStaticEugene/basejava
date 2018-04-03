package com.basejava.webapp.storage.serializer;

import com.basejava.webapp.model.Resume;
import com.basejava.webapp.util.JsonParser;

import java.io.*;

public class JsonStreamSerializer implements SerializerStrategy {

    @Override
    public void write(Resume r, OutputStream os) throws IOException {
        try (Writer writer = new OutputStreamWriter(os)) {
            JsonParser.write(r, writer);
        }
    }

    @Override
    public Resume read(InputStream is) throws IOException {
        try (Reader reader = new InputStreamReader(is)) {
            return JsonParser.read(reader, Resume.class);
        }
    }
}
