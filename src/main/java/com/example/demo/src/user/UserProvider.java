package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.store.model.GetStoreRes;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    public List<GetUserRes> getUsers() throws BaseException{
        try{
            List<GetUserRes> getUserRes = userDao.getUsers();
            return getUserRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserRes> getUsersById(String id) throws BaseException {
        try {
            List<GetUserRes> getUsersRes = userDao.getUsersById(id);
            if (getUsersRes.size() == 0) {
                throw new BaseException(DATA_IS_NULL);
            }
            return getUsersRes;
        } catch (BaseException e) {
            if (e.getStatus() == DATA_IS_NULL) {
                throw new BaseException(DATA_IS_NULL);
            }else{
                return null;
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetStoreRes> getStoresById(String id) throws BaseException{
        try{
            List<GetStoreRes> getStoresRes = userDao.getStoresById(id);
            if (getStoresRes.get(0).getStoreName() == null) {
                throw new BaseException(DATA_IS_NULL);
            }
            return getStoresRes;
        }catch (BaseException e){
            if (e.getStatus() == DATA_IS_NULL) {
                throw new BaseException(DATA_IS_NULL);
            }else{
                return null;
            }
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetPointRes> getPointById(String id) throws BaseException{
        try{
            List<GetPointRes> getPointsRes = userDao.getPointById(id);
            return getPointsRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetReviewRes> getReviewById(String id) throws BaseException{
        try{
            List<GetReviewRes> getReviewsRes = userDao.getReviewById(id);
            return getReviewsRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkEmail(String email) throws BaseException{
        try{
            return userDao.checkEmail(email);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkId(String id) throws BaseException{
        try{
            return userDao.checkId(id);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException{
        User user = userDao.getPwd(postLoginReq);
        String encryptPwd;
        try {
            encryptPwd=new SHA256().encrypt(postLoginReq.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }
        if(user.getPassword().equals(encryptPwd)){
            String id = user.getId();
            String jwt = jwtService.createJwt(id);
            return new PostLoginRes(id, jwt);
        }
        else{
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }
}
