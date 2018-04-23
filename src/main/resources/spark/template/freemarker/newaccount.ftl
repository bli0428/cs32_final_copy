<#assign content>

<h1> Create account </h1>
<p>
<form method="POST" action="/newaccountresults">
  <input type="text" name="username" placeholder="username"></input><br><br>
  <input name="password" type="password" placeholder="password"></input><br><br>
  <input name="password2" type="password" placeholder="confirm password"></input><br><br>
  <input type="submit">
</form>
</p>
<p>${message}</p>

</#assign>
<#include "main.ftl">
