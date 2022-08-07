let signinData = {
    email: "",
    password: ""
  }
  let signupData = {
    name: "",
    email: "",
    password: ""
  }
  let storedData = {
    name: "",
    email: ""
  }
  async function validate(email,password){
    try{
      let ires = await fetch("http://localhost:4002/validate", {
        method: "POST",
        headers: {
            Accept: 'application/json',
            'Content-Type': 'application/json'
        },
        body : JSON.stringify({
          "email" : email,
          "password" : password
        })
      })
      let sres = await ires.json();
      if(sres['message'] === "Logged In."){
        localStorage.setItem("email",email)
        localStorage.setItem("password",password)
        if(localStorage.getItem("email") === email && localStorage.getItem("password")=== password)
        document.getElementsByClassName("spinner")[1].style.display = "none";
        window.location = "home.html"
      }
      else if(sres['message'] === "Wrong email or password. Try again!"){
        alert("Wrong credentials!")
      }
      else if(sres['message'] === "Create a new Account!"){
        alert(sres["message"])
      }
    }
    catch(err){
      console.log(err)
    }
  }
  function signin() {
    let email = document.querySelector('#email-signin').value;
    let password = document.querySelector('#password-signin').value;
    if(!email || !password) window.alert("Please fill all the fields");
    else if(email.includes('`')) window.alert("Please use valid email address");
    else {
      document.getElementsByClassName("spinner")[1].style.display = "inline";
      validate(email,password)
    }
  }
  
  async function createAccount(name,email,password){
        try{
          let ires = await fetch("http://localhost:4002/createAccount", {
        method: "POST",
        headers: {
          Accept: 'application/json',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          "name": name,
          "email": email,
          "password": password
          })
        })
        let sres = await ires.json();
        if(sres.message === "Account already exists."){
          alert("Account already exists please sign in.")
        }
        else if(sres.message === "New Account Created."){
          localStorage.setItem("email",email)
          localStorage.setItem("password",password)
          if(localStorage.getItem("email") === email && localStorage.getItem("password")=== password)
          document.getElementsByClassName("spinner")[0].style.display = "none";
          {window.location = "signin.html"}
        }
        else{
          alert("Something went wrong try again!")
        }
        }
        catch(err){
          console.log(err)
        }
        
       }
  function signup() {
    let name = document.querySelector('#name').value;
    let email = document.querySelector('#email').value;
    let password = document.querySelector('#password').value;
    let password2 = document.querySelector('#password2').value;

    if(!name || !email || !password || !password2) window.alert("Please fill all fields");
    else if(password!==password2) window.alert("The passwords do not match");
    else if(email.includes('`')) window.alert("Please use valid email address");
    else {
      document.getElementsByClassName("spinner")[0].style.display = "inline";
       createAccount(name,email,password);
    }
  }