package com.samsungsds.eshop.currency;

import java.util.Map;

import com.samsungsds.eshop.payment.Money;

import io.swagger.annotations.Api; //👈👈  add line
import io.swagger.annotations.ApiOperation; //👈👈  add line

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/currencies")
@Api(tags = "통화", description = "통화 관련 API") //👈👈  add line
public class CurrencyController {
  private final CurrencyService currencyService;

  public CurrencyController(final CurrencyService currencyService) {
    this.currencyService = currencyService;
  }

  @GetMapping
  @ApiOperation(value = "통화 조회") //👈👈  add line
  public ResponseEntity<Map<String, Double>> fetchCurrencyMap() {
    Map<String, Double> currencies = null;
    try {
      currencies = currencyService.fetchCurrency();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(currencies);
    }
    return ResponseEntity.ok(currencies);
  }

  @PostMapping(value = "/convert")
  @ApiOperation(value = "통화 변환") //👈👈  add line
  public ResponseEntity<Money> convertMoneyCurrency(@RequestBody CurrencyConvertRequest request) {
    return ResponseEntity.ok(currencyService.convertMoneyCurrency(request));
  }

}