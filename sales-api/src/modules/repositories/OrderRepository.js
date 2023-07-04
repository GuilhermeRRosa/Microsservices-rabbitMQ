import Order from "../schemas/Order.js";

class OrderRepository{

    async findAll(){
        try {
            return await Order.find();
        } catch (error) {
            console.error(error);
            return null;
        }
    }

}

export default new OrderRepository();