import React from 'react';

const colors = {
  HEALTHY: 'risk-healthy',
  WARNING: 'risk-warning',
  CRITICAL: 'risk-critical',
  STOCKOUT_IMMINENT: 'risk-stockout'
};

export default function RiskBadge({ level }) {
  return (
    <span className={`risk-badge ${colors[level] || 'risk-warning'}`}>
      {level.replace('_', ' ')}
    </span>
  );
}
