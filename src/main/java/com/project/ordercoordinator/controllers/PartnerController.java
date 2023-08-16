package com.project.ordercoordinator.controllers;

import com.project.ordercoordinator.models.Partner;
import com.project.ordercoordinator.services.PartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/partner")
public class PartnerController {
    @Autowired
    private PartnerService partnerService;

    @GetMapping("/get")
    public ResponseEntity<Page<Partner>> getPartners(@RequestParam String s, @RequestParam Integer p, @RequestParam Integer size) {
        return ResponseEntity.ok(partnerService.getPartnerPaginate(s, p, size));
    }

    @GetMapping("/get-partner/{partnerId}")
    public ResponseEntity<Partner> getPartner(@PathVariable Integer partnerId) {
        return ResponseEntity.ok(partnerService.getPartner(partnerId));
    }

    @PostMapping("/create")
    public ResponseEntity<Partner> createPartner(@RequestBody Partner partner) {
        return ResponseEntity.ok(partnerService.createPartner(partner));
    }

    @PutMapping("/update/{partnerId}")
    public ResponseEntity<Partner> updatePartner(@RequestBody Partner partner,
                                                 @PathVariable Integer partnerId) {
        return ResponseEntity.ok(partnerService.updatePartner(partner, partnerId));
    }

    @DeleteMapping("/delete/{partnerId}")
    public ResponseEntity<String> deletePartner(@PathVariable Integer partnerId) {
        return ResponseEntity.ok(partnerService.deletePartner(partnerId));
    }
}
