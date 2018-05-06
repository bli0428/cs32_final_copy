<#assign content>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
	<a class="navbar-brand" href="/home">Chess32</a>
	<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarTogglerDemo02" aria-controls="navbarTogglerDemo02" aria-expanded="false" aria-label="Toggle navigation">
		<span class="navbar-toggler-icon"></span>
	</button>
	<div class="collapse navbar-collapse" id="navbarTogglerDemo02">
		<ul class="navbar-nav mr-auto mt-2 mt-lg-0">
			<li class="nav-item">
				<a class="nav-link" href="/home">Home</a>
			</li>
			<li class="nav-item">
				<a class="nav-link" href="/changeusername">Change Username</a>
			</li>
			<li class="nav-item">
				<a class="nav-link" href="/changepassword">Change Password</a>
			</li>
		</ul>
		<form class="form-inline my-2 my-lg-0" method="POST" action="/logout">
			<input class="btn btn-outline-success my-2 my-sm-0" type="submit" value="Log Out">
		</form>
	</div>
</nav>


<div class="container">
	<div class="row">
		<div class="col-lg-12 text-center" style="margin-top: 4%">
			<p class="display-4 saving" style="margin-bottom: 0px;margin-top: 10px">Waiting for game <span class="display-4" id="gameId">${gameId}</span> to start<span class="load">.</span><span class="load">.</span><span class="load">.</span></p>
		</div>
	</div>
	<div id='users' class="row align-items-center" style="margin-top: 4%">
		${users}
	</div>
</div>



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
