package ru.mail.park.main.responses;

public final class SuccessResponse {
    private long id;

    public SuccessResponse(long id) {
        this.id = id;
    }

    @SuppressWarnings("unused")
    public long getId() {
        return id;
    }
}
