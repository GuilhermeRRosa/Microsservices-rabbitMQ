import jwt from "jsonwebtoken";
import {promisify} from "util"
import * as secrets from "../constants/secret.js"
import * as httpStatus from "../constants/httpStatus.js"
import AuthException from "./AuthException.js";

/*
    Middleware de verificação de existência do Header Authorization
    e validação do Bearer Token JWT
*/

const bearer = "bearer ";

export default async (req, res, next) => {
    try {
        const {authorization} = req.headers;
        if(!authorization){
            throw new AuthException(
                httpStatus.UNAUTHORIZED,
                'Missing authorization'
            )
        }        let accessToken = authorization;
        if(authorization.includes(" ") && authorization.toLowerCase().includes(bearer)){
            accessToken = accessToken.split(" ")[1];
        }
        const decoded = await promisify(jwt.verify)(
            accessToken,
            secrets.API_SECRET
        )
        req.authUser = decoded.authUser;
        return next();
    } catch (error) {
        let status = error.status ? error.status : httpStatus.INTERNAL_SERVER_ERROR
        return res.status(status).json({
            status: status,
            message: error.message
        })
    }  

    
}


