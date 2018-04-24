<#assign content>

<h1> Change username </h1>
<p>
<form method="POST" action="/changeusernameresults">
  <input type="text" name="currusername" placeholder="current username"></input><br><br>
  <input name="password" type="password" placeholder="password"></input><br><br>
  <input name="newusername" type="text" placeholder="new username"></input><br><br>
  <input type="submit">
</form>
</p>
<p>${message}</p>

</#assign>
<#include "main.ftl">
