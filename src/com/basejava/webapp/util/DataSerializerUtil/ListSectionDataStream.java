package com.basejava.webapp.util.DataSerializerUtil;

import com.basejava.webapp.model.ListSection;
import com.basejava.webapp.model.Section;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class ListSectionDataStream {

    static void writeListSection(ListSection listSection, DataOutputStream dos) throws IOException {
        dos.writeUTF(listSection.getClass().getSimpleName());
        dos.writeInt(listSection.getItems().size());
        for (String item : listSection.getItems())
            dos.writeUTF(item);
    }

    static Section readListSection(DataInputStream dis) throws IOException {
        List<String> items = new ArrayList<>();
        int count = dis.readInt();
        for (int i = 0; i < count; i++)
            items.add(dis.readUTF());

        return new ListSection(items);
    }
}
