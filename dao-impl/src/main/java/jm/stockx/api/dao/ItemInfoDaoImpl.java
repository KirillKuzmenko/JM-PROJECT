package jm.stockx.api.dao;

import jm.stockx.dto.SizeInfoDto;
import jm.stockx.dto.itemInfo.*;
import jm.stockx.entity.*;
import jm.stockx.enums.ItemCategory;
import org.joda.money.Money;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ItemInfoDaoImpl extends AbstractDAO<ItemInfo, Long> implements ItemInfoDAO {

    @Override
    public ItemInfo getItemInfoByItemId(Long id) {
        return entityManager.createQuery("" +
                "SELECT i FROM ItemInfo AS i " +
                "WHERE i.item.id = :itemId", ItemInfo.class)
                .setParameter("itemId", id)
                .getSingleResult();
    }

    @Override
    public ItemInfo getItemInfoByItemName(String itemName) {
        return entityManager.createQuery("" +
                "SELECT i FROM ItemInfo AS i " +
                "WHERE i.item.name = :itemName", ItemInfo.class)
                .setParameter("itemName", itemName)
                .getSingleResult();
    }

    @Override
    public ItemCategory getItemCategoryByReleaseDate(LocalDate releaseDate) {
        return entityManager.createQuery("" +
                "SELECT DISTINCT i.itemCategory FROM ItemInfo AS i " +
                "WHERE i.releaseDate = :releaseDate", ItemCategory.class)
                .setParameter("releaseDate", releaseDate)
                .getSingleResult();
    }

    public ItemInfo getItemInfoByItemCategory(ItemCategory itemCategory) {
        return entityManager.createQuery("" +
                "SELECT i FROM ItemInfo AS i " +
                "WHERE i.itemCategory = :itemCategory", ItemInfo.class)
                .setParameter("itemCategory", itemCategory)
                .getSingleResult();
    }

    public List<ItemInfoCardDto> getItemInfoCardDtoMorePrice(Money price) {
        return entityManager.createQuery("" +
                "SELECT NEW jm.stockx.dto.itemInfo.ItemInfoCardDto(" +
                "i.item.name, " +
                "i.itemImageUrl, " +
                "i.lowestAsk " +
                ")" +
                "FROM ItemInfo i " +
                "WHERE i.price > :price", ItemInfoCardDto.class)
                .setParameter("price", price.getAmount())
                .getResultList();
    }

    @Override
    public List<ItemSearchDto> getItemSearchDtoBySearch(String search) {
        return entityManager.createQuery("" +
                "SELECT NEW jm.stockx.dto.itemInfo.ItemSearchDto(i.itemCategory, COUNT(i)) " +
                "FROM ItemInfo i JOIN Item it ON i.id = it.id " +
                "WHERE TRIM(LOWER(it.name)) LIKE LOWER(CONCAT('%', :search, '%')) " +
                "GROUP BY i.itemCategory " +
                "ORDER BY i.itemCategory DESC " +
                "", ItemSearchDto.class)
                .setParameter("search", search.trim())
                .getResultList();
    }

    @Override
    public List<ItemInfoCardDto> getItemInfoCardDtoByItemCategory(ItemCategory category) {
        return entityManager.createQuery("" +
                "SELECT NEW jm.stockx.dto.itemInfo.ItemInfoCardDto(" +
                "i.item.name," +
                "i.itemImageUrl," +
                "i.lowestAsk" +
                ")" +
                "FROM ItemInfo AS i " +
                "WHERE i.itemCategory = :itemCategory", ItemInfoCardDto.class)
                .setParameter("itemCategory", category)
                .getResultList();
    }

    @Override
    public void updateItemImageUrl(Long id, String url) {
        entityManager.createQuery("" +
                "UPDATE ItemInfo i SET i.itemImageUrl = :url " +
                "WHERE i.id = :id ")
                .setParameter("url", url)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public List<ReleaseItemInfoDto> getReleaseItemDtoByPeriod(LocalDateTime beginningPeriod, LocalDateTime endPeriod) {
        return entityManager.createQuery("" +
                "SELECT NEW jm.stockx.dto.itemInfo.ReleaseItemInfoDto(" +
                "i.item.name," +
                "i.condition, " +
                "i.itemImageUrl, " +
                "i.price, " +
                "i.releaseDate " +
                ") " +
                "FROM ItemInfo i " +
                "WHERE i.releaseDate BETWEEN :begin AND :end", ReleaseItemInfoDto.class)
                .setParameter("begin", beginningPeriod)
                .setParameter("end", endPeriod)
                .getResultList();
    }

    @Override
    public ItemInfoDto getItemInfoDtoByItemId(Long id) {
        return entityManager.createQuery("" +
                "SELECT NEW jm.stockx.dto.itemInfo.ItemInfoDto(" +
                "i.id, " +
                "i.price, " +
                "i.lowestAsk, " +
                "i.highestBid, " +
                "i.size.size, " +
                "i.item.id " +
                ") " +
                "FROM ItemInfo i " +
                "WHERE i.item.id = :id", ItemInfoDto.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public List<NotReleaseItemInfoDto> getNotReleasedItem() {
        return entityManager.createQuery("" +
                "SELECT NEW jm.stockx.dto.itemInfo.NotReleaseItemInfoDto(" +
                "i.item.name, " +
                "i.condition, " +
                "i.itemImageUrl, " +
                "i.releaseDate " +
                ") " +
                "FROM ItemInfo i " +
                "WHERE i.releaseDate >= GETDATE(current_date) ", NotReleaseItemInfoDto.class)
                .getResultList();
    }

    @Override
    public List<NotReleaseItemInfoDto> getNotReleasedItemsByBrand(Brand brand) {
        return entityManager.createQuery("" +
                "SELECT NEW jm.stockx.dto.itemInfo.NotReleaseItemInfoDto(" +
                "i.item.name, " +
                "i.condition, " +
                "i.itemImageUrl, " +
                "i.releaseDate " +
                ") " +
                "FROM ItemInfo i " +
                "WHERE i.releaseDate >= GETDATE(current_date) " +
                "AND i.brand = :brandId", NotReleaseItemInfoDto.class)
                .setParameter("brandId", brand.getId())
                .getResultList();
    }

    @Override
    public List<ItemInfoDto> searchItem(String search, Integer page, Integer size) {
        return entityManager.createQuery("" +
                "SELECT NEW jm.stockx.dto.itemInfo.ItemInfoDto(" +
                "i.id, " +
                "i.price, " +
                "i.lowestAsk, " +
                "i.highestBid, " +
                "i.size.size, " +
                "i.item.id " +
                ") " +
                "FROM ItemInfo i " +
                "WHERE i.item.name LIKE CONCAT('%', :search, '%')" , ItemInfoDto.class)
                .setParameter("search", search)
                .setFirstResult(size * (page - 1) + 1)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public ItemInfoDto getItemInfoDtoByItemName(String name) {
        return entityManager.createQuery("" +
                "SELECT NEW jm.stockx.dto.itemInfo.ItemInfoDto(" +
                "i.id, " +
                "i.price, " +
                "i.lowestAsk, " +
                "i.highestBid, " +
                "i.size.size, " +
                "i.item.id " +
                ") " +
                "FROM ItemInfo AS i " +
                "WHERE i.item.name = :name", ItemInfoDto.class)
                .setParameter("name", name)
                .getSingleResult();
    }

    @Override
    public List<ItemInfoDto> getItemInfoDtoByColor(String itemColors) {
        return entityManager.createQuery("" +
                "SELECT NEW jm.stockx.dto.itemInfo.ItemInfoDto(" +
                "i.id, " +
                "i.price, " +
                "i.lowestAsk, " +
                "i.highestBid, " +
                "i.size.size, " +
                "i.item.id " +
                ") " +
                "FROM ItemInfo i " +
                "WHERE i.itemColors = :itemColors", ItemInfoDto.class)
                .setParameter("itemColors", itemColors)
                .getResultList();
    }

    @Override
    public List<ItemInfoDto> getMostPopularItemByBrandName(String name) {
        return entityManager.createQuery("" +
                "SELECT NEW jm.stockx.dto.itemInfo.ItemInfoDto(" +
                "i.id, " +
                "i.price, " +
                "i.lowestAsk, " +
                "i.highestBid, " +
                "i.size.size, " +
                "i.item.id " +
                ") " +
                "FROM ItemInfo i " +
                "INNER JOIN BuyingInfo AS bi " +
                "ON i.id = bi.id " +
                "INNER JOIN Brand AS b " +
                "WHERE b.name = :name " +
                "ORDER BY COUNT(i.id) DESC", ItemInfoDto.class)
                .setParameter("name", name)
                .setMaxResults(10)
                .getResultList();
    }

    @Override
    public List<ItemInfoDto> getMostPopularItemByStyleId(Long id, int topLimit) {
        return entityManager.createQuery("" +
                "SELECT NEW jm.stockx.dto.itemInfo.ItemInfoDto(" +
                "i.id, " +
                "i.price, " +
                "i.lowestAsk, " +
                "i.highestBid, " +
                "i.size.size, " +
                "i.item.id " +
                ") " +
                "FROM SellingInfo si " +
                "LEFT JOIN ItemInfo AS i " +
                "ON si.id = i.id " +
                "WHERE i.style = :styleId " +
                "GROUP BY si.id " +
                "ORDER BY COUNT(si.id) DESC", ItemInfoDto.class)
                .setParameter("styleId", id)
                .setMaxResults(topLimit)
                .getResultList();
    }

    public SizeInfoDto getItemInfoDtoByIdAndSize(Long itemId, ItemSize itemSize) {
        return entityManager.createQuery("" +
                "SELECT NEW jm.stockx.dto.SizeInfoDto(" +
                "item_info.lowestAsk," +
                "item_info.highestBid," +
                "item_info.item.name," +
                "item_info.condition," +
                "item_info.buyingInfo.buyingPrice," +
                "item_info.size" +
                ")" +
                "FROM ItemInfo item_info " +
                "WHERE item_info.item.id = :itemId " +
                "AND   item_info.size = :itemSize", SizeInfoDto.class)
                .setParameter("itemId", itemId)
                .setParameter("itemSize", itemSize)
                .setMaxResults(1)
                .getResultList().get(0);
    }

    @Override
    public List<ItemInfoImageDto> getItemsBuyingYearByUserid(Long id) {
        LocalDateTime end = LocalDateTime.now();
        return entityManager.createQuery("" +
                "SELECT NEW jm.stockx.dto.itemInfo.ItemInfoImageDto(" +
                "i.item.id," +
                "i.item.name," +
                "i.itemImageUrl " +
                ") " +
                "FROM ItemInfo i " +
                "WHERE i.buyingInfo.buyingTimeStamp BETWEEN :begin AND :end " +
                "AND i.buyingInfo.id = :id", ItemInfoImageDto.class)
                .setParameter("begin", end.minusYears(1))
                .setParameter("end", end)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public int getCountOfUserBuyingByUserId(Long userId) {
        return entityManager.createQuery("" +
                "SELECT COUNT(i.buyingInfo.id) " +
                "FROM ItemInfo i " +
                "WHERE i.buyingInfo.id = :id " +
                "AND NOT i.buyingInfo.status = 'CANCELED'", Integer.class)
                .setParameter("id", userId)
                .getSingleResult();
    }
}


