import { useState } from "react";
import CreateProductForm from "./CreateProductForm";

export default function ProductActions({
  visible,
  loading,
  onToggle,
  onForceReload
}) {
  const [showForm, setShowForm] = useState(false);

  const handleProductCreated = () => {
    setShowForm(false);
    onForceReload();
  };

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
          onClick={() => setShowForm(!showForm)}
          className="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded shadow"
        >
          {showForm ? "Cancelar registro" : "Nuevo Producto"}
        </button>

        <button
          onClick={onForceReload}
          className="bg-gray-200 hover:bg-gray-300 text-gray-800 px-3 py-2 rounded"
        >
          Forzar recarga
        </button>

        {loading && <span className="text-sm text-gray-300 ml-2">Cargando...</span>}
      </div>

      {showForm && (
        <CreateProductForm
          onProductCreated={handleProductCreated}
          onCancel={() => setShowForm(false)}
        />
      )}
    </div>
  );
}
