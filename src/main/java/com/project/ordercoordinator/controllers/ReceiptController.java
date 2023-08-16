package com.project.ordercoordinator.controllers;

import com.project.ordercoordinator.DTO.ReceiptDetailDTO;
import com.project.ordercoordinator.DTO.ReceiptDetailListDTO;
import com.project.ordercoordinator.models.Receipt;
import com.project.ordercoordinator.services.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping("/api/receipt")
public class ReceiptController {
    @Autowired
    private ReceiptService receiptService;

    @PostMapping("/create")
    public ResponseEntity<String> createReceipt(@RequestBody ReceiptDetailListDTO receiptList,
                                        @RequestParam Integer supplierId,
                                        @RequestParam Integer employeeId,
                                        @RequestParam Integer warehouseId) {
        return ResponseEntity.ok(receiptService.createReceipt(supplierId, employeeId, warehouseId, receiptList));
    }

    // Helper method để chuyển đổi String sang Date
    private Date parseDate(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            // Xử lý lỗi nếu không thể chuyển đổi ngày từ String
            return null;
        }
    }

    @GetMapping("/get")
    public ResponseEntity<Page<Receipt>> getReceipts(
            @RequestParam Integer p,
            @RequestParam Integer size,
            @RequestParam(required = false) String  startDate,
            @RequestParam(required = false) String  endDate) {
        Date startDateAsDate = null;
        Date endDateAsDate = null;

        if (startDate != null && !startDate.isEmpty()) {
            startDateAsDate = parseDate(startDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDateAsDate);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            startDateAsDate = calendar.getTime();
        }

        if (endDate != null && !endDate.isEmpty()) {
            endDateAsDate = parseDate(endDate);
        }

        // Kiểm tra nếu startDate và endDate là null và xử lý logic tương ứng
        if (startDate == null && endDate == null) {
            // Cả startDate và endDate đều không được cung cấp, sử dụng logic mặc định
            return ResponseEntity.ok(receiptService.getReceiptList(p, size));
        } else if (startDate != null && endDate == null) {
            // Chỉ có startDate được cung cấp, xử lý logic lọc từ startDate
            return ResponseEntity.ok(receiptService.getReceiptListFromDate(p, size, startDateAsDate));
        } else if (startDate == null) {
            // Chỉ có endDate được cung cấp, xử lý logic lọc đến endDate
            return ResponseEntity.ok(receiptService.getReceiptListToDate(p, size, endDateAsDate));
        } else {
            // Cả startDate và endDate đều được cung cấp, xử lý logic lọc giữa các ngày
            return ResponseEntity.ok(receiptService.getReceiptListBetweenDates(p, size, startDateAsDate, endDateAsDate));
        }
    }
}
