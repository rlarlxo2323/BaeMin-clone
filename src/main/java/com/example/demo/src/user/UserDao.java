package com.example.demo.src.user;


import com.example.demo.src.store.model.GetStoreRes;
import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetUserRes> getUsers(){
        String getUsersQuery = "select * from member";
        return this.jdbcTemplate.query(getUsersQuery,
                (rs,rowNum) -> new GetUserRes(
                        rs.getString("id"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("nickname"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getString("point"),
                        rs.getString("rating"),
                        rs.getString("join"))
        );
    }

    public List<GetUserRes> getUsersById(String id){
        String getUsersByIdQuery = "select * from member where id =?";
        String getUsersByIdParams = id;
        return this.jdbcTemplate.query(getUsersByIdQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getString("id"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("nickname"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getString("point"),
                        rs.getString("rating"),
                        rs.getString("join")),
                getUsersByIdParams);
    }

    public int modifyUserPassword(PatchUserReq patchUserReq){
        String modifyUserNameQuery = "update member set password = ? where id = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getPassword(), patchUserReq.getId()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public int modifyUserNickname(PatchUserReq patchUserReq){
        String modifyUserNameQuery = "update member set nickname = ? where id = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getNickname(), patchUserReq.getId()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public List<GetStoreRes> getStoresById(String id){
        String getStoresByIdQuery = "select G.storeName, avg(G.grade) as grade, G.menu, G.priceMin, G.deliveryTip, G.deliveryTime, H.path\n" +
                "from\n" +
                "    (select E.storeName, F.grade, F.menu, E.priceMin, E.deliveryTip, E.deliveryTime, E.storeCode, E.categoryCode\n" +
                "     from\n" +
                "         (select A.storeCode, A.storeName, A.priceMin, B.deliveryTip, A.deliveryTime, A.categoryCode\n" +
                "          from\n" +
                "              (select P.storeName, P.priceMin, P.storeCode, P.categoryCode,\n" +
                "                      concat(cast(P.storeMinDeliveryTime as char(2)), ' ~ ', cast(P.storeMaxDeliveryTime as char(2))) as deliveryTime\n" +
                "               from store_has_category sc\n" +
                "                   inner join (select st.storeName, st.priceMin, st.storeCode, st.storeMinDeliveryTime, st.storeMaxDeliveryTime, shc.categoryCode\n" +
                "                               from store st\n" +
                "                                   inner join store_has_category shc\n" +
                "                                       on st.storeCode = shc.storeCode) P\n" +
                "                       on sc.storeCode = P.storeCode)A\n" +
                "                  inner join (select concat(\n" +
                "                      cast(IF(min(dc.deliveryTip) >= min(dz.deliveryTip), min(dz.deliveryTip), min(dc.deliveryTip)) as char),\n" +
                "                      ' ~ ',\n" +
                "                      cast(IF(max(dc.deliveryTip) >= max(dz.deliveryTip), max(dc.deliveryTip), max(dz.deliveryTip)) as char)) as deliveryTip,\n" +
                "                                  dz.storeCode\n" +
                "                              from store_has_delivery_zone dz\n" +
                "                                  inner join store_has_delivery_cost dc\n" +
                "                                      on dz.storeCode = dc.storeCode)B\n" +
                "                      on A.storeCode = B.storeCode)E\n" +
                "             inner join\n" +
                "             (select distinct C.storeCode, C.menu, D.grade\n" +
                "              from\n" +
                "                  (select group_concat(menuName) as menu, storeCode\n" +
                "                   from menu\n" +
                "                   group by storeCode) C\n" +
                "                      inner join (select U.grade, m.storeCode\n" +
                "                                  from menu m\n" +
                "                                      inner join (select T.grade, T.orderCode, ohm.menuCode\n" +
                "                                                  from order_has_menu ohm\n" +
                "                                                      inner join (select r.orderCode, r.grade\n" +
                "                                                                  from review r\n" +
                "                                                                      inner join orders o\n" +
                "                                                                          on r.orderCode = o.orderCode) T\n" +
                "                                                          on T.orderCode = ohm.orderCode) U\n" +
                "                                          on U.menuCode = m.menuCode) D\n" +
                "                          on C.storeCode = D.storeCode)F\n" +
                "                 on E.storeCode = F.storeCode)G\n" +
                "        inner join (select storeCode, concat(filePath, saveFile) as path\n" +
                "                    from image_store_logo) H\n" +
                "            on G.storeCode = H.storeCode\n" +
                "where G.storeCode in (select G.storeCode from member_has_favorite where id=?)";

        String getStoresByIdParams = id;
        return this.jdbcTemplate.query(getStoresByIdQuery,
                (rs, rowNum) -> new GetStoreRes(
                        rs.getString("storeName"),
                        rs.getString("grade"),
                        rs.getString("menu"),
                        rs.getString("priceMin"),
                        rs.getString("deliveryTip"),
                        rs.getString("deliveryTime"),
                        rs.getString("path")),
                getStoresByIdParams);
    }

    public List<GetPointRes> getPointById(String id){
        String getPointsByIdQuery = "select D.storeName, C.point, C.date, C.expiration\n" +
                "from\n" +
                "(select distinct B.storeCode, A.point, A.date, A.expiration\n" +
                "from\n" +
                "(select menuCode, o.point,\n" +
                "        DATE_FORMAT(orderTime, '%Y-%m-%d') as date,\n" +
                "        DATE_ADD(DATE_FORMAT(orderTime, '%Y-%m-%d'), INTERVAL 1 YEAR ) as expiration\n" +
                "from orders o\n" +
                "inner join order_has_menu ohm\n" +
                "on o.orderCode = ohm.orderCode\n" +
                "where userId = ?)A\n" +
                "inner join menu B\n" +
                "on A.menuCode = B.menuCode)C\n" +
                "inner join store D\n" +
                "on C.storeCode = D.storeCode";

        String getPointsByIdParams = id;
        return this.jdbcTemplate.query(getPointsByIdQuery,
                (rs, rowNum) -> new GetPointRes(
                        rs.getString("storeName"),
                        rs.getString("point"),
                        rs.getString("date"),
                        rs.getString("expiration")),
                getPointsByIdParams);
    }

    public List<GetReviewRes> getReviewById(String id){
        String getReviewByIdQuery = "select storeName, menu, \n" +
                "       grade, content, \n" +
                "       path, createAt, updateAt\n" +
                "from\n" +
                "(select E.userId, E.createAt, E.updateAt,\n" +
                "       E.content, E.grade, path, F.storeName,\n" +
                "       F.storeCode,\n" +
                "       group_concat(menuName) as menu\n" +
                "from\n" +
                "(select C.orderCode, userId, createAt,\n" +
                "       updateAt, content, grade, path,\n" +
                "       D.menuCode\n" +
                "from\n" +
                "(select A.orderCode, userId, createAt,\n" +
                "       updateAt, content, grade,\n" +
                "       concat(B.filePath,B.saveFile) as path\n" +
                "from\n" +
                "(select o.orderCode, userId, createAt,\n" +
                "        updateAt, content, grade\n" +
                "from review r\n" +
                "inner join orders o\n" +
                "on r.orderCode = o.orderCode)A\n" +
                "inner join image_review B\n" +
                "on A.orderCode = B.orderCode)C\n" +
                "inner join order_has_menu D\n" +
                "on C.orderCode = D.orderCode)E\n" +
                "inner join\n" +
                "(select menuCode, s.storeName,\n" +
                "        menuName, s.storeCode\n" +
                "from menu m\n" +
                "inner join store s\n" +
                "on m.storeCode = s.storeCode)F\n" +
                "on E.menuCode = F.menuCode)G\n" +
                "inner join member H\n" +
                "on G.userId = H.id\n" +
                "where id = ?";

        String getReviewByIdParams = id;
        return this.jdbcTemplate.query(getReviewByIdQuery,
                (rs, rowNum) -> new GetReviewRes(
                        rs.getString("storeName"),
                        rs.getString("menu"),
                        rs.getString("grade"),
                        rs.getString("content"),
                        rs.getString("path"),
                        rs.getString("createAt"),
                        rs.getString("updateAt")),
                getReviewByIdParams);
    }

    public int createReviewById(String id, PostReviewReq postReviewReq){
        String createReviewQuery = "insert into review (content, orderCode, grade) VALUES (?,?,?)";
        Object[] createReviewParams = new Object[]{postReviewReq.getContent(), postReviewReq.getOrderCode(), postReviewReq.getGrade()};

        return this.jdbcTemplate.update(createReviewQuery,createReviewParams);
    }

    public int createReviewImageById(String id, PostReviewReq postReviewReq){
        String createReviewImageQuery = "insert into image_review (originFile, saveFile, filePath, orderCode) VALUES (?,?,?,?)";
        Object[] createReviewImageParams = new Object[]{postReviewReq.getOriginFile(), postReviewReq.getSaveFile(), postReviewReq.getSaveFile(), postReviewReq.getOrderCode()};

        return this.jdbcTemplate.update(createReviewImageQuery, createReviewImageParams);
    }

    public int deleteReviewById(String id, String orderCode){
        String deleteReviewQuery = "delete from review where orderCode = ?";

        return this.jdbcTemplate.update(deleteReviewQuery,orderCode);
    }

    public int deleteReviewImageById(String id, String orderCode){
        String deleteReviewImageQuery = "delete from image_review where orderCode = ?";

        return this.jdbcTemplate.update(deleteReviewImageQuery,orderCode);
    }

    public int checkEmail(String email){
        String checkEmailQuery = "select email from member where email = ?";

        List<Object> result = this.jdbcTemplate.query(checkEmailQuery,
                (rs, rowNum) -> new String[]{
                        rs.getString("email")},
                email);
        return result.size();
    }

    public int checkId(String id){
        String checkIdQuery = "select id from member where id = ?";

        List<Object> result = this.jdbcTemplate.query(checkIdQuery,
                (rs, rowNum) -> new String[]{
                        rs.getString("id")},
                id);
        return result.size();
    }

    public int createUser(PostUserReq postUserReq){
        String createUserQuery = "insert into member (id, password, name, nickname, phone, email, address) VALUES (?,?,?,?,?,?,?)";
        Object[] createUserParams = new Object[]{
                postUserReq.getId(),
                postUserReq.getPassword(),
                postUserReq.getName(),
                postUserReq.getNickname(),
                postUserReq.getPhone(),
                postUserReq.getEmail(),
                postUserReq.getAddress()
        };
        return this.jdbcTemplate.update(createUserQuery,createUserParams);
    }

    public User getPwd(PostLoginReq postLoginReq){
        String getPwdQuery = "select * from member where id = ?";
        String getPwdParams = postLoginReq.getId();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new User(
                        rs.getString("id"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("nickname"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getString("point"),
                        rs.getString("rating"),
                        rs.getString("join")),
                getPwdParams
                );

    }


}
