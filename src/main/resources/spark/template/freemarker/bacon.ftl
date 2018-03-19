<#assign content>

<h1> Bacon </h1>
<p>Enter the names of two actors to see the connection between them.</p><br>
<p>
<form method="GET" action="/baconresults">
<table>
<tr>
  <td>Actor 1:</td><td><textarea name="actor1" id="searchbar1" class="searchbar" style="width: 20vw"></textarea></td>
  <td>Actor 2:</td><td><textarea name="actor2" id="searchbar2" class="searchbar" style="width: 20vw"></textarea></td>
  <td><input type="submit"></td></tr>
<tr>
  <td></td><td><div id="suggestions1" class="suggestions"></div></td>
  <td></td><td><div id="suggestions2" class="suggestions"></div></td>
</tr>
</table>
</form>
</p>


</#assign>
<#include "main.ftl">