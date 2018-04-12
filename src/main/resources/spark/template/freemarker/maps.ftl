<#assign content>
<center><h1>maps</h1></center>
<div onscroll="zoom(.001)">
<p>
  Enter intersection: <textarea id="start1"></textarea>, 
  <textarea id="start2"></textarea>, 
  <textarea id="end1"></textarea>, 
  <textarea id="end2"></textarea>
  <button onclick="route()">Submit</button>
</p>
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