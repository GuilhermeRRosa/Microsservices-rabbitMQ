import { Router } from "express";
import UserController from "../controller/UserController.js"
import checkToken from "../../../config/auth/checkToken.js";

const router = new Router();

// Public routes
router.post("/api/user/auth", UserController.getAccessToken);

//Protected routes
//Protege as rotas a partir deste ponto
router.use(checkToken)

// Path param, ex.: '/api/user/email/gui3100@gmail.com'
router.get("/api/user/email/:email", UserController.findByEmail);

export default router;