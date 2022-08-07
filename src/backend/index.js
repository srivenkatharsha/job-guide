import mongoose from "mongoose"
import express from "express"
import dotenv from "dotenv"
import cors from "cors"
import route from "./routes/operations.route.js"


dotenv.config()

const application = express()

application.use(cors())
application.use(route)

mongoose.connect(process.env.DB_URL,() => {
    console.log("CONNECTED TO THE DATABASE.")
})

application.listen(process.env.PORT_NUMBER,() => {
    console.log("SERVER IS ONLINE.")
})