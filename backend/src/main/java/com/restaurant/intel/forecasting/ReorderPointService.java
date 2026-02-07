package com.restaurant.intel.forecasting;

import com.restaurant.intel.config.ForecastingProperties;
import org.springframework.stereotype.Service;

@Service
public class ReorderPointService {
  private final ForecastingProperties properties;

  public ReorderPointService(ForecastingProperties properties) {
    this.properties = properties;
  }

  public double calculateReorderPoint(double adjustedDailyUsage, int leadTimeDays) {
    if (adjustedDailyUsage <= 0) {
      return 0.0;
    }
    double safetyStock = adjustedDailyUsage * Math.max(properties.getSafetyStockDays(), 0);
    return (adjustedDailyUsage * Math.max(leadTimeDays, 0)) + safetyStock;
  }
}
