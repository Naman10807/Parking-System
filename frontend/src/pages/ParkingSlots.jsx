import { useCallback, useEffect, useState } from 'react';
import { slotService } from '../api/slotService';
import AlertMessage from '../components/AlertMessage';
import LoadingSpinner from '../components/LoadingSpinner';
import PageHeader from '../components/PageHeader';
import StatusBadge from '../components/StatusBadge';
import { SLOT_STATUSES, VEHICLE_TYPES } from '../constants/navigation';
import { useAuth } from '../context/AuthContext';
import { getErrorMessage } from '../utils/apiError';

const EMPTY_FORM = {
  slotNumber: '',
  vehicleType: 'CAR',
  status: 'AVAILABLE',
};

export default function ParkingSlots() {
  const { isAdmin } = useAuth();
  const [slots, setSlots] = useState([]);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [showForm, setShowForm] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [form, setForm] = useState(EMPTY_FORM);

  const loadSlots = useCallback(async () => {
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
  }, []);

  useEffect(() => {
    loadSlots();
  }, [loadSlots]);

  const resetForm = () => {
    setForm(EMPTY_FORM);
    setEditingId(null);
    setShowForm(false);
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const openCreateForm = () => {
    setForm(EMPTY_FORM);
    setEditingId(null);
    setShowForm(true);
    setSuccess('');
  };

  const openEditForm = (slot) => {
    setForm({
      slotNumber: slot.slotNumber,
      vehicleType: slot.vehicleType,
      status: slot.status,
    });
    setEditingId(slot.id);
    setShowForm(true);
    setSuccess('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    setError('');
    setSuccess('');

    try {
      if (editingId) {
        await slotService.update(editingId, form);
        setSuccess('Parking slot updated successfully');
      } else {
        await slotService.create(form);
        setSuccess('Parking slot created successfully');
      }
      resetForm();
      await loadSlots();
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async (id, slotNumber) => {
    if (!window.confirm(`Delete slot ${slotNumber}?`)) return;

    setError('');
    setSuccess('');
    try {
      await slotService.remove(id);
      setSuccess(`Slot ${slotNumber} deleted successfully`);
      await loadSlots();
    } catch (err) {
      setError(getErrorMessage(err));
    }
  };

  return (
    <>
      <PageHeader
        title="Parking Slots"
        subtitle={isAdmin ? 'Manage parking slots' : 'View parking slot availability'}
      />
      <AlertMessage message={error} onClose={() => setError('')} />
      <AlertMessage type="success" message={success} onClose={() => setSuccess('')} />

      <div className="d-flex flex-wrap gap-2 mb-3">
        <button type="button" className="btn btn-outline-primary btn-sm" onClick={loadSlots}>
          Refresh
        </button>
        {isAdmin && (
          <button type="button" className="btn btn-primary btn-sm" onClick={openCreateForm}>
            + Create Slot
          </button>
        )}
      </div>

      {isAdmin && showForm && (
        <div className="card content-card mb-4">
          <div className="card-body">
            <h5 className="card-title mb-3">{editingId ? 'Update Slot' : 'Create New Slot'}</h5>
            <form onSubmit={handleSubmit} className="row g-3">
              <div className="col-md-4">
                <label className="form-label">Slot Number</label>
                <input
                  type="text"
                  className="form-control"
                  name="slotNumber"
                  value={form.slotNumber}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="col-md-4">
                <label className="form-label">Vehicle Type</label>
                <select
                  className="form-select"
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
              <div className="col-md-4">
                <label className="form-label">Status</label>
                <select
                  className="form-select"
                  name="status"
                  value={form.status}
                  onChange={handleChange}
                  required
                >
                  {SLOT_STATUSES.map((status) => (
                    <option key={status} value={status}>
                      {status}
                    </option>
                  ))}
                </select>
              </div>
              <div className="col-12 d-flex gap-2">
                <button type="submit" className="btn btn-primary" disabled={saving}>
                  {saving ? 'Saving...' : editingId ? 'Update' : 'Create'}
                </button>
                <button type="button" className="btn btn-outline-secondary" onClick={resetForm}>
                  Cancel
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      <div className="card content-card">
        <div className="card-body">
          {loading ? (
            <LoadingSpinner message="Loading slots..." />
          ) : slots.length === 0 ? (
            <p className="text-muted mb-0">No parking slots found.</p>
          ) : (
            <div className="table-responsive">
              <table className="table table-hover align-middle mb-0">
                <thead className="table-light">
                  <tr>
                    <th>ID</th>
                    <th>Slot Number</th>
                    <th>Vehicle Type</th>
                    <th>Status</th>
                    {isAdmin && <th className="text-end">Actions</th>}
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
                      {isAdmin && (
                        <td className="text-end">
                          <button
                            type="button"
                            className="btn btn-sm btn-outline-primary me-2"
                            onClick={() => openEditForm(slot)}
                          >
                            Edit
                          </button>
                          <button
                            type="button"
                            className="btn btn-sm btn-outline-danger"
                            onClick={() => handleDelete(slot.id, slot.slotNumber)}
                          >
                            Delete
                          </button>
                        </td>
                      )}
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
