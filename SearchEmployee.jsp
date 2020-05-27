<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>社員情報検索</title>
</head>
<body>
<h1>社員情報検索</h1>
<form action="/FinalEmployee/SearchControl" method="post">
  <!-- 社員番号入力 -->
  <label for="employeeID">社員番号:</label>
  <input type="text" name="employeeID"><br><br>
  <!-- 名前の一部を入力 -->
  <label for="name">名前:</label>
  <input type="text" name="name" id="name"><br><br>
  <!-- 部署を入力 -->
  <label for="Department">部署:</label>
  <select name="Department" id="Department">
    <!-- 部署リストをドロップダウンリストに取り込む -->
    <c:forEach var="depart" items="${list}">
      <option value="${depart.id}">
        <c:out value="${depart.name}"/>
      </option>
    </c:forEach>
  </select><br><br>
  <!-- 検索条件確定ボタン -->
  <input type="submit" value="検索">
</form><br>

<table border="1">
  <tr>
    <td>
      <!-- 社員リスト一覧へ -->
      <form action="/FinalEmployee/FirstAccess" method="get">
      <input type="submit" value="社員リスト一覧へ">
    </form>
    </td>
    <td>
      <!-- 部署リスト一覧へ -->
      <form action="/FinalEmployee/TopListControl" method="get">
        <input type="hidden" name="action" value="DepartList">
        <input type="submit" value="部署リスト一覧へ">
      </form>
    </td>
  </tr>
</table>
</body>
</html>