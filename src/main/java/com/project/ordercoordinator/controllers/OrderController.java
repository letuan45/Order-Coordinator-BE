package com.project.ordercoordinator.controllers;

import com.project.ordercoordinator.DTO.OrderDetailListDTO;
import com.project.ordercoordinator.models.Order;
import com.project.ordercoordinator.models.Warehouse;
import com.project.ordercoordinator.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@RequestBody OrderDetailListDTO orderDetailList,
                                              @RequestParam Integer employeeId,
                                              @RequestParam Integer customerId,
                                              @RequestParam Integer districtId,
                                              @RequestParam String additionAddress) {
        return ResponseEntity.ok(orderService.createOrder(employeeId, customerId, districtId, additionAddress, orderDetailList));
    }

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
    public ResponseEntity<Page<Order>> getOrders( @RequestParam Integer p,
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
            return ResponseEntity.ok(orderService.getOrders(p, size));
        } else if (startDate != null && endDate == null) {
            // Chỉ có startDate được cung cấp, xử lý logic lọc từ startDate
            return ResponseEntity.ok(orderService.getOrdersFromDate(p, size, startDateAsDate));
        } else if (startDate == null) {
            // Chỉ có endDate được cung cấp, xử lý logic lọc đến endDate
            return ResponseEntity.ok(orderService.getOrdertsToDate(p, size, endDateAsDate));
        } else {
            // Cả startDate và endDate đều được cung cấp, xử lý logic lọc giữa các ngày
            return ResponseEntity.ok(orderService.getOrdersBetweenDates(p, size, startDateAsDate, endDateAsDate));
        }
    }
//
    @PutMapping("/coordinate")
    public ResponseEntity<List<Order>> OrderCoordinate(@RequestParam String ordersString) {
        return ResponseEntity.ok(orderService.coordinateOrders(ordersString));
    }

    @PutMapping("/confirm/{orderId}")
    public ResponseEntity<Order> comfirmOrder(@PathVariable Integer orderId) {
        return ResponseEntity.ok(orderService.confirmOrder(orderId));
    }

    @PutMapping("/cancel/{orderId}")
    public ResponseEntity<Order> cancelOrder(@PathVariable Integer orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(orderId));
    }

//    @GetMapping("/test")
//    public void test() {
//        orderService.coordinateOrders("1");
//    }
}
