import express from "express"

const env = process.env;
const PORT = env.PORT || 8080
const app = express();

app.get("/api/status", (req, res)=>{
    return res.status(200).json({
        service: "Auth-API",
        status: "up",
        httpStatus: 200
    })
})

app.listen(PORT, ()=>{
    console.log(`Server up at ${PORT}`)
})