const TYPE_ICONS = {
  success: '✓',
  danger: '✕',
  warning: '!',
  info: 'i',
};

export default function ToastContainer({ toasts, onDismiss }) {
  if (!toasts.length) return null;

  return (
    <div className="toast-stack" aria-live="polite" aria-atomic="false">
      {toasts.map((toast) => (
        <div
          key={toast.id}
          className={`toast-item toast-item--${toast.type}`}
          role="alert"
        >
          <span className="toast-item__icon" aria-hidden="true">
            {TYPE_ICONS[toast.type] || TYPE_ICONS.info}
          </span>
          <p className="toast-item__message">{toast.message}</p>
          <button
            type="button"
            className="toast-item__close"
            aria-label="Dismiss notification"
            onClick={() => onDismiss(toast.id)}
          >
            ×
          </button>
        </div>
      ))}
    </div>
  );
}
