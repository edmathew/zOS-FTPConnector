package net.ejpm.conn.remote.zos.dataset;

public class PDSMember extends DataSet {

    private final String memberName;

    public PDSMember(final PDSMemberBuilder builder) {
        super(builder.fullDsName);
        this.memberName = builder.memberName;
    }

    public String getMemberName() {
        return memberName;
    }

    public static class PDSMemberBuilder {

        private String fullDsName;
        private String memberName;

        public PDSMemberBuilder(final String fullDsName) {
            this.fullDsName = fullDsName;
        }

        public PDSMemberBuilder memberName(final String memberName) {
            this.memberName = memberName;
            return this;
        }

        public PDSMember build() {
            return new PDSMember(this);
        }

    }

}
