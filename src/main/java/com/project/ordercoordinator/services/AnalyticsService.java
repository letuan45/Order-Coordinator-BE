package com.project.ordercoordinator.services;

import com.project.ordercoordinator.models.*;
import com.project.ordercoordinator.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AnalyticsService {
    //SERVICE NÀY RETURN VỀ CÁC DỮ LIỆU THỐNG KÊ
    @Autowired
    private ReceiptDetailRepository receiptDetailRepository;

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private ReceiptDetailService receiptDetailService;

    @Autowired
    private OrderDetailService orderDetailService;

    public List<Map<String, Integer>> getCardData() {
        Calendar today = Calendar.getInstance();
        int thisYear = today.get(Calendar.YEAR);
        int thisMonth = today.get(Calendar.MONTH);

        List<Map<String, Integer>> cardData = new ArrayList<>();

        //Tổng SL nhập hàng
        HashMap<String, Integer> mapReceipt = new HashMap<>();
        List<ReceiptDetail> allReceiptDetails = receiptDetailRepository.findAll();
        Integer allReceiptAmount = 0;
        for(ReceiptDetail receiptDetailItem : allReceiptDetails) {
            Date receiptDate = receiptDetailItem.getId().getReceipt().getDate();
            Calendar calDateToCheck = Calendar.getInstance();
            calDateToCheck.setTime(receiptDate);
            int yearToCheck = calDateToCheck.get(Calendar.YEAR);
            if(thisYear == yearToCheck) {
                allReceiptAmount += receiptDetailItem.getAmount();
            }
        }
        mapReceipt.put("total", allReceiptAmount); //total
        Integer allReceiptCountThisMonth = 0;
        List<Receipt> allReceipts = receiptRepository.findAll();
        for(Receipt receiptItem: allReceipts) {
            Date receiptDate = receiptItem.getDate();
            Calendar receiptDateCalendar = Calendar.getInstance();
            receiptDateCalendar.setTime(receiptDate);
            int receiptMonth = receiptDateCalendar.get(Calendar.MONTH);
            if(thisMonth == receiptMonth) {
                allReceiptCountThisMonth++;
            }
        }
        mapReceipt.put("addition", allReceiptCountThisMonth);
        cardData.add(mapReceipt);

        // Tổng nhân sự và nhân sự có tài khoản
        HashMap<String, Integer> mapEmployee = new HashMap<>();
        List<Employee> allEmployee = employeeRepository.findAll();
        int countEmployee = 0;
        int countEmployeeHasAccount = 0;
        for(Employee employeeItem : allEmployee) {
            if(employeeItem.getActive() == true) {
                countEmployee++;
                if(accountService.employeeHasAccount(employeeItem.getId()) == true) {
                    countEmployeeHasAccount++;
                }
            }
        }
        mapEmployee.put("total", countEmployee);
        mapEmployee.put("addition", countEmployeeHasAccount);
        cardData.add(mapEmployee);

        // Lượng đơn hàng và lượng đơn đã điều phối
        HashMap<String, Integer> mapOrder = new HashMap<>();
        int totalCoordinatedOrders = 0;
        List<Order> allOrders = orderRepository.findAll();
        int totalOrders = allOrders.size();
        for(Order orderItem : allOrders) {
            if(orderItem.getStatus() == 2 || orderItem.getStatus() == 3)
                totalCoordinatedOrders++;
        }
        mapOrder.put("total", totalOrders);
        mapOrder.put("addition", totalCoordinatedOrders);
        cardData.add(mapOrder);

        // Tổng tồn kho và các chi nhánh kho còn hoạt động
        HashMap<String, Integer> mapStock = new HashMap<>();
        int totalStockAmount = 0;
        int totalActiveWarehouse = 0;
        List<Stock> allStocks = stockRepository.findAll();
        for(Stock stockItem : allStocks) {
            if(stockItem.getActive() == true) {
                totalStockAmount += stockItem.getQuantity();
            }
        }
        List<Warehouse> allWarehouses = warehouseRepository.findAll();
        for(Warehouse warehouseItem : allWarehouses) {
            if(warehouseItem.getActive() == true) totalActiveWarehouse++;
        }
        mapStock.put("total", totalStockAmount);
        mapStock.put("addition", totalActiveWarehouse);
        cardData.add(mapStock);

        return cardData;
    }

    public List<Map<String, List<Integer>>> getChartsData() {
        List<Map<String, List<Integer>>> cardData = new ArrayList<>();
        // Format để chỉ lấy thông tin ngày tháng năm
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        // Lấy các ngày trong tuần
        Calendar today = Calendar.getInstance();
        today.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        List<Date> datesOfWeek = new ArrayList<>();
        datesOfWeek.add(today.getTime());
        for (int i = 1; i < 7; i++) {
            today.add(Calendar.DAY_OF_WEEK, 1);
            datesOfWeek.add(today.getTime());
        }


        List<Receipt> receipts = receiptRepository.findAll();
        List<Integer> receiptsData = new ArrayList<>();
        List<Order> orders = orderRepository.findAll();
        List<Integer> ordersData = new ArrayList<>();
        for(Date dateOfWeek : datesOfWeek) {
            // Lấy data tồn kho tăng trong tuần
            Integer receiptsSingleData = 0;
            for(Receipt receiptItem : receipts) {
                if(sdf.format(dateOfWeek).equals(sdf.format(receiptItem.getDate()))) {
                    List<ReceiptDetail> receiptDetails = receiptDetailService.getByReceiptId(receiptItem.getId());
                    for(ReceiptDetail receiptDetailItem : receiptDetails) {
                        receiptsSingleData += receiptDetailItem.getAmount();
                    }
                }
            }
            receiptsData.add(receiptsSingleData);

            // Lấy data tồn kho xuất đi trong tuần
            Integer ordersSingleData = 0;
            for(Order orderItem : orders) {
                if(sdf.format(dateOfWeek).equals(sdf.format(orderItem.getDate())) && orderItem.getStatus() == 3) {
                    List<OrderDetail> orderDetails = orderDetailService.getByOrderId(orderItem.getId());
                    for(OrderDetail orderDetailItem : orderDetails) {
                        ordersSingleData += orderDetailItem.getAmount();
                    }
                }
            }
            ordersData.add(ordersSingleData);
        }
        HashMap<String, List<Integer>> receiptAnalyzeData = new HashMap<>();
        receiptAnalyzeData.put("data", receiptsData);

        HashMap<String, List<Integer>> orderAnalyzeData = new HashMap<>();
        orderAnalyzeData.put("data", ordersData);

        cardData.add(receiptAnalyzeData);
        cardData.add(orderAnalyzeData);

        HashMap<String, List<Integer>> coordinatedOrderAnalyzeData = new HashMap<>();
        List<Integer> coordinatedCounters = new ArrayList<>();
        // Lấy lượng đơn hàng đã điều phối trong năm
        for(int i = 0; i < 12; i++) {
            Integer coordinatedOrdersCounter = 0;
            for(Order orderItem : orders) {
                Date orderDate = orderItem.getDate();
                Calendar dummyDate = Calendar.getInstance();
                dummyDate.setTime(orderDate);
                int monthOrder = dummyDate.get(Calendar.MONTH);
                if(monthOrder == i && (orderItem.getStatus() == 3 || orderItem.getStatus() == 2)) {
                    coordinatedOrdersCounter++;
                }
            }
            coordinatedCounters.add(coordinatedOrdersCounter);
        }
        coordinatedOrderAnalyzeData.put("data", coordinatedCounters);
        cardData.add(coordinatedOrderAnalyzeData);

        return cardData;
    }
}
