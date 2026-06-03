import { useState } from 'react';
import { NavLink, Outlet } from 'react-router-dom';

const NAV_ITEMS = [
  { to: '/', label: 'Dashboard', icon: '📊' },
  { to: '/slots', label: 'Parking Slots', icon: '🅿️' },
  { to: '/entry', label: 'Vehicle Entry', icon: '🚗' },
  { to: '/exit', label: 'Vehicle Exit', icon: '🚪' },
  { to: '/history', label: 'Parking History', icon: '📋' },
];

export default function Layout() {
  const [sidebarOpen, setSidebarOpen] = useState(false);

  return (
    <div className="app-layout">
      <button
        type="button"
        className="btn btn-dark mobile-toggle"
        onClick={() => setSidebarOpen((open) => !open)}
      >
        ☰ Menu
      </button>

      <aside className={`sidebar ${sidebarOpen ? 'open' : ''}`}>
        <div className="sidebar-brand">Smart Parking</div>
        <nav className="sidebar-nav">
          {NAV_ITEMS.map((item) => (
            <NavLink
              key={item.to}
              to={item.to}
              end={item.to === '/'}
              className={({ isActive }) => `sidebar-link ${isActive ? 'active' : ''}`}
              onClick={() => setSidebarOpen(false)}
            >
              <span>{item.icon}</span>
              <span>{item.label}</span>
            </NavLink>
          ))}
        </nav>
      </aside>

      <main className="main-content">
        <Outlet />
      </main>
    </div>
  );
}
