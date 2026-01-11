package com.ioidigital.shop.shop.controller;

import com.ioidigital.shop.shop.controller.model.*;
import com.ioidigital.shop.shop.service.ShopService;
import com.ioidigital.shop.util.BaseDataResponse;
import com.ioidigital.shop.util.BaseResponse;
import com.ioidigital.shop.util.PagedData;
import com.ioidigital.shop.util.ResponseFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/shops")
@RequiredArgsConstructor
public class ShopController {

    private final ResponseFactory responseFactory;
    private final ShopService shopService;

    @GetMapping()
    public ResponseEntity<BaseDataResponse<PagedData<PageShopItem>>> getShops(@PageableDefault Pageable pageable,
                                                                              @RequestParam(name = "longitude", required = false) Double longitude,
                                                                              @RequestParam(name = "latitude", required = false) Double latitude) {

        var page = shopService.getShops(pageable, longitude, latitude);
        return responseFactory.success(HttpStatus.OK, page);
    }

    @GetMapping("/{shopId}/menu")
    public ResponseEntity<BaseDataResponse<ShopMenuGetRes>> getShopMenu(@PathVariable Long shopId) {
        var menu = shopService.getShopMenu(shopId);
        return responseFactory.success(HttpStatus.OK, menu);
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('OWNER')")
    public ResponseEntity<BaseResponse> createShop(@RequestBody ShopCreateReq createReq) {

        var id = shopService.createShop(createReq);

        return responseFactory.success(HttpStatus.OK, id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('OWNER')")
    public ResponseEntity<BaseResponse> updateShop(@PathVariable(name = "id") Long shopId,
                                                   @RequestBody ShopUpdateReq updateReq) {

        var id = shopService.updateShop(shopId, updateReq);

        return responseFactory.success(HttpStatus.OK, id);
    }

}
