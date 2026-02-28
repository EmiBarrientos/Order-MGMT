import { useState } from "react";
import { getAllProducts } from "../../services/products";
import ProductActions from "../../components/products/ProductsActions";
import CreateProductForm from "../../components/products/ProductForm";



export default function Products() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [visible, setVisible] = useState(false);
  const [formMode, setFormMode] = useState(null); // null | "create" | "edit"
  const [selectedProduct, setSelectedProduct] = useState(null);


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
      <h1 className="text-2xl font-bold mb-4">Productos</h1>
        <ProductActions
          visible={visible}
          loading={loading}
          onToggle={loadOrders}
          onForceReload={() => {
            setVisible(false);
            loadOrders();
          }}
          formMode={formMode}
          setFormMode={setFormMode}
        />

        {visible && (
          <div className="space-y-4 mt-4">
            {products.map((product) => (
              <div
                key={product.id}
                className="p-4 bg-slate-800 rounded-lg border border-slate-700"
              >
                <p>
                  <span className="font-semibold">Nombre del producto:</span>{" "}
                  {product.productName}
                </p>
                <p>
                  <span className="font-semibold">Stock:</span>{" "}
                  {product.stock}
                </p>
                <p>
                  <span className="font-semibold">Precio:</span>{" "}
                  {product.price.toFixed(2)}
                </p>
                  <button
                        type="button"
                        onClick={() => {
                              setSelectedProduct(product);
                              setFormMode("edit");
                            }}
                      className="mt-2.5 bg-blue-400 hover:bg-blue-300 text-white px-4 py-2 rounded shadow"
                    > Editar
                        
                    </button>
              </div>
          ))}  
        </div>
        
      )}
      
       {formMode && (
          <div className="fixed inset-0 bg-white/30 backdrop-blur-sm bg-opacity-50 flex items-center justify-center z-50"
               onClick={(e) => {
                  if (e.target === e.currentTarget) {
                    setFormMode(null);
                    setSelectedProduct(null);
                  }
                 }}
          > 
            <div className="bg-white rounded-lg p-6 w-[500px] relative">
              <button
                onClick={() => {
                  setFormMode(null);
                  setSelectedProduct(null);
                 }}
                 className="absolute top-1 right-3 text-black"
                  >
                    ✕
              </button>
              <div
                    className=" bg-slate-600 jus rounded-lg p-6 relative"
                    onClick={(e) => e.stopPropagation()}
                >
                            
                <CreateProductForm
                    mode={formMode}
                    product={selectedProduct}
                    onProductCreated={() => {
                      setFormMode(null);
                      setSelectedProduct(null);
                      loadOrders();
                    }}
                    onCancel={() => {
                      setFormMode(null);
                      setSelectedProduct(null);
                    }}
                  />
              </div>  
            </div>
          </div>  
        )}


    </div>
  );
}



{/*/openFormId === posicion.id ? "Cerrar form" : "Abrir form"*/} 
/**
 * 
 * 
 * className="bg-yellow-600 hover:bg-yellow-700 text-white px-4 py-2 rounded shadow"
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
        
        

        <div
                  className="bg-blue-500 rounded-lg p-6 w-[500px] relative"
                  onClick={(e) => e.stopPropagation()}
                >
        </div>
        
        */


