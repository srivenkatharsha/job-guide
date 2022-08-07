// operations.route.js is subjective to changes

import express from "express"
import Profile from "../models/Profile.js"
import bcrypt from "bcryptjs"
import child_process from 'child_process'

const route = express.Router()
route.use(express.json())

//validating user info
route.post("/validate", async (req,res) => {
    const profile = req.body
    const checkIfEmailExists = await Profile.findOne({"email" : profile.email})
    if(!checkIfEmailExists){
        res.json({"message" : "Create a new Account!"})
        return
    }
    //verifying the password (with password from db)
    const passwordCheck = await bcrypt.compare(profile.password, checkIfEmailExists.password); 
    if(passwordCheck){
        res.json({"message":"Logged In."})
    }
    else{
        res.json({"message" : "Wrong email or password. Try again!"})
    }
})

// Creating account
route.post("/createAccount", async(req,res) => {
    const profile = req.body
    const newProfile = new Profile(profile)
    const checkIfExists = await Profile.findOne({"email" : newProfile.email})
     if(checkIfExists){
         res.json({"message" : "Account already exists."})
     }
    else if(!checkIfExists){
        await newProfile.save()
        res.json({"message" : "New Account Created."})
     }
    
})

// Getting current preferences of a particular user
route.post("/getPreferences", async(req,res) => {
    const profile = req.body
    const details = await Profile.findOne(profile)
    const preferencesJSON = {
        "name" : details.name,
        "email" : details.email,
        "rolePreference" : details.rolePreference,
        "locationPreference" : details.locationPreference,
        "typePreference" : details.typePreference,
        "salaryPreference" : details.salaryPreference
    }
    res.json(preferencesJSON)
})


// updating preferences
route.put("/updatePreferences", async(req,res) => {
    const info = req.body
    const filter = info.filter // filter represents current preferences
    const update = info.update // update represents new (desired) preferences
    const updatedPreferences = await Profile.findOneAndUpdate(filter,update,{new : true})
    if(!updatedPreferences){
        res.json({"message" : "Something went wrong in updating try again!"})
    }
    else{
        res.json({
            "rolePreference" : updatedPreferences.rolePreference,
            "locationPreference" : updatedPreferences.locationPreference,
            "typePreference" : updatedPreferences.typePreference,
            "salaryPreference" : updatedPreferences.salaryPreference
        })
        child_process.child = child_process.exec(`java -jar ..\\..\\joboffers-automation.jar "${updatedPreferences.rolePreference}" "${updatedPreferences.locationPreference}" "${updatedPreferences.typePreference}" "${updatedPreferences.salaryPreference}" "${updatedPreferences.name}" "${updatedPreferences.email}"`,
            (err,stdout,stderr)=>{
                console.log("stdout : " + stdout)
                if(stderr) console.log("stderr: "+ stderr)
                if(err) console.log("err: " + err)
            }
        )
    }
})



export default route
