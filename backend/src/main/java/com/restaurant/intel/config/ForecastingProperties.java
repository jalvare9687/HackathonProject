package com.restaurant.intel.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.forecasting")
public class ForecastingProperties {
  private int historyDays = 14;
  private int safetyStockDays = 2;

  public int getHistoryDays() {
    return historyDays;
  }

  public void setHistoryDays(int historyDays) {
    this.historyDays = historyDays;
  }

  public int getSafetyStockDays() {
    return safetyStockDays;
  }

  public void setSafetyStockDays(int safetyStockDays) {
    this.safetyStockDays = safetyStockDays;
  }
}
