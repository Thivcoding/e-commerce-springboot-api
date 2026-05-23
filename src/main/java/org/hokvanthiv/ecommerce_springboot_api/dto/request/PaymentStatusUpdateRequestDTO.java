package org.hokvanthiv.ecommerce_springboot_api.dto.request;

import jakarta.validation.constraints.NotNull;
import org.hokvanthiv.ecommerce_springboot_api.Enum.PaymentStatus;

public class PaymentStatusUpdateRequestDTO {
    @NotNull
    private PaymentStatus status;

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
}
