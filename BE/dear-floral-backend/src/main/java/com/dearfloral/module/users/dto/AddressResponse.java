package com.dearfloral.module.users.dto;

public record AddressResponse(
        Long addressId,
        String receiverName,
        String receiverPhone,
        String addressLine,
        String ward,
        String district,
        String province,
        Boolean isDefault,
        String note
) {
}
