export function getErrorMessage(error) {
  const data = error?.response?.data;
  if (data?.message) {
    return data.message;
  }
  if (data?.fieldErrors?.length) {
    return data.fieldErrors.map((e) => e.message).join(', ');
  }
  return error?.message || 'Something went wrong. Please try again.';
}
