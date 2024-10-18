package com.samsungsds.eshop.ad;

import java.util.List;
import java.util.Random;

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

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

@ExtendWith({RestDocumentationExtension.class})
@WebMvcTest(AdController.class)
public class AdControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdRepository adRepository;

    @MockBean
    private Random random;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)) 
                .build();
    }

    @Test
    public void getRandomAds() throws Exception {
        Ad ad1 = new Ad();
        ad1.setId(1);
        ad1.setRedirectUrl("testUrl-1");
        ad1.setCategory("testCategory-1");
        ad1.setText("testText-1");

        Ad ad2 = new Ad();
        ad2.setId(2);
        ad2.setRedirectUrl("testUrl-2");
        ad2.setCategory("testCategory-2");
        ad2.setText("testText-2");

        List<Ad> allAds = List.of(ad1, ad2);
        given(adRepository.findAll()).willReturn(allAds);
        given(random.nextInt(2)).willReturn(0).willReturn(1);

        this.mockMvc.perform(
            MockMvcRequestBuilders.get("/api/ads")
        )
        .andExpect(status().isOk())
        .andDo(document("get-ad-random", responseFields(
                    fieldWithPath("[].id").description("광고 id"),
                    fieldWithPath("[].category").description("광고 카테고리"),
                    fieldWithPath("[].redirectUrl").description("광고 리다이렉트 주소"),
                    fieldWithPath("[].text").description("광고 설명")
                )
            )
        );
    }

    @Test
    public void getAdsByCategory() throws Exception {
        Object[] inputs = new Object[]{"category"};

        Ad ad1 = new Ad();
        ad1.setId(1);
        ad1.setRedirectUrl("testUrl-1");
        ad1.setCategory("category");
        ad1.setText("testText-1");
        
        List<Ad> allAds = List.of(ad1);

        given(adRepository.findByCategoryIn(any())).willReturn(allAds);
        
        this.mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/ads/{categories}", inputs)
        )
        .andExpect(status().isOk())
        .andDo(
            document("get-ad-by-category", 
                pathParameters(
                    parameterWithName("categories").description("광고 카테고리")
                ),
                responseFields(
                    fieldWithPath("[].id").description("광고 id"),
                    fieldWithPath("[].category").description("광고 카테고리"),
                    fieldWithPath("[].redirectUrl").description("광고 리다이렉트 주소"),
                    fieldWithPath("[].text").description("광고 설명")
                )
            )
        );
    }
}