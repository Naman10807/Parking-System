import { useState } from 'react';
import { NavLink, Outlet, useNavigate } from 'react-router-dom';
import { NAV_ITEMS } from '../constants/navigation';
import { useAuth } from '../context/AuthContext';

export default function Layout() {
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const { username, role, logout, isAdmin } = useAuth();
  const navigate = useNavigate();

  const visibleNavItems = NAV_ITEMS.filter((item) => item.roles.includes(role));

  const handleLogout = () => {
    logout();
    navigate('/login', { replace: true });
  };

  return (
    <div className="app-layout">
      <button
        type="button"
        className="btn btn-dark mobile-toggle"
        onClick={() => setSidebarOpen((open) => !open)}
        aria-label="Toggle menu"
      >
        ☰ Menu
      </button>

      {sidebarOpen && (
        <button
          type="button"
          className="sidebar-backdrop"
          aria-label="Close menu"
          onClick={() => setSidebarOpen(false)}
        />
      )}

      <aside className={`sidebar ${sidebarOpen ? 'open' : ''}`}>
        <div className="sidebar-brand">
          <span>Smart Parking</span>
        </div>

        <div className="sidebar-user px-3 py-3">
          <div className="small text-white-50">Signed in as</div>
          <div className="fw-semibold text-white">{username}</div>
          <span className={`badge mt-2 ${isAdmin ? 'text-bg-primary' : 'text-bg-info'}`}>
            {role}
          </span>
        </div>

        <nav className="sidebar-nav">
          {visibleNavItems.map((item) => (
            <NavLink
              key={item.to}
              to={item.to}
              end={item.to === '/'}
              className={({ isActive }) => `sidebar-link ${isActive ? 'active' : ''}`}
              onClick={() => setSidebarOpen(false)}
            >
              <span className="nav-icon">{item.icon}</span>
              <span>{item.label}</span>
            </NavLink>
          ))}
        </nav>

        <div className="sidebar-footer p-3">
          <button type="button" className="btn btn-outline-light w-100" onClick={handleLogout}>
            Logout
          </button>
        </div>
      </aside>

      <main className="main-content">
        <Outlet />
      </main>
    </div>
  );
}
