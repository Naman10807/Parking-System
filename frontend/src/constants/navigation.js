export const NAV_ITEMS = [
  { to: '/', label: 'Dashboard', icon: '📊', roles: ['ADMIN'] },
  { to: '/slots', label: 'Parking Slots', icon: '🅿️', roles: ['ADMIN', 'ATTENDANT'] },
  { to: '/entry', label: 'Vehicle Entry', icon: '🚗', roles: ['ADMIN', 'ATTENDANT'] },
  { to: '/exit', label: 'Vehicle Exit', icon: '🚪', roles: ['ADMIN', 'ATTENDANT'] },
  { to: '/history', label: 'Parking History', icon: '📋', roles: ['ADMIN', 'ATTENDANT'] },
];

export const VEHICLE_TYPES = ['CAR', 'BIKE', 'TRUCK'];
export const SLOT_STATUSES = ['AVAILABLE', 'OCCUPIED'];
export const PAGE_SIZE = 10;
