package com.samsungsds.eshop.cart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

@ExtendWith({RestDocumentationExtension.class})
@WebMvcTest(CartController.class)
public class CartControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)) 
                .build();
    }

    @Test
    void testAddToCart() throws Exception {
        CartItem param = new CartItem();
        param.setId("1");
        param.setQuantity(1);

        doNothing().when(cartService).addToCart(param);
        
        Gson gson = new Gson();
        String inputJson = gson.toJson(param);
        this.mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/api/carts")
                .content(inputJson)
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andDo(
            document("post-add-carts",
                requestFields( 
                    fieldWithPath("id").type(JsonFieldType.STRING).description("상품 ID"), 
                    fieldWithPath("quantity").type(JsonFieldType.NUMBER).description("상품 갯수") 
                )
            )
        );
    }

    @Test
    void testEmptyCart() throws Exception {
        doNothing().when(cartService).emptyCart();
        this.mockMvc.perform(
            MockMvcRequestBuilders.post("/api/carts/empty")
        )
        .andExpect(status().isOk())
        .andDo(document("post-empty-carts"));
    }

    @Test
    void testGetCartItems() throws Exception {
        CartItem cartItem1 = new CartItem();
        cartItem1.setId("1");
        cartItem1.setQuantity(1);
        CartItem cartItem2 = new CartItem();
        cartItem2.setId("2");
        cartItem2.setQuantity(2);

        given(cartService.getCartItems()).willReturn(List.of(cartItem1, cartItem2));
        
        this.mockMvc.perform(
            MockMvcRequestBuilders.get("/api/carts")
        )
        .andExpect(status().isOk())
        .andDo(document("get-carts", 
                responseFields(
                    fieldWithPath("[].id").description("상품 id"),
                    fieldWithPath("[].quantity").description("상품 갯수")
                )
            )
        );
    }

    @Test
    void testGetCartItemsCount() throws Exception {
        Long responseMock = 1L;
        given(cartService.getCartItemsCount()).willReturn(responseMock);
        
        this.mockMvc.perform(
            MockMvcRequestBuilders.get("/api/carts/count")
        )
        .andExpect(status().isOk())
        .andDo(document("get-count-carts"));
    }
}