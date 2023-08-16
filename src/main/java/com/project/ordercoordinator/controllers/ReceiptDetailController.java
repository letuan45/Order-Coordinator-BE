package com.project.ordercoordinator.controllers;

import com.project.ordercoordinator.models.ReceiptDetail;
import com.project.ordercoordinator.services.ReceiptDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/receipt-detail")
public class ReceiptDetailController {
    @Autowired
    private ReceiptDetailService receiptDetailService;

    @GetMapping("/get-by-id/{receiptId}")
    public ResponseEntity<List<ReceiptDetail>> getReceiptDetailsByReceipt(@PathVariable Integer receiptId) {
        return ResponseEntity.ok(receiptDetailService.getByReceiptId(receiptId));
    }
}
