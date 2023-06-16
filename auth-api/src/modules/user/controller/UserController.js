import UserServices from "../service/UserServices.js"

/*
    Classe que recebe a requisição do router,
    manda a request para o service,
    recebe e retorna a response da classe de serviço
*/
class UserController{

    async findByEmail(req, res){
        let user = await UserServices.findByEmail(req);
        return res.status(user.status).json(user);
    }

    async getAccessToken(req, res){
        let accessToken = await UserServices.getAccessToken(req);
        return res.status(accessToken.status).json(accessToken);
    }

}

export default new UserController();