import { useState, useEffect } from "react";
import { createProduct, updateProduct } from "../../services/products";
import {  toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';


export default function CreateProductForm({
    mode, 
    product, 
    onProductCreated,
    onCancel 
    }) {

    const [formData, setFormData] = useState({
        productName: "",
        stock: "",
        price: "",
    }); 
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
   


    useEffect(() => {
        if (mode === "edit" && product) {
            setFormData(product);
        }
    }, [mode, product]);


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
    
            if(mode ==="create"){
                await createProduct(payload);   
                toast.success("Producto creado correctamente");
            }else{
                await updateProduct(product.id, payload);
                toast.success("Producto editado correctamente");
             }

       
            onProductCreated();
          
        } catch (err) {
            if(mode === "edit"){
                setError("Error al editar el producto: " + err.message);    
            }else{
                 setError("Error al agregar el producto: " + err.message);
            }
           
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="bg-white p-6 rounded-lg shadow-lg border border-gray-200 mt-4">
            <h3 className="text-xl font-bold mb-4 text-black "> {mode=== "create" ? "Agregar Nuevo Producto"  : "Editar producto"}</h3>
            <form onSubmit={handleSubmit} className="space-y-4">
                
                <div>
                    <label className="block text-sm font-medium text-black bg-white">Nombre del Producto</label>
                    <input
                        type="text"
                        name="productName"
                        value={formData.productName}
                        onChange={handleChange}
                        required
                        className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2 bg-white text-black"
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
                        className="px-4 py-2 text-sm font-medium  bg-gray-100 hover:bg-gray-200 rounded-md text-black"
                    >
                        Cancelar
                    </button>
                    <button
                        type="submit"
                        disabled={loading}
                        className="px-4 py-2 text-sm font-medium text-white bg-green-600 hover:bg-green-700 rounded-md disabled:opacity-50"
                    >
                        {loading ? "Guardando..." : mode === "edit" ? "Guardar cambios" : "Guardar Producto"}
                    </button>
                </div>
            </form>
        </div>
    );
}
