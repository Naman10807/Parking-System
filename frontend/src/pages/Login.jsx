import { useState } from 'react';
import { Navigate, useLocation, useNavigate } from 'react-router-dom';
import LoadingSpinner from '../components/LoadingSpinner';
import { useAuth } from '../context/AuthContext';
import { useToast } from '../context/ToastContext';
import { notifyApiError } from '../utils/apiError';

export default function Login() {
  const { login, isAuthenticated, role } = useAuth();
  const { showError } = useToast();
  const navigate = useNavigate();
  const location = useLocation();
  const [form, setForm] = useState({ username: '', password: '' });
  const [loading, setLoading] = useState(false);

  if (isAuthenticated) {
    const redirectTo = role === 'ADMIN' ? '/' : '/entry';
    return <Navigate to={location.state?.from?.pathname || redirectTo} replace />;
  }

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      const data = await login({
        username: form.username.trim(),
        password: form.password,
      });
      const defaultPath = data.role === 'ADMIN' ? '/' : '/entry';
      const from = location.state?.from?.pathname || defaultPath;
      navigate(from, { replace: true });
    } catch (err) {
      notifyApiError(err, showError);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-page min-vh-100 d-flex align-items-center justify-content-center p-3">
      <div className="card login-card shadow-lg border-0" style={{ maxWidth: 420, width: '100%' }}>
        <div className="card-body p-4 p-md-5">
          <div className="text-center mb-4">
            <div className="login-logo mb-3">🅿️</div>
            <h2 className="fw-bold mb-1">Smart Parking</h2>
            <p className="text-muted mb-0">Sign in to manage your parking facility</p>
          </div>

          {loading ? (
            <LoadingSpinner message="Signing in..." />
          ) : (
            <form onSubmit={handleSubmit}>
              <div className="mb-3">
                <label htmlFor="username" className="form-label">
                  Username
                </label>
                <input
                  type="text"
                  className="form-control form-control-lg"
                  id="username"
                  name="username"
                  value={form.username}
                  onChange={handleChange}
                  placeholder="Enter username"
                  required
                  autoComplete="username"
                />
              </div>
              <div className="mb-4">
                <label htmlFor="password" className="form-label">
                  Password
                </label>
                <input
                  type="password"
                  className="form-control form-control-lg"
                  id="password"
                  name="password"
                  value={form.password}
                  onChange={handleChange}
                  placeholder="Enter password"
                  required
                  autoComplete="current-password"
                />
              </div>
              <button type="submit" className="btn btn-primary btn-lg w-100">
                Sign In
              </button>
            </form>
          )}

          <div className="mt-4 p-3 bg-light rounded small text-muted">
            <strong>Demo accounts:</strong>
            <div className="mt-1">Admin — admin / admin123</div>
            <div>Attendant — attendant / attendant123</div>
          </div>
        </div>
      </div>
    </div>
  );
}
