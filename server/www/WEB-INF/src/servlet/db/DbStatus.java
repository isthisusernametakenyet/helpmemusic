package servlet.db;

public enum DbStatus {
    SUCCESS(true),
    FAILURE(false);

    private boolean code;

    DbStatus (boolean code) {
        this.code = code;
    }

    public boolean code() {
        return code;
    }

}

