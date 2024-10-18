package com.samsungsds.eshop.recommend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.samsungsds.eshop.payment.Money;
import com.samsungsds.eshop.product.Product;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import java.util.Set;

@ExtendWith({RestDocumentationExtension.class})
@WebMvcTest(RecommendController.class)
public class RecommendControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    RecommendService recommendService;
    
    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)) 
                .build();
    }
    
    @Test
    void testRecommendProducts() throws Exception {
        
        Recommendations recommendations = new Recommendations();

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
        
        Product[] products = new Product[]{p1, p2};

        recommendations.setRecommendations(products);
        given(recommendService.recommendProducts()).willReturn(recommendations);

        this.mockMvc.perform(
            MockMvcRequestBuilders.get("/api/recommends")
        )
        .andExpect(status().isOk())
        .andDo(
            document("get-recommends",
                responseFields(
                    fieldWithPath("recommendations.[].id").description("추천 상품 id"),
                    fieldWithPath("recommendations.[].name").description("추천 상품 이름"),
                    fieldWithPath("recommendations.[].description").description("추천 상품 설명"),
                    fieldWithPath("recommendations.[].picture").description("추천 상품 이미지"),
                    fieldWithPath("recommendations.[].priceUsd.currencyCode").description("추천 상품 가격 통화 코드"),
                    fieldWithPath("recommendations.[].priceUsd.units").description("추천 상품 가격 Unit"),
                    fieldWithPath("recommendations.[].priceUsd.nanos").description("추천 상품 가격 Nano"),
                    fieldWithPath("recommendations.[].categories").description("추천 상품 카테고리")
                )
            )
        );
    }
}