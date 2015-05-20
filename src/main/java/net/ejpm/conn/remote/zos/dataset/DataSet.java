package net.ejpm.conn.remote.zos.dataset;

import net.ejpm.conn.remote.resources.RemoteFileResource;

/**
 * Representation of a MVS dataset. Example of a dataset name:
 * 'AMN001.PLN.REPORT.CYCLE.C140423'
 *
 * @author ejpmateus
 */
public abstract class DataSet implements RemoteFileResource {

    private final String fullDSName;
    private static final String SINGLE_QUOTE = "'";

    public DataSet(final String fullDSName) {
        this.fullDSName = fullDSName;
    }

    public String getFullDSName() {
        return fullDSName;
    }

    @Override
    public String getRemoteID() {
        return getFullDSName();
    }

    public String getFTPName() {
        return SINGLE_QUOTE + fullDSName + SINGLE_QUOTE;
    }
}
