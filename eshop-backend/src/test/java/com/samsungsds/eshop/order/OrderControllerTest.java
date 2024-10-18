package com.samsungsds.eshop.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.common.collect.Iterables;
import com.google.gson.Gson;
import com.samsungsds.eshop.cart.CartItem;
import com.samsungsds.eshop.cart.CartService;
import com.samsungsds.eshop.payment.CreditCardInfo;
import com.samsungsds.eshop.payment.Money;
import com.samsungsds.eshop.payment.PaymentRequest;
import com.samsungsds.eshop.payment.PaymentService;
import com.samsungsds.eshop.product.Product;
import com.samsungsds.eshop.product.ProductService;
import com.samsungsds.eshop.product.Products;
import com.samsungsds.eshop.shipping.Address;
import com.samsungsds.eshop.shipping.ShippingResult;
import com.samsungsds.eshop.shipping.ShippingService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

import java.util.List;
import java.util.Set;

@ExtendWith({RestDocumentationExtension.class})
@WebMvcTest(OrderController.class)
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private ShippingService shippingService;

    @MockBean
    private CartService cartService;

    @MockBean
    private PaymentService paymentService;

    @MockBean
    private ProductService productService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)) 
                .build();
    }

    @Test
    void testPlaceOrder() throws Exception {
        CreditCardInfo creditCardInfo = new CreditCardInfo();
        creditCardInfo.setCvv(0);
        creditCardInfo.setCreditCardExpirationYear(2023);
        creditCardInfo.setCreditCardExpirationMonth(3);
        creditCardInfo.setCreditCardNumber("1234567890");
        Address address = new Address();
        address.setCity("test-city");
        address.setCountry("test-country");
        address.setState("test-state");
        address.setStreetAddress("test-street");
        address.setZipCode("test-zipcode");
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setCreditCardInfo(creditCardInfo);
        orderRequest.setAddress(address);
        orderRequest.setEmailAddress("test-email");

        Gson gson = new Gson();
        String inputJson = gson.toJson(orderRequest);

        CartItem cartItem = new CartItem();
        cartItem.setId("1");
        cartItem.setQuantity(1);
        List<CartItem> cartItemList = List.of(cartItem);

        given(cartService.getCartItems()).willReturn(cartItemList);

        Product product = new Product();
        product.setId("1");
        product.setDescription("test_desc");
        product.setName("test_name");
        product.setPicture("test_picture");
        Money money = new Money();
        money.setCurrencyCode("USD");
        money.setUnits(10L);
        money.setNanos(11L);
        product.setPriceUsd(money);
        product.setCategories(Set.of("test_category"));

        Product[] productList = new Product[]{product};
        given(productService.fetchProductsByIds(new String[]{"1"})).willReturn(new Products(productList));

        Money itemPrice = new Money();
        itemPrice.setCurrencyCode("USD");
        itemPrice.setUnits(10L);
        itemPrice.setNanos(11L);
        given(orderService.calculateItemPrice(Iterables.toArray(cartItemList, CartItem.class), productList)).willReturn(itemPrice);

        Money calculatedCost = new Money();
        calculatedCost.setCurrencyCode("USD");
        calculatedCost.setUnits(4L);
        calculatedCost.setNanos(737192818L);
        given(shippingService.calculateShippingCostFromCartItems(Iterables.toArray(cartItemList, CartItem.class))).willReturn(itemPrice);

        PaymentRequest paymentRequest = new PaymentRequest(orderRequest.getCreditCardInfo(), itemPrice.plus(calculatedCost));
        given(paymentService.requestPayment(paymentRequest)).willReturn(null);

        Money shipMoney = new Money();
        shipMoney.setCurrencyCode("USD");
        shipMoney.setUnits(4L);
        shipMoney.setNanos(737192818L);
        
        given(shippingService.shipOrder(any())).willReturn(new ShippingResult("test-trackingId", shipMoney));
        
        given(orderService.createOrderId(orderRequest)).willReturn("test-orderId");
        
        doNothing().when(cartService).emptyCart();

        this.mockMvc.perform(
            MockMvcRequestBuilders
                .post("/api/checkouts/orders")
                .content(inputJson)
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andDo(
            document("post-orders",
                requestFields( 
                    fieldWithPath("emailAddress").type(JsonFieldType.STRING).description("이메일 주소"), 
                    fieldWithPath("address.streetAddress").type(JsonFieldType.STRING).description("상세 주소"), 
                    fieldWithPath("address.city").type(JsonFieldType.STRING).description("도시"), 
                    fieldWithPath("address.state").type(JsonFieldType.STRING).description("주"), 
                    fieldWithPath("address.country").type(JsonFieldType.STRING).description("국가"), 
                    fieldWithPath("address.zipCode").type(JsonFieldType.STRING).description("우편번호"),
                    fieldWithPath("creditCardInfo.creditCardNumber").type(JsonFieldType.STRING).description("카드번호"), 
                    fieldWithPath("creditCardInfo.cvv").type(JsonFieldType.NUMBER).description("cvv"), 
                    fieldWithPath("creditCardInfo.creditCardExpirationYear").type(JsonFieldType.NUMBER).description("유효기간 년"), 
                    fieldWithPath("creditCardInfo.creditCardExpirationMonth").type(JsonFieldType.NUMBER).description("유효기간 월") 
                ),
                responseFields(
                    fieldWithPath("orderId").description("주문 id"),
                    fieldWithPath("shippingTrackingId").description("배송 트래킹 id"),
                    fieldWithPath("shippingCost.currencyCode").description("배송비 통화코드"),
                    fieldWithPath("shippingCost.units").description("배송비 Unit"),
                    fieldWithPath("shippingCost.nanos").description("배송비 Nano"),
                    fieldWithPath("totalPaid.currencyCode").description("총비용 통화코드"),
                    fieldWithPath("totalPaid.units").description("총비용 Unit"),
                    fieldWithPath("totalPaid.nanos").description("총비용 Nano")
                )
            )
        );
    }
}