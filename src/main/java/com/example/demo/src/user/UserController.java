package com.example.demo.src.user;

import com.example.demo.src.store.StoreService;
import com.example.demo.src.store.model.GetStoreRes;
import com.example.demo.src.store.model.PostOrdersReq;
import com.example.demo.src.store.model.PostOrdersRes;
import com.example.demo.utils.NaverOAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;
import static com.example.demo.utils.ValidationRegex.isRegexPhone;

@RestController
@RequestMapping("/app/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final StoreService storeService;
    @Autowired
    private final NaverOAuthService oAuthService;

    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService, StoreService storeService, NaverOAuthService oAuthService){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
        this.storeService = storeService;
        this.oAuthService = oAuthService;
    }

    /**
     * 1. 회원 조회 API
     * [GET] /users
     * 회원 아이디 검색 조회 API
     * [GET] /users ?id=
     * @return BaseResponse<List<GetUserRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("") // (GET) giri.shop/app/users
    public BaseResponse<List<GetUserRes>> getUsers(@RequestParam(required = false) String id) {
        try{
            if(id == null){
                List<GetUserRes> getUsersRes = userProvider.getUsers();
                return new BaseResponse<>(getUsersRes);
            }
            // Get Users
            List<GetUserRes> getUsersRes = userProvider.getUsersById(id);
            return new BaseResponse<>(getUsersRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 2. 회원 PW 변경 API
     * [PATCH] /users/:userId/password
     * @return BaseResponse<String>
     */
    // Path-variable
    @ResponseBody
    @PatchMapping("/{id}/password") // (PATCH) giri.shop/app/users/:userId/password
    public BaseResponse<String> modifyUserPassword(@PathVariable("id") String id, @RequestBody User user) {
        try{
            //jwt에서 id 추출.
            String userIdByJwt = jwtService.getUserId();
            //userId와 접근한 유저가 같은지 확인
            if(!id.equals(userIdByJwt)){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            PatchUserReq patchUserReq = new PatchUserReq(id, user.getPassword(), user.getNickname());

            userService.modifyUserPassword(patchUserReq);

            String result = user.getPassword()+"로 변경되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 3. 회원 닉네임 변경 API
     * [PATCH] /users/:userId/nickname
     * @return BaseResponse<String>
     */
    // Path-variable
    @ResponseBody
    @PatchMapping("/{id}/nickname") // (PATCH) giri.shop/app/users/:userId/nickname
    public BaseResponse<String> modifyUserNickname(@PathVariable("id") String id, @RequestBody User user) {
        try{
            //jwt에서 id 추출.
            String userIdByJwt = jwtService.getUserId();
            //userId와 접근한 유저가 같은지 확인
            if(!id.equals(userIdByJwt)){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            PatchUserReq patchUserReq = new PatchUserReq(id, user.getPassword(), user.getNickname());

            userService.modifyUserNickname(patchUserReq);

            String result = user.getNickname()+"로 변경되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 9. 회원 찜 목록 조회 API
     * [GET] /users/:userId/favorite
     * @return BaseResponse<List<GetStoreRes>> 예외처리 - 없는 회원, 찜 목록 없는경우
     */
    @ResponseBody
    @GetMapping("/{id}/favorite") // (GET) giri.shop/app/users/:userId/favorite
    public BaseResponse<List<GetStoreRes>> getStoreByUser(@PathVariable("id") String id) {
        try{
            //jwt에서 id 추출.
            String userIdByJwt = jwtService.getUserId();
            //userId와 접근한 유저가 같은지 확인
            if(!id.equals(userIdByJwt)){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            List<GetStoreRes> getStoresRes = userProvider.getStoresById(id);
            return new BaseResponse<>(getStoresRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 12. 회원 보유 포인트 조회 API
     * [GET] /users/:userId/point
     * @return BaseResponse<List<GetPointRes>> 예외처리 - 없는 회원, 포인트 없는경우
     */
    @ResponseBody
    @GetMapping("/{id}/point") // (GET) giri.shop/app/users/:userId/point
    public BaseResponse<List<GetPointRes>> getPointById(@PathVariable("id") String id) {
        try{
            //jwt에서 id 추출.
            String userIdByJwt = jwtService.getUserId();
            //userId와 접근한 유저가 같은지 확인
            if(!id.equals(userIdByJwt)){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            List<GetPointRes> GetPointsRes = userProvider.getPointById(id);
            return new BaseResponse<>(GetPointsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 18. 회원 리뷰 조회 API
     * [GET] /users/:userId/review
     * @return BaseResponse<List<GetReviewRes>> 예외처리 - 없는 회원, 포인트 없는경우
     */
    @ResponseBody
    @GetMapping("/{id}/review") // (GET) giri.shop/app/users/:userId/review
    public BaseResponse<List<GetReviewRes>> getReviewById(@PathVariable("id") String id) {
        try{
            //jwt에서 id 추출.
            String userIdByJwt = jwtService.getUserId();
            //userId와 접근한 유저가 같은지 확인
            if(!id.equals(userIdByJwt)){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            List<GetReviewRes> GetReviewsRes = userProvider.getReviewById(id);
            return new BaseResponse<>(GetReviewsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 19. 회원 리뷰 작성 API
     * [POST] /users/:userId/review
     * @return BaseResponse<String> 예외처리 - 없는 회원, 포인트 없는경우
     */
    @ResponseBody
    @PostMapping("/{id}/review") // (POST) giri.shop/app/users/:userId/review
    public BaseResponse<String> createReviewById(@PathVariable("id") String id,
                                                 @RequestBody PostReviewReq postReviewReq) {
        try{
            //jwt에서 id 추출.
            String userIdByJwt = jwtService.getUserId();
            //userId와 접근한 유저가 같은지 확인
            if(!id.equals(userIdByJwt)){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            userService.createReviewById(id, postReviewReq);
            String result = "성공적으로 추가 되었습니다.";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 20. 회원 리뷰 삭제 API
     * [DELETE] /users/:userId/review/:orderCode
     * @return BaseResponse<String>
     */

    @ResponseBody
    @DeleteMapping("/{id}/review/{orderCode}") // (DELETE) giri.shop/app/users/:userId/review/:orderCode
    public BaseResponse<String> deleteReviewById(@PathVariable("id") String id,
                                                    @PathVariable("orderCode") String orderCode) {
        try{
            //jwt에서 id 추출.
            String userIdByJwt = jwtService.getUserId();
            //userId와 접근한 유저가 같은지 확인
            if(!id.equals(userIdByJwt)){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            userService.deleteReviewById(id, orderCode);
            String result = "성공적으로 제거 되었습니다.";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 21. 회원가입 API
     * [POST] /users
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("")
    public BaseResponse<String> createUser(@RequestBody PostUserReq postUserReq) {
        if(postUserReq.getEmail() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        //이메일 정규표현
        if(!isRegexEmail(postUserReq.getEmail())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        if (!isRegexPhone(postUserReq.getPhone())){
            return new BaseResponse<>(POST_USERS_INVALID_PHONE);
        }
        try{
            userService.createUser(postUserReq);
            String result = "회원가입이 완료되었습니다.";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 22. 로그인 API
     * [POST] /users/login
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq){
        try{
            PostLoginRes postLoginRes = userProvider.logIn(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 26. 주문 API
     * [POST] /users/:userId/:storeName/orders
     * @return BaseResponse<PostOrdersRes>
     */
    @ResponseBody
    @PostMapping("/{id}/{storeName}/orders")
    public BaseResponse<List<PostOrdersRes>> ordering(@PathVariable("id") String id,
                                                @PathVariable("storeName") String storeName,
                                                @RequestBody PostOrdersReq postOrdersReq){
        try{
            //jwt에서 id 추출.
            String userIdByJwt = jwtService.getUserId();
            //userId와 접근한 유저가 같은지 확인
            if(!id.equals(userIdByJwt)){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            List<PostOrdersRes> postOrdersRes = storeService.ordering(id, storeName, postOrdersReq);
            return new BaseResponse<>(postOrdersRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 27. 소셜 로그인 인가코드 API
     * [GET] /users/login/naver
     */
    @ResponseBody
    @GetMapping("/login/naver")
    public void naverConnect(HttpServletRequest request, HttpServletResponse response){
        final String loginUrl = oAuthService.getRequestLoginUrl();

        try {
            response.sendRedirect(loginUrl);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @ResponseBody
    @GetMapping("/login/naver/callback")
    public Object requestAccessCallback(@RequestParam(value = "code") String authCode,
                                      @RequestParam(value = "state") String state){

        ResponseEntity<?> responseEntity = oAuthService.requestAccessToken(authCode, state);

        Object responseMessage = responseEntity.getBody();

        if(responseEntity.getStatusCode() == HttpStatus.OK){
            // 자바 객체로 변환
            // 레파지토리에 객체 저장
            return responseMessage;
        }else{
            // 응답에러 처리
            return responseMessage;
        }
    }
}
