import { useEffect, useMemo, useState } from 'react';
import { historyService } from '../api/historyService';
import AlertMessage from '../components/AlertMessage';
import LoadingSpinner from '../components/LoadingSpinner';
import PageHeader from '../components/PageHeader';
import Pagination from '../components/Pagination';
import StatusBadge from '../components/StatusBadge';
import { PAGE_SIZE } from '../constants/navigation';
import { getErrorMessage } from '../utils/apiError';
import { formatCurrency, formatDateTime } from '../utils/formatters';
import { paginateItems } from '../utils/pagination';

export default function ParkingHistory() {
  const [records, setRecords] = useState([]);
  const [search, setSearch] = useState('');
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [filterMode, setFilterMode] = useState('all');

  const pagination = useMemo(
    () => paginateItems(records, currentPage, PAGE_SIZE),
    [records, currentPage]
  );

  const loadAllHistory = async () => {
    setLoading(true);
    setError('');
    setFilterMode('all');
    try {
      const { data } = await historyService.getAll();
      setRecords(data);
      setCurrentPage(1);
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadAllHistory();
  }, []);

  const handleSearch = async (e) => {
    e.preventDefault();
    if (!search.trim()) {
      loadAllHistory();
      return;
    }

    setLoading(true);
    setError('');
    setFilterMode('vehicle');
    setStartDate('');
    setEndDate('');

    try {
      const { data } = await historyService.getByVehicle(search.trim().toUpperCase());
      setRecords(data);
      setCurrentPage(1);
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setLoading(false);
    }
  };

  const handleDateFilter = async (e) => {
    e.preventDefault();
    if (!startDate || !endDate) {
      setError('Please select both start and end dates');
      return;
    }

    setLoading(true);
    setError('');
    setFilterMode('date');
    setSearch('');

    try {
      const { data } = await historyService.getByDateRange(startDate, endDate);
      setRecords(data);
      setCurrentPage(1);
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setLoading(false);
    }
  };

  const handleClear = () => {
    setSearch('');
    setStartDate('');
    setEndDate('');
    loadAllHistory();
  };

  const getRecordStatus = (record) => (record.exitTime ? 'COMPLETED' : 'ACTIVE');

  return (
    <>
      <PageHeader title="Parking History" subtitle="Search, filter, and browse parking records" />
      <AlertMessage message={error} onClose={() => setError('')} />

      <div className="card content-card mb-4">
        <div className="card-body">
          <form className="row g-2 align-items-end mb-3" onSubmit={handleSearch}>
            <div className="col-md-8">
              <label htmlFor="search" className="form-label">
                Search by Vehicle Number
              </label>
              <input
                type="text"
                className="form-control"
                id="search"
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                placeholder="UP32AB1234"
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

          <form className="row g-2 align-items-end" onSubmit={handleDateFilter}>
            <div className="col-md-4">
              <label htmlFor="startDate" className="form-label">
                Start Date
              </label>
              <input
                type="date"
                className="form-control"
                id="startDate"
                value={startDate}
                onChange={(e) => setStartDate(e.target.value)}
              />
            </div>
            <div className="col-md-4">
              <label htmlFor="endDate" className="form-label">
                End Date
              </label>
              <input
                type="date"
                className="form-control"
                id="endDate"
                value={endDate}
                onChange={(e) => setEndDate(e.target.value)}
              />
            </div>
            <div className="col-md-4">
              <button type="submit" className="btn btn-outline-primary w-100">
                Filter by Date
              </button>
            </div>
          </form>
        </div>
      </div>

      <div className="card content-card">
        <div className="card-body">
          <div className="d-flex flex-wrap justify-content-between align-items-center mb-3 gap-2">
            <div>
              <h5 className="card-title mb-0">Records</h5>
              <small className="text-muted">
                Showing {pagination.items.length} of {pagination.totalItems} records
                {filterMode !== 'all' && ` (${filterMode} filter)`}
              </small>
            </div>
            <button type="button" className="btn btn-sm btn-outline-primary" onClick={loadAllHistory}>
              Refresh
            </button>
          </div>

          {loading ? (
            <LoadingSpinner message="Loading history..." />
          ) : records.length === 0 ? (
            <p className="text-muted mb-0">No parking records found.</p>
          ) : (
            <>
              <div className="table-responsive">
                <table className="table table-hover align-middle mb-0">
                  <thead className="table-light">
                    <tr>
                      <th>Record ID</th>
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
                    {pagination.items.map((record) => (
                      <tr key={record.recordId}>
                        <td>{record.recordId}</td>
                        <td className="fw-semibold">{record.vehicleNumber}</td>
                        <td>{record.ownerName}</td>
                        <td>{record.vehicleType}</td>
                        <td>{record.slotNumber}</td>
                        <td>{formatDateTime(record.entryTime)}</td>
                        <td>{formatDateTime(record.exitTime)}</td>
                        <td>{formatCurrency(record.parkingFee)}</td>
                        <td>
                          <StatusBadge status={getRecordStatus(record)} />
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
              <Pagination
                currentPage={pagination.currentPage}
                totalPages={pagination.totalPages}
                onPageChange={setCurrentPage}
              />
            </>
          )}
        </div>
      </div>
    </>
  );
}
