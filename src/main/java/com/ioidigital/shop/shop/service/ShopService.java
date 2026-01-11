package com.ioidigital.shop.shop.service;

import com.ioidigital.shop.shop.controller.model.*;
import com.ioidigital.shop.util.BaseDataResponse;
import com.ioidigital.shop.util.PagedData;
import org.springframework.data.domain.Pageable;

public interface ShopService {

    Long createShop(ShopCreateReq createReq);

    Long updateShop(Long shopId,
                    ShopUpdateReq updateReq);

    PagedData<PageShopItem> getShops(Pageable pageable,
                                     Double longitude,
                                     Double latitude);

    ShopMenuGetRes getShopMenu(Long shopId);
}
