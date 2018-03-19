<#assign content>

<h1> ${title} </h1>
<h2> ${message} </h2>

<form method ="POST" action="/validate">

<p>First, upload a corpus in REPL or a choose default corpus below!</p>
<button type="button" id="dict">Upload Dictionary</button>
<button type="button" id="ge">Upload Great Expectations</button>


<br />
<br />
<p>What types of suggestions would you like?</p>

<button type="button" id="default">Recommended Suggestions Settings</button>
<br />
<br />

Prefix: &nbsp;
<select id="prefix">
<option value="off">off</option>
<option value="on">on</option>
</select>

<br />
<br />
Whitespace: &nbsp;
<select id="whitespace">
<option value="off">off</option>
<option value="on">on</option>
</select>

<br />
<br />
Led: (0 is assumed on error) &nbsp;
<select id="led">
<option value="0">0</option>
<option value="1">1</option>
<option value="2">2</option>
<option value="3">3</option>
<option value="4">4</option>
<option value="5">5</option>
<option value="6">6</option>
<option value="7">7</option>
<option value="8">8</option>
<option value="9">9</option>
<option value="10">10</option>
</select>

<br />
<br />
Smart Ranking: &nbsp;
<select id="smart">
<option value="off">off</option>
<option value="on">on</option>
</select>

<br />
<br />
<br />
As you type, click suggestions to replace the word!
<br />
<br />
<input type="text" name="input" id="input" list="mainlist" placeholder="Start Typing Here..."></input>
<ul id="mainlist"></ul>
</form>
</#assign>
<#include "main.ftl">
