export default function PageHeader({ title, subtitle }) {
  return (
    <div className="page-header">
      <h1>{title}</h1>
      {subtitle && <p className="text-muted mb-0">{subtitle}</p>}
    </div>
  );
}
