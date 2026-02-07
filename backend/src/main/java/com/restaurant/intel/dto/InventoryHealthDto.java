package com.restaurant.intel.dto;

import com.restaurant.intel.forecasting.RiskLevel;

public record InventoryHealthDto(
    String ingredientName,
    String location,
    Double currentStock,
    Double avgDailyUsage,
    Double adjustedUsage,
    Double daysRemaining,
    RiskLevel riskLevel,
    boolean reorderRecommended,
    String upcomingEvent
) {}
