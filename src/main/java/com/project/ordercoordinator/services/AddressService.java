package com.project.ordercoordinator.services;


import com.project.ordercoordinator.models.District;
import com.project.ordercoordinator.models.Province;
import com.project.ordercoordinator.models.Region;
import com.project.ordercoordinator.repositories.DistrictRepository;
import com.project.ordercoordinator.repositories.ProvinceRepository;
import com.project.ordercoordinator.repositories.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {
    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private RegionRepository regionRepository;

    public List<Region> getAllRegions() {
        return regionRepository.findAll();
    }

    public List<Province> getProvinceByRegion(Integer regionId) {
        return provinceRepository.findByRegionId(regionId);
    }

    public List<District> getDistrictByProvince(Integer provinceId) {return districtRepository.findByProvinceId(provinceId);}

    public District getDistrictById(Integer districtId) {
        District district = districtRepository.findById(districtId);
        if(district == null) {
            throw new IllegalArgumentException("Không tìm thấy địa chỉ");
        }
        return districtRepository.findById(districtId);
    }
}
