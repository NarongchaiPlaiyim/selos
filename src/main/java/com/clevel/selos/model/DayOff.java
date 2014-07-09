package com.clevel.selos.model;

public enum DayOff {
    SATURDAY{
        @Override
        public boolean equals(final String string) {
            return "SATURDAY".equalsIgnoreCase(string);
        }
    },
    SUNDAY{
        @Override
        public boolean equals(final String string) {
            return "SUNDAY".equalsIgnoreCase(string);
        }
    };
    public abstract boolean equals(final String string);
}
