package net.ejpm.conn.remote.zos.dataset;

/**
 * A sequentialDataset is a MVS PS Dataset representation.
 *
 * @author XPTA455
 */
public class SequentialDataSet extends DataSet {

    private final String lastName;

    private SequentialDataSet(final SequentialDataSetBuilder builder) {
        super(builder.fullDsName);
        this.lastName = builder.lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public static class SequentialDataSetBuilder {

        private String fullDsName;
        private String lastName;

        public SequentialDataSetBuilder(final String fullDsName) {
            this.fullDsName = fullDsName;
        }

        public SequentialDataSetBuilder lastName(final String lastName) {
            this.lastName = lastName;
            return this;
        }

        public SequentialDataSet build() {
            return new SequentialDataSet(this);
        }

    }

}
