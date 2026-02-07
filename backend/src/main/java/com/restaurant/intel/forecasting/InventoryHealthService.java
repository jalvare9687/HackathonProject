package com.restaurant.intel.forecasting;

import com.restaurant.intel.domain.ingredient.Ingredient;
import com.restaurant.intel.domain.ingredient.IngredientRepository;
import com.restaurant.intel.domain.inventory.InventoryLogRepository;
import com.restaurant.intel.domain.location.Location;
import com.restaurant.intel.domain.location.LocationRepository;
import com.restaurant.intel.dto.InventoryHealthDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class InventoryHealthService {
  private final LocationRepository locationRepository;
  private final IngredientRepository ingredientRepository;
  private final InventoryLogRepository inventoryLogRepository;
  private final BaselineUsageService baselineUsageService;
  private final EventMultiplierService eventMultiplierService;
  private final AdjustedUsageService adjustedUsageService;
  private final ReorderPointService reorderPointService;

  public InventoryHealthService(
      LocationRepository locationRepository,
      IngredientRepository ingredientRepository,
      InventoryLogRepository inventoryLogRepository,
      BaselineUsageService baselineUsageService,
      EventMultiplierService eventMultiplierService,
      AdjustedUsageService adjustedUsageService,
      ReorderPointService reorderPointService
  ) {
    this.locationRepository = locationRepository;
    this.ingredientRepository = ingredientRepository;
    this.inventoryLogRepository = inventoryLogRepository;
    this.baselineUsageService = baselineUsageService;
    this.eventMultiplierService = eventMultiplierService;
    this.adjustedUsageService = adjustedUsageService;
    this.reorderPointService = reorderPointService;
  }

  public List<InventoryHealthDto> getInventoryHealth(Optional<Long> locationId) {
    List<Location> locations = locationId
        .map(id -> locationRepository.findById(id).map(List::of).orElseGet(List::of))
        .orElseGet(locationRepository::findAll);

    List<Ingredient> ingredients = ingredientRepository.findAll();
    List<InventoryHealthDto> results = new ArrayList<>();

    for (Location location : locations) {
      Map<Long, Double> avgUsageByIngredient = baselineUsageService.calculateAvgDailyUsage(location.getId());
      for (Ingredient ingredient : ingredients) {
        double currentStock = inventoryLogRepository
            .findTopByLocationIdAndIngredientIdOrderByLoggedAtDesc(location.getId(), ingredient.getId())
            .map(log -> log.getOnHandAfter() == null ? 0.0 : log.getOnHandAfter())
            .orElse(0.0);

        double avgDailyUsage = avgUsageByIngredient.getOrDefault(ingredient.getId(), 0.0);
        int leadTimeDays = ingredient.getLeadTimeDays() == null ? 0 : ingredient.getLeadTimeDays();

        EventMultiplierResult eventResult = eventMultiplierService.getMultiplier(location, leadTimeDays);
        double adjustedUsage = adjustedUsageService.applyMultiplier(avgDailyUsage, eventResult.multiplier());
        Double daysRemaining = adjustedUsage > 0 ? currentStock / adjustedUsage : null;
        RiskLevel riskLevel = calculateRiskLevel(daysRemaining);
        double reorderPoint = reorderPointService.calculateReorderPoint(adjustedUsage, leadTimeDays);
        boolean reorderRecommended = adjustedUsage > 0 && currentStock <= reorderPoint;

        results.add(new InventoryHealthDto(
            ingredient.getName(),
            location.getName(),
            currentStock,
            avgDailyUsage,
            adjustedUsage,
            daysRemaining,
            riskLevel,
            reorderRecommended,
            eventResult.eventName()
        ));
      }
    }

    return results;
  }

  private RiskLevel calculateRiskLevel(Double daysRemaining) {
    if (daysRemaining == null) {
      return RiskLevel.WARNING;
    }
    if (daysRemaining > 7) {
      return RiskLevel.HEALTHY;
    }
    if (daysRemaining > 3) {
      return RiskLevel.WARNING;
    }
    if (daysRemaining > 1) {
      return RiskLevel.CRITICAL;
    }
    return RiskLevel.STOCKOUT_IMMINENT;
  }
}
