package com.basejava.webapp.storage.serializer;

import com.basejava.webapp.model.ContactType;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.model.Section;
import com.basejava.webapp.model.SectionType;

import java.io.*;
import java.util.Map;

import static com.basejava.webapp.util.DataSerializerUtil.DataStream.*;

public class DataStreamSerializer implements SerializerStrategy {

    @Override
    public void write(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            dos.writeInt(r.getContacts().size());
            for (Map.Entry<ContactType, String> entry : r.getContacts().entrySet()) {
                dos.writeUTF(entry.getKey().toString());
                dos.writeUTF(entry.getValue());
            }

            dos.writeInt(r.getSections().size());
            for (Map.Entry<SectionType, Section> entry : r.getSections().entrySet()) {
                dos.writeUTF(entry.getKey().toString());
                writeSection(entry.getValue(), dos);
            }
        }
    }

    @Override
    public Resume read(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            Resume resume = new Resume(dis.readUTF(), dis.readUTF());
            int count = dis.readInt();
            for (int i = 0; i < count; i++) {
                resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }

            count = dis.readInt();
            for (int i = 0; i < count; i++) {
                resume.addSection(SectionType.valueOf(dis.readUTF()), readSection(dis));
            }

            return resume;
        }
    }
}
