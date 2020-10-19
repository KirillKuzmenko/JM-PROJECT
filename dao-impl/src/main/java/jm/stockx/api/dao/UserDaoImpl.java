package jm.stockx.api.dao;

import jm.stockx.dto.item.ItemPurchaseDto;
import jm.stockx.dto.user.UserDto;
import jm.stockx.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDaoImpl extends AbstractDAO<User, Long> implements UserDAO {

    @Override
    public List<ItemPurchaseDto> getPurchaseStatisticsByUserId(Long id) {
        List<Tuple> list = entityManager.createNativeQuery(
                "SELECT i.item_category AS COfI, " +
                        "count(i.item_category) AS Count " +
                        "FROM userdb.public.items AS i " +
                        "JOIN buying_item b " +
                        "    on i.id = b.item_id " +
                        "JOIN user_buying u  " +
                        "    on b.buying_id = u.buying_id " +
                        "WHERE user_id = :userid " +
                        "GROUP BY i.item_category " +
                        "ORDER BY i.item_category" +
                        "", Tuple.class)
                .setParameter("userid", id)
                .getResultList();

        List<ItemPurchaseDto> itemPurchaseDtoList = new ArrayList<>();
        for (Tuple el : list
        ) {
            itemPurchaseDtoList.add(new ItemPurchaseDto(
                    el.get("COfI", String.class),
                    el.get("Count", BigInteger.class).intValue()
            ));
        }
        return itemPurchaseDtoList;
    }

    @Override
    public UserDto getUserDtoByUserUsername(String username) {
        return entityManager.createQuery("" +
                "SELECT NEW jm.stockx.dto.user.UserDto(" +
                "u.firstName," +
                "u.lastName," +
                "u.email," +
                "u.username," +
                "u.password," +
                "u.sellerLevel," +
                "u.vacationMode," +
                "u.localeTag" +
                ")" +
                "FROM User AS u " +
                "WHERE u.username =: username", UserDto.class)
                .setParameter("username", username)
                .getSingleResult();
    }

    @Override
    public UserDto getUserDtoByUserEmail(String email) {
        return entityManager.createQuery("" +
                "SELECT NEW jm.stockx.dto.user.UserDto(" +
                "u.firstName," +
                "u.lastName," +
                "u.email," +
                "u.username," +
                "u.password," +
                "u.sellerLevel," +
                "u.vacationMode," +
                "u.localeTag" +
                ")" +
                "FROM User AS u " +
                "WHERE u.email =: email", UserDto.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    @Override
    public UserDto getUserDtoByUserAppleId(String appleId) {
        return entityManager.createQuery("" +
                "SELECT NEW jm.stockx.dto.user.UserDto(" +
                "u.firstName," +
                "u.lastName," +
                "u.email," +
                "u.username," +
                "u.password," +
                "u.sellerLevel," +
                "u.vacationMode," +
                "u.localeTag" +
                ")" +
                "FROM User AS u " +
                "WHERE u.appleUserId =: appleId", UserDto.class)
                .setParameter("appleId", appleId)
                .getSingleResult();
    }

    @Override
    public UserDto getUserDtoByUserId(Long id) {
        return entityManager.createQuery("" +
                "SELECT NEW jm.stockx.dto.user.UserDto(" +
                "u.firstName," +
                "u.lastName," +
                "u.email," +
                "u.username," +
                "u.password," +
                "u.sellerLevel," +
                "u.vacationMode," +
                "u.localeTag" +
                ")" +
                "FROM User AS u " +
                "WHERE u.id =: id", UserDto.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public User getUserByUsername(String username) {
        return entityManager.createQuery("" +
                "FROM User AS u WHERE u.username =: username", User.class)
                .setParameter("username", username)
                .getSingleResult();
    }

    @Override
    public User getUserById(Long id) {
        return entityManager.createQuery("" +
                "FROM User AS u WHERE u.id =: id", User.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public User getUserByEmail(String email) {
        try {
            return entityManager.createQuery("" +
                "FROM User AS u WHERE u.email =: email", User.class)
                .setParameter("email", email)
                .getSingleResult();
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
