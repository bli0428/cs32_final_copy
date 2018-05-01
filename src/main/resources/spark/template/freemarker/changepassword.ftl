<#assign content>

<h1> Change password </h1>
<p>
<form method="POST" action="/changepasswordresults">
  <input type="text" name="username" placeholder="username"></input><br><br>
  <input name="currpassword" type="password" placeholder="current password"></input><br><br>
  <input name="newpassword" type="password" placeholder="new password"></input><br><br>
  <input name="newpassword2" type="password" placeholder="confirm new password"></input><br><br>
</form>
</p>
<p>${message}</p>

</#assign>
<#include "main.ftl">
