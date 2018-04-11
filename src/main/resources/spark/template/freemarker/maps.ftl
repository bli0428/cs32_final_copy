<#assign content>
<center><h1>maps</h1></center>
<div onscroll="zoom(.001)">
<center><canvas id="map"></canvas></center>
</div>
<button onclick="zoom(.001)">-</button>
<button onclick="zoom(-.001)">+</button>
<button onclick="changeBox(50, 0)"><</button>
<button onclick="changeBox(0, 50)">^</button>
<button onclick="changeBox(-50, 0)">></button>
<button onclick="changeBox(0, -50)">v</button>
</#assign>
<#include "main.ftl">