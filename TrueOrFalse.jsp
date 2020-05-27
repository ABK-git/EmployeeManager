<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>社員情報登録確認ページ</title>
</head>
<body>
<%
  String message=(String)request.getAttribute("Message");
%>
<p>
<%=message %>
</p>
<!-- トップに戻る -->
<form action="/FinalEmployee/FirstAccess" method="get">
  <input type="submit" value="社員リスト一覧へ">
</form>
<!-- 部署リストトップに戻る -->
<form action="/FinalEmployee/TopListControl">
  <input type="hidden" name="action" value="DepartList">
  <input type="submit" value="部署リスト一覧へ">
</form>
</body>
</html>