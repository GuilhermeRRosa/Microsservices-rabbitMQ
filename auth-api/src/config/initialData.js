import bcrypt from "bcrypt";
import User from "../modules/user/model/User.js";

export async function createInitialData(){
    try {
        await User.sync({force: true})
        let password = await bcrypt.hash("123456", 10)

        await User.create({
            name: "User Test 1",
            email: "emailteste1@teste.com",
            password: password
        });

        await User.create({
            name: "User Test 2",
            email: "emailteste2@teste.com",
            password: password
        })
    } catch (error) {
        console.error(error)
    }
}