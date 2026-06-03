export default function LoadingSpinner({ message = 'Loading...', fullPage = false }) {
  return (
    <div className={`text-center py-${fullPage ? '5' : '4'}`}>
      <div className="spinner-border text-primary" role="status">
        <span className="visually-hidden">Loading</span>
      </div>
      {message && <p className="mt-3 text-muted mb-0">{message}</p>}
    </div>
  );
}
