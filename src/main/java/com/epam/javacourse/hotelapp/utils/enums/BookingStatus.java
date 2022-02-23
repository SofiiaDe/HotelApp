package com.epam.javacourse.hotelapp.utils.enums;

public enum BookingStatus {
        NONE("none"),
        NEW("new"),
        CANCELLED("cancelled"),
        PAID("paid"),
        FINISHED("finished"),
        ONGOING("ongoing");

private final String text;

        BookingStatus(String text) {
        this.text = text;
        }

public String getText() {
        return this.text;
        }

public static BookingStatus fromString(String text) {
        for (BookingStatus b : BookingStatus.values()) {
        if (b.text.equalsIgnoreCase(text)) {
        return b;
        }
        }
        return BookingStatus.NONE;
        }
}
