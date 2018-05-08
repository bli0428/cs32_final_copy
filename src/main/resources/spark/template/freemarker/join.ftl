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
	<div class="row" style="margin-top: 5%">
		<div class="col-sm-12 offset-sm-5">
			<button class="btn btn-danger btn-lg" onclick="leaveGame()">Leave Game</button>
		</div>
	</div>
</div>


<div class="modal fade" id="AIDifficulty">
    <div class="modal-dialog modal-sm modal-dialog-centered">
      <div class="modal-content">
        <!-- Modal Header -->
        <div class="modal-header">
          <h4 class="modal-title" style="margin-top: 0">Choose AI Difficulty:</h4>
          <button type="button" class="close" data-dismiss="modal">&times;</button>
        </div>
        <!-- Modal body -->
        <div class="modal-body">
          <button id="0" type="button" class="btn btn-outline-primary">Easy</button>
          <button id="1" type="button" class="btn btn-outline-primary">Hard</button>
        </div>
      </div>
    </div>
  </div>






</#assign>
<#include "main.ftl">

<script>
	$(document).ready(function() {
		setupMenu();
	});
</script>
