import { createBrowserRouter } from "react-router-dom";
import App from "../App";
import Home from "../pages/home/Home";
import Productos from "../pages/products/Products";
import Ordenes from "../pages/orders/Orders";

export const router = createBrowserRouter([
  {
    path: "/",
    element: <App />,
    children: [
      { index: true, element: <Home /> },
      { path: "products", element: <Productos /> },
      { path: "orders", element: <Ordenes /> },
    ],
  },
]);
