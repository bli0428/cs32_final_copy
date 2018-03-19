<#assign content>
<h1> ${title} </h1>
<h2> ${message} </h2>

<button onclick="help()">Instructions</button> &nbsp;
<button id="smalldb"> Small Bacon Database</button> &nbsp;
<button id="db"> Bacon Database</button> &nbsp;


<div>
<br />
Actor 1:
<input type="text" id="actor1" name="actor1" placeholder="Enter actor one"></input>
<ul style="" id = "ac1"></ul>

Actor 2:
<input type="text" id="actor2" name="actor2" placeholder="Enter actor two"></input>
<ul style="" id = "ac2"></ul>

<br />

<div>


</div>

<input type="submit" id = "submit" name = "submit">

<h3 id = "message"></h3>


<div>
<ul id="finalpath"></ul>
</div>

</div>

<style>

</style>

<script>
function help() {
    
    alert("Welcome to my Bacon! First, load a database of actors and movies. Then simply type two actors you wish to find a path between and let the program do the rest! (capitalize first and last names!)");
    
}
</script>

</#assign>
<#include "main.ftl">
