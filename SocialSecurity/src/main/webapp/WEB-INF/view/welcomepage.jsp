<%-- 
    Document   : welcomepage
    Created on : 9 juin 2017, 11:55:15
    Author     : adychka
--%>



<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="resources/css/bootstrap.min.css" rel="stylesheet">
    
  </head>
  <body>
     
    <%@include file="header.jsp" %>
    <div class="container">
      <div class="jumbotron">
        <h1>Social Security application</h1>
        <a class="btn btn-lg btn-primary" href="/" role="button">Go to attribute builder</a>

        <a class="btn btn-lg btn-primary" href="/" role="button">Go to harm tree builder</a>
      </div>
    </div>
    <%@include file="footer.jsp" %>
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="resources/js/bootstrap.min.js"></script>
  </body>
</html>
