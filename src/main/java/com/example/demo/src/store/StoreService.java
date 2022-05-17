package com.example.demo.src.store;

import com.example.demo.config.BaseException;
import com.example.demo.src.store.model.PostMenuReq;
import com.example.demo.src.store.model.PostOptionReq;
import com.example.demo.src.store.model.PostOrdersReq;
import com.example.demo.src.store.model.PostOrdersRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class StoreService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final StoreDao storeDao;
    private final StoreProvider storeProvider;
    private final JwtService jwtService;

    @Autowired
    public StoreService(StoreDao storeDao, StoreProvider storeProvider, JwtService jwtService) {
        this.storeDao = storeDao;
        this.storeProvider = storeProvider;
        this.jwtService = jwtService;
    }

    //POST
    public void createOptionByStoreName(String storeName, PostOptionReq postOptionReq) throws BaseException {
        if(storeProvider.checkStore(storeName) == 0){
            throw new BaseException(POST_STORES_NOT_EXISTS);
        }
        try{
            int result = storeDao.createOptionByStoreName(storeName, postOptionReq);
            if (result == 0) {
                throw new BaseException(DATABASE_ERROR);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //DELETE
    public void deleteOptionByStore(String storeName, String optionCode) throws BaseException {
        if(storeProvider.checkStore(storeName) == 0){
            throw new BaseException(POST_STORES_NOT_EXISTS);
        }
        try{
            int result = storeDao.deleteOptionByStore(storeName, optionCode);
            if (result == 0) {
                throw new BaseException(DATABASE_ERROR);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //POST
    public void createOptionByMenuCode(String storeName, String menuCode, Map<String, String> postOptionByMenuCodeReq) throws BaseException {
        if(storeProvider.checkStore(storeName) == 0){
            throw new BaseException(POST_STORES_NOT_EXISTS);
        }
        try{
            int result = storeDao.createOptionByMenuCode(storeName, menuCode, postOptionByMenuCodeReq);
            if (result == 0) {
                throw new BaseException(DATABASE_ERROR);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //POST
    public void createMenu(String storeName, PostMenuReq postMenuReq) throws BaseException {
        if(storeProvider.checkStore(storeName) == 0){
            throw new BaseException(POST_STORES_NOT_EXISTS);
        }
        if(storeProvider.checkMenu(postMenuReq.getMenuCode()) == 1){
            throw new BaseException(POST_STORES_EXISTS_MENU);
        }

        try{
            int result = storeDao.createMenu(storeName, postMenuReq);
            if (result == 0) {
                throw new BaseException(DATABASE_ERROR);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //PATCH
    public void modifyMenu(String storeName, PostMenuReq postMenuReq) throws BaseException {
        if(storeProvider.checkStore(storeName) == 0){
            throw new BaseException(POST_STORES_NOT_EXISTS);
        }
        if(storeProvider.checkMenu(postMenuReq.getMenuCode()) == 0){
            throw new BaseException(POST_STORES_NOT_EXISTS_MENU);
        }

        try{
            int result = storeDao.modifyMenu(storeName, postMenuReq);
            if (result == 0) {
                throw new BaseException(DATABASE_ERROR);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //DELETE
    public void deleteMenu(String storeName, String menuCode) throws BaseException {
        if(storeProvider.checkStore(storeName) == 0){
            throw new BaseException(POST_STORES_NOT_EXISTS);
        }
        if(storeProvider.checkMenu(menuCode) == 0){
            throw new BaseException(POST_STORES_NOT_EXISTS_MENU);
        }
        try{
            int result = storeDao.deleteMenu(storeName, menuCode);
            if (result == 0) {
                throw new BaseException(DATABASE_ERROR);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<PostOrdersRes> ordering(String id, String storeName, PostOrdersReq postOrdersReq) throws BaseException {
        if (storeProvider.checkStore(storeName) == 0) {
            throw new BaseException(POST_STORES_NOT_EXISTS);
        }
        if (storeProvider.checkMenu(postOrdersReq.getMenuCode()) == 0) {
            throw new BaseException(POST_STORES_NOT_EXISTS_MENU);
        }
        try{
            String result = storeDao.ordering(id, storeName, postOrdersReq);
            return storeProvider.ordering(result);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
