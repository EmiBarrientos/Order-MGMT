import { useState } from "react";
import { getAllProducts, deleteProduct } from "../../services/products";
import Swal from 'sweetalert2';
import 'react-toastify/dist/ReactToastify.css';
import ProductActions from "../../components/products/ProductsActions";
import CreateProductForm from "../../components/products/ProductForm";




export default function Products() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [visible, setVisible] = useState(false);
  const [formMode, setFormMode] = useState(null); // null | "create" | "edit"
  const [selectedProduct, setSelectedProduct] = useState(null);



  const handleDelete = async (id) => {
     Swal.fire({
        title: '¿Estás seguro?',
        text: "¡No podrás revertir esta acción!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar'
        }).then(async (result)=>{
          if(result.isConfirmed){
             try {
              await deleteProduct(id);
                
              await fetchData(); // recargar lista
              Swal.fire(
                '¡Eliminado!',
                'El producto ha sido borrado.',
                'success'
              );
            } catch (err) {
              console.error(err);
              }

          }
        })
       
  };

  const fetchData = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await getAllProducts();
      setProducts(data);
    } catch (err) {
      console.error(err);
      setError("Error al cargar productos");
    } finally {
      setLoading(false);
    }
};



  const loadProducts = async () => {
    if (visible) {
      setVisible(false);
    } else {
      setVisible(true);
      await fetchData(); // Solo carga si se va a mostrar
    }
  };


  return (
    <div className="p-6 text-white">
      <h1 className="text-2xl font-bold mb-4">Productos</h1>
        <ProductActions
          visible={visible}
          loading={loading}
          onToggle={loadProducts}
         
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
                    <button
                        type="button"
                        onClick={() => {handleDelete(product.id) }}
                      className="mt-2.5 ml-2 bg-red-700 hover:bg-red-500 text-white px-4 py-2 rounded shadow"
                    > Eliminar
                        
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
                      loadProducts();
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
        

 onForceReload={() => {
            setVisible(false);
            loadProducts();
          }}



        */


