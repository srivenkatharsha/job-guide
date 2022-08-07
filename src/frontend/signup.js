$(".login-form").hide();
$(".login").css("background", "none");
$("body").css("width","100%");
$(".login-form").css("height","380px");
$(".login").click(function(){
  $(".signup-form").hide();
  $(".login-form").show();
  $(".signup").css("background", "none");
  $(".login").css("background", "#fff");
});

$(".signup").click(function(){
  $(".signup-form").show();
  $(".login-form").hide();
  $(".login").css("background", "none");
  $(".signup").css("background", "#fff");
});

// $(".btn").click(function(){
//   $(".input").val("");
// });