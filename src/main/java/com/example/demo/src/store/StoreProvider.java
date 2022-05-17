package com.example.demo.src.store;

import com.example.demo.config.BaseException;
import com.example.demo.src.store.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.POST_STORES_NOT_EXISTS;

//Provider : Read의 비즈니스 로직 처리
@Service
public class StoreProvider {

    private final StoreDao storeDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public StoreProvider(StoreDao storeDao, JwtService jwtService) {
        this.storeDao = storeDao;
        this.jwtService = jwtService;
    }

    public List<GetStoreRes> getStores() throws BaseException {
        try{
            List<GetStoreRes> getStoreRes = storeDao.getStores();
            return getStoreRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetStoreRes> getStoresByCategory(String category) throws BaseException{
        try{
            List<GetStoreRes> getStoreRes = storeDao.getStoresByCategory(category);
            return getStoreRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetStoreRes> getStoresByMenu(String menu) throws BaseException{
        try{
            List<GetStoreRes> getStoreRes = storeDao.getStoresByMenu(menu);
            return getStoreRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetMenuRes> getMenuByStoreName(String storeName) throws BaseException{
        if(checkStore(storeName) == 0){
            throw new BaseException(POST_STORES_NOT_EXISTS);
        }
        try{
            List<GetMenuRes> getMenuRes = storeDao.getMenuByStoreName(storeName);
            return getMenuRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetInfoRes> getInfoByStoreName(String storeName) throws BaseException{
        if(checkStore(storeName) == 0){
            throw new BaseException(POST_STORES_NOT_EXISTS);
        }
        try{
            List<GetInfoRes> getInfoRes = storeDao.getInfoByStoreName(storeName);
            return getInfoRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetStoreInfoRes> getStoreInfoByStoreName(String storeName) throws BaseException{
        if(checkStore(storeName) == 0){
            throw new BaseException(POST_STORES_NOT_EXISTS);
        }
        try{
            List<GetStoreInfoRes> getStoreInfoRes = storeDao.getStoreInfoByStoreName(storeName);
            return getStoreInfoRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetDeliveryTipRes> getDeliveryTipByStoreName(String storeName) throws BaseException{
        if(checkStore(storeName) == 0){
            throw new BaseException(POST_STORES_NOT_EXISTS);
        }
        try{
            List<GetDeliveryTipRes> getDeliveryTipRes = storeDao.getDeliveryTipByStoreName(storeName);
            return getDeliveryTipRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetOptionRes> getOptionByStoreName(String storeName) throws BaseException{
        if(checkStore(storeName) == 0){
            throw new BaseException(POST_STORES_NOT_EXISTS);
        }
        try{
            List<GetOptionRes> getOptionsRes = storeDao.getOptionByStoreName(storeName);
            return getOptionsRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetOptionRes> getOptionByMenuCode(String storeName, String menuCode) throws BaseException{
        if(checkStore(storeName) == 0){
            throw new BaseException(POST_STORES_NOT_EXISTS);
        }
        try{
            List<GetOptionRes> getOptionsRes = storeDao.getOptionByMenuCode(storeName, menuCode);
            return getOptionsRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkStore(String storeName) throws BaseException{
        try{
            return storeDao.checkStore(storeName);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkMenu(String menuCode) throws BaseException{
        try{
            return storeDao.checkMenu(menuCode);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<PostOrdersRes> ordering(String oderCode) throws BaseException{
        try{
            List<PostOrdersRes> postOrdersRes = storeDao.getOderInfo(oderCode);
            return postOrdersRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
