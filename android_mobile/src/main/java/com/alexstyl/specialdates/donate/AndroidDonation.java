package com.alexstyl.specialdates.donate;

enum AndroidDonation implements Donation {
    SKU_DONATE_1("1€", "donate_1"),
    SKU_DONATE_2("3€", "donate_2"),
    SKU_DONATE_3("5€", "donate_3"),
    SKU_DONATE_4("8€", "donate_4"),
    SKU_DONATE_5("10€", "donate_5"),
    SKU_DONATE_6("15€", "donate_6"),
    SKU_DONATE_7("20€", "donate_7");

    private final String priceLabel;
    private final String identifier;

    AndroidDonation(String priceLabel, String identifier) {
        this.priceLabel = priceLabel;
        this.identifier = identifier;
    }

    @Override
    public String getAmount() {
        return priceLabel;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    public static Donation valueOfIndex(int index) {
        return values()[index];
    }
}
