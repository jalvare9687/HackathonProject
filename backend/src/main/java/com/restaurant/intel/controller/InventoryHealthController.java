package com.restaurant.intel.controller;

import com.restaurant.intel.dto.InventoryHealthDto;
import com.restaurant.intel.forecasting.InventoryHealthService;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
public class InventoryHealthController {
  private final InventoryHealthService inventoryHealthService;

  public InventoryHealthController(InventoryHealthService inventoryHealthService) {
    this.inventoryHealthService = inventoryHealthService;
  }

  @GetMapping("/health")
  public List<InventoryHealthDto> getInventoryHealth(@RequestParam Optional<Long> locationId) {
    return inventoryHealthService.getInventoryHealth(locationId);
  }
}
