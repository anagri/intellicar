package com;

import java.io.IOException;
import java.io.InputStream;

public class SocketInputWrapper extends InputStream {
    private final InputStream input;
    private final Database database;

    public SocketInputWrapper(InputStream input, Database database) {
        this.input = input;
        this.database = database;
    }

    @Override
    public int available() throws IOException {
        return input.available();
    }

    @Override
    public void close() throws IOException {
        input.close();
    }

    @Override
    public void mark(int readlimit) {
        input.mark(readlimit);
    }

    @Override
    public boolean markSupported() {
        return input.markSupported();
    }

    @Override
    public int read() throws IOException {
        return input.read();
    }

    @Override
    public int read(byte[] buffer) throws IOException {
        int read = input.read(buffer);
        Database.requestId++;
        database.insertInput(Database.requestId, buffer);
        return read;
    }

    @Override
    public int read(byte[] buffer, int byteOffset, int byteCount) throws IOException {
        return input.read(buffer, byteOffset, byteCount);
    }

    @Override
    public void reset() throws IOException {
        input.reset();
    }

    @Override
    public long skip(long byteCount) throws IOException {
        return input.skip(byteCount);
    }
}
