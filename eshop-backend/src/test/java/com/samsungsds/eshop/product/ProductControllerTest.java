package com.samsungsds.eshop.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.samsungsds.eshop.payment.Money;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import java.util.Set;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;

@ExtendWith({RestDocumentationExtension.class})
@WebMvcTest(ProductController.class)
public class ProductControllerTest {
    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)) 
                .build();
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void testFetchProducts() throws Exception {
        String ids = "id1,id2";

        Product p1 = new Product();
        p1.setId("id1");
        p1.setDescription("test_desc_id1");
        p1.setName("test_name_id1");
        p1.setPicture("test_picture_id1");
        p1.setPriceUsd(new Money());
        p1.setCategories(Set.of("test_category_id1"));

        Product p2 = new Product();
        p2.setId("id2");
        p2.setDescription("test_desc_id2");
        p2.setName("test_name_id2");
        p2.setPicture("test_picture_id2");
        p2.setPriceUsd(new Money());
        p2.setCategories(Set.of("test_category_id2"));

        Product[] product = new Product[]{p1, p2};
        Products products = new Products(product);

        given(productService.fetchProductsByIds(ids.split(","))).willReturn(products);

        this.mockMvc.perform(
            MockMvcRequestBuilders.get("/api/products").queryParam("ids", ids)
        )
        .andExpect(status().isOk())
        .andDo(
            document("get-products", 
                requestParameters(
                    parameterWithName("ids").description("상품 id")
                ),
                responseFields(
                    fieldWithPath("products.[].id").description("상품 id"),
                    fieldWithPath("products.[].name").description("상품 이름"),
                    fieldWithPath("products.[].description").description("상품 설명"),
                    fieldWithPath("products.[].picture").description("상품 이미지"),
                    fieldWithPath("products.[].priceUsd.currencyCode").description("상품 가격 통화 코드"),
                    fieldWithPath("products.[].priceUsd.units").description("상품 가격 Unit"),
                    fieldWithPath("products.[].priceUsd.nanos").description("상품 가격 Nano"),
                    fieldWithPath("products.[].categories").description("상품 카테고리")
                )
            )
        );
    }

    @Test
    void testFetchProductsByIds() throws Exception {
        String id = "id1";

        Product p1 = new Product();
        p1.setId("id1");
        p1.setDescription("test_desc_id1");
        p1.setName("test_name_id1");
        p1.setPicture("test_picture_id1");
        p1.setPriceUsd(new Money());
        p1.setCategories(Set.of("test_category_id1"));

        given(productService.fetchProductById(id)).willReturn(p1);

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/products/{id}", id)
        )
        .andExpect(status().isOk())
        .andDo(
            document("get-products-by-id",
                pathParameters(
                    parameterWithName("id").description("상품 id")
                ),
                responseFields(
                    fieldWithPath("id").description("상품 id"),
                    fieldWithPath("name").description("상품 이름"),
                    fieldWithPath("description").description("상품 설명"),
                    fieldWithPath("picture").description("상품 이미지"),
                    fieldWithPath("priceUsd.currencyCode").description("상품 가격 통화 코드"),
                    fieldWithPath("priceUsd.units").description("상품 가격 Unit"),
                    fieldWithPath("priceUsd.nanos").description("상품 가격 Nano"),
                    fieldWithPath("categories").description("상품 카테고리")
                )
            )
        );

    }
}