package com.samsungsds.eshop.recommend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api; //👈👈  add line
import io.swagger.annotations.ApiOperation; //👈👈  add line

@RestController
@RequestMapping(value="/api/recommends")
@Api(tags = "추천", description = "추천 관련 API") //👈👈  add line
public class RecommendController {
  private final RecommendService recommendService;

  public RecommendController(final RecommendService recommendService) {
    this.recommendService = recommendService;
  }

  @GetMapping
  @ApiOperation(value = "추천 상품 조회") //👈👈  add line
  public ResponseEntity<Recommendations> recommendProducts() {
    return ResponseEntity.ok(recommendService.recommendProducts());
  }
}