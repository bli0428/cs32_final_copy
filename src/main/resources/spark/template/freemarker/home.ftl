<#assign content>

<h1> Home </h1>
${content}
<button type="button" onclick="addGame('chess')">Create Chess game</button>
<button type="button" onclick="addGame('bughouse')">Create Bughouse game</button>
<form method="POST" action="/logout"><input type="submit" value="Log out"></form><br>
<a href="/changeusername">Change username</a><br>
<a href="/changepassword">Change password</a>

</#assign>
<#include "main.ftl">
