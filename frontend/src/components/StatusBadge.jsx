const STATUS_MAP = {
  AVAILABLE: 'success',
  OCCUPIED: 'danger',
  ACTIVE: 'warning',
  COMPLETED: 'secondary',
};

export default function StatusBadge({ status }) {
  const variant = STATUS_MAP[status] || 'secondary';
  return <span className={`badge text-bg-${variant}`}>{status}</span>;
}
