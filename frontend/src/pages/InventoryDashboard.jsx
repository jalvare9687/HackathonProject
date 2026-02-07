import React, { useEffect, useState } from 'react';
import InventoryHealthTable from '../components/InventoryHealthTable.jsx';
import { fetchInventoryHealth } from '../services/inventoryApi.js';

export default function InventoryDashboard() {
  const [items, setItems] = useState([]);
  const [status, setStatus] = useState('loading');
  const fallbackItems = [
    {
      ingredientName: 'Tomato Sauce',
      location: 'Downtown',
      currentStock: 18.5,
      avgDailyUsage: 6.2,
      adjustedUsage: 8.7,
      daysRemaining: 2.1,
      riskLevel: 'CRITICAL',
      reorderRecommended: true,
      upcomingEvent: 'Sports'
    },
    {
      ingredientName: 'Mozzarella',
      location: 'Downtown',
      currentStock: 42.0,
      avgDailyUsage: 5.1,
      adjustedUsage: 5.1,
      daysRemaining: 8.2,
      riskLevel: 'HEALTHY',
      reorderRecommended: false,
      upcomingEvent: null
    }
  ];

  useEffect(() => {
    fetchInventoryHealth()
      .then((data) => {
        setItems(data);
        setStatus('ready');
      })
      .catch(() => {
        setItems(fallbackItems);
        setStatus('fallback');
      });
  }, []);

  return (
    <div className="dashboard">
      <header className="dashboard-header">
        <h1>Inventory Health Monitor</h1>
        <p>Predictive stockout risk and reorder guidance for each location.</p>
      </header>

      {status === 'loading' && <p>Loading inventory health...</p>}
      {status === 'fallback' && (
        <div className="error">Using sample data. Connect the API for live results.</div>
      )}
      {(status === 'ready' || status === 'fallback') && (
        <InventoryHealthTable items={items} />
      )}
    </div>
  );
}
