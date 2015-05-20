package net.ejpm.conn.remote.zos;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import net.ejpm.conn.remote.RemoteHostException;
import net.ejpm.conn.remote.RemoteHost;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import net.ejpm.conn.remote.RemoteFileHost;
import net.ejpm.conn.remote.resources.RemoteResource;
import net.ejpm.conn.remote.zos.dataset.DataSet;
import net.ejpm.conn.remote.zos.dataset.DatasetFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ZOSRemoteHost implements RemoteHost, RemoteFileHost<DataSet> {

    private final static Logger LOGGER = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
    private static final String SINGLE_QUOTE = "'";

    private final String username;
    private final transient String password;
    private final String host;
    private final FTPClient ftpClient;

    public ZOSRemoteHost(final String host, final String username, final String password, final FTPClient ftp_client) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.ftpClient = ftp_client;
        ftpSetup();
    }

    private void ftpSetup() {
        ftpClient.enterLocalPassiveMode();
    }

    @Override
    public void connect() throws RemoteHostException {
        try {
            ftpClient.connect(host);
            LOGGER.debug("Connect response: " + ftpClient.getReplyString());
        } catch (IOException ex) {
            LOGGER.fatal("Error connecting to remote host " + host);
            throw new RemoteHostException(ex.getCause());
        }
    }

    @Override
    public boolean login() throws RemoteHostException {
        try {
            final boolean login_rc = ftpClient.login(username, password);
            LOGGER.debug("Login response: " + ftpClient.getReplyString());
            return login_rc;
        } catch (IOException ex) {
            LOGGER.fatal("Connection error to remote host " + host);
            throw new RemoteHostException(ex.getCause());
        }
    }

    @Override
    public void disconnect() throws RemoteHostException {
        try {
            ftpClient.disconnect();
            LOGGER.debug("Disconnect response: " + ftpClient.getReplyString());
        } catch (IOException ex) {
            LOGGER.fatal("Error disconnecting from remote host " + host);
            throw new RemoteHostException(ex.getCause());
        }
    }

    private RemoteResource getDataset(final String resourceId) throws RemoteHostException {
        //final 

        try {
            boolean a = ftpClient.changeWorkingDirectory(resourceId);
            System.out.println(a);
            final FTPFile[] files = ftpClient.listFiles();
            System.out.println("Count: " + files.length);
            for (int i = 0; i < files.length; i++) {
                System.out.println(files[i].getName());
            }
            LOGGER.info("getDataset Response: " + ftpClient.getReplyString().trim());
        } catch (IOException ex) {
            LOGGER.fatal("Error getting dataset from remote host " + host);
            throw new RemoteHostException(ex.getCause());
        }
        return null;
    }

    public FTPClient getFtp_client() {
        return ftpClient;
    }

    @Override
    public File[] getFileListFromCurrentDirectory(final String localPath) throws IOException {
        final FTPFile[] ftpFiles = ftpClient.listFiles();
        final File[] localFiles = new File[ftpFiles.length];

        for (int i = 0; i < ftpFiles.length; i++) {
            //  localFiles[i] = getFile(ftpFiles[i]., localPath);
        }

        return null;
    }

    public List<DataSet> getRemoteResourceList() throws IOException {
        final LinkedList<DataSet> list = new LinkedList<>();
        final FTPFile[] ftpFiles = ftpClient.listFiles();
        LOGGER.info(ftpClient.getReplyString().trim());
        if (getCurrentDirectoryType() == DirType.PDS) {
            for (FTPFile file : ftpFiles) {
                list.add(new DatasetFactory().createFromID(ftpClient.printWorkingDirectory().replaceAll("'", "") + "(" + file.getName() + ")"));
            }
        } else {
            for (FTPFile file : ftpFiles) {
                list.add(new DatasetFactory().createFromID(ftpClient.printWorkingDirectory().replaceAll("'", "") + file.getName()));
            }
        }

        return list;
    }

    @Override
    public boolean changeDirectory(final String remoteDir) throws IOException {
        return ftpClient.changeWorkingDirectory(SINGLE_QUOTE + remoteDir + SINGLE_QUOTE);
    }

    public boolean changeWorkingPrefix(final String remoteDir) throws IOException {
        return ftpClient.changeWorkingDirectory(remoteDir);
    }

    @Override
    public File getFile(final DataSet remoteFile, final String localName) throws FileNotFoundException, IOException {
        LOGGER.debug("Getting file " + remoteFile.getFTPName());

        final File f = new File(localName);

        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        if (ftpClient.retrieveFile(remoteFile.getFTPName(), outStream)) {
            outStream.close();
            outStream.writeTo(new FileOutputStream(f));
        } else {
            throw new FileNotFoundException("File " + remoteFile.getFTPName() + "not found");
        }

        return f;
    }

    private DirType getCurrentDirectoryType() throws IOException {
        ftpClient.pwd();
        return ftpClient.getReplyString().trim().contains("partitioned data set") ? DirType.PDS : DirType.PREFIX;
    }

}
