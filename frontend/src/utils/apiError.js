const STATUS_MESSAGES = {
  400: 'Invalid request. Please check your input.',
  401: 'Authentication required. Please sign in again.',
  403: 'You do not have permission to perform this action.',
  404: 'The requested resource was not found.',
  409: 'This action conflicts with existing data.',
  422: 'Validation failed. Please correct the highlighted fields.',
  500: 'Server error. Please try again later.',
  503: 'Service temporarily unavailable. Please try again.',
};

function formatFieldName(field) {
  if (!field) return 'Field';
  const name = field.includes('.') ? field.split('.').pop() : field;
  return name.replace(/([A-Z])/g, ' $1').replace(/^./, (c) => c.toUpperCase()).trim();
}

/**
 * Maps Axios / API ErrorResponse payloads to a user-friendly message.
 */
export function getErrorMessage(error) {
  if (!error) {
    return 'Something went wrong. Please try again.';
  }

  if (!error.response) {
    if (error.code === 'ECONNABORTED') {
      return 'Request timed out. Please try again.';
    }
    return 'Unable to reach the server. Check your connection and try again.';
  }

  const { status, data } = error.response;

  if (data?.fieldErrors?.length) {
    return data.fieldErrors
      .map((e) => `${formatFieldName(e.field)}: ${e.message}`)
      .join(' ');
  }

  if (data?.message) {
    return data.message;
  }

  return STATUS_MESSAGES[status] || `Request failed (${status}). Please try again.`;
}

/**
 * Whether the axios interceptor should show a global toast for this error.
 */
export function shouldNotifyGlobally(error) {
  const status = error?.response?.status;
  if (!status) return true;
  return status === 403 || status >= 500;
}

/** Show a toast for API errors unless the axios interceptor already did. */
export function notifyApiError(error, showError) {
  if (!shouldNotifyGlobally(error)) {
    showError(getErrorMessage(error));
  }
}
