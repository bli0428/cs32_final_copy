<#assign content>
<center><h1>maps</h1></center>
<div onscroll="zoom(.001)">

<center>
<p>

<button id="db"> Load Maps Database</button> &nbsp;

	<br /> 
		<br /> 

  Enter intersection Here:
  
	<br />   
    <br /> 
  
  <div>
  <input type="text" id="start1" placeholder = "Street 1"></input>
  <ul style="" id = "ac1"></ul>
  
  <input type="text" id="start2" placeholder = "Street 2"></input>
  <ul style="" id = "ac2"></ul>
  
  <input type="text" id="end1" placeholder = "Street 1"></input>
  <ul style="" id = "ac3"></ul>
  
  <input type="text" id="end2" placeholder = "Street 2"></input>
  <ul style="" id = "ac4"></ul>
  
  </div>
  
  <button onclick="route()">Submit</button>
</p>
</center>

<br />
<br />
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