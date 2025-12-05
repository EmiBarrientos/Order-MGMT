import { createBrowserRouter } from "react-router-dom";
import App from "../App";
import Home from "../pages/home/Home";
import Products from "../pages/Products/Products";
import Orders from "../pages/Orders/Orders";

export const router = createBrowserRouter([
  {
    path: "/",
    element: <App />,
    children: [
      { index: true, element: <Home /> },
      { path: "products", element: <Products /> },
      { path: "orders", element: <Orders /> },
    ],
  },
]);
