<#assign content>
<h1> ${message} </h1>
<form method="POST" action="/results">

<p>What type of search would you like to run?</p>
<input type="radio" name="type" value="Neighbors" checked/> Neighbors</input> &nbsp;
<input type="radio" name="type" value="Radius"/> Radius</input>

<br />
<p>For neighbors, how many neighbors do you want to find?</p>
<input type="text" id="k" name="k" placeholder="Non-negative integer!"></input>
<br />

<p>For radius, how large of a radius?</p>
<input type="text" id="r" name="r" placeholder="Non-negative double!"></input>
<br />

<p>Would you like to search around a coordinate or a star?</p>
<input type="radio" name="type2" value="Coordinate" checked/> Coordinate</input> &nbsp;
<input type="radio" name="type2" value="Star"/> Star</input>

<br />
<p>What is the Position you wish to search around?</p>
<input type="text" id="x" name="x" placeholder="X Value!"></input>
<input type="text" id="y" name="y" placeholder="Y Value!"></input>
<input type="text" id="z" name="z" placeholder="Z Value!"></input>
<br />

<p>What is the Starname you wish to search around?</p>
<input type="text" id="starname" name="starname" placeholder="Enter Starname!"></input>
<br />
<br />
  <input type="submit">
</form>
</p>


</#assign>
<#include "main.ftl">
