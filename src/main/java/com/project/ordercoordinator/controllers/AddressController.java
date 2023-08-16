package com.project.ordercoordinator.controllers;

import com.project.ordercoordinator.models.District;
import com.project.ordercoordinator.models.Province;
import com.project.ordercoordinator.models.Region;
import com.project.ordercoordinator.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @GetMapping("/regions")
    public ResponseEntity<List<Region>> getAllRegion() {
        return ResponseEntity.ok(addressService.getAllRegions());
    }

    @GetMapping("/province/{regionId}")
    public ResponseEntity<List<Province>> getProvinceByRegion(@PathVariable Integer regionId) {
        return ResponseEntity.ok(addressService.getProvinceByRegion(regionId));
    }

    @GetMapping("/district/{provinceId}")
    public ResponseEntity<List<District>> getDistrictsByProvince(@PathVariable Integer provinceId) {
        return ResponseEntity.ok(addressService.getDistrictByProvince(provinceId));
    }

    @GetMapping("/district/get-by-id/{districtId}")
    public ResponseEntity<District> getDistrictById(@PathVariable Integer districtId) {
        return ResponseEntity.ok(addressService.getDistrictById(districtId));
    }
}
