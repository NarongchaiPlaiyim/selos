package com.clevel.selos.model;
public enum CommandType {
    CREATE{
        @Override
        public boolean equals(final String string) {
            return "CREATE".equalsIgnoreCase(string);
        }
    },
    UPDATE{
        @Override
        public boolean equals(final String string) {
            return "UPDATE".equalsIgnoreCase(string);
        }
    },
    DELETE{
        @Override
        public boolean equals(final String string) {
            return "DELETE".equalsIgnoreCase(string);
        }
    };

    public abstract boolean equals(final String string);
}
