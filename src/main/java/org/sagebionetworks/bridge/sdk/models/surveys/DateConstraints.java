package org.sagebionetworks.bridge.sdk.models.surveys;

public class DateConstraints extends Constraints {

    protected boolean allowFuture = false;

    public DateConstraints() {
        setDataType(DataType.DATE);
    }

    public boolean getAllowFuture() {
        return allowFuture;
    }
    public void setAllowFuture(boolean allowFuture) {
        this.allowFuture = allowFuture;
    }

}
