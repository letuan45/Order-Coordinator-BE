package com.project.ordercoordinator.services;

import com.project.ordercoordinator.models.DeliveryPrice;
import com.project.ordercoordinator.models.DeliveryType;
import com.project.ordercoordinator.models.Order;
import com.project.ordercoordinator.models.Partner;
import com.project.ordercoordinator.repositories.DeliveryPriceRepository;
import com.project.ordercoordinator.repositories.DeliveryTypeRepository;
import com.project.ordercoordinator.repositories.OrderRepository;
import com.project.ordercoordinator.repositories.PartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DeliveryPriceService {
    @Autowired
    private DeliveryTypeRepository deliveryTypeRepository;

    @Autowired
    private DeliveryPriceRepository deliveryPriceRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private OrderRepository orderRepository;

    public List<DeliveryType> getAllDeliveryTypes() {
        return deliveryTypeRepository.findAll();
    }

    public DeliveryPrice createDeliveryPrice (Integer deliveryPrice,
                                              Integer partnerId,
                                              Integer deliveryTypeId) {
        Partner partner = partnerRepository.findById(partnerId);
        Optional<DeliveryType> deliveryType = deliveryTypeRepository.findById(deliveryTypeId);

        if(partner == null) {
            throw new IllegalArgumentException("Không tìm thấy đối tác vận chuyển");
        }
        if(deliveryType == null) {
            throw new IllegalArgumentException("Không tìm thấy loại vận chuyển");
        }
        DeliveryType foundedDeliveryType = deliveryType.get();

        DeliveryPrice deliveryPriceData = new DeliveryPrice();
        deliveryPriceData.setPartner(partner);
        deliveryPriceData.setDeliveryType(foundedDeliveryType);
        deliveryPriceData.setPrice(deliveryPrice);

        return deliveryPriceRepository.save(deliveryPriceData);
    }

    public List<DeliveryPrice> getDeliveryPricesByPartner(Integer partnerId) {
        Partner partner = partnerRepository.findById(partnerId);
        if(partner == null) {
            throw new IllegalArgumentException("Không tìm thấy đối tác vận chuyển");
        }
        List<DeliveryPrice> allDeliveryPrices = deliveryPriceRepository.findAll();
        List<DeliveryPrice> result = new ArrayList<>();
        for(DeliveryPrice deliveryPriceItem : allDeliveryPrices) {
            if(deliveryPriceItem.getPartner() == partner) {
                result.add(deliveryPriceItem);
            }
        }
        return result;
    }

    public DeliveryPrice updateDeliveryPrice(Integer deliveryPriceId, DeliveryPrice deliveryPriceBodyData) {
        Optional<DeliveryPrice> deliveryPrice = deliveryPriceRepository.findById(deliveryPriceId);
        if(deliveryPrice == null) {
            throw new IllegalArgumentException("Không tìm thấy giá vận chuyển");
        }
        DeliveryPrice deliveryPriceData = deliveryPrice.get();
        deliveryPriceData.setPrice(deliveryPriceBodyData.getPrice());
        Boolean priceIsActive = deliveryPriceBodyData.getActive();
        // Nếu vô hiệu giá, cần xem xét có đơn hàng nào đang áp dụng giá này không
        // nếu có, throw exception
        if(priceIsActive == false) {
            List<Order> allOrders = orderRepository.findAll();
            for(Order orderItem : allOrders) {
                if(orderItem.getStatus() == 2) {
                    List<DeliveryPrice> deliveryPrices = getDeliveryPricesByPartner(orderItem.getPartner().getId());
                    for(DeliveryPrice deliveryPriceItem : deliveryPrices) {
                        if(deliveryPriceItem == deliveryPriceData) {
                            throw new IllegalArgumentException("Giá này đang áp dụng cho đơn hàng chưa hoàn tất id:"
                                    + orderItem.getId().toString());
                        }
                    }
                }
            }
        }

        deliveryPriceData.setActive(priceIsActive);
        return deliveryPriceRepository.save(deliveryPriceData);
    }
}
