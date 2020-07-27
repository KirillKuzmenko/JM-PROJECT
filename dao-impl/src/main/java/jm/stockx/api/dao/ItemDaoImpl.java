package jm.stockx.api.dao;

import jm.stockx.dto.ItemDto;
import jm.stockx.entity.Item;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ItemDaoImpl extends AbstractDAO<Item, Long> implements ItemDAO {

    @Override
    public Optional<Item> getByName(String name) {
        Item item = entityManager.createQuery("FROM Item i WHERE i.name = :itemName", Item.class)
                .setParameter("itemName", name)
                .getSingleResult();
        return Optional.of(item);
    }

    @Override
    public List<ItemDto> searchItem(String search, Integer page, Integer size) {
        return entityManager.createQuery("SELECT i FROM Item i  WHERE " +
                "i.name LIKE '%" + search + "%'", Item.class)
                .setFirstResult(size * (page - 1) + 1)
                .setMaxResults(size)
                .getResultList()
                .stream()
                .map(ItemDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> getMostPopularItems(String brand) {
        String sql = "SELECT i.id " +
                " FROM items as i " +
                "INNER JOIN buying_item bi on i.id = bi.item_id INNER JOIN brand AS b" +
                " WHERE b.name =:brand" +
                " ORDER BY COUNT(i.id) DESC";

        return entityManager.createNativeQuery(sql, Item.class)
                .setParameter("brand", brand)
                .setMaxResults(10)
                .getResultList();
    }

    public List<Item> getTopItemsByStyleFromSellingInfo(Long styleId, int topLimit) {
        String sql = "select i.* from selling_info as si left join items as i on si.item_id=i.id " +
                " where i.style_id = :styleId" +
                " group by si.item_id order by count(si.item_id) desc";
        return entityManager.createNativeQuery(sql, Item.class)
                .setParameter("styleId", styleId)
                .setMaxResults(topLimit)
                .getResultList();
    }

    @Override
    public void addItemImage(Long id, Byte[] array) {
        entityManager.createQuery("UPDATE Item i SET i.itemImage = :bytesOfImage WHERE id=:id", Item.class)
                .setParameter("bytesOfImage", array)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public Byte[] getItemImage(Long id) {
        List<Item> list = entityManager
                .createQuery("FROM Item WHERE id=:id", Item.class)
                .setParameter("id", id)
                .getResultList();
        if (list.size() == 0) {
            return null;
        }
        return list.get(0).getItemImage();
    }

    @Override
    public List<Item> getByColors(String itemColors) {
        return entityManager.createQuery("FROM Item i WHERE i.itemColors = :itemColors", Item.class)
                .setParameter("itemColors", itemColors)
                .getResultList();
    }
}
