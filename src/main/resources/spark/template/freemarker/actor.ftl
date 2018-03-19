<h1> ${title} </h1>

<button onclick="goBack()">Back to Previous Page</button>
<a href="/bacon"><button>Back to Home</button></a>


<div method="GET" action="/bacon/actor/:actorid">

<#if 0 < movies?size >
<ul>
<#list movies as movie>
   <li><a href="/bacon/movie/${movie.id}">${movie.name} </a>
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

ul{
list-style: none;
}

a{
color: black;
text-decoration: none;
}
</style>

<script>
function goBack() {
    window.history.back();
}
</script>
