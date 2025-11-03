package me.bttf.smartstore;

public interface InventoryRowView {
    Long getOptionId();   // opt.id
    Long getProductId();  // p.id
    String getName();     // p.name
    String getImageUrl(); // p.thumbnailUrl
    String getCategory(); // 대표 카테고리명
    Long getSalePrice();  // opt.price (정수/원 단위라면 Long)
    Integer getStockQty();// opt.stockQty
    Boolean getSoldOut(); // 품절 여부
}