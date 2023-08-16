package com.project.ordercoordinator.controllers;

import com.project.ordercoordinator.models.DeliveryPrice;
import com.project.ordercoordinator.models.DeliveryType;
import com.project.ordercoordinator.services.DeliveryPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/delivery")
public class DeliveryPriceController {
    @Autowired
    private DeliveryPriceService deliveryPriceService;

    @GetMapping("/get-all")
    public ResponseEntity<List<DeliveryType>> getAllDeliveryType() {
        return ResponseEntity.ok(deliveryPriceService.getAllDeliveryTypes());
    }

    @PostMapping("/create")
    public ResponseEntity<DeliveryPrice> createDeliveryPrice(@RequestParam Integer deliveryPrice,
                                                             @RequestParam Integer partnerId,
                                                             @RequestParam Integer deliveryTypeId) {
        return ResponseEntity.ok(deliveryPriceService.createDeliveryPrice(deliveryPrice, partnerId, deliveryTypeId));
    }

    @GetMapping("/get-by-partner/{partnerId}")
    public ResponseEntity<List<DeliveryPrice>> getDeliveryPricesByPartner(@PathVariable Integer partnerId) {
        return ResponseEntity.ok(deliveryPriceService.getDeliveryPricesByPartner(partnerId));
    }

    @PutMapping("/update/{deliveryPriceId}")
    public ResponseEntity<DeliveryPrice> updateDeliveryPrice(@PathVariable Integer deliveryPriceId,
                                                             @RequestBody DeliveryPrice deliveryPrice) {
        return ResponseEntity.ok(deliveryPriceService.updateDeliveryPrice(deliveryPriceId, deliveryPrice));
    }
}
