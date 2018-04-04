package com.basejava.webapp.util.DataSerializerUtil;

import com.basejava.webapp.model.Organization;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.Month;

class PositionDataStream {

    static void writePosition(Organization.Position position, DataOutputStream dos) throws IOException {
        dos.writeInt(position.getStartDate().getYear());
        dos.writeInt(position.getStartDate().getMonth().getValue());
        dos.writeInt(position.getEndDate().getYear());
        dos.writeInt(position.getEndDate().getMonth().getValue());
        dos.writeUTF(position.getTitle());
        if (position.getDescription() != null)
            dos.writeUTF(position.getDescription());
    }

    static Organization.Position readPosition(DataInputStream dis) throws IOException {
        return new Organization.Position(
                dis.readInt(),
                Month.of(dis.readInt()),
                dis.readInt(),
                Month.of(dis.readInt()),
                dis.readUTF(),
                dis.readUTF()
        );
    }
}
