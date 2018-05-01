<#assign content>

<h1> Log in </h1>
<p>
<form method="POST" action="/loginresults">
  <input type="text" name="username" placeholder="username"></input><br><br>
  <input name="password" type="password" placeholder="password"></input><br><br>
  <input type="checkbox" name="remember" value="remember"> Remember Me<br><br>
  <input type="submit">
</form>
</p>
<p>${message}</p>
<a href="/newaccount">Create account</a>

</#assign>
<#include "main.ftl">
