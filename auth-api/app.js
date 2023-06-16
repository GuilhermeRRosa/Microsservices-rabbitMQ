import express from "express";
import * as db from "./src/config/initialData.js"
import UserRoutes from "./src/modules/user/routes/UserRoutes.js"

const env = process.env;
const PORT = env.PORT || 8080

db.createInitialData();

console.log("PORT: "+env)
const app = express();

//Define o padrão da aplicação como JSON
app.use(express.json());

//Status da aplicação
app.get('/api/status', (req, res)=> {
    return res.status(200).json({
        service: "Auth-API",
        status: "up",
        httpStatus: 200
    })
})

//Define as rotas para a aplicação
app.use(UserRoutes);

//Define o filtro checkToken para as requisições após este ponto
//app.use(checkToken);

app.listen(PORT, ()=> {
    console.log(`server up at ${PORT}`)
})