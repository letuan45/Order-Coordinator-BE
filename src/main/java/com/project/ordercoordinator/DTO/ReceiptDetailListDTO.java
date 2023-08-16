package com.project.ordercoordinator.DTO;

import java.util.List;

public class ReceiptDetailListDTO {
    private List<ReceiptDetailDTO> receiptDetails;

    public ReceiptDetailListDTO() {
    }

    public ReceiptDetailListDTO(List<ReceiptDetailDTO> receiptDetails) {
        this.receiptDetails = receiptDetails;
    }

    public List<ReceiptDetailDTO> getReceiptDetails() {
        return receiptDetails;
    }

    public void setReceiptDetails(List<ReceiptDetailDTO> receiptDetails) {
        this.receiptDetails = receiptDetails;
    }
}
