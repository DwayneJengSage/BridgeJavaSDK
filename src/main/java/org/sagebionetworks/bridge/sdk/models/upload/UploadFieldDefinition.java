package org.sagebionetworks.bridge.sdk.models.upload;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.StringUtils;

import org.sagebionetworks.bridge.sdk.exceptions.BridgeSDKException;

/**
 * This class represents a field definition for an upload schema. This could map to a top-level key-value pair in the
 * raw JSON, or to a column in a Synapse table.
 */
@JsonDeserialize(builder = UploadFieldDefinition.Builder.class)
public final class UploadFieldDefinition {
    private final String name;
    private final boolean required;
    private final UploadFieldType type;

    /** Private constructor. Construction of an UploadFieldDefinition should go through the Builder. */
    private UploadFieldDefinition(String name, boolean required, UploadFieldType type) {
        this.name = name;
        this.required = required;
        this.type = type;
    }

    /** The field name, always non-null and non-empty. */
    public String getName() {
        return name;
    }

    /** True if the field is required to have data, false otherwise. */
    public boolean isRequired() {
        return required;
    }

    /**
     * The field's type, always non-null.
     *
     * @see org.sagebionetworks.bridge.sdk.models.upload.UploadFieldType
     */
    public UploadFieldType getType() {
        return type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Objects.hashCode(name);
        result = prime * result + Objects.hashCode(required);
        result = prime * result + Objects.hashCode(type);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        UploadFieldDefinition other = (UploadFieldDefinition) obj;
        return Objects.equals(name, other.name) && Objects.equals(required, other.required)
                && Objects.equals(type, other.type);
    }

    @Override
    public String toString() {
        return String.format("UploadFieldDefinition[name=%s, required=%b, type=%s]", name, required, type.name());
    }

    /** Builder for UploadFieldDefinition */
    public static class Builder {
        private String name;
        private Boolean required;
        private UploadFieldType type;

        /** @see org.sagebionetworks.bridge.sdk.models.upload.UploadFieldDefinition#getName */
        public String getName() {
            return name;
        }

        /** @see org.sagebionetworks.bridge.sdk.models.upload.UploadFieldDefinition#getName */
        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        /** @see org.sagebionetworks.bridge.sdk.models.upload.UploadFieldDefinition#isRequired */
        public Boolean getRequired() {
            return required;
        }

        /** @see org.sagebionetworks.bridge.sdk.models.upload.UploadFieldDefinition#isRequired */
        public Builder withRequired(Boolean required) {
            this.required = required;
            return this;
        }

        /** @see org.sagebionetworks.bridge.sdk.models.upload.UploadFieldDefinition#getType */
        public UploadFieldType getType() {
            return type;
        }

        /** @see org.sagebionetworks.bridge.sdk.models.upload.UploadFieldDefinition#getType */
        public Builder withType(UploadFieldType type) {
            this.type = type;
            return this;
        }

        /**
         * Builds and validates an UploadFieldDefinition. name must be non-null and non-empty. type must be
         * non-null. required may be null and defaults to true. If this is called with invalid fields, it will throw an
         * BridgeSDKException.
         *
         * @return validated UploadFieldDefinition
         * @throws BridgeSDKException
         *         if called with invalid fields
         */
        public UploadFieldDefinition build() throws BridgeSDKException {
            if (StringUtils.isBlank(name)) {
                throw new BridgeSDKException("name cannot be blank");
            }
            if (required == null) {
                required = true;
            }
            if (type == null) {
                throw new BridgeSDKException("type cannot be null");
            }

            return new UploadFieldDefinition(name, required, type);
        }
    }
}
