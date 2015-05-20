package net.ejpm.conn.remote;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import net.ejpm.conn.remote.resources.RemoteFileResource;

public interface RemoteFileHost<E extends RemoteFileResource> {

    public File getFile(final E remoteFile, final String localName) throws IOException, FileNotFoundException;

    public File[] getFileListFromCurrentDirectory(final String localPath) throws IOException, FileNotFoundException;

    public boolean changeDirectory(final String remoteDir) throws IOException;

}
