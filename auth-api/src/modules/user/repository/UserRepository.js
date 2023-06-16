import User from "../model/User.js"

/*
    Classe que encapsula o acesso ao banco de dados
*/ 
class UserRepository{

    async findById(id){
        try {
            return User.findByPk(id);
        } catch (error) {
            console.error(error.message);
            return null;
        }
    }

    async findByEmail(email){
        try {
            return await User.findOne({ where: { email } })
        } catch (error) {
            console.error(error.message);
            return null;
        }
    }

}

export default new UserRepository();