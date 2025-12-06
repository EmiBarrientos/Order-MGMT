import {  useState } from "react";
import { getAllProducts } from "../../services/products";


export default function Products() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
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
     const data = await getAllProducts();
     setProducts(data);
   } catch (err) {
     console.error(err);
     setError("Error al cargar órdenes");
   } finally {
     setLoading(false);
   }
 };


  return (
    <div className="p-6 text-white">
      <h1 className="text-2xl font-bold">Productos</h1>

       <button
        onClick={loadOrders}
        className="bg-blue-600 text-white px-4 py-2 rounded shadow"
      >
         {visible ? "Ocultar productos" : "Mostrar productos"}

      </button>
      
      {visible && (<div className="space-y-4">
        {products.map((product) => (
            <div
              key={product.id}
              className="p-4 bg-slate-800 rounded-lg border border-slate-700"
            >
              <p>
                <span className="font-semibold">ID:</span> {product.id}
              </p>
              <p>
                <span className="font-semibold">nombre del producto:</span>{" "}
                {product.productName}
              </p>
              <p>
                <span className="font-semibold">Stock:</span>{" "}
                {product.stock}
              </p>
              <p>
                <span className="font-semibold">precio:</span>{" "}
                {product.price.toFixed(2)}
              </p>
            </div>
          ))}
      </div>
      )}
    </div>
  );
}

/**
 * 
 * 
 * private int id;
    private String productName;
    private Double stock;
    private Double price;

 {/*  
        
        <ul className="pl-4 space-y-1 list-disc list-inside list-none">
          {Array.isArray(order.productDtoList) &&
            order.productDtoList.map((product) => (
              <li key={`${order.id}-${product.id}`} className="text-sm">
                  <div className="font-bold">{product.productName}</div>
                  <div className="ml-2 text-gray-400">ID: {product.id}</div> 
                  <div className="ml-2">Precio: ${product.price.toFixed(2)}</div>
              </li>
            ))}
        </ul>
        
        
        
        
        */
       

  