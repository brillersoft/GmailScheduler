<!DOCTYPE html>
<html>
<head>
	<title>Reset Password</title>
	<link rel="stylesheet" type="text/css" href="custom.css">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
	<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
    <link href="./css/bootstrap.min.css" rel="stylesheet">
  <!--<link rel="stylesheet" href="css/css1/font-awesome.min.css">-->
  <link href="css/custom.css" rel="stylesheet">
  <link href="css/bargraph.component.css" rel="stylesheet">
  <link rel='stylesheet prefetch' href='https://cdnjs.cloudflare.com/ajax/libs/normalize/5.0.0/normalize.min.css'/>
  <link rel='stylesheet prefetch' href='https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css'/>
  <link rel="stylesheet" type="text/css" href="css/sirCSS.css">
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JSP Page</title>
    <style type="text/css">
      *{
             font-family: cursive;
       }
        </style>
   <script type="text/javascript">
    function confirmPass() {
    	
        var pass1 = document.getElementById("new_pass");
        var pass2 = document.getElementById("confirm_pass");
        
   
         var message = document.getElementById('error-nwl');
    var goodColor = "#66cc66";
    var badColor = "#ff6666";
 	
    var passw = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,20}$/;
    if(pass1.value.match(passw)) 
    { 
    	 message.innerHTML = ""
    return true;
    }
    else
    { 
    	message.style.color = badColor;
       // message.innerHTML = "Please Enter The Password In correct Format."
        return false;
    
    }
  
    
    
    }
  function checkconfirmPass() {
    	
        var pass1 = document.getElementById("new_pass");
        var pass2 = document.getElementById("confirm_pass");
        
        
         var message = document.getElementById('error-nwl');
    var goodColor = "#66cc66";
    var badColor = "#ff6666";

    if(pass1.value == pass2.value)
    {
    	message.innerHTML = ""
        return true;
    }
	else
    {
		message.style.color = badColor;
        message.innerHTML = "Password Do not match"
        return false;
    }
    
    
    }
    
/*  $(document).ready(function() {
	  
      $('.komunikat').hide();
      $('#orderform :input').focus(function(){
    	  
         $('.komunikat').fadeIn(); 
      });
      $('#orderform :input').blur(function(){
    	  alert("hi3")
         $('.komunikat').fadeOut(); 
      });
  });
  */
  
   
</script>
</head>
<body style="background-image: url(images/bg.jpg);background-size: 100% 800px;" class="Ubuntu-L">
	<div class="container" style="align-content: center;margin-top:10% ">
		<div style="height: 600px;">
			<div class="col-lg-12" style="background: white;border-radius:6px;height:70%">
			
			<form method="post" id="orderform" action="app/noauth/changepasswordsection" onsubmit="return validate(this);">
				<div style="text-align: center;">
					<div style="padding-top: 40px;margin-bottom: 30px;">
					<img src="images/logoh.png" style="width: 13%;height: 6%;">
					</div>
					<div style="">
					<a  class="Ubuntu-L" style="margin-top: 5px;background-color:#17bbc5;padding-left:5.5em;padding-right:5.5em;padding-top:0.7em;padding-bottom:0.1em;border-radius:25px;color:white;font-size: 20px;  ">Reset your Password</a>	
					</div>
					<div class="Ubuntu-L" style="margin-top: 20px; font-size: 14px; "id="error-nwl"></div>
					<div style="padding-top: 0.7em;padding-bottom: 1.5em;">
						<div style="width: 30px;margin-bottom: 0.4em;margin-left: 51px;display:inline;min-width: 30px;">						
							<img src="images/right.png" id="new_pass_show_correct" class="right_img inline_img" style="">
							&nbsp;
						</div>
						<div class="inline_img inpt_style">
							<img src="images/key.png"  style="margin-bottom: 0.5em;height:15px;padding-right: 0.3em;">
							<input type="password" name="new_pass" id="new_pass" class="plc_dsn" placeholder="New Password" onkeyup="confirmPass();" >
						</div>
						<span id="question_mark">
							<img src="images/qm.png"  class="inline_img" style="width: 4.3%;padding-left: 1%;margin-right: -30px;padding-bottom: 0.5%;">
							<div class="show_p_rules" >
								<div class="triangle_reset_rules" >
								<div class="dropdown_reset_rules" >
									<p><u>Password Requirements :</u></p>
									<ul style="text-align: left;margin-left: -20px">
										<li>Minimum 8 Character long</li>
										<li>Atleast one lowecase character</li>
										<li>Atleast one uppercase character</li>
										<li>Atleast one number</li>
										<li>Atleast one special character</li>
									</ul> 
									</div>
								</div>
							</div>
						</span>
					</div>
					<div style="margin-right: 31px;">
						<div style="width: 30px;margin-bottom: 0.4em;display:inline;min-width: 30px;">						
							<img src="images/right.png" id="confrm_pass_show_correct" class="right_img inline_img" style="">
							&nbsp;
						</div>
						<div class="inline_img inpt_style">
							<img src="images/key.png" class="inline_img" style="margin-bottom: 0.2em;height: 15px;padding-right: 0.3em;">
							<input type="password" name="confirm_pass"  id= "confirm_pass"  class="plc_dsn" placeholder="Confirm Password" onkeyup="checkconfirmPass();" >
						</div>
						<div>
						<% String token= request.getParameter("cred");%>
						<input type="hidden" name="token"  value="<%=token%>">
						</div>
					</div>
					<div style="padding-top: 2em;">
						<input style=" border-radius : 18px; background-color: #497487; color:white; width: 181px;height : 36px; text-align: center" type="submit"   name="submit" value="Submit" disable="true" class="Ubuntu-L">
					</div>
					
					<div id="error-nwl"></div>
			    </div>
			    </form>
			</div>
		</div>   
	</div>

</body>
<style>
	li
	{
		font-size: 12px;
		 
		
	}
</style>
</html>