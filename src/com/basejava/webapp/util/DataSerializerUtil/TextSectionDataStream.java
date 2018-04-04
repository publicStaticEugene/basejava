package com.basejava.webapp.util.DataSerializerUtil;

import com.basejava.webapp.model.Section;
import com.basejava.webapp.model.TextSection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

class TextSectionDataStream {

    static void writeTextSection(TextSection textSection, DataOutputStream dos) throws IOException {
        dos.writeUTF(textSection.getClass().getSimpleName());
        dos.writeUTF(textSection.getContent());
    }

    static Section readTextSection(DataInputStream dis) throws IOException {
        return new TextSection(dis.readUTF());
    }
}
