package com.samsungsds.eshop.currency;

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

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

import java.util.HashMap;
import java.util.Map;

@ExtendWith({RestDocumentationExtension.class})
@WebMvcTest(CurrencyController.class)
public class CurrencyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyService currencyService;
    
    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)) 
                .build();
    }

    @Test
    void testConvertMoneyCurrency() throws Exception {
        Map<String, Double> mockResponse = new HashMap<String, Double>();
        mockResponse.put("CHF", 1.136);
        mockResponse.put("HRK", 7.421);
        mockResponse.put("MXN", 21.7999);
        mockResponse.put("ZAR", 16.0583);
        mockResponse.put("INR", 79.432);
        mockResponse.put("CNY", 7.5857);
        mockResponse.put("THB", 36.012);
        mockResponse.put("AUD", 1.6072);
        mockResponse.put("ILS", 4.0875);
        mockResponse.put("KRW", 1275.05);
        mockResponse.put("JPY", 126.4);
        mockResponse.put("PLN", 4.2996);
        mockResponse.put("GBP", 0.8597);
        mockResponse.put("IDR", 15999.4);
        mockResponse.put("HUF", 315.51);
        mockResponse.put("PHP", 59.083);
        mockResponse.put("TRY", 6.1247);
        mockResponse.put("RUB", 74.4208);
        mockResponse.put("ISK", 136.8);
        mockResponse.put("HKD", 8.8743);
        mockResponse.put("EUR", 1.0);
        mockResponse.put("DKK", 7.4609);
        mockResponse.put("USD", 1.1305);
        mockResponse.put("CAD", 1.5128);
        mockResponse.put("MYR", 4.6289);
        mockResponse.put("BGN", 1.9558);
        mockResponse.put("NOK", 9.804);
        mockResponse.put("RON", 4.7463);
        mockResponse.put("SGD", 1.5349);
        mockResponse.put("CZK", 25.592);
        mockResponse.put("SEK", 10.5375);
        mockResponse.put("NZD", 1.6679);
        mockResponse.put("BRL", 4.2682);
        given(currencyService.fetchCurrency()).willReturn(mockResponse);

        this.mockMvc.perform(
            MockMvcRequestBuilders.get("/api/currencies")
        )
        .andExpect(status().isOk())
        .andDo(document("get-currencies", 
            responseFields(
                    fieldWithPath("CHF").description("통화코드 CHF 변환 값"),
                    fieldWithPath("HRK").description("통화코드 HRK 변환 값"),
                    fieldWithPath("MXN").description("통화코드 MXN 변환 값"),
                    fieldWithPath("ZAR").description("통화코드 ZAR 변환 값"),
                    fieldWithPath("INR").description("통화코드 INR 변환 값"),
                    fieldWithPath("CNY").description("통화코드 CNY 변환 값"),
                    fieldWithPath("THB").description("통화코드 THB 변환 값"),
                    fieldWithPath("AUD").description("통화코드 AUD 변환 값"),
                    fieldWithPath("ILS").description("통화코드 ILS 변환 값"),
                    fieldWithPath("KRW").description("통화코드 KRW 변환 값"),
                    fieldWithPath("JPY").description("통화코드 JPY 변환 값"),
                    fieldWithPath("PLN").description("통화코드 PLN 변환 값"),
                    fieldWithPath("GBP").description("통화코드 GBP 변환 값"),
                    fieldWithPath("IDR").description("통화코드 IDR 변환 값"),
                    fieldWithPath("HUF").description("통화코드 HUF 변환 값"),
                    fieldWithPath("PHP").description("통화코드 PHP 변환 값"),
                    fieldWithPath("TRY").description("통화코드 TRY 변환 값"),
                    fieldWithPath("RUB").description("통화코드 RUB 변환 값"),
                    fieldWithPath("ISK").description("통화코드 ISK 변환 값"),
                    fieldWithPath("HKD").description("통화코드 HKD 변환 값"),
                    fieldWithPath("EUR").description("통화코드 EUR 변환 값"),
                    fieldWithPath("DKK").description("통화코드 DKK 변환 값"),
                    fieldWithPath("USD").description("통화코드 USD 변환 값"),
                    fieldWithPath("CAD").description("통화코드 CAD 변환 값"),
                    fieldWithPath("MYR").description("통화코드 MYR 변환 값"),
                    fieldWithPath("BGN").description("통화코드 BGN 변환 값"),
                    fieldWithPath("NOK").description("통화코드 NOK 변환 값"),
                    fieldWithPath("RON").description("통화코드 RON 변환 값"),
                    fieldWithPath("SGD").description("통화코드 SGD 변환 값"),
                    fieldWithPath("CZK").description("통화코드 CZK 변환 값"),
                    fieldWithPath("SEK").description("통화코드 SEK 변환 값"),
                    fieldWithPath("NZD").description("통화코드 NZD 변환 값"),
                    fieldWithPath("BRL").description("통화코드 BRL 변환 값")
                )
            )
        );
    }
}