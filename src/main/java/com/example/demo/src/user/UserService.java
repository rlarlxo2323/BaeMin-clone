package com.example.demo.src.user;



import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

    }

    public void createUser(PostUserReq postUserReq) throws BaseException {
        //중복
        if(userProvider.checkEmail(postUserReq.getEmail()) == 1){
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }
        if(userProvider.checkId(postUserReq.getId()) == 1){
            throw new BaseException(POST_USERS_EXISTS_ID);
        }

        String pwd;
        try{
            //암호화
            pwd = new SHA256().encrypt(postUserReq.getPassword());
            postUserReq.setPassword(pwd);

        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try{
            int result = userDao.createUser(postUserReq);
            if (result == 0){
                throw new BaseException(DATABASE_ERROR);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyUserPassword(PatchUserReq patchUserReq) throws BaseException {
        // 패스워드 글자 수 검증
        if(patchUserReq.getPassword().length() <= 6)
            throw new BaseException(INSUFFICIENT_LENGTH);

        try{
            int result = userDao.modifyUserPassword(patchUserReq);
            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_USER_PASSWORD);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyUserNickname(PatchUserReq patchUserReq) throws BaseException {
        // 닉네임 글자 수 검증
        if(patchUserReq.getNickname().length() > 6)
            throw new BaseException(OVERFLOW_LENGTH);

        try{
            int result = userDao.modifyUserNickname(patchUserReq);
            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_USER_PASSWORD);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void createReviewById(String id, PostReviewReq postReviewReq) throws BaseException {
        try{
            int result = userDao.createReviewById(id, postReviewReq);
            int result2 = userDao.createReviewImageById(id, postReviewReq);
            if (result == 0 || result2 == 0) {
                throw new BaseException(DATABASE_ERROR);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //DELETE
    public void deleteReviewById(String id, String orderCode) throws BaseException {
        try{
            int result2 = userDao.deleteReviewImageById(id, orderCode);
            int result = userDao.deleteReviewById(id, orderCode);
            if (result == 0 || result2 == 0) {
                throw new BaseException(DATABASE_ERROR);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
