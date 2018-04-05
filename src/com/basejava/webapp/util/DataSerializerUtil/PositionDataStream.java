package com.basejava.webapp.util.DataSerializerUtil;

import com.basejava.webapp.model.Organization;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;

class PositionDataStream {

    static void writePosition(Organization.Position position, DataOutputStream dos) throws IOException {
        dos.writeUTF(position.getStartDate().toString());
        dos.writeUTF(position.getEndDate().toString());
        dos.writeUTF(position.getTitle());
        if (position.getDescription() != null)
            dos.writeUTF(position.getDescription());
        else
            dos.writeUTF("null");
    }

    static Organization.Position readPosition(DataInputStream dis) throws IOException {
        LocalDate startDate = LocalDate.parse(dis.readUTF());
        LocalDate endDate = LocalDate.parse(dis.readUTF());
        String title = dis.readUTF();
        String description = dis.readUTF();
        return new Organization.Position(
                startDate.getYear(),
                startDate.getMonth(),
                endDate.getYear(),
                endDate.getMonth(),
                title,
                description.equals("null") ? null : description
        );
    }
}
