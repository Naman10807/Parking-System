import { createContext, useCallback, useContext, useEffect, useMemo, useState } from 'react';
import ToastContainer from '../components/ToastContainer';
import { registerToastHandlers, unregisterToastHandlers } from '../utils/toastBridge';

const ToastContext = createContext(null);

const AUTO_DISMISS_MS = 5000;

export function ToastProvider({ children }) {
  const [toasts, setToasts] = useState([]);

  const removeToast = useCallback((id) => {
    setToasts((prev) => prev.filter((t) => t.id !== id));
  }, []);

  const addToast = useCallback(
    (message, type = 'info') => {
      if (!message) return;
      const id = `${Date.now()}-${Math.random().toString(36).slice(2, 9)}`;
      setToasts((prev) => [...prev, { id, message, type }]);
      window.setTimeout(() => removeToast(id), AUTO_DISMISS_MS);
    },
    [removeToast]
  );

  const showError = useCallback((message) => addToast(message, 'danger'), [addToast]);
  const showSuccess = useCallback((message) => addToast(message, 'success'), [addToast]);
  const showWarning = useCallback((message) => addToast(message, 'warning'), [addToast]);
  const showInfo = useCallback((message) => addToast(message, 'info'), [addToast]);

  const value = useMemo(
    () => ({ showError, showSuccess, showWarning, showInfo }),
    [showError, showSuccess, showWarning, showInfo]
  );

  useEffect(() => {
    registerToastHandlers({ showError, showSuccess });
    return unregisterToastHandlers;
  }, [showError, showSuccess]);

  return (
    <ToastContext.Provider value={value}>
      {children}
      <ToastContainer toasts={toasts} onDismiss={removeToast} />
    </ToastContext.Provider>
  );
}

export function useToast() {
  const context = useContext(ToastContext);
  if (!context) {
    throw new Error('useToast must be used within ToastProvider');
  }
  return context;
}
