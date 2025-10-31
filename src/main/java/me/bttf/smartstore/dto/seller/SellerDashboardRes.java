package me.bttf.smartstore.dto.seller;

public record SellerDashboardRes(
        long productCount,
        long orderCount,
        long lowStockCount
) {}
