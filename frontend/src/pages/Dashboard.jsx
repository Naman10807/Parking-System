import { useCallback, useEffect, useState } from 'react';
import { dashboardService } from '../api/dashboardService';
import LoadingSpinner from '../components/LoadingSpinner';
import PageHeader from '../components/PageHeader';
import StatCard from '../components/StatCard';
import { useToast } from '../context/ToastContext';
import { notifyApiError } from '../utils/apiError';
import { formatCurrency } from '../utils/formatters';

export default function Dashboard() {
  const { showError } = useToast();
  const [summary, setSummary] = useState(null);
  const [loading, setLoading] = useState(true);

  const loadSummary = useCallback(async () => {
    setLoading(true);
    try {
      const { data } = await dashboardService.getSummary();
      setSummary(data);
    } catch (err) {
      notifyApiError(err, showError);
    } finally {
      setLoading(false);
    }
  }, [showError]);

  useEffect(() => {
    loadSummary();
  }, [loadSummary]);

  return (
    <>
      <PageHeader
        title="Dashboard"
        subtitle="Real-time analytics and revenue overview"
      />

      {loading ? (
        <LoadingSpinner message="Loading dashboard metrics..." fullPage />
      ) : (
        summary && (
          <>
            <div className="row g-4">
              <div className="col-sm-6 col-xl-4">
                <StatCard label="Total Slots" value={summary.totalSlots} variant="primary" icon="🅿️" />
              </div>
              <div className="col-sm-6 col-xl-4">
                <StatCard label="Available Slots" value={summary.availableSlots} variant="success" icon="✅" />
              </div>
              <div className="col-sm-6 col-xl-4">
                <StatCard label="Occupied Slots" value={summary.occupiedSlots} variant="danger" icon="🔴" />
              </div>
              <div className="col-sm-6 col-xl-4">
                <StatCard label="Active Vehicles" value={summary.activeVehicles} variant="warning" icon="🚗" />
              </div>
              <div className="col-sm-6 col-xl-4">
                <StatCard
                  label="Total Vehicles Parked"
                  value={summary.totalVehiclesParked}
                  variant="info"
                  icon="📈"
                />
              </div>
              <div className="col-sm-6 col-xl-4">
                <StatCard
                  label="Total Revenue"
                  value={formatCurrency(summary.totalRevenue)}
                  variant="dark"
                  icon="💰"
                />
              </div>
            </div>
            <div className="mt-4">
              <button type="button" className="btn btn-outline-primary" onClick={loadSummary}>
                Refresh Data
              </button>
            </div>
          </>
        )
      )}
    </>
  );
}
