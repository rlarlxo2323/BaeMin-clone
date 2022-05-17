package com.example.demo.src.store;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.store.model.*;
import com.example.demo.src.store.model.PostOrdersReq;
import com.example.demo.src.store.model.PostOrdersRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app/stores")
public class StoreController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final StoreProvider storeProvider;
    @Autowired
    private final StoreService storeService;
    @Autowired
    private final JwtService jwtService;

    public StoreController(StoreProvider storeProvider, StoreService storeService, JwtService jwtService) {
        this.storeProvider = storeProvider;
        this.storeService = storeService;
        this.jwtService = jwtService;
    }

    /**
     * 4. 가게 조회 (카테고리 || 메뉴) API
     * [GET] /store
     * @return BaseResponse<GetStoreRes> 예외처리-카테고리 없을 때, 메뉴 없을 때.
     */

    @ResponseBody
    @GetMapping("") // (GET) giri.shop/app/store
    public BaseResponse<List<GetStoreRes>> getStores(@RequestParam(required = false) String category,
                                                     @RequestParam(required = false) String menu) {
        try{
            if(category == null && menu == null){
                List<GetStoreRes> getStoresRes = storeProvider.getStores();
                return new BaseResponse<>(getStoresRes);
            }else if(menu == null){
                List<GetStoreRes> getStoresRes = storeProvider.getStoresByCategory(category);
                return new BaseResponse<>(getStoresRes);
            }else{
                List<GetStoreRes> getStoresRes = storeProvider.getStoresByMenu(menu);
                return new BaseResponse<>(getStoresRes);
            }
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 5. 가게 메뉴 조회 API
     * [GET] /store/:storeName/menu
     * @return BaseResponse<GetMenuRes> 예외처리-가게명 없을 때
     */

    @ResponseBody
    @GetMapping("/{storeName}/menu") // (GET) giri.shop/app/store/:storeName/menu
    public BaseResponse<List<GetMenuRes>> getMenus(@PathVariable("storeName") String storeName) {
        try{
            List<GetMenuRes> getMenusRes = storeProvider.getMenuByStoreName(storeName);
            return new BaseResponse<>(getMenusRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 6. 가게 정보 조회 API
     * [GET] /store/:storeName/info
     * @return BaseResponse<GetInfoRes> 예외처리-가게명 없을 때
     */

    @ResponseBody
    @GetMapping("/{storeName}/info") // (GET) giri.shop/app/store/:storeName/info
    public BaseResponse<List<GetInfoRes>> getInfos(@PathVariable("storeName") String storeName) {
        try{
            List<GetInfoRes> getInfosRes = storeProvider.getInfoByStoreName(storeName);
            return new BaseResponse<>(getInfosRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 7. 가게 영업정보 조회 API
     * [GET] /store/:storeName/storeInfo
     * @return BaseResponse<GetStoreInfoRes> 예외처리-가게명 없을 때
     */

    @ResponseBody
    @GetMapping("/{storeName}/storeInfo") // (GET) giri.shop/app/store/:storeName/storeInfo
    public BaseResponse<List<GetStoreInfoRes>> getStoreInfos(@PathVariable("storeName") String storeName) {
        try{
            List<GetStoreInfoRes> getStoreInfosRes = storeProvider.getStoreInfoByStoreName(storeName);
            return new BaseResponse<>(getStoreInfosRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 8. 가게 배달 팁 조회 API
     * [GET] /store/:storeName/deliveryTip
     * @return BaseResponse<GetDeliveryTipRes> 예외처리-가게명 없을 때
     */

    @ResponseBody
    @GetMapping("/{storeName}/deliveryTip") // (GET) giri.shop/app/store/:storeName/deliveryTip
    public BaseResponse<List<GetDeliveryTipRes>> getDeliveryTip(@PathVariable("storeName") String storeName) {
        try{
            List<GetDeliveryTipRes> getDeliveryTipsRes = storeProvider.getDeliveryTipByStoreName(storeName);
            return new BaseResponse<>(getDeliveryTipsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 13. 가게 보유 옵션 조회 API
     * [GET] /store/:storeName/option
     * @return BaseResponse<GetOptionRes> 예외처리-가게명 없을 때
     */

    @ResponseBody
    @GetMapping("/{storeName}/option") // (GET) giri.shop/app/store/:storeName/option
    public BaseResponse<List<GetOptionRes>> getOptionByStoreName(@PathVariable("storeName") String storeName) {
        try{
            List<GetOptionRes> getOptionsRes = storeProvider.getOptionByStoreName(storeName);
            return new BaseResponse<>(getOptionsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 14. 가게 보유 옵션 추가 API
     * [POST] /stores/:storeName/option
     * @return BaseResponse<PostOptionRes> 예외처리-가게명 없을 때
     */
    // Body
    @ResponseBody
    @PostMapping("/{storeName}/option") // (POST) giri.shop/app/store/:storeName/option
    public BaseResponse<String> createOptionByStoreName(@PathVariable("storeName") String storeName,
                                                               @RequestBody PostOptionReq postOptionReq) {
        try{
            storeService.createOptionByStoreName(storeName, postOptionReq);
            String result = "성공적으로 추가 되었습니다.";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 15. 가게 보유 옵션 삭제 API
     * [DELETE] /store/:storeName/option/:optionCode
     * @return BaseResponse<String> 예외처리-가게명 없을 때
     */

    @ResponseBody
    @DeleteMapping("/{storeName}/option/{optionCode}") // (DELETE) giri.shop/app/store/:storeName/option/:optionCode
    public BaseResponse<String> deleteOptionByStore(@PathVariable("storeName") String storeName,
                                                    @PathVariable("optionCode") String optionCode) {
        try{
            storeService.deleteOptionByStore(storeName, optionCode);
            String result = "성공적으로 제거 되었습니다.";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 16. 음식 보유 옵션 조회 API
     * [GET] /store/:storeName/:menuCode/option
     * @return BaseResponse<GetOptionRes> 예외처리-가게명 없을 때
     */

    @ResponseBody
    @GetMapping("/{storeName}/{menuCode}/option") // (GET) giri.shop/app/store/:storeName/:menuCode/option
    public BaseResponse<List<GetOptionRes>> getOptionByMenuCode(@PathVariable("storeName") String storeName,
                                                                 @PathVariable("menuCode") String menuCode) {
        try{
            List<GetOptionRes> getOptionsRes = storeProvider.getOptionByMenuCode(storeName, menuCode);
            return new BaseResponse<>(getOptionsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 17. 음식 보유 옵션 추가 API
     * [POST] /stores/:storeName/:menuCode/option
     * @return BaseResponse<String> 예외처리-가게명 없을 때
     */
    // Body
    @ResponseBody
    @PostMapping("/{storeName}/{menuCode}/option") // (POST) giri.shop/app/store/:storeName/:menuCode/option
    public BaseResponse<String> createOptionByMenuCode(@PathVariable("storeName") String storeName,
                                                       @PathVariable("menuCode") String menuCode,
                                                        @RequestBody Map<String, String> postOptionByMenuCodeReq) {
        try{
            storeService.createOptionByMenuCode(storeName, menuCode, postOptionByMenuCodeReq);
            String result = "성공적으로 추가 되었습니다.";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 23. 음식 추가 API
     * [POST] /stores/:storeName/menu
     * @return BaseResponse<String> 예외처리-가게명 없을 때
     */
    // Body
    @ResponseBody
    @PostMapping("/{storeName}/menu") // (POST) giri.shop/app/store/:storeName/menu
    public BaseResponse<String> createMenu(@PathVariable("storeName") String storeName,
                                           @RequestBody PostMenuReq postMenuReq) {
        try{
            storeService.createMenu(storeName, postMenuReq);
            String result = "성공적으로 추가 되었습니다.";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 24. 음식 변경 API
     * [PATCH] /stores/:storeName/menu
     * @return BaseResponse<String> 예외처리-가게명 없을 때
     */
    // Body
    @ResponseBody
    @PatchMapping("/{storeName}/menu") // (PATCH) giri.shop/app/store/:storeName/menu
    public BaseResponse<String> modifyMenu(@PathVariable("storeName") String storeName,
                                           @RequestBody PostMenuReq postMenuReq) {
        try{
            storeService.modifyMenu(storeName, postMenuReq);
            String result = "성공적으로 수정 되었습니다.";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 25. 음식 삭제 API
     * [DELETE] /stores/:storeName/menu/:menuCode
     * @return BaseResponse<String> 예외처리-가게명 없을 때
     */
    // Body
    @ResponseBody
    @DeleteMapping("/{storeName}/menu/{menuCode}") // (DELETE) giri.shop/app/store/:storeName/menu/:menuCode
    public BaseResponse<String> createMenu(@PathVariable("storeName") String storeName,
                                           @PathVariable("menuCode") String menuCode) {
        try{
            storeService.deleteMenu(storeName, menuCode);
            String result = "성공적으로 제거 되었습니다.";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
