import { Navigate, Route, Routes } from 'react-router-dom';
import Layout from './components/Layout';
import Dashboard from './pages/Dashboard';
import ParkingSlots from './pages/ParkingSlots';
import VehicleEntry from './pages/VehicleEntry';
import VehicleExit from './pages/VehicleExit';
import ParkingHistory from './pages/ParkingHistory';

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<Layout />}>
        <Route index element={<Dashboard />} />
        <Route path="slots" element={<ParkingSlots />} />
        <Route path="entry" element={<VehicleEntry />} />
        <Route path="exit" element={<VehicleExit />} />
        <Route path="history" element={<ParkingHistory />} />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Route>
    </Routes>
  );
}
