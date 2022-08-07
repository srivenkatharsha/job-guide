import mongoose from "mongoose"
import bcrypt from "bcryptjs"

const details = new mongoose.Schema({
    email : {
        type: String,
        required : true
    },
    name : {
        type : String,
        required : false,
        default : "Anonymous"
    },
    password : {
        type: String,
        required : true
    },
    rolePreference : {
        type: String,
        required : false,
        default : null
    },
    locationPreference: {
        type: String,
        required : false,
        default : null
        
    },
    typePreference : {
        type: String,
        required : false,
        default : null
    },
    salaryPreference: {
        type : String,
        required : false,
        default : null
    }
});

// hashing the password
details.pre("save",async function(next) {
    if(this.isModified('password')){
        this.password = await bcrypt.hash(this.password,15)
    }
    next()
})

const Profile = mongoose.model("profiles",details)

export default Profile