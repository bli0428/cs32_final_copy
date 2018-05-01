<#assign content>

<div id="messageHolder">
	<p id="message"></p>
</div>
<div id="main">
	<div id="boardHolder">
		<table id="chessboard"></table>
	</div>
	<div id="bankHolder">
		<table id="bank"></table>
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