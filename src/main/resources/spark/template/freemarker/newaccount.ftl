<#assign content>

<h1> Create account </h1>
<p>
<form method="POST" action="/newaccountresults">
  <input type="text" name="username" placeholder="username"></input><br><br>
  <input name="password" type="password" placeholder="password"></input><br><br>
  <input name="password2" type="password" placeholder="confirm password"></input><br><br>
  <input id="submit" type="submit">
  <button id="button" type="button">Click Me!</button>
</form>
</p>
<p>${message}</p>
<a href="/login">Already have an account? Log in.</a>

</#assign>
<#include "main.ftl">
