$(".signup-form").hide();
$(".signup").css("background", "none");
$(".login-form").css("height","380px");
$("body").css("width","100%");
$(".signup").click(function(){
  $(".login-form").hide();
  $(".signup-form").show();
  $(".login").css("background", "none");
  $(".signup").css("background", "#fff");
});

$(".login").click(function(){
  $(".login-form").show();
  $(".signup-form").hide();
  $(".signup").css("background", "none");
  $(".login").css("background", "#fff");
});

// $(".btn").click(function(){
//   $(".input").val("");
// });