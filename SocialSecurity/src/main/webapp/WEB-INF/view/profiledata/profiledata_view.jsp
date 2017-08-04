<%-- 
    Document   : userdata_all.jsp
    Created on : 5 juil. 2017, 15:33:34
    Author     : adychka
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Profile data</title>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/resources/css/attribute_add.css" rel="stylesheet">
    </head>
    <body>
        <script>var ctx = "${pageContext.request.contextPath}"</script>
        <%@include file="../basic/header.jsp" %>
        <div class='add_attr_form panel panel-default'>
             <div class="panel panel-heading">
                 Profile data details
             </div>
             <div class="panel panel-body">
                 <div  class="input_fields_wrap">
                     <br>
                     <label for="name" data-toggle="tooltip" data-placement="top" >Name:</label>
                     <input type="text" name='name' id="name" class="form-control" value="${data.name}" readonly/>

                     <br>
                     <label for="fb_url" data-toggle="tooltip" data-placement="top" >Facebook profile url:</label>
                     <input type="text" name='fb_url' id="fb_url" class="form-control" value="${data.requestUrl}" readonly/>

                     <br>
                     <label for="completed" data-toggle="tooltip" data-placement="top" >Is completed:</label>
                     <input type="text" name='completed' id="completed" class="form-control" value="${data.completed}" readonly/>
                     
                     <br>
                     <label for="completed_time_es" data-toggle="tooltip" data-placement="top" >Estimate complete time :</label>
                     <input type="text" name='completed_time_es' id="completed_time_es" class="form-control" value="${data.getEstimateFinishTimeDate()}" readonly/>
                    
                    <c:if test = "${data.completed == true}">
                        <br>
                        <label for="completed_time_ex" data-toggle="tooltip" data-placement="top" >Exact complete time :</label>
                        <input type="text" name='completed_time_ex' id="completed_time_ex" class="form-control" value="${data.getRealFinishTimeDate()}" readonly/>
                        
                        
                        <a id=chng_btn type="button" class="btn btn-danger" href="${pageContext.request.contextPath}/profiledata/remove/${data.id}">Delete</a>
                        <a style="margin: 10px" id=chng_btn type="button" class="btn btn-primary" href="${pageContext.request.contextPath}/profiledata/fgraph/${data.id}">View friendship graph</a>
                        <a style="margin: 10px" id=chng_btn type="button" class="btn btn-primary" href="${pageContext.request.contextPath}/profiledata/amatrix/${data.id}">View attribute matrix</a>
                        <a style="margin: 10px" id=chng_btn type="button" class="btn btn-primary" href="${pageContext.request.contextPath}/profiledata/avmatrix/${data.id}">View attribute visibility matrix</a>
                        <a style="margin: 10px" id=chng_btn type="button" class="btn btn-primary" href="${pageContext.request.contextPath}/profiledata/analysis/${data.id}">View analysis report</a>
                     </c:if>
                 </div>
             </div>
         </div>
        
        
        <%@include file="../basic/footer.jsp" %>
        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>    
    </body>
</html>
