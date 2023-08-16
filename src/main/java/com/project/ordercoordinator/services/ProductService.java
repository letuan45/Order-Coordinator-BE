package com.project.ordercoordinator.services;

import com.project.ordercoordinator.DTO.ProductIdDTO;
import com.project.ordercoordinator.models.OrderDetail;
import com.project.ordercoordinator.models.Product;
import com.project.ordercoordinator.models.ReceiptDetail;
import com.project.ordercoordinator.models.Stock;
import com.project.ordercoordinator.repositories.OrderDetailRepository;
import com.project.ordercoordinator.repositories.ProductRepository;
import com.project.ordercoordinator.repositories.ReceiptDetailRepository;
import com.project.ordercoordinator.repositories.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReceiptDetailRepository receiptDetailRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    public Product getProduct(Integer productId) {
        Product product = productRepository.findById(productId);
        if(product == null) {
            throw new IllegalArgumentException("Không tìm thấy sản phẩm");
        }

        return product;
    }

    public Product createProduct(Product product) {
        if(product.getPrice() < 0) {
            throw new IllegalArgumentException("Giá không hợp lệ");
        }
        if(product.getWeight() < 0) {
            throw new IllegalArgumentException("Cân nặng không hợp lệ");
        }
        return productRepository.save(product);
    }

    public Product updateProduct(Product product, Integer productId) {
        Product foundedProduct = productRepository.findById(productId);
        if(foundedProduct == null) {
            throw new IllegalArgumentException("Không tìm thấy sản phẩm");
        }
        foundedProduct.setName(product.getName());
        foundedProduct.setWeight(product.getWeight());
        foundedProduct.setPrice(product.getPrice());

        return productRepository.save(foundedProduct);
    }

    public String disableProduct(Integer productId) {
        Product foundedProduct = productRepository.findById(productId);
        if(foundedProduct == null) {
            throw new IllegalArgumentException("Không tìm thấy sản phẩm");
        }
        if(foundedProduct.getActive() == false) {
            throw new IllegalArgumentException("Sản phẩm đã vô hiệu hóa");
        }

        foundedProduct.setActive(false);
        productRepository.save(foundedProduct);
        return "Vô hiệu hóa sản phẩm thành công";
    }

    public String reactiveProduct(Integer productId) {
        Product foundedProduct = productRepository.findById(productId);
        if(foundedProduct == null) {
            throw new IllegalArgumentException("Không tìm thấy sản phẩm");
        }
        if(foundedProduct.getActive() == true) {
            throw new IllegalArgumentException("Sản phẩm đã kích hoạt");
        }

        foundedProduct.setActive(true);
        productRepository.save(foundedProduct);
        return "Kích hoạt sản phẩm thành công";
    }

    public Page<Product> getProductPaginate(String searchKeyword, Integer page, Integer size) {
        Product product = new Product();
        product.setName(searchKeyword);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Product> example = Example.of(product, matcher);

        return productRepository.findAll(example, PageRequest.of(page, size, Sort.by("id").descending()));
    }

    public List<Product> getProductsByIdList(ProductIdDTO productIds) {
        List<Product> products = new ArrayList<Product>();
        for(Integer productId : productIds.getProductIdList()) {
            Product product = productRepository.findById(productId);
            if(product.getActive() == false) {
                throw new IllegalArgumentException("Sản phầm #" + productId + " đã vô hiệu hóa!");
            }
            if(product == null) {
                throw new IllegalArgumentException("Không tìm thấy sản phẩm có id: " + productId);
            }
            products.add(product);
        }
        return products;
    }

    public String deleteProduct(Integer productId) {
        List<ReceiptDetail> receiptDetailList = receiptDetailRepository.findAll();
        for(ReceiptDetail receiptDetailItem : receiptDetailList) {
            if(receiptDetailItem.getId().getProduct().getId() == productId) {
                throw new IllegalArgumentException("Xóa thất bại! sản phẩm đã giao dịch!");
            }
        }
        List<Stock> stockList = stockRepository.findAll();
        for(Stock stockItem : stockList) {
            if(stockItem.getStockId().getProduct().getId() == productId) {
                throw new IllegalArgumentException("Xóa thất bại! sản phẩm đang tồn kho!");
            }
        }
        List<OrderDetail> orderDetailList = orderDetailRepository.findAll();
        for(OrderDetail orderDetailItem : orderDetailList) {
            if(orderDetailItem.getId().getProduct().getId() == productId) {
                throw new IllegalArgumentException("Xóa thất bại! sản phẩm đã giao dịch!");
            }
        }
        Product product = productRepository.findById(productId);
        productRepository.delete(product);
        return "Xóa sản phẩm thành công";
    }
}
