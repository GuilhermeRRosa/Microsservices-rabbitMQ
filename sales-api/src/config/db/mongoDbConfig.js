import mongoose from "mongoose";
import { MONGO_DB_URL } from "../secrets/secrets.js";

export function connectMongoDB() {
    mongoose.connect(MONGO_DB_URL, {
        useNewUrlParser: true
    })

    mongoose.connection.on('connected', function(){
        console.info("The application connected to MongoDB successfully");
    });
    mongoose.connection.on('error', function(){
        console.error("The application dont have connected to MongoDB successfully");
    });
}
