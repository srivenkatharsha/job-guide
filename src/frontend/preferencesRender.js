async function getCurrentPreferences(email){
    try{
        let ires = await fetch("http://localhost:4002/getPreferences",{
        method: "POST",
        headers: {
            Accept: 'application/json',
            'Content-Type': 'application/json'
        },
        body : JSON.stringify({
          "email" : email
        })
      })
        let sres = await ires.json()
        let username = sres.name
        let currentRole = sres.rolePreference
        let currentLocation = sres.locationPreference
        let currentType = sres.typePreference
        let currentSalary = sres.salaryPreference
        document.getElementById("username").textContent = username
        document.getElementById("email").textContent = localStorage.getItem("email")
        if(!currentRole || !currentLocation || !currentType || !currentSalary){
            currentRole = null
            currentLocation = null 
            currentSalary = null 
            currentType = null
        }
        else if(currentRole.includes("intern")){
            currentRole = currentRole.replace("intern","")
        }
        else{
            
        document.getElementById("role").textContent = currentRole
        document.getElementById("location").textContent = currentLocation
        document.getElementById("type").textContent = currentType
        document.getElementById("salary").textContent = currentSalary + " LPA."
        }

    }
    catch(err){
        console.log(err)
    }
}

getCurrentPreferences(localStorage.getItem("email"))