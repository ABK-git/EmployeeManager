<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="dao.DepartmentDAO" %>
<%@ page import="beans.Department" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>部署リスト一覧</title>
</head>
<body>
<h1>部署一覧</h1>
<!-- 部署リストを取得する -->
  <table border="1">
    <tr>
      <th>部署ID</th>
      <th>部署名</th>
    </tr>
    <!-- リストを表示する -->
    <c:forEach var="depart" items="${DepartmentList}">
      <tr>
        <td>POST<c:out value="${depart.id}"/></td>
        <td><c:out value="${depart.name}"/></td>
        <td>
          <!-- 編集ボタン -->
          <form action="/FinalEmployee/DepartmentControl" method="get">
            <input type="hidden" name="id" value="${depart.id}">
            <input type="submit" value="編集">
          </form>
        </td>
        <td>
          <!-- 削除ボタン -->
          <form action="/FinalEmployee/DepartmentControl" method="post">
            <input type="hidden" name="id" value="${depart.id}">
            <input type="submit" value="削除">
          </form>
        </td>
      </tr>
    </c:forEach>
  </table>
  <!-- 新規追加ボタン -->
  <form action="/FinalEmployee/DepartmentControl" method="get">
    <input type="submit" value="新規追加">
  </form>

  <!-- 社員リスト一覧へ -->
  <form action="/FinalEmployee/FirstAccess" method="get">
    <input type="submit" value="社員リスト一覧へ">
  </form>

</body>
</html>