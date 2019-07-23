<!DOCTYPE html>
<html>
<head>
<title>Reset Password Secuss</title>
<link rel="stylesheet" type="text/css" href="custom.css">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
	integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm"
	crossorigin="anonymous">
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"
	integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN"
	crossorigin="anonymous"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"
	integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q"
	crossorigin="anonymous"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"
	integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl"
	crossorigin="anonymous"></script>
<link href="./css/bootstrap.min.css" rel="stylesheet">
<!--<link rel="stylesheet" href="css/css1/font-awesome.min.css">-->
<link href="css/custom.css" rel="stylesheet">
<link href="css/bargraph.component.css" rel="stylesheet">
<link rel='stylesheet prefetch'
	href='https://cdnjs.cloudflare.com/ajax/libs/normalize/5.0.0/normalize.min.css' />
<link rel='stylesheet prefetch'
	href='https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css' />
<link rel="stylesheet" type="text/css" href="css/sirCSS.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSP Page</title>
<style type="text/css">
* {
	font-family: cursive;
}
</style>

</head>
<body
	style="background-image: url(bg.jpg); background-size: 100% 800px;"
	class="Ubuntu-L">
	<div class="container" style="align-content: center; margin-top: 10%">
		<div style="height: 600px;" align="center">
			<div class="col-lg-12"
				style="background: white; border-radius: 6px; height: 70%">


				<div style="text-align: center;">
					<div style="padding-top: 40px; margin-bottom: 30px;">
						<img src="images/logoh.png" style="width: 13%; height: 6%;">
					</div>
					<div style="">
							<%
        String status = request.getParameter("cred");
                          %>
                          
						<a
							style="margin-top: px; background-color: #17bbc5; padding-left: 5.5em; padding-right: 5.5em; padding-top: 0.7em; padding-bottom: 0.7em; border-radius: 25px; color: white; font-size: 20px;">
							<%=status%></a>
					</div>
                   
                   <div style="padding-top: 0.5em;" >
						<font size="3.5">You can now login to your account.</font>
					</div>
					
				</div>

                    <div style="padding-top: 2em;" align="center" >
						<input type="submit" name="submit" padding-left: 5.5em; value="Sign-In" onclick="window.location='index.jsp';">
						
					</div>


				<div id="error-nwl"></div>
			</div>

		</div>
	</div>
	</div>

</body>
<style>
li {
	font-size: 12px;
}
</style>
</html>