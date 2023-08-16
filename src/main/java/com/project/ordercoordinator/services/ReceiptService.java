package com.project.ordercoordinator.services;

import com.project.ordercoordinator.DTO.ReceiptDetailDTO;
import com.project.ordercoordinator.DTO.ReceiptDetailListDTO;
import com.project.ordercoordinator.keys.ReceiptDetailId;
import com.project.ordercoordinator.keys.StockId;
import com.project.ordercoordinator.models.*;
import com.project.ordercoordinator.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EmbeddedId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ReceiptService {
    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReceiptDetailRepository receiptDetailRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private StockRepository stockRepository;

    public String createReceipt (Integer supplierId,
                                 Integer employeeId,
                                 Integer warehouseId,
                                 ReceiptDetailListDTO receiptDetails) {
        Integer totalPrice = 0;
        Date date = new Date();
        Supplier supplier = supplierRepository.findById(supplierId);
        Employee employee = employeeRepository.findById(employeeId);
        Warehouse warehouse = warehouseRepository.findById(warehouseId);

        if(supplier == null) {
            throw new IllegalArgumentException("Nhà cung cấp không hợp lệ");
        }
        if(employee == null) {
            throw new IllegalArgumentException("Nhân viên không hợp lệ");
        }
        if(warehouse == null) {
            throw new IllegalArgumentException("Kho không hợp lệ");
        }

        for(ReceiptDetailDTO item : receiptDetails.getReceiptDetails()) {
            Product product = productRepository.findById(item.getProductId());
            if(product == null || product.getActive() == false) {
                String message = "Sản phẩm " + product.getId().toString() + " không hợp lệ!";
                throw new IllegalArgumentException(message);
            }

            //Cộng dồn tổng giá
            totalPrice += (product.getPrice() * item.getAmount());
        }

        Receipt receipt = new Receipt(date, totalPrice, supplier, employee, warehouse);
        //Lưu phiếu nhập
        Receipt storedReceipt = receiptRepository.save(receipt);

        //Tạo các chi tiết phiếu nhập
        List<ReceiptDetail> receiptDetailsList = new ArrayList<ReceiptDetail>();

        for(ReceiptDetailDTO item : receiptDetails.getReceiptDetails()) {
            Product product = productRepository.findById(item.getProductId());
            ReceiptDetailId detailId = new ReceiptDetailId(storedReceipt, product);

            ReceiptDetail receiptDetail = new ReceiptDetail(item.getAmount(), detailId);
            receiptDetailsList.add(receiptDetail);

            //Tạo tồn kho, hoặc cộng dồn tồn kho
            StockId stockId = new StockId(warehouse, product);
            Optional<Stock> stockOptional = stockRepository.findById(stockId);

            if(stockOptional.isPresent()) {
                Stock stock = stockOptional.get();
                Integer currentStock = stock.getQuantity();
                currentStock += item.getAmount();
                stock.setQuantity(currentStock);
                stockRepository.save(stock);
            } else {
                stockRepository.save(new Stock(item.getAmount(), stockId));
            }
        }
        receiptDetailRepository.saveAll(receiptDetailsList);

        return "Nhập hàng thành công!";
    }

    public Page<Receipt> getReceiptList(Integer page, Integer size) {
        return receiptRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending()));
    }

    public Page<Receipt> getReceiptListFromDate(Integer page, Integer size, Date startDate) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        return receiptRepository.findByDateAfter(startDate, pageRequest);
    }

    public Page<Receipt> getReceiptListToDate(Integer page, Integer size, Date endDate) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        return receiptRepository.findByDateBefore(endDate, pageRequest);
    }

    public Page<Receipt> getReceiptListBetweenDates(Integer page, Integer size, Date startDate, Date endDate) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        return receiptRepository.findByDateBetween(startDate, endDate, pageRequest);
    }


}
