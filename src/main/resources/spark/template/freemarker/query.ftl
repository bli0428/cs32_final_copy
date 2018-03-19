<#assign content>

<h1> Query Stars </h1>

<h2> Search for a specific number of neighbors </h2>
<p>
<form method="GET" action="/neighbors">
  Number of neighbors: <textarea name="numneighbors"></textarea><br><br>
  Enter coordinates: (<textarea name="n-x" placeholder="x"></textarea>, 
  <textarea name="n-y" placeholder="y"></textarea>, 
  <textarea name="n-z" placeholder="z"></textarea>)
  <b>or</b> name: <textarea name="n-name" style="width: 100px"></textarea>.
  <input type="submit">
</form>
</p>

<h2> Search for stars within a radius </h2>
<p>
<form method="GET" action="/radius">
  Radius: <textarea name="radius"></textarea><br><br>
  Enter coordinates: (<textarea name="r-x" placeholder="x"></textarea>, 
  <textarea name="r-y" placeholder="y"></textarea>, 
  <textarea name="r-z" placeholder="z"></textarea>)
  <b>or</b> name: <textarea name="r-name" style="width: 100px"></textarea>.
  <input type="submit">
</form>
</p>

</#assign>
<#include "main.ftl">
