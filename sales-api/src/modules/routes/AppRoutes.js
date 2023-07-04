import { Router } from "express";
import OrderController from "../controllers/OrderController.js";
import checkToken from "../../config/auth/checkToken.js";

const router = new Router();

//Protected Routes bellow
router.use(checkToken);

router.get("/order/", OrderController.findAll);

export default router;