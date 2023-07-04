import OrderServices from "../services/OrderServices.js";

class OrderController{
    
    async findAll(req, res){
        let orders = await OrderServices.findAll(req);
        return res.status(orders.status).json(orders);
    }

}

export default new OrderController();