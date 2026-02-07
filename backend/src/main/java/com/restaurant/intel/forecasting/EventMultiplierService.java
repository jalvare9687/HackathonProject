package com.restaurant.intel.forecasting;

import com.restaurant.intel.domain.events.Event;
import com.restaurant.intel.domain.events.EventRepository;
import com.restaurant.intel.domain.location.Location;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EventMultiplierService {
  private final EventRepository eventRepository;

  public EventMultiplierService(EventRepository eventRepository) {
    this.eventRepository = eventRepository;
  }

  public EventMultiplierResult getMultiplier(Location location, int leadTimeDays) {
    if (location == null || location.getId() == null) {
      return new EventMultiplierResult(1.0, null);
    }
    LocalDate today = LocalDate.now();
    LocalDate endDate = today.plusDays(Math.max(leadTimeDays, 0));
    List<Event> events = eventRepository.findByLocationIdAndEventDateBetween(
        location.getId(),
        today,
        endDate
    );
    if (events.isEmpty()) {
      return new EventMultiplierResult(1.0, null);
    }

    Event topEvent = events.stream()
        .filter(event -> event.getEventType() != null)
        .filter(event -> event.getEventType().getDemandMultiplier() != null)
        .max(Comparator.comparingDouble(event -> event.getEventType().getDemandMultiplier()))
        .orElse(null);

    if (topEvent == null || topEvent.getEventType() == null
        || topEvent.getEventType().getDemandMultiplier() == null) {
      return new EventMultiplierResult(1.0, null);
    }

    return new EventMultiplierResult(topEvent.getEventType().getDemandMultiplier(),
        topEvent.getEventType().getName());
  }
}
