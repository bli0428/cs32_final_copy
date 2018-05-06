<#assign content>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
  <a class="navbar-brand" href="/home">ChesS32</a>
  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarTogglerDemo02" aria-controls="navbarTogglerDemo02" aria-expanded="false" aria-label="Toggle navigation">
    <span class="navbar-toggler-icon"></span>
  </button>
  <div class="collapse navbar-collapse" id="navbarTogglerDemo02">
    <ul class="navbar-nav mr-auto mt-2 mt-lg-0">
    </ul>
    <form class="form-inline my-2 my-lg-0" method="POST" action="/logout">
        <input class="btn btn-outline-success my-2 my-sm-0" type="submit" value="Log Out">
    </form>
  </div>
</nav>

<div id="gameId">${gameId}</div>
<div id="gamePosition">${gamePosition}</div>

<div id="messageHolder">
	<p id="message"></p>
</div>
<div id="boardHolder">
	<table id="chessboard"></table>
</div>
<div id="bankHolder">
	<table id="bank"></table>
</div>

<div class="modal">
  <div class="modalContent">
  	<ul id="promotionMenu">
  		<li>Choose Promotion:</li>
  		<li id="rook" class="promoteOption">Rook</li>
  		<li id="queen" class="promoteOption">Queen</li>
  		<li id="knight" class="promoteOption">Knight</li>
  		<li id="bishop" class="promoteOption">Bishop</li>
  	</ul>
  </div>
</div>


<!-- <label class="switch">
	<input type="checkbox" id='moveToggle'>
	<span class="slider round"></span>
</label> -->

</#assign>
<#include "main.ftl">

<script>
$(document).ready(function() {
  setup_live_moves();
});
</script>
