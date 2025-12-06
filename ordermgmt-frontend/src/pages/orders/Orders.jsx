import {  useState } from "react";
import { getAllOrders } from "../../services/orders";

export default function Orders() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [visible, setVisible] = useState(false);

  const loadOrders = async () => {
     if (visible) {
      setVisible(false);
      return;
    }

    setVisible(true);
    setLoading(true);
    setError(null);
   
   
  try {
    const data = await getAllOrders();
    setOrders(data);
  } catch (err) {
    console.error(err);
    setError("Error al cargar órdenes");
  } finally {
    setLoading(false);
  }
};



  
  return (
    <div className="text-white">
      <h1 className="text-2xl font-bold mb-4">Órdenes</h1>

        <button
        onClick={loadOrders}
        className="bg-blue-600 text-white px-4 py-2 rounded shadow"
      >
         {visible ? "Ocultar órdenes" : "Mostrar órdenes"}

      </button>

      {visible && (<div className="space-y-4">
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
              <h3 className="font-semibold mt-3 mb-2 text-blue-300">
                Productos:
              </h3>
              
              <ul className="pl-4 space-y-1 list-disc list-inside list-none">
                {Array.isArray(order.productDtoList) &&
                  order.productDtoList.map((product) => (
                    <li key={`${order.id}-${product.id}`} className="text-sm">
                        <div className="font-bold">{product.productName}</div>
                        
                        <div className="ml-2">Precio: ${product.price.toFixed(2)}</div>
                    </li>
                  ))}
              </ul>
              <p>
                <span className="font-semibold">Estado:</span>{" "}
                {order.estadoPedido}
              </p>
            </div> 
        ))}
        
      </div>
      )}
    </div>
  );
}

/**********************************************
 *   
 
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



 * 
 * 
 * 
 * 
*/