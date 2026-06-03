/**
 * Lets axios interceptors trigger toasts without importing React context.
 */
let handlers = null;

export function registerToastHandlers(next) {
  handlers = next;
}

export function unregisterToastHandlers() {
  handlers = null;
}

export function notifyError(message) {
  handlers?.showError?.(message);
}

export function notifySuccess(message) {
  handlers?.showSuccess?.(message);
}
