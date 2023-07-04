import AuthException from "../../modules/exceptions/AuthException.js";
import jwt from "jsonwebtoken";
import {promisify} from "util";
import * as httpStatus from "../consts/httpStatus.js";
import { API_SECRET } from "../secrets/secrets.js";

const bearer = "bearer ";

export default async(req, res, next) => {
    try {
        // Recupera o Header Authorization
        const {authorization} = req.headers;

        // Verifica se o Header existe
        if(!authorization){
            throw new AuthException(
                httpStatus.UNAUTHORIZED,
                "Missing Authorization"
            );
        }

        // Captura apenas o token
        let accessToken = authorization;
        if (accessToken.includes(" ") && authorization.toLowerCase().includes(bearer)){
            accessToken = accessToken.split(" ")[1];
        }

        // Tenta decodificar o token com a chave secreta da aplicação
        const decoded = await promisify(jwt.verify)(
            accessToken,
            API_SECRET
        );

        // Setta o usuário como autenticado e chama a função next para continuar a requisição;
        req.authUser = decoded.authUser;
        return next();
        
    } catch (error) {
        let status = error.status? error.status : httpStatus.INTERNAL_SERVER_ERROR
        return res.status(status).json({
            status: status,
            message: error.message
        })
    }
}