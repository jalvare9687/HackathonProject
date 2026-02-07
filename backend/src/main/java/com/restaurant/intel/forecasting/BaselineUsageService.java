package com.restaurant.intel.forecasting;

import com.restaurant.intel.config.ForecastingProperties;
import com.restaurant.intel.domain.orders.Order;
import com.restaurant.intel.domain.orders.OrderItem;
import com.restaurant.intel.domain.orders.OrderItemRepository;
import com.restaurant.intel.domain.orders.OrderRepository;
import com.restaurant.intel.domain.recipes.Recipe;
import com.restaurant.intel.domain.recipes.RecipeRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class BaselineUsageService {
  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;
  private final RecipeRepository recipeRepository;
  private final ForecastingProperties properties;

  public BaselineUsageService(
      OrderRepository orderRepository,
      OrderItemRepository orderItemRepository,
      RecipeRepository recipeRepository,
      ForecastingProperties properties
  ) {
    this.orderRepository = orderRepository;
    this.orderItemRepository = orderItemRepository;
    this.recipeRepository = recipeRepository;
    this.properties = properties;
  }

  public Map<Long, Double> calculateAvgDailyUsage(Long locationId) {
    LocalDate endDate = LocalDate.now();
    LocalDate startDate = endDate.minusDays(properties.getHistoryDays());
    LocalDateTime startDateTime = startDate.atStartOfDay();
    LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

    List<Order> orders = orderRepository.findByLocationIdAndOrderedAtBetween(
        locationId,
        startDateTime,
        endDateTime
    );
    if (orders.isEmpty()) {
      return Map.of();
    }

    List<Long> orderIds = orders.stream().map(Order::getId).toList();
    List<OrderItem> items = orderItemRepository.findByOrderIdIn(orderIds);

    Map<Long, Double> totalUsage = new HashMap<>();
    for (OrderItem item : items) {
      if (item.getMenuItem() == null) {
        continue;
      }
      List<Recipe> recipes = recipeRepository.findByMenuItem(item.getMenuItem());
      int quantity = item.getQuantity() == null ? 0 : item.getQuantity();
      for (Recipe recipe : recipes) {
        if (recipe.getIngredient() == null || recipe.getQuantityRequired() == null) {
          continue;
        }
        long ingredientId = recipe.getIngredient().getId();
        double usage = quantity * recipe.getQuantityRequired();
        totalUsage.merge(ingredientId, usage, Double::sum);
      }
    }

    long days = Math.max(1, ChronoUnit.DAYS.between(startDate, endDate));
    return totalUsage.entrySet().stream()
        .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue() / days));
  }
}
