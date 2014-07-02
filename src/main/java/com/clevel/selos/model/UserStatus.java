package com.clevel.selos.model;

public enum UserStatus {
    NORMAL {
        @Override
        public boolean equals(String string) {
            return "NORMAL".equalsIgnoreCase(string);
        }
    }, DISABLED {
        @Override
        public boolean equals(String string) {
            return "DISABLED".equalsIgnoreCase(string);
        }
    }, MARK_AS_DELETED {
        @Override
        public boolean equals(String string) {
            return "MARK_AS_DELETED".equalsIgnoreCase(string);
        }
    };

    public abstract boolean equals(final String string);
}
