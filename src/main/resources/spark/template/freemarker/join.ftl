<#assign content>

<h1> Joining game <div id="gameId">${gameId}</div> </h1>
<div id="users">
${users}
</div>
<button onclick="switchTeam()">Switch team</button>

</#assign>
<#include "main.ftl">
<script>
$(document).ready(function() {
  setupMenu();
});
</script>
