package com.basejava.webapp.util.DataSerializerUtil;

import com.basejava.webapp.model.ListSection;
import com.basejava.webapp.model.OrganizationSection;
import com.basejava.webapp.model.Section;
import com.basejava.webapp.model.TextSection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static com.basejava.webapp.util.DataSerializerUtil.TextSectionDataStream.*;
import static com.basejava.webapp.util.DataSerializerUtil.ListSectionDataStream.*;
import static com.basejava.webapp.util.DataSerializerUtil.OrganizationSectionDataStream.*;

public class DataStream {

    public static void writeSection(Section section, DataOutputStream dos) throws IOException {
        if (section instanceof TextSection) writeTextSection((TextSection) section, dos);
        else if (section instanceof ListSection) writeListSection((ListSection) section, dos);
        else if (section instanceof OrganizationSection) writeOrganizationSection((OrganizationSection) section, dos);
    }

    public static Section readSection(DataInputStream dis) throws IOException {
        String sectionClass = dis.readUTF();
        Section section = null;
        if (sectionClass.equals(TextSection.class.getSimpleName())) section = readTextSection(dis);
        else if (sectionClass.equals(ListSection.class.getSimpleName())) section = readListSection(dis);
        else if (sectionClass.equals(OrganizationSection.class.getSimpleName())) section =readOrganizationSection(dis);
        return section;
    }
}
