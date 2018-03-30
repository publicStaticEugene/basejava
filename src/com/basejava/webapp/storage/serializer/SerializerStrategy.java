package com.basejava.webapp.storage.serializer;

import com.basejava.webapp.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface SerializerStrategy {

    void write(Resume r, OutputStream os) throws IOException;

    Resume read(InputStream is) throws IOException;
}
