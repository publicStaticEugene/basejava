package com.basejava.webapp.util.DataSerializerUtil;

import com.basejava.webapp.model.Organization;
import com.basejava.webapp.model.OrganizationSection;
import com.basejava.webapp.model.Section;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.basejava.webapp.util.DataSerializerUtil.OrganizationDataStream.*;

class OrganizationSectionDataStream {

    static void writeOrganizationSection(OrganizationSection organizationSection, DataOutputStream dos) throws IOException {
        dos.writeUTF(organizationSection.getClass().getSimpleName());
        dos.writeInt(organizationSection.getOrganizations().size());
        for (Organization organization : organizationSection.getOrganizations())
            writeOrganization(organization, dos);
    }

    static Section readOrganizationSection(DataInputStream dis) throws IOException {
        List<Organization> organizations = new ArrayList<>();
        int count = dis.readInt();
        for (int i = 0; i < count; i++)
            organizations.add(readOrganization(dis));

        return new OrganizationSection(organizations);
    }
}
