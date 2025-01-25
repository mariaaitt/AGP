<%--
  Created by IntelliJ IDEA.
  User: jianke
  Date: 2025/1/25
  Time: 02:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Travel Offer System</title>
</head>
<body>
<h1>Welcome to Travel Offer System</h1>
<p>Please select the following operation:</p>
<ul>
    <! -- Be careful to add ${pageContext.request.contextPath} here, otherwise you will get an error in non-ROOT contexts -->
    <li><a href="${pageContext.request.contextPath}/lucene/rebuild">Rebuild Index</a></li>
    <li><a href="${pageContext.request.contextPath}/query/exec?bdeQuery=SELECT%20*%20FROM%20site">View All Sites</a></li>
    <!-- more links  -->
</ul>
</body>
</html>


