package com.project.ordercoordinator.services;

import com.project.ordercoordinator.DTO.OrderDetailDTO;
import com.project.ordercoordinator.DTO.OrderDetailListDTO;
import com.project.ordercoordinator.DTO.RemainStockDTO;
import com.project.ordercoordinator.DTO.RemainStockItemDTO;
import com.project.ordercoordinator.keys.OrderDetailId;
import com.project.ordercoordinator.models.*;
import com.project.ordercoordinator.repositories.*;
import com.project.ordercoordinator.utils.IdComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepositoty;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private StockService stockService;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private DeliveryPriceRepository deliveryPriceRepository;

    @Autowired
    private DeliveryTypeRepository deliveryTypeRepository;

    public String createOrder(Integer employeeId,
                              Integer customerId,
                              Integer districtId,
                              String additionAddress,
                              OrderDetailListDTO orderDetails) {
        Date date = new Date();
        Employee employee = employeeRepository.findById(employeeId);
        District district = districtRepository.findById(districtId);
        Optional<Customer> customerRaw = customerRepository.findById(customerId);
        if(employee == null) {
            throw new IllegalArgumentException("Không tìm thấy nhân viên");
        }
        if(customerRaw == null) {
            throw new IllegalArgumentException("Không tìm thấy khách hàng");
        }
        Customer customer = customerRaw.get();
        Order order = new Order(date, employee, customer, district, additionAddress);
        //Lưu đơn hàng
        Order storedOrder = orderRepository.save(order);

        //Tạo các chi tiết hóa đơn
        List<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();

        for(OrderDetailDTO item : orderDetails.getOrderDetails()) {
            Product product = productRepository.findById(item.getProductId());
            OrderDetailId orderId = new OrderDetailId(storedOrder, product);

            OrderDetail orderDetail = new OrderDetail(item.getAmount(), orderId);
            orderDetailList.add(orderDetail);
        }
        orderDetailRepositoty.saveAll(orderDetailList);

        return "Tạo đơn đặt hàng thành công!";
    }


    public Page<Order> getOrders(Integer page, Integer size) {
        return orderRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending()));
    }

    public Page<Order> getOrdersFromDate(Integer page, Integer size, Date startDate) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        return orderRepository.findByDateAfter(startDate, pageRequest);
    }
    public Page<Order> getOrdertsToDate(Integer page, Integer size, Date endDate) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        return orderRepository.findByDateBefore(endDate, pageRequest);
    }

    public Page<Order> getOrdersBetweenDates(Integer page, Integer size, Date startDate, Date endDate) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        return orderRepository.findByDateBetween(startDate, endDate, pageRequest);
    }

    // Method hỗ trợ: lấy RemainStockDTO từ warehouseId
    public RemainStockDTO getRemainStockByWarehouse(Integer warehouseId, List<RemainStockDTO> remainStockDTOS) {
        for(RemainStockDTO remainItem : remainStockDTOS) {
            if(warehouseId == remainItem.getWarehouseId()) {
                return remainItem;
            }
        }
        return null;
    }

    // Method hỗ trợ: tìm đơn vị vận chuyển giá rẻ nhất
    // Type 1: Nội tỉnh
    // Type 2: Nội vùng
    // Type 3: Liên vùng
    public DeliveryPrice getLowestPricePartner(Order order) {
        List<DeliveryPrice> allDeliveryPrice = deliveryPriceRepository.findAll();
        Province orderProvince = order.getDistrict().getProvince();
        Province warehouseProvince =  order.getDeliveryWarehouse().getDistrict().getProvince();
        DeliveryPrice deliveryPriceResult = null;
        // Nếu là vận chuyển nội tỉnh
        if(orderProvince == warehouseProvince) {
            // Chọn ra giá vận chuyển rẻ nhất và có idType = 1
            Integer lowestPrice = 9999999;
            for(DeliveryPrice deliveryPriceItem : allDeliveryPrice)  {
                if(deliveryPriceItem.getPrice() < lowestPrice && deliveryPriceItem.getDeliveryType().getId() == 1) {
                    lowestPrice = deliveryPriceItem.getPrice();
                    deliveryPriceResult = deliveryPriceItem;
                }
            }
            return deliveryPriceResult;
        }
        // Nếu là vận chuyển nội vùng
        else if(orderProvince.getRegion() == warehouseProvince.getRegion()) {
            // Chọn ra giá vận chuyển rẻ nhất và có idType = 2
            Integer lowestPrice = 9999999;
            for(DeliveryPrice deliveryPriceItem : allDeliveryPrice)  {
                if(deliveryPriceItem.getPrice() < lowestPrice && deliveryPriceItem.getDeliveryType().getId() == 2) {
                    lowestPrice = deliveryPriceItem.getPrice();
                    deliveryPriceResult = deliveryPriceItem;
                }
            }
            return deliveryPriceResult;
        }
        // Trường hợp còn lại là vận chuyển ngoại vùng
        Integer lowestPrice = 9999999;
        for(DeliveryPrice deliveryPriceItem : allDeliveryPrice)  {
            if(deliveryPriceItem.getPrice() < lowestPrice && deliveryPriceItem.getDeliveryType().getId() == 3) {
                lowestPrice = deliveryPriceItem.getPrice();
                deliveryPriceResult = deliveryPriceItem;
            }
        }
        return deliveryPriceResult;
    }

    // Method hỗ trợ: Với đơn hàng cung cấp, lọc ra các kho đủ tồn cho đơn hàng
    public List<Warehouse> getFilteredWarehousesByOrder(Integer orderId, List<RemainStockDTO> remainStockDTOS) {
        List<OrderDetail> orderDetails = orderDetailService.getByOrderId(orderId);
        List<Warehouse> warehousesList = warehouseRepository.findAll();
        List<Warehouse> result = new ArrayList<Warehouse>();

        //Quy định lượng order detail để đạt đến
        Integer numOfSuitableOrderDetail = orderDetails.size();

        for(Warehouse item : warehousesList) {
            Integer numOfSuitable = 0;
            if(item.getActive() == true) {
                // Lấy các stock tương ứng của kho
                List<RemainStockItemDTO> remainStockItems = getRemainStockByWarehouse(item.getId(), remainStockDTOS)
                        .getRemainStockItemDTOList();
                // Lặp qua các chi tiết đơn hàng
                for(int i = 0; i < orderDetails.size(); i++) {
                    OrderDetail orderDetailItem = orderDetails.get(i);
                    // Lặp qua các stock
                    for(int j = 0; j < remainStockItems.size(); j++) {
                        RemainStockItemDTO remainStockItemDTO = remainStockItems.get(j);
                        if(orderDetailItem.getId().getProduct().getId() == remainStockItemDTO.getProductId()) {
                            // Tăng số đếm mà thỏa mãn đơn hàng
                            Product product = productRepository.findById(remainStockItemDTO.getProductId());
                            if(remainStockItemDTO.getQuantity() >= orderDetailItem.getAmount()
                                    && product.getActive() == true) {
                                numOfSuitable++;
                                break;
                            }
                        }
                    }
                }
            }

            // Nếu như thỏa số lượng đếm, thêm kho vào danh sách kho đủ tồn
            if(numOfSuitable == numOfSuitableOrderDetail) {
                result.add(item);
            }
        }
        return result;
    }

    // Method hỗ trợ: Cung cấp 1 list các Province, tham số 1 Id Provice, sẽ nhận về List Province được sort
    // gần giá trị tham số id nhất, từ đó chọn ra provice đứng đầu danh sách
    public Province getRelatedProvince(List<Province> provinces ,Integer targetProvinceId) {
        Collections.sort(provinces, new IdComparator(targetProvinceId));
        return provinces.get(0);
    }

    // Method hỗ trợ: xét xem có tồn tại bảng giá nào cho phương thức vận chuyển
    public List<Boolean> hasInnerPrice() {
        List<Boolean> result = new ArrayList<>();
        result.add(false);
        result.add(false);
        result.add(false);
        List<DeliveryPrice> deliveryPrices = deliveryPriceRepository.findAll();
        for(DeliveryPrice deliveryPriceItem : deliveryPrices) {
            if(deliveryPriceItem.getDeliveryType().getId() == 1 && deliveryPriceItem.getActive() == true) {
                result.set(0, true);
            }
            else if (deliveryPriceItem.getDeliveryType().getId() == 2 && deliveryPriceItem.getActive() == true) {
                result.set(1, true);
            }
            else if(deliveryPriceItem.getDeliveryType().getId() == 3 && deliveryPriceItem.getActive() == true) {
                result.set(2, true);
            }
        }
        return result;
    }

    // 1. Method hỗ trợ: với danh sách kho đã cho, địa chỉ đã cho, tìm ra kho đầu tiên cùng tỉnh
    // 2. Nếu không có cùng tỉnh, tìm kho cùng miền với đối tác
    // 3. Nếu 2 yếu tố trên không thỏa, tìm kho nào index (Id) ít chênh lệch nhất
    public Warehouse getNearestWarehouse(List<Warehouse> warehouseList, Integer orderId) {
        List<Boolean> hasDeliveryTypes = hasInnerPrice();
        if(warehouseList.size() == 0) {
            return null;
        }
        Order order = orderRepository.findById(orderId).get();
        Province orderProvince = order.getDistrict().getProvince();
        Region orderRegion = orderProvince.getRegion();

        List<Warehouse> sameRegionWarehouses = new ArrayList<>();

        for(Warehouse warehouseItem : warehouseList) {
            // Nếu có kho cùng tỉnh và có giá nội tỉnh
            if(orderProvince == warehouseItem.getDistrict().getProvince() && hasDeliveryTypes.get(0) == true) {
                return warehouseItem;
            } else if(orderRegion == warehouseItem.getDistrict().getProvince().getRegion()) {
                // Lấy ra các kho có cùng vùng miền
                sameRegionWarehouses.add(warehouseItem);
            }
        }

        // Nếu như có các kho cùng vùng miền và có giá nội miền
        if(sameRegionWarehouses.size() > 0 && hasDeliveryTypes.get(1) == true) {
            if(sameRegionWarehouses.size() == 1) {
                // Nếu chỉ có 1 kho cùng miền với đối tác thì return;
                return sameRegionWarehouses.get(0);
            } else {
                // Nếu không, chọn ra kho có idProvince gần idProvince của partner nhất
                List<Province> provinces = new ArrayList<>();
                for(Warehouse warehouseItem : sameRegionWarehouses) {
                    provinces.add(warehouseItem.getDistrict().getProvince());
                }
                Province closestProvince = getRelatedProvince(provinces, orderProvince.getId());
                for(Warehouse warehouseItem : sameRegionWarehouses) {
                    if(warehouseItem.getDistrict().getProvince() == closestProvince) {
                        return warehouseItem;
                    }
                }
            }
        }

        // Trường hợp khó xảy ra
        if(hasDeliveryTypes.get(2) == false) {
            return null;
        }

        // Nếu như không có kho nào cùng vùng miền, tìm kho gần nhất thông qua chênh lệch Id
        List<Province> provinces = new ArrayList<>();
        for(Warehouse warehouseItem : warehouseList) {
            provinces.add(warehouseItem.getDistrict().getProvince());
        }
        Province closestProvince = getRelatedProvince(provinces, orderProvince.getId());
        for(Warehouse warehouseItem : warehouseList) {
            if(warehouseItem.getDistrict().getProvince() == closestProvince) {
                return warehouseItem;
            }
        }

        // Không có kho nào phù hợp
        return null;
    }

    // Điều phối 1 list các đơn hàng, nếu đơn hàng không thể điều phối, set null
    public List<Order> coordinateOrders(String orderListString) {
        List<DeliveryPrice> allDeliveryPrices = deliveryPriceRepository.findAll();
        //Nếu không có bảng giá nào active thì kết thúc
        Boolean hasPrices = false;
        for(DeliveryPrice deliveryPriceItem : allDeliveryPrices) {
            if(deliveryPriceItem.getActive() == true) hasPrices = true;
        }
        if(hasPrices == false) throw new IllegalArgumentException("Không có bảng giá vận chuyến!");

        // Tách chuỗi đầu vào thành array, lấy list order tương ứng
        List<Order> orderList = new ArrayList<>();
        String[] parts = orderListString.split(",");
        for(int i = 0; i < parts.length; i++) {
            int orderId = Integer.parseInt(parts[i]);
            Optional<Order> orderItem = orderRepository.findById(orderId);
            if(orderItem == null) throw new IllegalArgumentException("Mã đơn không hợp lệ!");
            orderList.add(orderItem.get());
        }
        if(orderList.size() == 0) {
            throw new IllegalArgumentException("Đơn không hợp lệ");
        }

        // Tạo bản sao các tồn kho và gọi là tồn khả dụng của kho
        List<RemainStockDTO> remainStockDTOS = new ArrayList<>();
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        for(Warehouse warehouseItem : warehouseList) {
            List<Stock> foundedStocks = stockService.getStocksByWarehouse(warehouseItem.getId());
            List<RemainStockItemDTO> remainStocks = new ArrayList<>();
            for(Stock stockItem : foundedStocks) {
                remainStocks.add(new RemainStockItemDTO(
                        stockItem.getStockId().getProduct().getId() ,
                        stockItem.getQuantity()));
            }
            remainStockDTOS.add(new RemainStockDTO(warehouseItem.getId(), remainStocks));
        }

        // Điều phối đơn
        for(Order orderItem : orderList) {
            // Lấy ra kho đáp ứng đủ tồn cho đơn
            List<Warehouse> suitableWarehouses = getFilteredWarehousesByOrder(orderItem.getId(), remainStockDTOS);
            if(suitableWarehouses.size() > 0) {
                // Chọn kho đầu tiên phù hợp
                Warehouse mostSuitableWarehouse = getNearestWarehouse(suitableWarehouses, orderItem.getId());
                orderItem.setDeliveryWarehouse(mostSuitableWarehouse);
                orderItem.setStatus(2);

                //Chọn đơn vị vận chuyển phù hợp
                DeliveryPrice priceObj = getLowestPricePartner(orderItem);
                Partner deliveryPartner = priceObj.getPartner();
                Integer deliveryPrice = priceObj.getPrice();
                orderItem.setPartner(deliveryPartner);
                orderItem.setDeliveryPrice(deliveryPrice);

                // Từ tồn khả dụng: tìm tồn khả dụng của kho, lấy các tồn và trừ
                List<OrderDetail> orderDetails = orderDetailService.getByOrderId(orderItem.getId());
                for(RemainStockDTO remainStockDTOItem : remainStockDTOS) {
                    if(mostSuitableWarehouse.getId() == remainStockDTOItem.getWarehouseId()) {
                        List<RemainStockItemDTO> remainStockItemDTOList = remainStockDTOItem.getRemainStockItemDTOList();
                        for(RemainStockItemDTO remainStockItemDTO : remainStockItemDTOList) {
                            for(OrderDetail orderDetailItem : orderDetails) {
                                if(orderDetailItem.getId().getProduct().getId() == remainStockItemDTO.getProductId()) {
                                    Integer orderAmount = orderDetailItem.getAmount();
                                    Integer stockLeft = remainStockItemDTO.getQuantity() - orderAmount;
                                    remainStockItemDTO.setQuantity(stockLeft);
                                }
                            }
                        }
                    }
                }
            }
        }
        return orderRepository.saveAll(orderList);
    }

    public Order confirmOrder(Integer orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if(order == null) {
            throw new RuntimeException("Không tìm thấy đơn hàng");
        }
        List<OrderDetail> orderDetails = orderDetailService.getByOrderId(orderId);
        Warehouse warehouse = order.get().getDeliveryWarehouse();
        List<Stock> stocks = stockService.getStocksByWarehouse(warehouse.getId());
        for(OrderDetail orderDetailItem : orderDetails) {
            for(Stock stockItem: stocks) {
                if(orderDetailItem.getId().getProduct() == stockItem.getStockId().getProduct()) {
                    Integer amount = orderDetailItem.getAmount();
                    Integer quantityLeft = stockItem.getQuantity() - amount;
                    if(stockItem.getStockId().getProduct().getActive() == false) {
                        throw new IllegalArgumentException("Sản phẩm có Id " +
                                stockItem.getStockId().getProduct().getId().toString() + " đã vô hiệu hóa");
                    }
                    if(quantityLeft < 0) {
                        throw new IllegalArgumentException("Sản phẩm có Id " +
                                stockItem.getStockId().getProduct().getId().toString() + " không đủ cho đơn hàng");
                    }
                    stockItem.setQuantity(quantityLeft);
                    stockRepository.save(stockItem);
                    break;
                }
            }
        }
        Order orderData = order.get();
        orderData.setStatus(3);

        return orderRepository.save(orderData);
    }

    public Order cancelOrder(Integer orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if(order == null) {
            throw new RuntimeException("Không tìm thấy đơn hàng");
        }
        Order orderData = order.get();
        orderData.setStatus(4);
        return orderRepository.save(orderData);
    }
}
