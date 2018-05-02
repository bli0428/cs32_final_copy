<#assign content>

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
  		<li class="promoteOption">Rook</li>
  		<li class="promoteOption">Queen</li>
  		<li class="promoteOption">Knight</li>
  		<li class="promoteOption">Bishop</li>
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