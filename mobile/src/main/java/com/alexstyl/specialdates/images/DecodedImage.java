package com.alexstyl.specialdates.images;

public final class DecodedImage {

    public static final DecodedImage EMPTY = new DecodedImage(null);

    private final byte[] bytes;

    DecodedImage(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
