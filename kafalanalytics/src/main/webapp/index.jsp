<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>Briller</title>

    <!-- Bootstrap -->
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link href="css/custom.css" rel="stylesheet">
    <style>
        .custom-control-label:before{
  background-color:#fff;
  border: 1px solid #242424;
}

  </style>

</head>

<body class="bg">

    <%
    Cookie[] cookies=request.getCookies();
    String userName = "", password = "",rememberVal="";
    if (cookies != null) {
         for (Cookie cookie : cookies) {
           if(cookie.getName().equals("cookuser")) {
             userName = cookie.getValue();
           }
           if(cookie.getName().equals("cookpass")){
             password = cookie.getValue();
           }
           if(cookie.getName().equals("cookrem")){
             rememberVal = cookie.getValue();
           }
        }
    }
	%>

    <div>

        <div class="col-md-12">&nbsp;</div>
        <div class="container-fluid"><br>
            <div class="row">
                <div class="col-md-4">
                    <div>
                        <!-- <img src="images/white-logo.png"> -->
                        <img src="images/Hanogi-n-Kafal-45-05-white-final.png" style="width: 45%; margin-left: 30%;">
                    </div>
                    <div class="font-size-12 color-ff Ubuntu-L line-height-2px padding-top-2">
                        <!-- <p>Empowering people</p>
                        <p>A business solution for better growth</p> -->
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="color-ff Ubuntu-L text-center font-size-45px line-height-10">
                        <!-- <p>Welcome</p>
                        <span class="font-size-25">Back</span> -->
                    </div>
                </div>
                <div class="col-md-4">

                    <div class="form-row align-item-center float-right">
                        <div class="col line-height-0 color-ff">
                            <!-- <p class="font-size-12 text-center" >Not a member</p>
                            <button style="background-color: #941F5E" type="button" class="btn border-radius-20 height-35px color-ff Ubuntu-L font-size-15  width-130">Send a query</button>  -->
                        </div>
                    </div>
                </div>
            </div><br>
            <div class="col-md-12">&nbsp;</div>


        </div>
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-4 padding-left-4 padding-right-2">
                    <form method="post" action="app/noauth/employee/login">
                        <div class="background-color-ff width-70px height-70px border-radius-50 " style="margin: 4% auto">
                            <img class="padding-5px width-70px height-70px" src="images/user-100.png">
                        </div>
                        <div class="form-group align-items-center margin-bottom-2">
                            <div class="col-md-12 inputWithIcon">
                                <span></span>
                                <input type="text" name="userLogin" class="form-control mb-2 border-radius-20 height-40px font-size-13 "
                                    id="inlineFormInput" placeholder="Enter Email">
                                <span><i class="fa fa-user-o" aria-hidden="true"></i></span>
                            </div>
                        </div>
                        <div class="form-group align-items-center margin-bottom-2">
                            <div class="col-12 inputWithIcon">
                                <input type="password" name="userPassword" class="form-control mb-2 border-radius-20 height-40px font-size-13"
                                    id="inlineFormInput1" placeholder="Password">
                                <span><i class="fa fa-key" aria-hidden="true"></i></span>
                            </div>
                        </div>
                        <div class="form-group align-item-center margin-bottom-3">
                            <div class="col-md-12">
                                <button type="submit" style="background-color: #2e3f51" type="button" class="btn width-full border-radius-20 height-40px Ubuntu-B font-size-17 color-ff">Sign
                                    In</button>
                            </div>
                        </div>
                        <div class="form-group col-md-12">
                            <div class="col-sm-6 custom-control custom-checkbox float-left">
                                <input type="checkbox" class="custom-control-input " id="customControlInline" name="remember"
                                    value="1" <%="1" .equals(rememberVal.trim()) ? "checked=\" checked\"" : "" %>
                                />
                                <label class="custom-control-label font-size-12" for="customControlInline">Remember
                                    me</label>
                            </div>
                            <div class="col-sm-6 float-left custom-control custom-checkbox my-1">
                                <a class="font-size-12" href="confirmEmail.jsp"><span>Forgot Password</span></a>
                            </div>
                        </div>
                        <!-- <div class="col-sm-7">
                                <div class="custom-control custom-checkbox">
                                    <input type="checkbox" class="custom-control-input " id="customControlInline" name="remember"
                                        value="1" <%="1" .equals(rememberVal.trim()) ? "checked=\" checked\"" : "" %>
                                    />
                                    <label class="custom-control-label font-size-12" for="customControlInline">Remember
                                        me</label>
                                </div>


                            </div>
                            <div class="col-sm-8 col-lg-10">

                                <div class="custom-control custom-checkbox my-1 mr-sm-2">
                                    <a class="font-size-12" href="confirmEmail.jsp"><span>Forgot Password</span></a>
                                </div>
                            </div> -->
                </div>

                </form>
            </div>
            <div class="col-md-4">

            </div>
            <div class="col-md-4"></div>
        </div>
    </div>
    </div>





    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <!-- jQuery library -->
    <!-- jQuery library -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>

    <!-- Popper JS -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>

    <!-- Latest compiled JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>








</body>

</html>