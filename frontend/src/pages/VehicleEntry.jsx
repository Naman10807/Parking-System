import { useState } from 'react';
import { parkingService } from '../api/parkingService';
import LoadingSpinner from '../components/LoadingSpinner';
import PageHeader from '../components/PageHeader';
import { VEHICLE_TYPES } from '../constants/navigation';
import { useToast } from '../context/ToastContext';
import { notifyApiError } from '../utils/apiError';
import { formatDateTime } from '../utils/formatters';

const INITIAL_FORM = {
  vehicleNumber: '',
  ownerName: '',
  vehicleType: 'CAR',
};

export default function VehicleEntry() {
  const { showError, showSuccess } = useToast();
  const [form, setForm] = useState(INITIAL_FORM);
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState(null);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setResult(null);

    try {
      const { data } = await parkingService.registerEntry({
        vehicleNumber: form.vehicleNumber.trim(),
        ownerName: form.ownerName.trim(),
        vehicleType: form.vehicleType,
      });
      setResult(data);
      showSuccess(data.message || 'Vehicle entered successfully');
      setForm(INITIAL_FORM);
    } catch (err) {
      notifyApiError(err, showError);
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <PageHeader title="Vehicle Entry" subtitle="Register a new vehicle into the parking system" />

      <div className="row g-4">
        <div className="col-lg-6">
          <div className="card content-card">
            <div className="card-body">
              <h5 className="card-title mb-4">Entry Form</h5>
              {loading ? (
                <LoadingSpinner message="Registering entry..." />
              ) : (
                <form onSubmit={handleSubmit}>
                  <div className="mb-3">
                    <label htmlFor="vehicleNumber" className="form-label">
                      Vehicle Number
                    </label>
                    <input
                      type="text"
                      className="form-control"
                      id="vehicleNumber"
                      name="vehicleNumber"
                      value={form.vehicleNumber}
                      onChange={handleChange}
                      placeholder="UP32AB1234"
                      required
                    />
                  </div>
                  <div className="mb-3">
                    <label htmlFor="ownerName" className="form-label">
                      Owner Name
                    </label>
                    <input
                      type="text"
                      className="form-control"
                      id="ownerName"
                      name="ownerName"
                      value={form.ownerName}
                      onChange={handleChange}
                      placeholder="Naman"
                      required
                    />
                  </div>
                  <div className="mb-4">
                    <label htmlFor="vehicleType" className="form-label">
                      Vehicle Type
                    </label>
                    <select
                      className="form-select"
                      id="vehicleType"
                      name="vehicleType"
                      value={form.vehicleType}
                      onChange={handleChange}
                      required
                    >
                      {VEHICLE_TYPES.map((type) => (
                        <option key={type} value={type}>
                          {type}
                        </option>
                      ))}
                    </select>
                  </div>
                  <button type="submit" className="btn btn-primary w-100">
                    Register Entry
                  </button>
                </form>
              )}
            </div>
          </div>
        </div>

        {result && (
          <div className="col-lg-6">
            <div className="card content-card border-success">
              <div className="card-body">
                <h5 className="card-title text-success mb-3">Entry Confirmed</h5>
                <ul className="list-group list-group-flush">
                  <li className="list-group-item d-flex justify-content-between">
                    <span>Vehicle</span>
                    <strong>{result.vehicleNumber}</strong>
                  </li>
                  <li className="list-group-item d-flex justify-content-between">
                    <span>Slot</span>
                    <strong>{result.slotNumber}</strong>
                  </li>
                  <li className="list-group-item d-flex justify-content-between">
                    <span>Entry Time</span>
                    <strong>{formatDateTime(result.entryTime)}</strong>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        )}
      </div>
    </>
  );
}
