import { useState } from "react";
import CreateProductForm from "./ProductForm";




export default function ProductActions({
  visible,
  loading,
  onToggle,
  onForceReload
}) {
 
  const [formMode, setFormMode] = useState(null); // null | "create" | "edit"

        

 

  return (
    <div className="flex flex-col gap-4">
      <div className="flex gap-2 items-center">
        <button
          onClick={onToggle}
          className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded shadow disabled:opacity-60"
          disabled={loading}
        >
          {visible ? "Ocultar productos" : "Mostrar productos"}
        </button>

        <button
          type="button"
          onClick={() => setFormMode("create")}
          className="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded shadow"
        > Nuevo Producto
          
        </button>
         {formMode &&(
           <div className="fixed inset-0 bg-white/30 backdrop-blur-sm bg-opacity-50 flex items-center justify-center z-50"
               onClick={(e) => {
                  if (e.target === e.currentTarget) {
                    setFormMode(null);
                  }
                 }}
          > 
            <CreateProductForm
              mode={formMode}
              onProductCreated={() => {
                        setFormMode(null);              
                        loadOrders();
                      }}
            
              onCancel={() => {
                        setFormMode(null);        
                      }}
            
            />

          </div>  
         )}


      
        <button
          onClick={onForceReload}
          className="bg-gray-200 hover:bg-gray-300 text-gray-800 px-3 py-2 rounded"
        >
           Recargar
        </button>

        {loading && <span className="text-sm text-gray-300 ml-2">Cargando...</span>}
      </div>

      
    </div>
  );
}


/*


lo dejo aca porque no se usaba pero no se por ahi lo necesite despues xd

const handleProductCreated = () => {
      setFormMode(null);
    onForceReload();
  };



*/