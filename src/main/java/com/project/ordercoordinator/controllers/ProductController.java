package com.project.ordercoordinator.controllers;

import com.project.ordercoordinator.DTO.ProductIdDTO;
import com.project.ordercoordinator.models.Product;
import com.project.ordercoordinator.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/get-all")
    public ResponseEntity<List<Product>> getAllProduct() {
        return ResponseEntity.ok(productService.getAllProduct());
    }

    @GetMapping("/get/ids")
    public ResponseEntity<List<Product>> getProductByIdList(@RequestParam String idList) {
        String[] idDataList = idList.split(",");
        List<Integer> idListTransfer = new ArrayList<>();
        for (String item : idDataList) {
            idListTransfer.add(Integer.valueOf(item));
        }
        ProductIdDTO productIdDTO = new ProductIdDTO(idListTransfer);
        return ResponseEntity.ok(productService.getProductsByIdList(productIdDTO));
    }

    @GetMapping("/get/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer productId) {
        return ResponseEntity.ok(productService.getProduct(productId));
    }

    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productService.createProduct(product));
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product, @PathVariable Integer productId) {
        return ResponseEntity.ok(productService.updateProduct(product, productId));
    }

    @PutMapping("/disable/{productId}")
    public ResponseEntity<String> disableProduct(@PathVariable Integer productId) {
        return ResponseEntity.ok(productService.disableProduct(productId));
    }

    @PutMapping("/reactive/{productId}")
    public ResponseEntity<String> reactiveProduct(@PathVariable Integer productId) {
        return ResponseEntity.ok(productService.reactiveProduct(productId));
    }

    @GetMapping("/get/paginate")
    public ResponseEntity<Page<Product>> getProductPaginate(@RequestParam String s ,@RequestParam Integer p, @RequestParam Integer size) {
        return ResponseEntity.ok(productService.getProductPaginate(s, p, size));
    }

//    @GetMapping("/get/paginate/s")
//    public ResponseEntity<Page<Product>> getProductWithSearch(@RequestParam String s,
//                                                              @RequestParam Integer p,
//                                                              @RequestParam Integer size) {
//        return ResponseEntity.ok(productService.getProductPaginateWithSearch(s, p, size));
//    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer productId) {
        return ResponseEntity.ok(productService.deleteProduct(productId));
    }
}
