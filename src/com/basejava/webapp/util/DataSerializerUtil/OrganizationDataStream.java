package com.basejava.webapp.util.DataSerializerUtil;

import com.basejava.webapp.model.Link;
import com.basejava.webapp.model.Organization;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.basejava.webapp.util.DataSerializerUtil.LinkDataStream.*;
import static com.basejava.webapp.util.DataSerializerUtil.PositionDataStream.*;

class OrganizationDataStream {

    static void writeOrganization(Organization organization, DataOutputStream dos) throws IOException {
        writeLink(organization.getHomePage(), dos);
        dos.writeInt(organization.getPositions().size());
        for (Organization.Position position : organization.getPositions())
            writePosition(position, dos);
    }

    static Organization readOrganization(DataInputStream dis) throws IOException {
        Link link = readLink(dis);
        List<Organization.Position> positions = new ArrayList<>();
        int count = dis.readInt();
        for (int i = 0; i < count; i++)
            positions.add(readPosition(dis));

        return new Organization(link, positions);
    }
}
