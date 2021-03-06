package ru.alexey_ovcharov.rusfootballmanager.entities.transfer;

public enum TransferStatus {

    ON_TRANSFER("На продажу", 0),
    TO_RENT("В аренду", 1),
    ON_TRANSFER_OR_RENT("На продажу или в аренду", 2),
    ANY("Не важно", 3),
    ON_CONTRACT("Контракт", 4),
    FREE_AGENT("Свободный агент", 5);
    private final String description;
    private final int number;

    private TransferStatus(String description, int number) {
        this.description = description;
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public String getDescription() {
        return description;
    }

}
