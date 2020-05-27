<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<!-- タイトルの表示 -->
<c:choose>
  <c:when test="${empty Depart}">
    <title>部署情報新規追加</title>
  </c:when>
  <c:otherwise>
    <title>部署情報編集</title>
  </c:otherwise>
</c:choose>
</head>
<body>
<!-- 見出し表示 -->
<c:choose>
  <c:when test="${empty Depart}">
    <h1>部署情報新規追加</h1>
  </c:when>
  <c:otherwise>
    <h1>部署情報編集</h1>
  </c:otherwise>
</c:choose>
<form action="/FinalEmployee/DepartmentControl" method="post">
  <!-- 部署のIDを送る -->
  部署ID:
  <c:choose>
    <c:when test="${not empty Depart}">
      POST<c:out value="${Depart.id}  "/>
      <input type="hidden" name="id" value="${Depart.id}">
      <input type="hidden" name="action" value="edit">
    </c:when>
    <c:otherwise>
      POST<c:out value="${id}"/>
      <input type="hidden" name="id" value="${id}">
      <input type="hidden" name="action" value="add">
    </c:otherwise>
  </c:choose><br>

  <!-- 部署の名前を入力 -->
  部署名:<input type="text" name="name" value="${Depart.name}"><br>
  <!-- 登録ボタン -->
  <input type="submit" value="登録">
</form>
<br>
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