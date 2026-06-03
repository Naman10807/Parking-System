export default function StatCard({ label, value, variant = 'primary', icon }) {
  return (
    <div className="card stat-card h-100">
      <div className="card-body">
        <div className="d-flex justify-content-between align-items-start">
          <div>
            <div className="stat-label">{label}</div>
            <div className={`stat-value text-${variant}`}>{value}</div>
          </div>
          {icon && <span className="fs-2 opacity-50">{icon}</span>}
        </div>
      </div>
    </div>
  );
}
