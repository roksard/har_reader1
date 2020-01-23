package roksard.model;

public enum ContentType {
    APPLICATION_JAVASCRIPT("application/javascript"),
    APPLICATION_JSON("application/json"),
    APPLICATION_OCTET_STREAM("application/octet-stream"),
    APPLICATION_X_JAVASCRIPT("application/x-javascript"),
    IMAGE_GIF("image/gif"),
    IMAGE_JPEG("image/jpeg"),
    IMAGE_PNG("image/png"),
    IMAGE_WEBP("image/webp"),
    IMAGE_X_ICON("image/x-icon"),
    TEXT_HTML("text/html"),
    TEXT_JAVASCRIPT("text/javascript"),
    TEXT_PLAIN("text/plain"),
    X_UNKNOWN("x-unknown");

    String type;

    private ContentType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

    public String getType() {
        return type;
    }
}
