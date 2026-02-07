import React from 'react';
import RiskBadge from './RiskBadge.jsx';
import EventTag from './EventTag.jsx';

const riskOrder = {
  STOCKOUT_IMMINENT: 0,
  CRITICAL: 1,
  WARNING: 2,
  HEALTHY: 3
};

export default function InventoryHealthTable({ items }) {
  const sorted = [...items].sort((a, b) => {
    return riskOrder[a.riskLevel] - riskOrder[b.riskLevel];
  });

  return (
    <table className="health-table">
      <thead>
        <tr>
          <th>Ingredient</th>
          <th>Location</th>
          <th>Current Stock</th>
          <th>Avg Daily Usage</th>
          <th>Adjusted Usage</th>
          <th>Days Remaining</th>
          <th>Risk</th>
          <th>Reorder</th>
          <th>Upcoming Event</th>
        </tr>
      </thead>
      <tbody>
        {sorted.map((item) => (
          <tr key={`${item.ingredientName}-${item.location}`}>
            <td>{item.ingredientName}</td>
            <td>{item.location}</td>
            <td>{item.currentStock?.toFixed(2)}</td>
            <td>{item.avgDailyUsage?.toFixed(2)}</td>
            <td>{item.adjustedUsage?.toFixed(2)}</td>
            <td>{item.daysRemaining ? item.daysRemaining.toFixed(1) : 'N/A'}</td>
            <td>
              <RiskBadge level={item.riskLevel} />
            </td>
            <td>
              <span className={item.reorderRecommended ? 'reorder-yes' : 'reorder-no'}>
                {item.reorderRecommended ? 'Yes' : 'No'}
              </span>
            </td>
            <td>
              <EventTag event={item.upcomingEvent} />
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
