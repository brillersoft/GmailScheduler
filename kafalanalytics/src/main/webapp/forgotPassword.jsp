<!DOCTYPE html>
<html>
    <head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
  <title>Kafal</title>

  <!-- Bootstrap -->
  <link href="css/bootstrap.min.css" rel="stylesheet">
  
  <link href="css/custom.css" rel="stylesheet">
  <link rel='stylesheet prefetch' href='https://cdnjs.cloudflare.com/ajax/libs/normalize/5.0.0/normalize.min.css'/>
  <link rel='stylesheet prefetch' href='https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css'/>
    <link rel="shortcut icon" href="">

</head>

  <body class="bg">
              
    <div >
        
        <div class="col-md-12">&nbsp;</div>
        <div class="container"><br>
            <div class="row">
                <div class="col-md-4">
                    <div>
                        <img src="images/Hanogi-n-Kafal-45-05-white-final.png" style="width: 45%">
                    </div>
                    <!-- <div class="font-size-12 color-ff Ubuntu-L line-height-2px padding-top-2">
                        <p">Empowering people</p>
                        <p>A business solution for better growth</p>
                    </div> -->
                </div>
                <div class="col-md-4">
                    <!-- <div class="color-ff Ubuntu-L text-center font-size-45px line-height-10">
                        <p>Welcome</p>
                        <span class="font-size-25">Back</span>
                    </div> -->
                </div>
                <div class="col-md-4">
                        
                    <!-- <div class="form-row align-item-center float-right">                    
                        <div class="col line-height-0 color-ff">
                            <p class="font-size-12 text-center" >Not a member</p>
                            <button style="background-color: #941F5E" type="button" class="btn border-radius-20 height-35px color-ff Ubuntu-L font-size-15  width-130">Send a query</button>                            
                        </div>                        
                    </div> -->
                </div>
            </div><br>
         <div class="col-md-12">&nbsp;</div>
       
           
        </div>
        <div class="container-fluid">
             <div class="row ">
             	<div class="col-md-4"></div>
                <div class="col-md-4 padding-left-4 padding-right-2">
                    <form method="post" action="app/noauth/updatepassword">
                    <div class="background-color-ff width-70px height-70px border-radius-50 " style="margin-left: 141px;">
                        <img class="padding-5px width-70px height-70px" src="images/user-100.png">
                    </div>
                    <br>
                    <div class="form-row align-items-center padding-top-2">
                        <div class="col-12">
                          <label class="sr-only Ubuntu-L" for="inlineFormInput">New Password</label>
                          <input type="password" name="userPassword"  class="form-control mb-2 border-radius-20 height-35px Ubuntu-M" id="inlineFormInput1" placeholder="New Password" required="required">
                      		
                        </div>
                    </div>
                    <div class="form-row align-items-center padding-top-2">
                        <div class="col-12">
                          <label class="sr-only Ubuntu-L" for="inlineFormInput">Confirm Password</label>
                          <input type="password" name="userConfirmPassword"  class="form-control mb-2 border-radius-20 height-35px Ubuntu-M" id="inlineFormInput2" placeholder="Confirm Password" required="required">
                        	
                        </div>
                    </div>
                    <div class="form-row align-items-center padding-top-2">
                        <div class="col-12">
                          <label class="sr-only Ubuntu-L" for="inlineFormInput">OTP</label>
                          <input type="text" name="userOtp" class="form-control mb-2 border-radius-20 height-35px Ubuntu-M" id="inlineFormInput" placeholder="Enter OTP" required="required">
                          
                        </div>
                    </div>
                    <div class="form-row align-item-center padding-top-2">
                        <div class="col-md-12">
                            <button type="submit" style="background-color: #941F5E" type="button" class="btn width-full border-radius-20 height-35px Ubuntu-L font-size-13 color-ff" onclick="return Validate()">Confirm</button>                            
                        </div>                        
                    </div>
                    <div class="form-row">
                            <div class="col-7">
	                            <div class="custom-control custom-checkbox my-1 mr-sm-2">
	                                    <a class="font-size-12 Ubuntu-M color-00" href="index.jsp">Back to Login</a>
	                                 </div>
                               <!--  <div class="custom-control custom-checkbox my-1 mr-sm-2">
                                    <input type="checkbox" class="custom-control-input" id="customControlInline">
                                    <label class="custom-control-label font-size-12" for="customControlInline">Remember me</label>
                                 </div> -->                                 
                            </div>
                            <div class="col-5">                            
                                <!--  <div class="custom-control custom-checkbox my-1 mr-sm-2">
                                    <a class="font-size-12 Ubuntu-M color-00" href="index.jsp">Back to Login</a>
                                 </div> -->
                            </div>
                        </div>                         
                    </form>
                </div>
                <div class="col-md-4"></div>
                
            </div>
        </div>
    </div>



<script type="text/javascript">
function Validate() {
    var password = document.getElementById("inlineFormInput1").value;
    var confirmPassword = document.getElementById("inlineFormInput2").value;
    if (password != confirmPassword) {
        alert("Passwords do not match with confirmPassword.");
        return false;
    }
    return true;
}
</script>




  <!-- Include all compiled plugins (below), or include individual files as needed -->
 <script type="text/javascript" src="js/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js" integrity="sha384-b/U6ypiBEHpOf/4+1nzFpr53nxSS+GLCkfwBdFNTxtclqqenISfwAzpKaMNFNmj4" crossorigin="anonymous"></script>
  <script type="text/javascript" src="js/bootstrap.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-slider/10.0.0/bootstrap-slider.min.js"></script>






   </body>
</html>