package com.example.deundeun.review.api.dto.response;

public record ReceiptInfoDto(
        String storeName,
        String cardName,
        String amount,
        String dateTime
) {}
