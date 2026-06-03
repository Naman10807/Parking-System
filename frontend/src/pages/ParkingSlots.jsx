import { useEffect, useState } from 'react';
import { slotService } from '../api/slotService';
import AlertMessage from '../components/AlertMessage';
import PageHeader from '../components/PageHeader';
import StatusBadge from '../components/StatusBadge';
import { getErrorMessage } from '../utils/apiError';

export default function ParkingSlots() {
  const [slots, setSlots] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    loadSlots();
  }, []);

  const loadSlots = async () => {
    setLoading(true);
    setError('');
    try {
      const { data } = await slotService.getAll();
      setSlots(data);
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <PageHeader title="Parking Slots" subtitle="View all parking slots and their status" />
      <AlertMessage message={error} onClose={() => setError('')} />

      <div className="card content-card">
        <div className="card-body">
          <div className="d-flex justify-content-between align-items-center mb-3">
            <h5 className="card-title mb-0">All Slots</h5>
            <button type="button" className="btn btn-sm btn-outline-primary" onClick={loadSlots}>
              Refresh
            </button>
          </div>

          {loading ? (
            <div className="text-center py-4">
              <div className="spinner-border spinner-border-sm text-primary" />
            </div>
          ) : slots.length === 0 ? (
            <p className="text-muted mb-0">No parking slots found. Create slots via the API or Swagger UI.</p>
          ) : (
            <div className="table-responsive">
              <table className="table table-hover align-middle mb-0">
                <thead className="table-light">
                  <tr>
                    <th>ID</th>
                    <th>Slot Number</th>
                    <th>Vehicle Type</th>
                    <th>Status</th>
                  </tr>
                </thead>
                <tbody>
                  {slots.map((slot) => (
                    <tr key={slot.id}>
                      <td>{slot.id}</td>
                      <td className="fw-semibold">{slot.slotNumber}</td>
                      <td>{slot.vehicleType}</td>
                      <td>
                        <StatusBadge status={slot.status} />
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>
    </>
  );
}
