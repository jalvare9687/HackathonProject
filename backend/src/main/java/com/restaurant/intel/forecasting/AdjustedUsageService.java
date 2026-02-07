package com.restaurant.intel.forecasting;

import org.springframework.stereotype.Service;

@Service
public class AdjustedUsageService {
  public double applyMultiplier(double avgDailyUsage, double multiplier) {
    if (avgDailyUsage <= 0) {
      return 0.0;
    }
    return avgDailyUsage * Math.max(multiplier, 0.0);
  }
}
