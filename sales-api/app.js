import express from "express"
import { connectMongoDB} from "./src/config/db/mongoDbConfig.js";
import { createInitialData } from "./src/config/db/initialData.js";
import Order from "./src/modules/schemas/Order.js";
import routes from "./src/modules/routes/AppRoutes.js"
import { connectRabbitMq } from "./src/modules/rabbitmq/rabbitConfig.js";

const env = process.env;
const PORT = env.PORT || 8081
const app = express();

connectMongoDB();
await createInitialData();
connectRabbitMq();

//Define o padrão da aplicação como JSON
app.use(express.json());

app.get("/api/status", async (req, res)=>{
    
    return res.status(200).json({
        service: "Sales-API",
        status: "up",
        httpStatus: 200
    })
})

//Define as rotas
app.use(routes)

app.listen(PORT, ()=>{
    console.log(`Server up at ${PORT}`)
})