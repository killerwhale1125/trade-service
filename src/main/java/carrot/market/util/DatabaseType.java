package carrot.market.util;

public enum DatabaseType {

    MASTER("MASTER"), SLAVE("SLAVE");

    private String type;

    DatabaseType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
