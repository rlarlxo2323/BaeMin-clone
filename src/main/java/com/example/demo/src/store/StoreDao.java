package com.example.demo.src.store;

import com.example.demo.src.store.model.*;
import com.example.demo.utils.OrderNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.sql.DataSource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class StoreDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetStoreRes> getStores(){
        String getStoresQuery = "select G.storeName, avg(G.grade) as grade, G.menu, G.priceMin, G.deliveryTip, G.deliveryTime, H.path\n" +
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
                "            on G.storeCode = H.storeCode";
        return this.jdbcTemplate.query(getStoresQuery,
                (rs,rowNum) -> new GetStoreRes(
                        rs.getString("storeName"),
                        rs.getString("grade"),
                        rs.getString("menu"),
                        rs.getString("priceMin"),
                        rs.getString("deliveryTip"),
                        rs.getString("deliveryTime"),
                        rs.getString("path"))
        );
    }

    public List<GetStoreRes> getStoresByCategory(String category){
        String getStoresByCategoryQuery = "select G.storeName, avg(G.grade) as grade, G.menu, G.priceMin, G.deliveryTip, G.deliveryTime, H.path\n" +
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
                "where G.categoryCode in (select categoryCode from category where categoryName = ?)";

        String getStoresByCategoryParams = category;
        return this.jdbcTemplate.query(getStoresByCategoryQuery,
                (rs, rowNum) -> new GetStoreRes(
                        rs.getString("storeName"),
                        rs.getString("grade"),
                        rs.getString("menu"),
                        rs.getString("priceMin"),
                        rs.getString("deliveryTip"),
                        rs.getString("deliveryTime"),
                        rs.getString("path")),
                getStoresByCategoryParams);
    }

    public List<GetStoreRes> getStoresByMenu(String menu){
        String getStoresByMenuQuery = "select G.storeName, avg(G.grade) as grade, G.menu, G.priceMin, G.deliveryTip, G.deliveryTime, H.path\n" +
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
                "                   where menuName like ?" +
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
                "            on G.storeCode = H.storeCode";

        String getStoresByMenuParams = "%"+menu+"%";
        return this.jdbcTemplate.query(getStoresByMenuQuery,
                (rs, rowNum) -> new GetStoreRes(
                        rs.getString("storeName"),
                        rs.getString("grade"),
                        rs.getString("menu"),
                        rs.getString("priceMin"),
                        rs.getString("deliveryTip"),
                        rs.getString("deliveryTime"),
                        rs.getString("path")),
                getStoresByMenuParams);
    }

    public List<GetMenuRes> getMenuByStoreName(String storeName){
        String getMenuByStoreNameQuery = "select B.storeName, A.menuName, A.price, A.menuInfo, A.tag, path\n" +
                "from\n" +
                "(select m.menuName, m.price, menuInfo, m.tag, concat(img.filePath, img.saveFile) as path, m.storeCode\n" +
                "from menu m\n" +
                "    inner join image_store img\n" +
                "    on m.menuCode = img.menuCode) A\n" +
                "inner join store B\n" +
                "on A.storeCode = B.storeCode\n" +
                "where storeName = ?";

        String getMenuByStoreNameParams = storeName;
        return this.jdbcTemplate.query(getMenuByStoreNameQuery,
                (rs, rowNum) -> new GetMenuRes(
                        rs.getString("menuName"),
                        rs.getString("price"),
                        rs.getString("menuInfo"),
                        rs.getString("tag"),
                        rs.getString("path")),
                getMenuByStoreNameParams);
    }

    public List<GetInfoRes> getInfoByStoreName(String storeName){
        String getInfoByStoreNameQuery = "select storeName, priceMin, payment, deliveryTip, G.deliveryTime, favorite, review\n" +
                "from\n" +
                "(select E.storeCode, storeName, priceMin, payment, deliveryTime, deliveryTip\n" +
                "from\n" +
                "(select storeCode, storeName, priceMin, payment,\n" +
                "       concat(storeMinDeliveryTime, ' ~ ', storeMaxDeliveryTime) as deliveryTime\n" +
                "from store)E\n" +
                "inner join\n" +
                "(select concat(\n" +
                "    cast(IF(min(dc.deliveryTip) >= min(dz.deliveryTip), min(dz.deliveryTip), min(dc.deliveryTip)) as char),\n" +
                "    ' ~ ',\n" +
                "    cast(IF(max(dc.deliveryTip) >= max(dz.deliveryTip), max(dc.deliveryTip), max(dz.deliveryTip)) as char)) as deliveryTip,\n" +
                "    dz.storeCode\n" +
                "from store_has_delivery_zone dz\n" +
                "    inner join store_has_delivery_cost dc\n" +
                "        on dz.storeCode = dc.storeCode)F\n" +
                "on E.storeCode = F.storeCode)G\n" +
                "inner join\n" +
                "(select favorite, review, C.storeCode\n" +
                "from\n" +
                "(select count(*) as favorite, storeCode\n" +
                "from member_has_favorite\n" +
                "where storeCode='A-001')C\n" +
                "inner join\n" +
                "(select count(distinct A.orderCode) as review, storeCode\n" +
                "from\n" +
                "(select r.grade, o.orderCode\n" +
                "from review r\n" +
                "right join orders o\n" +
                "on r.orderCode = o.orderCode)A\n" +
                "left join\n" +
                "(select m.storeCode, ohm.orderCode\n" +
                "from menu m\n" +
                "inner join order_has_menu ohm\n" +
                "on m.menuCode = ohm.menuCode)B\n" +
                "on A.orderCode = B.orderCode\n" +
                "where storeCode='A-001')D\n" +
                "on C.storeCode = D.storeCode)H\n" +
                "on G.storeCode = H.storeCode\n" +
                "where G.storeName = ?";

        String getInfoByStoreNameParams = storeName;
        return this.jdbcTemplate.query(getInfoByStoreNameQuery,
                (rs, rowNum) -> new GetInfoRes(
                        rs.getString("storeName"),
                        rs.getString("priceMin"),
                        rs.getString("payment"),
                        rs.getString("deliveryTip"),
                        rs.getString("deliveryTime"),
                        rs.getString("favorite"),
                        rs.getString("review")),
                getInfoByStoreNameParams);
    }

    public List<GetStoreInfoRes> getStoreInfoByStoreName(String storeName){
        String getStoreInfoByStoreNameQuery = "select A.brandName,\n" +
                "       A.holiday,\n" +
                "       A.phone,\n" +
                "       concat(B.tag,\n" +
                "           ' - ',\n" +
                "       IF(INSTR(DATE_FORMAT(B.storeStartTime, '%p %h:%i'), 'PM') > 0,\n" +
                "          REPLACE(DATE_FORMAT(B.storeStartTime, '%p %h:%i'), 'PM', '오후'),\n" +
                "          REPLACE(DATE_FORMAT(B.storeStartTime, '%p %h:%i'), 'AM', '오전')),\n" +
                "           ' ~ ',\n" +
                "        IF(INSTR(DATE_FORMAT(B.storeEndTime, '%p %h:%i'), 'PM') > 0,\n" +
                "          REPLACE(DATE_FORMAT(B.storeEndTime, '%p %h:%i'), 'PM', '오후'),\n" +
                "          REPLACE(DATE_FORMAT(B.storeEndTime, '%p %h:%i'), 'AM', '오전'))) as hours\n" +
                "from\n" +
                "    (select o.storeCode,\n" +
                "        o.storeName as brandName,\n" +
                "        s.holiday,\n" +
                "        s.phone,\n" +
                "        s.storeName\n" +
                "     from owner o\n" +
                "         inner join store s\n" +
                "             on o.storeCode = s.storeCode)A\n" +
                "        inner join store_operating_time B\n" +
                "            on A.storeCode = B.storeCode\n" +
                "where storeName = ?";

        String getStoreInfoByStoreNameParams = storeName;
        return this.jdbcTemplate.query(getStoreInfoByStoreNameQuery,
                (rs, rowNum) -> new GetStoreInfoRes(
                        rs.getString("brandName"),
                        rs.getString("holiday"),
                        rs.getString("phone"),
                        rs.getString("hours")),
                getStoreInfoByStoreNameParams);
    }

    public List<GetDeliveryTipRes> getDeliveryTipByStoreName(String storeName){
        String getDeliveryTipByStoreNameQuery = "select division, deliveryTip\n" +
                "from\n" +
                "((select\n" +
                "        IF(costOver=costUnder, concat(costOver, '이상'), concat(costUnder, ' ~ ', costOver)) as division,\n" +
                "        deliveryTip,\n" +
                "        storeCode\n" +
                "from store_has_delivery_cost)\n" +
                "union\n" +
                "(select group_concat(deliveryArea) as devision, deliveryTip, storeCode\n" +
                "from store_has_delivery_zone\n" +
                "group by deliveryTip)) A\n" +
                "inner join store B\n" +
                "on A.storeCode = B.storeCode\n" +
                "where storeName = ?";

        String getDeliveryTipByStoreNameParams = storeName;
        return this.jdbcTemplate.query(getDeliveryTipByStoreNameQuery,
                (rs, rowNum) -> new GetDeliveryTipRes(
                        rs.getString("division"),
                        rs.getString("deliveryTip")),
                getDeliveryTipByStoreNameParams);
    }

    public List<GetOptionRes> getOptionByStoreName(String storeName){
        String getOptionByStoreNameQuery = "select optionName, additionalPrice, category\n" +
                "from store_has_option sho\n" +
                "inner join store s on sho.storeCode = s.storeCode\n" +
                "where storeName = ?";

        String getOptionByStoreNameParams = storeName;
        return this.jdbcTemplate.query(getOptionByStoreNameQuery,
                (rs, rowNum) -> new GetOptionRes(
                        rs.getString("optionName"),
                        rs.getString("additionalPrice"),
                        rs.getString("category")),
                getOptionByStoreNameParams);
    }

    public int createOptionByStoreName(String storeName, PostOptionReq postOptionReq){
        String getStoreCodeByStoreName = "select storeCode from store where storeName = ?";
        String getStoreCodeByStoreParams = storeName;

        List<String> getStoreCode = this.jdbcTemplate.query(getStoreCodeByStoreName,
                (rs, rowNum) -> new String(
                        rs.getString("storeCode")),getStoreCodeByStoreParams);
        String storeCode = getStoreCode.get(0);
        String getOptionNum = "select max(id) from store_has_option";
        int optionNum = this.jdbcTemplate.queryForObject(getOptionNum,int.class)+1;
        String optionCode = storeCode + "-" + (Integer.toString(optionNum));

        String modifyUserNameQuery = "insert into store_has_option (optionCode, storeCode, optionName, additionalPrice, category) VALUES (?,?,?,?,?)";
        Object[] modifyUserNameParams = new Object[]{optionCode, storeCode, postOptionReq.getOptionName(), postOptionReq.getAdditionalPrice(), postOptionReq.getCategory()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public int deleteOptionByStore(String storeName, String optionCode){
        String getStoreCodeByStoreName = "select storeCode from store where storeName = ?";
        String getStoreCodeByStoreParams = storeName;

        List<String> getStoreCode = this.jdbcTemplate.query(getStoreCodeByStoreName,
                (rs, rowNum) -> new String(
                        rs.getString("storeCode")),getStoreCodeByStoreParams);
        String storeCode = getStoreCode.get(0);

        String deleteOptionQuery = "delete from store_has_option where storeCode = ? and optionCode = ?";
        Object[] deleteOptionParams = new Object[]{storeCode, optionCode};

        return this.jdbcTemplate.update(deleteOptionQuery,deleteOptionParams);
    }

    public List<GetOptionRes> getOptionByMenuCode(String storeName, String menuCode){
        String getStoreCodeByStoreName = "select storeCode from store where storeName = ?";
        String getStoreCodeByStoreParams = storeName;

        List<String> getStoreCode = this.jdbcTemplate.query(getStoreCodeByStoreName,
                (rs, rowNum) -> new String(
                        rs.getString("storeCode")),getStoreCodeByStoreParams);
        String storeCode = getStoreCode.get(0);

        String getOptionByMenuCodeQuery = "select optionName, additionalPrice, category\n" +
                "from\n" +
                "(select sho.optionCode, optionName,\n" +
                "       additionalPrice, category,\n" +
                "       menuCode\n" +
                "from store_has_option sho\n" +
                "inner join menu_has_option mho\n" +
                "on sho.optionCode = mho.optionCode)A\n" +
                "inner join menu B\n" +
                "on A.menuCode = B.menuCode\n" +
                "where A.menuCode=? and storeCode=?";

        Object[] getOptionByMenuCodeParams = new Object[]{menuCode, storeCode};
        return this.jdbcTemplate.query(getOptionByMenuCodeQuery,
                (rs, rowNum) -> new GetOptionRes(
                        rs.getString("optionName"),
                        rs.getString("additionalPrice"),
                        rs.getString("category")),
                getOptionByMenuCodeParams);
    }

    public int createOptionByMenuCode(String storeName, String menuCode, Map<String, String> postOptionByMenuCodeReq){
        String getStoreCodeByStoreName = "select storeCode from store where storeName = ?";
        String getStoreCodeByStoreParams = storeName;

        List<String> getStoreCode = this.jdbcTemplate.query(getStoreCodeByStoreName,
                (rs, rowNum) -> new String(
                        rs.getString("storeCode")),getStoreCodeByStoreParams);
        String storeCode = getStoreCode.get(0);

        String modifyUserNameQuery = "insert into menu_has_option (optionCode, menuCode) VALUES (?,?)";
        Object[] modifyUserNameParams = new Object[]{postOptionByMenuCodeReq.get("optionCode"), menuCode};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public int createMenu(String storeName, PostMenuReq postMenuReq){
        String getStoreCodeByStoreName = "select storeCode from store where storeName = ?";
        String getStoreCodeByStoreParams = storeName;

        List<String> getStoreCode = this.jdbcTemplate.query(getStoreCodeByStoreName,
                (rs, rowNum) -> new String(
                        rs.getString("storeCode")),getStoreCodeByStoreParams);
        String storeCode = getStoreCode.get(0);

        String createMenuImageQuery = "insert into image_store (originFile, saveFile, filePath, storeCode, menuCode) VALUES (?,?,?,?,?)";


        String createMenuQuery = "insert into menu (menuCode, storeCode, menuName, price, menuInfo, tag) VALUES (?,?,?,?,?,?)";
        Object[] createMenuParams = new Object[]{
                postMenuReq.getMenuCode(),
                storeCode,
                postMenuReq.getMenuName(),
                postMenuReq.getPrice(),
                postMenuReq.getMenuInfo(),
                postMenuReq.getTag()
        };

        return this.jdbcTemplate.update(createMenuQuery,createMenuParams);
    }

    public int checkStore(String storeName){
        String searchStoreQuery = "select storeCode from store where storeName = ?";

        List<Object> result = this.jdbcTemplate.query(searchStoreQuery,
                (rs, rowNum) -> new String[]{
                        rs.getString("storeCode")},
                storeName);
        return result.size();
    }

    public int checkMenu(String menuCode){
        String searchStoreQuery = "select menuCode from menu where menuCode = ?";

        List<Object> result = this.jdbcTemplate.query(searchStoreQuery,
                (rs, rowNum) -> new String[]{
                        rs.getString("menuCode")},
                menuCode);
        return result.size();
    }

    public int modifyMenu(String storeName, PostMenuReq postMenuReq){
        String getStoreCodeByStoreName = "select storeCode from store where storeName = ?";
        String getStoreCodeByStoreParams = storeName;

        List<String> getStoreCode = this.jdbcTemplate.query(getStoreCodeByStoreName,
                (rs, rowNum) -> new String(
                        rs.getString("storeCode")),getStoreCodeByStoreParams);
        String storeCode = getStoreCode.get(0);

        String modifyMenuByMenuNameQuery = "update menu set menuName = ? where menuCode = ? and storeCode = ?";
        String modifyMenuByMenuPriceQuery = "update menu set price = ? where menuCode = ? and storeCode = ?";
        String modifyMenuByMenuInfoQuery = "update menu set menuInfo = ? where menuCode = ? and storeCode = ?";
        String modifyMenuByMenuTagQuery = "update menu set tag = ? where menuCode = ? and storeCode = ?";
        int result = 0;
        Object[] modifyMenuParams;
        if (postMenuReq.getMenuName() != null){
            modifyMenuParams = new Object[]{
                    postMenuReq.getMenuName(),
                    postMenuReq.getMenuCode(),
                    storeCode
            };
            result += this.jdbcTemplate.update(modifyMenuByMenuNameQuery,modifyMenuParams);
        }
        if (postMenuReq.getPrice() != null){
            modifyMenuParams = new Object[]{
                    postMenuReq.getPrice(),
                    postMenuReq.getMenuCode(),
                    storeCode
            };
            result += this.jdbcTemplate.update(modifyMenuByMenuPriceQuery,modifyMenuParams);
        }
        if (postMenuReq.getMenuInfo() != null){
            modifyMenuParams = new Object[]{
                    postMenuReq.getMenuInfo(),
                    postMenuReq.getMenuCode(),
                    storeCode
            };
            result += this.jdbcTemplate.update(modifyMenuByMenuInfoQuery,modifyMenuParams);
        }
        if (postMenuReq.getTag() != null){
            modifyMenuParams = new Object[]{
                    postMenuReq.getTag(),
                    postMenuReq.getMenuCode(),
                    storeCode
            };
            result += this.jdbcTemplate.update(modifyMenuByMenuTagQuery,modifyMenuParams);
        }
        return result;
    }

    public int deleteMenu(String storeName, String menuCode){
        String getStoreCodeByStoreName = "select storeCode from store where storeName = ?";
        String getStoreCodeByStoreParams = storeName;

        List<String> getStoreCode = this.jdbcTemplate.query(getStoreCodeByStoreName,
                (rs, rowNum) -> new String(
                        rs.getString("storeCode")),getStoreCodeByStoreParams);
        String storeCode = getStoreCode.get(0);

        String deleteOptionQuery = "delete from menu_has_option where menuCode = ?";
        this.jdbcTemplate.update(deleteOptionQuery, menuCode);

        String deleteImageQuery = "delete from image_store where menuCode = ?";
        this.jdbcTemplate.update(deleteImageQuery, menuCode);

        String deleteMenuQuery = "delete from menu where storeCode = ? and menuCode = ?";
        Object[] deleteMenuParams = new Object[]{storeCode, menuCode};

        return this.jdbcTemplate.update(deleteMenuQuery,deleteMenuParams);
    }

    public String ordering(String id, String storeName, PostOrdersReq postOrdersReq){
        String getStoreCodeByStoreName = "select storeCode from store where storeName = ?";
        String getStoreCodeByStoreParams = storeName;

        List<String> getStoreCode = this.jdbcTemplate.query(getStoreCodeByStoreName,
                (rs, rowNum) -> new String(
                        rs.getString("storeCode")),getStoreCodeByStoreParams);
        String storeCode = getStoreCode.get(0);

        Date now = new Date();
        String orderCode = OrderNumber.makeString(now);

        String createOrdersQuery = "insert into orders (orderCode, userId, userAddress, demandStore, demandDelivery) VALUES (?,?,?,?,?)";
        Object[] createOrderParams = new Object[]{
                orderCode,
                id,
                postOrdersReq.getAddress(),
                postOrdersReq.getDemandStore(),
                postOrdersReq.getDemandDelivery()
        };
        this.jdbcTemplate.update(createOrdersQuery, createOrderParams);

        String getPriceByStoreName = "select price from menu where menuCode = ?";
        String getPriceByStoreParams = postOrdersReq.getMenuCode();

        List<String> getPrice = this.jdbcTemplate.query(getPriceByStoreName,
                (rs, rowNum) -> new String(
                        rs.getString("price")),getPriceByStoreParams);
        String menuPrice = getPrice.get(0);
        String realPrice = Integer.toString((Integer.parseInt(menuPrice)) * (Integer.parseInt(postOrdersReq.getAmount())));

        System.out.println(realPrice);
        String createOrderToMenu;
        Object[] createMenuParams;
        if (postOrdersReq.getOptionCode() == null){
            createOrderToMenu = "insert into order_has_menu (orderCode, menuCode, amount, price) VALUES (?,?,?,?)";
            createMenuParams = new Object[]{
                    orderCode,
                    postOrdersReq.getMenuCode(),
                    postOrdersReq.getAmount(),
                    realPrice
            };
        }else {
            createOrderToMenu = "insert into order_has_menu (orderCode, menuCode, amount, price, option) VALUES (?,?,?,?,?)";
            createMenuParams = new Object[]{
                    orderCode,
                    postOrdersReq.getMenuCode(),
                    postOrdersReq.getAmount(),
                    realPrice,
                    postOrdersReq.getOptionCode()
            };
        }
        this.jdbcTemplate.update(createOrderToMenu, createMenuParams);
        return orderCode;
    }

    public List<PostOrdersRes> getOderInfo(String oderCode){
        String getOderInfoQuery = "select D.storeName,\n" +
                "       menuName,\n" +
                "       price,\n" +
                "       orderCode,\n" +
                "       userAddress,\n" +
                "       orderTime,\n" +
                "       IF(orderFlag = 1,\n" +
                "         '배달완료', '배달준비') as flag,\n" +
                "       amount,\n" +
                "       `option`\n" +
                "from\n" +
                "(select A.storeCode,\n" +
                "       A.menuName,\n" +
                "       A.price,\n" +
                "       B.orderCode,\n" +
                "       userId,\n" +
                "       userAddress,\n" +
                "       orderTime,\n" +
                "       orderFlag,\n" +
                "       A.menuCode,\n" +
                "       amount,\n" +
                "       `option`\n" +
                "from menu A\n" +
                "inner join\n" +
                "(select o.orderCode,\n" +
                "       o.userId,\n" +
                "       o.userAddress,\n" +
                "       o.orderTime,\n" +
                "       o.orderFlag,\n" +
                "       ohm.menuCode,\n" +
                "       ohm.amount,\n" +
                "       ohm.`option`\n" +
                "from orders o\n" +
                "inner join order_has_menu ohm\n" +
                "on o.orderCode = ohm.orderCode)B\n" +
                "on A.menuCode = B.menuCode)C\n" +
                "inner join store D\n" +
                "on C.storeCode = D.storeCode\n" +
                "where orderCode=?";

        String getOderInfoParams = oderCode;
        return this.jdbcTemplate.query(getOderInfoQuery,
                (rs, rowNum) -> new PostOrdersRes(
                        rs.getString("storeName"),
                        rs.getString("menuName"),
                        rs.getString("price"),
                        rs.getString("orderCode"),
                        rs.getString("userAddress"),
                        rs.getString("orderTime"),
                        rs.getString("flag"),
                        rs.getString("amount"),
                        rs.getString("option")),
                getOderInfoParams);
    }
}
