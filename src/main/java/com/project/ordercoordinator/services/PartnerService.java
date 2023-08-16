package com.project.ordercoordinator.services;

import com.project.ordercoordinator.models.*;
import com.project.ordercoordinator.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.servlet.http.Part;
import java.util.List;

@Service
public class PartnerService {
    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private DeliveryPriceRepository deliveryPriceRepository;

    public Page<Partner> getPartnerPaginate(String searchKeyword, Integer page, Integer size) {
        Partner partner = new Partner();
        partner.setName(searchKeyword);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Partner> example = Example.of(partner, matcher);

        return partnerRepository.findAll(example, PageRequest.of(page, size, Sort.by("id").descending()));
    }

    public Partner getPartner(Integer partnerId) {
        Partner partner = partnerRepository.findById(partnerId);
        if(partner == null) {
            throw new IllegalArgumentException("Không tìm thấy đối tác vận chuyển");
        }
        return partner;
    }

    public Partner createPartner(Partner partner) {
        return partnerRepository.save(partner);
    }

    public Partner updatePartner(Partner partner, Integer partnerId) {
        Partner foundedPartner = partnerRepository.findById(partnerId);
        if(foundedPartner == null)  {
            throw new IllegalArgumentException("Không tìm thấy đối tác vận chuyển");
        }
        foundedPartner.setName(partner.getName());
        foundedPartner.setPhone(partner.getPhone());
        foundedPartner.setActive(partner.getActive());
        return partnerRepository.save(foundedPartner);
    }

    public String deletePartner(Integer partnerId) {
        List<DeliveryPrice> deliveryPriceList = deliveryPriceRepository.findAll();
        for(DeliveryPrice deliveryPriceItem : deliveryPriceList) {
            if(deliveryPriceItem.getPartner().getId() == partnerId) {
                throw new IllegalArgumentException("Không thể xóa đối tác này!");
            }
        }
        Partner partner = partnerRepository.findById(partnerId);
        partnerRepository.delete(partner);
        return "Xóa đối tác thành công!";
    }
}
