package net.ejpm.conn.remote.zos.dataset;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatasetFactory {

    private static final String PDS_MEMBER_REGEX = "\\(([^)]+)\\)";

    public DataSet createFromID(final String resourceID) {
        return isPDSMember(resourceID) ? createPDSMember(resourceID) : createSequential(resourceID);
    }

    private DataSet createPDSMember(final String resourceID) {
        return new PDSMember.PDSMemberBuilder(resourceID).
                memberName(getPDSMemberName(resourceID)).build();
    }

    private DataSet createSequential(final String resourceID) {
        final String name = resourceID.substring(resourceID.lastIndexOf(".") + 1, resourceID.length());

        return new SequentialDataSet.SequentialDataSetBuilder(resourceID).lastName(name).build();
    }

    /**
     * Evaluates if the resourceID is a valid full PDS Member Name. Ex:
     * HLQ.BLA.BLA.LOAD(LOAD0001)
     *
     * @param resourceID
     * @return
     */
    private boolean isPDSMember(final String resourceID) {
        return Pattern.compile(PDS_MEMBER_REGEX).matcher(resourceID).find();
    }

    private String getPDSMemberName(final String resourceID) {
        Matcher m = Pattern.compile(PDS_MEMBER_REGEX).matcher(resourceID);
        m.find();
        return m.group(1);
    }
}
