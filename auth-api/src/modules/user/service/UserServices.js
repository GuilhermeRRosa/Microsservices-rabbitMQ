import UserRepository from "../repository/UserRepository.js";
import * as httpStatus from "../../../config/constants/httpStatus.js";
import UserException from "../exception/UserException.js";
import bcrypt from "bcrypt";
import jwt from "jsonwebtoken";
import * as secret from "../../../config/constants/secret.js"

/*
    Classe que recebe a request e faz a validação
*/
class UserService{

    async findByEmail(req){
        try {
            const {email} = req.params;
            const { authUser } = req;
            await this.validateAuthenticatedEmail(email, authUser);
            await this.validateData(email);
            let user = await UserRepository.findByEmail(email);
            await this.validateUserNotFound(user);
            return {
                status: httpStatus.SUCCESS,
                user: {
                    id: user.id,
                    name: user.name,
                    email: user.email
                }
            }
        } catch (error) {
            return {
                status: error.status ? error.status : httpStatus.INTERNAL_SERVER_ERROR,
                message: error.message
            }
        }
    }

    async getAccessToken(req){
        try {
            let {email, password} = req.body;
            await this.validateAccessTokenData(email, password);
            let user = await UserRepository.findByEmail(email);
            await this.validateUserNotFound(user);
            await this.validatePassword(password.toString(), user.password.toString());
            const authUser = {
                id: user.id,
                name: user.name,
                email: user.email
            }
            const accessToken = jwt.sign({authUser}, secret.API_SECRET, {
                expiresIn: '1d'
            })

            return {
                status: httpStatus.SUCCESS,
                accessToken
            }
        } catch (error) {
            return {
                status: error.status ? error.status : httpStatus.INTERNAL_SERVER_ERROR,
                message: error.message
            }
        }
    }

    // Validations

    async validateAuthenticatedEmail(email, authUser){
        if(!authUser || email !== authUser.email){
            throw new UserException(
                httpStatus.FORBIDDEN,
                "You dont have permission for this"
            )
        }
    }

    async validateAuthenticatedUser(user, authUser){
        if(!authUser || user.id !== authUser.id){
            throw new UserException(
                httpStatus.FORBIDDEN,
                "You dont have permission for this"
            )
        }
    }

    async validatePassword(password, hashPassword){
        if(!bcrypt.compare(password, hashPassword)){
            throw new UserException(
                httpStatus.UNAUTHORIZED,
                'Wrong Password'
            )
        }
    }

    async validateAccessTokenData(email, password){
        if(!email || !password) {
            throw new UserException(
                httpStatus.UNAUTHORIZED,
                'Email and Password must be provided'
            );
        }
    }

    async validateData(data){
        if(!data){
            throw new UserException(
                httpStatus.BAD_REQUEST,
                'data is null, no parameters available'
            )
        }
    }

    async validateUserNotFound(user){
        if(!user){
            throw new UserException(
                httpStatus.NOT_FOUND,
                'User not found'
            )
        }
    }

}

export default new UserService();