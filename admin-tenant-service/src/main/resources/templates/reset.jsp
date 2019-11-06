<%@ page language="java" contentType="text/html; charset=US-ASCII" pageEncoding="US-ASCII" import="java.util.*"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!-- <!DOCTYPE html> -->
<%@taglib uri="spring.io" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<html>
<head>
<%@ page isELIgnored="false" %>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	crossorigin="anonymous">
</head>
<body>
	<div class="d-flex justify-content-center">
		<div class="row mx-5">
			<div
				class="col-md-12 col-sm-12 col-12 col-lg-12 col-xl-12 my-5 d-flex align-items-end">
				<h1 class="img-text">Reset Your Password</h1>
			</div>
		</div>
	</div>
	<br>
	<br>
	<div class="d-flex justify-content-center">


		<div class="col-12 col-md-8 col-lg-8 col-xl-8 col-sm-12">

			<div class="d-flex justify-content-center" id="signinlogo">

				<img
					src="https://cdn1.imggmi.com/uploads/2019/9/25/902e72f1a18d915fbaa2a192719eb7d0-full.png"
					alt="sudha" border="0" />
			</div>


			<div class="my-3">
				<h1>Hi PaaS Member ${msg},</h1>
				<p style="font-size: 14px;">
					You recently requested to reset your password for your account. Use
					the button below to reset it. <strong>This password reset
						is only valid for the next 24 hours.</strong>
				</p>
			</div>
			<div class="my-3">
				<a href="http://localhost:4200/resetpassword" class="btn"
					style="border-radius: 20px; background-color: #6F5996; padding: 10px; color: #FFF;"
					target="_blank">Reset your password</a>
			</div>
			<div class="my-3">
				<p style="font-size: 14px;">If you did not request a password
					reset, please ignore this email.</p>
				<p style="font-size: 14px;">
					Thanks, <br>Team SPSoft
				</p>
			</div>
			<!--  <div class="my-3">
                <p class="sub">If you’re having trouble with the button above, copy and paste the URL
                    below into your web browser.</p>
                <p class="sub">http://18.215.4.80:80/resetpassword</p>
            </div> -->
		</div>
	</div>
	<script src="webjars/bootstrap/4.2.1/js/bootstrap.min.js"></script>
	</div>
</body>
</html>