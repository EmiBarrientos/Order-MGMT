import { Outlet, Link } from "react-router-dom";

export default function App() {
  return (
    <div className="min-h-screen bg-slate-900 text-white">
      <nav className="p-4 flex gap-4 bg-slate-800">
        <Link to="/" className="hover:underline">Home</Link>
        <Link to="/products" className="hover:underline">Productos</Link>
        <Link to="/orders" className="hover:underline">Ordenes</Link>
      </nav>

      <div className="p-4">
        <Outlet />
      </div>
    </div>
  );
}