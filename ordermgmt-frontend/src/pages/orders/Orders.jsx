import { useEffect, useState } from "react";
import { getAllOrders } from "../../services/orders";

export default function Orders() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    getAllOrders()
      .then(data => {
        setOrders(data);
        setLoading(false);
      })
      .catch(err => {
        console.error(err);
        setError("Error al cargar órdenes");
        setLoading(false);
      });
  }, []);

  if (loading) {
    return <div className="text-white">Cargando órdenes...</div>;
  }

  if (error) {
    return <div className="text-red-400">{error}</div>;
  }

  if (!orders.length) {
    return <div className="text-white">No hay órdenes registradas.</div>;
  }

  return (
    <div className="text-white">
      <h1 className="text-2xl font-bold mb-4">Órdenes</h1>

      <div className="space-y-4">
        {orders.map((order) => (
          <div
            key={order.id}
            className="p-4 bg-slate-800 rounded-lg border border-slate-700"
          >
            <p>
              <span className="font-semibold">ID:</span> {order.id}
            </p>
            <p>
              <span className="font-semibold">ID Usuario:</span>{" "}
              {order.userId}
            </p>
            <p>
              <span className="font-semibold">Productos (IDs):</span>{" "}
              {Array.isArray(order.idProducto)
                ? order.idProducto.join(", ")
                : "—"}
            </p>
            <p>
              <span className="font-semibold">Estado:</span>{" "}
              {order.estadoPedido}
            </p>
          </div>
        ))}
      </div>
    </div>
  );
}