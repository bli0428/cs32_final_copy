<#assign content>

<h1> Joining game <span id="gameId">${gameId}</span> </h1>
<div id="users">
${users}
</div>
<button onclick="switchTeam()">Switch team</button>
<button onclick="leaveGame()">Leave game</button>

</#assign>
<#include "main.ftl">
<script>
$(document).ready(function() {
  setupMenu();
});
</script>
