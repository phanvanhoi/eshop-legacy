package com.samsungsds.eshop.shipping;

import java.util.List;

import com.samsungsds.eshop.payment.Money; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api; //👈👈  add line
import io.swagger.annotations.ApiOperation; //👈👈  add line

@RestController
@RequestMapping(value="/api/checkouts/shippings")
@Api(tags = "배송비", description = "배송비 관련 API") //👈👈  add line
public class ShippingController {
    private final Logger logger = LoggerFactory.getLogger(ShippingController.class);
    private final ShippingService shippingService;

    public ShippingController(ShippingService shippingService) {
        this.shippingService = shippingService;
    }

    @PostMapping(value = "/cost")
    @ApiOperation(value = "배송비 조회") //👈👈  add line
    public ResponseEntity<Money> calculateShippingCost(@RequestBody List<ShippingItem> shippingList) {
        logger.info("calculateShippingCost");
        int itemCount = shippingList.stream()
                .map(ShippingItem::getQuantity)
                .reduce(0, Integer::sum);
        Money shippingCost = shippingService.calculateShippingCostFromCount(itemCount);
        logger.info("shippingCost : " + shippingCost);
        return ResponseEntity.ok(shippingCost);
    }
}
