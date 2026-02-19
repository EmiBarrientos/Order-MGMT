import { useState } from "react";
import { createProduct } from "../../services/products";

export default function CreateProductForm({ onProductCreated, onCancel }) {
    const [formData, setFormData] = useState({
        productName: "",
        stock: "",
        price: "",
    });
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prev) => ({
            ...prev,
            [name]: value 
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);
        

        try {
             const payload = {
            ...formData,
            stock: Number(formData.stock),
            price: Number(formData.price),
        };
            await createProduct(payload);
            onProductCreated();
        } catch (err) {
            setError("Error al crear el producto: " + err.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="bg-white p-6 rounded-lg shadow-lg border border-gray-200 mt-4">
            <h3 className="text-xl font-bold mb-4">Agregar Nuevo Producto</h3>
            <form onSubmit={handleSubmit} className="space-y-4">
                <div>
                    <label className="block text-sm font-medium text-gray-700 text-black bg-white">Nombre del Producto</label>
                    <input
                        type="text"
                        name="productName"
                        value={formData.productName}
                        onChange={handleChange}
                        required
                        className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2 text-black"
                    />
                </div>
                <div className="grid grid-cols-2 gap-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Stock</label>
                        <input
                            type="number"
                            name="stock"
                            value={formData.stock}
                            onChange={handleChange}
                            required
                           
                            className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2 text-black"
                        />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Precio</label>
                        <input
                            type="number"
                            name="price"
                            value={formData.price}
                            onChange={handleChange}
                            required
                            min="0"
                            step="0.01"
                            className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2 text-black"
                        />
                    </div>
                </div>
                {error && <p className="text-red-500 text-sm">{error}</p>}
                <div className="flex justify-end gap-2">
                    <button
                        type="button"
                        onClick={onCancel}
                        className="px-4 py-2 text-sm font-medium text-gray-700 bg-gray-100 hover:bg-gray-200 rounded-md text-black"
                    >
                        Cancelar
                    </button>
                    <button
                        type="submit"
                        disabled={loading}
                        className="px-4 py-2 text-sm font-medium text-white bg-green-600 hover:bg-green-700 rounded-md disabled:opacity-50"
                    >
                        {loading ? "Guardando..." : "Guardar Producto"}
                    </button>
                </div>
            </form>
        </div>
    );
}
