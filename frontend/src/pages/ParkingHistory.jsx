import { useEffect, useState } from 'react';
import { parkingService } from '../api/parkingService';
import AlertMessage from '../components/AlertMessage';
import PageHeader from '../components/PageHeader';
import StatusBadge from '../components/StatusBadge';
import { getErrorMessage } from '../utils/apiError';
import { formatCurrency, formatDateTime } from '../utils/formatters';

export default function ParkingHistory() {
  const [records, setRecords] = useState([]);
  const [search, setSearch] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const loadHistory = async (searchTerm = '') => {
    setLoading(true);
    setError('');
    try {
      const { data } = await parkingService.getHistory(searchTerm);
      setRecords(data);
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadHistory('');
  }, []);

  const handleSearch = (e) => {
    e.preventDefault();
    loadHistory(search);
  };

  const handleClear = () => {
    setSearch('');
    loadHistory('');
  };

  return (
    <>
      <PageHeader title="Parking History" subtitle="Search and view all parking records" />
      <AlertMessage message={error} onClose={() => setError('')} />

      <div className="card content-card mb-4">
        <div className="card-body">
          <form className="row g-2 align-items-end" onSubmit={handleSearch}>
            <div className="col-md-8">
              <label htmlFor="search" className="form-label">
                Search
              </label>
              <input
                type="text"
                className="form-control"
                id="search"
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                placeholder="Vehicle number, owner name, or slot number"
              />
            </div>
            <div className="col-md-4 d-flex gap-2">
              <button type="submit" className="btn btn-primary flex-grow-1">
                Search
              </button>
              <button type="button" className="btn btn-outline-secondary" onClick={handleClear}>
                Clear
              </button>
            </div>
          </form>
        </div>
      </div>

      <div className="card content-card">
        <div className="card-body">
          {loading ? (
            <div className="text-center py-4">
              <div className="spinner-border spinner-border-sm text-primary" />
            </div>
          ) : records.length === 0 ? (
            <p className="text-muted mb-0">No parking records found.</p>
          ) : (
            <div className="table-responsive">
              <table className="table table-hover align-middle mb-0">
                <thead className="table-light">
                  <tr>
                    <th>ID</th>
                    <th>Vehicle</th>
                    <th>Owner</th>
                    <th>Type</th>
                    <th>Slot</th>
                    <th>Entry</th>
                    <th>Exit</th>
                    <th>Fee</th>
                    <th>Status</th>
                  </tr>
                </thead>
                <tbody>
                  {records.map((record) => (
                    <tr key={record.id}>
                      <td>{record.id}</td>
                      <td className="fw-semibold">{record.vehicleNumber}</td>
                      <td>{record.ownerName}</td>
                      <td>{record.vehicleType}</td>
                      <td>{record.slotNumber}</td>
                      <td>{formatDateTime(record.entryTime)}</td>
                      <td>{formatDateTime(record.exitTime)}</td>
                      <td>{formatCurrency(record.parkingFee)}</td>
                      <td>
                        <StatusBadge status={record.status} />
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
