package com.basejava.webapp.util.DataSerializerUtil;

import com.basejava.webapp.model.Link;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

class LinkDataStream {

    static void writeLink(Link link, DataOutputStream dos) throws IOException {
        dos.writeUTF(link.getName());
        if (link.getUrl() != null)
            dos.writeUTF(link.getUrl());
        else
            dos.writeUTF("null");
    }

    static Link readLink(DataInputStream dis) throws IOException {
        String name = dis.readUTF();
        String url = dis.readUTF();
        return new Link(
                name,
                url.equals("null") ? null : url
        );
    }
}
