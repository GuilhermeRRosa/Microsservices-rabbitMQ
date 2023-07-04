import OrderRepository from "../repositories/OrderRepository.js";
import * as httpStatus from "../../config/consts/httpStatus.js";

class UserService{
    async findAll(req){
        try{
            let orders = await OrderRepository.findAll();
            return {
                status: httpStatus.SUCCESS,
                orders: orders
            }
        } catch (err) {
            return {
                status: err.status? err.status : httpStatus.INTERNAL_SERVER_ERROR,
                message: err.message
            }
        }
    }
}

export default new UserService();