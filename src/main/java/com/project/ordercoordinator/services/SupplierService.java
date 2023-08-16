package com.project.ordercoordinator.services;

import com.project.ordercoordinator.models.Product;
import com.project.ordercoordinator.models.Receipt;
import com.project.ordercoordinator.models.Supplier;
import com.project.ordercoordinator.repositories.ReceiptRepository;
import com.project.ordercoordinator.repositories.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ReceiptRepository receiptRepository;

    public List<Supplier> getAllSupplier() {
        return supplierRepository.findAll();
    }

    public Supplier createSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    public Supplier updateSupplier(Supplier supplier, Integer supplierId) {
        Supplier updateSupplier = supplierRepository.findById(supplierId);
        if(updateSupplier == null) {
            throw new IllegalArgumentException("Không tìm thấy nhà cung cấp");
        }

        updateSupplier.setName(supplier.getName());
        updateSupplier.setPhone(supplier.getPhone());

        return supplierRepository.save(updateSupplier);
    }

    public Supplier getSupplier(Integer supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId);
        if(supplier == null) {
            throw new IllegalArgumentException("Không tìm thấy nhà cung cấp");
        }
        return supplier;
    }

    public Page<Supplier> getSupplierWithPaginate(String searchKeyword, Integer page, Integer pageSize) {
        Supplier supplier = new Supplier();
        supplier.setName(searchKeyword);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Supplier> example = Example.of(supplier, matcher);

        Page<Supplier> suppliers = supplierRepository.findAll(example, PageRequest.of(page, pageSize, Sort.by("id").descending()));
        return suppliers;
    }

    public String deleteSupplier(Integer supplierId) {
        List<Receipt> receiptList = receiptRepository.findAll();
        if(receiptList.size() > 0) {
            for (Receipt receiptItem : receiptList) {
                if (receiptItem.getSupplier().getId() == supplierId) {
                    throw new IllegalArgumentException("Xóa thất bại! nhà cung cấp đã có giao dịch!");
                }
            }
        }
        receiptRepository.deleteById(supplierId);
        return "Xóa nhà cung cấp thành công";
    }
}
