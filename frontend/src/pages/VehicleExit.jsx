import { useState } from 'react';
import { parkingService } from '../api/parkingService';
import LoadingSpinner from '../components/LoadingSpinner';
import PageHeader from '../components/PageHeader';
import { useToast } from '../context/ToastContext';
import { notifyApiError } from '../utils/apiError';
import { formatCurrency, formatDateTime } from '../utils/formatters';

export default function VehicleExit() {
  const { showError, showWarning } = useToast();
  const [vehicleNumber, setVehicleNumber] = useState('');
  const [loading, setLoading] = useState(false);
  const [receipt, setReceipt] = useState(null);

  const handleExit = async (e) => {
    e.preventDefault();
    const normalized = vehicleNumber.trim().toUpperCase();
    if (!normalized) {
      showWarning('Please enter a vehicle number');
      return;
    }

    setLoading(true);
    setReceipt(null);

    try {
      const { data } = await parkingService.processExit(normalized);
      setReceipt(data);
      setVehicleNumber('');
    } catch (err) {
      notifyApiError(err, showError);
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <PageHeader title="Vehicle Exit" subtitle="Process exit and generate parking fee receipt" />

      <div className="row g-4">
        <div className="col-lg-5">
          <div className="card content-card">
            <div className="card-body">
              <h5 className="card-title mb-4">Search & Exit</h5>
              {loading ? (
                <LoadingSpinner message="Processing exit..." />
              ) : (
                <form onSubmit={handleExit}>
                  <div className="mb-4">
                    <label htmlFor="vehicleNumber" className="form-label">
                      Vehicle Number
                    </label>
                    <input
                      type="text"
                      className="form-control form-control-lg"
                      id="vehicleNumber"
                      value={vehicleNumber}
                      onChange={(e) => setVehicleNumber(e.target.value)}
                      placeholder="UP32AB1234"
                      required
                    />
                  </div>
                  <button type="submit" className="btn btn-danger w-100">
                    Process Exit
                  </button>
                </form>
              )}
            </div>
          </div>
        </div>

        {receipt && (
          <div className="col-lg-7">
            <div className="card receipt-card">
              <div className="card-body p-4">
                <div className="d-flex justify-content-between align-items-center mb-4">
                  <h4 className="mb-0">Parking Receipt</h4>
                  <span className="badge bg-success">{receipt.message}</span>
                </div>
                <div className="row g-3">
                  <div className="col-sm-6">
                    <small className="text-white-50">Vehicle Number</small>
                    <div className="fs-5 fw-semibold">{receipt.vehicleNumber}</div>
                  </div>
                  <div className="col-sm-6">
                    <small className="text-white-50">Slot Number</small>
                    <div className="fs-5 fw-semibold">{receipt.slotNumber}</div>
                  </div>
                  <div className="col-sm-6">
                    <small className="text-white-50">Entry Time</small>
                    <div>{formatDateTime(receipt.entryTime)}</div>
                  </div>
                  <div className="col-sm-6">
                    <small className="text-white-50">Exit Time</small>
                    <div>{formatDateTime(receipt.exitTime)}</div>
                  </div>
                  <div className="col-sm-6">
                    <small className="text-white-50">Duration</small>
                    <div className="fs-5 fw-semibold">{receipt.durationInHours} hour(s)</div>
                  </div>
                  <div className="col-sm-6">
                    <small className="text-white-50">Parking Fee</small>
                    <div className="fs-3 fw-bold text-warning">
                      {formatCurrency(receipt.parkingFee)}
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        )}
      </div>
    </>
  );
}
