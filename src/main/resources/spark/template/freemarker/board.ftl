<#assign content>

<div id="gameId">${gameId}</div>

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
