import { Navigate, Route, Routes } from 'react-router-dom';
import Layout from './components/Layout';
import ProtectedRoute from './components/ProtectedRoute';
import RoleRoute from './components/RoleRoute';
import Dashboard from './pages/Dashboard';
import Login from './pages/Login';
import ParkingHistory from './pages/ParkingHistory';
import ParkingSlots from './pages/ParkingSlots';
import VehicleEntry from './pages/VehicleEntry';
import VehicleExit from './pages/VehicleExit';

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />

      <Route element={<ProtectedRoute />}>
        <Route element={<Layout />}>
          <Route
            index
            element={
              <RoleRoute roles={['ADMIN']}>
                <Dashboard />
              </RoleRoute>
            }
          />
          <Route path="slots" element={<ParkingSlots />} />
          <Route path="entry" element={<VehicleEntry />} />
          <Route path="exit" element={<VehicleExit />} />
          <Route path="history" element={<ParkingHistory />} />
        </Route>
      </Route>

      <Route path="*" element={<Navigate to="/login" replace />} />
    </Routes>
  );
}
