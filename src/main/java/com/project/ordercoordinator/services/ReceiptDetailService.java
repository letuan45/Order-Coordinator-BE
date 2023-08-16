package com.project.ordercoordinator.services;

import com.project.ordercoordinator.models.Receipt;
import com.project.ordercoordinator.models.ReceiptDetail;
import com.project.ordercoordinator.repositories.ReceiptDetailRepository;
import com.project.ordercoordinator.repositories.ReceiptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReceiptDetailService {
    @Autowired
    ReceiptDetailRepository receiptDetailRepository;

    @Autowired
    ReceiptRepository receiptRepository;

    public List<ReceiptDetail> getByReceiptId(Integer receiptId) {
        Optional<Receipt> receipt = receiptRepository.findById(receiptId);
        if(receipt == null) {
            throw new IllegalArgumentException("Không tìm thấy phiếu nhập");
        }
        List<ReceiptDetail> receiptDetails = receiptDetailRepository.findAll();
        List<ReceiptDetail> result = new ArrayList<ReceiptDetail>();

        for(ReceiptDetail item : receiptDetails) {
            if(item.getId().getReceipt() == receipt.get()) {
                result.add(item);
            }
        }

        return result;
    }
}
