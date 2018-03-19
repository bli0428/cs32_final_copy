<h1> ${title} </h1>

<button onclick="goBack()">Back to Previous Page</button>
<a href="/bacon"><button>Back to Home</button></a>

<div method="GET" action="/bacon/movie/:movieid">

<#if 0 < actors?size >
<ul>
<#list actors as actor>
   <li><a href="/bacon/actor/${actor.id}">${actor.name} </a>
    </li>
</#list>
</ul>
</#if>

</div>


<style>
body {
    background-color: #67BCDB;
    color: black;
text-align: center;
font-family: "Comic Sans MS", cursive, sans-serif;
}

a{
color: black;
text-decoration: none;
}

ul{
list-style: none;
}
</style>

<script>
function goBack() {
    window.history.back();
}
</script>
