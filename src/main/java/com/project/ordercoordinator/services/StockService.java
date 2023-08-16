package com.project.ordercoordinator.services;

import com.project.ordercoordinator.keys.StockId;
import com.project.ordercoordinator.models.Product;
import com.project.ordercoordinator.models.Stock;
import com.project.ordercoordinator.models.Warehouse;
import com.project.ordercoordinator.repositories.ProductRepository;
import com.project.ordercoordinator.repositories.StockRepository;
import com.project.ordercoordinator.repositories.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StockService {
    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    public Stock createStock(Integer productId, Integer warehouseId, Integer quantity) {
        Product product = productRepository.findById(productId);
        Warehouse warehouse = warehouseRepository.findById(warehouseId);
        if(product == null) {
            throw new IllegalArgumentException("Không tìm thấy sản phẩm");
        }
        if(warehouse == null) {
            throw new IllegalArgumentException("Không tìm thấy kho");
        }
        StockId stockId = new StockId(warehouse, product);
        Stock stock = new Stock(quantity, true, stockId);
        return stockRepository.save(stock);
    }

    public List<Stock> getStocksByWarehouse(Integer warehouseId) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId);
        List<Stock> dbStock = stockRepository.findAll();
        List<Stock> result = new ArrayList<Stock>();
        for(Stock item : dbStock) {
            if(item.getStockId().getWarehouse() == warehouse) {
                result.add(item);
            }
        }
        return result;
    }
}
