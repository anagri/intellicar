package com;

import java.io.IOException;
import java.io.OutputStream;

public class SocketOutputWrapper extends OutputStream {
    private final OutputStream out;
    private final Database database;

    public SocketOutputWrapper(OutputStream out, Database database) {
        this.out = out;
        this.database = database;
    }

    @Override
    public void write(byte[] buffer) throws IOException {
        database.insertOutput(Database.requestId, buffer);
        super.write(buffer);
    }

    @Override
    public void write(int oneByte) throws IOException {
        this.out.write(oneByte);
    }

    @Override
    public void close() throws IOException {
        out.close();
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }
}
