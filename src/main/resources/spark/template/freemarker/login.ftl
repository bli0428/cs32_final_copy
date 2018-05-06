<#assign content>



<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
	<a class="navbar-brand" href="/home">Chess32</a>
	<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarTogglerDemo02" aria-controls="navbarTogglerDemo02" aria-expanded="false" aria-label="Toggle navigation">
		<span class="navbar-toggler-icon"></span>
	</button>
	<div class="collapse navbar-collapse" id="navbarTogglerDemo02">
		<ul class="navbar-nav mr-auto mt-2 mt-lg-0">
			<li class="nav-item">
				<a class="nav-link" href="/newaccount">Sign Up</a>
			</li>
			<li class="nav-item">
				<a class="nav-link active" href="/login">Login<span class="sr-only">(current)</span></a>
			</li>
		</ul>
	</div>
</nav>

<div class="container">
	<div class="row">
		<div class="col-lg-12 text-center" style="margin-top: 4%">
			<p class="display-4" style="margin-bottom: 0px;margin-top: 10px">Welcome to Chess32!</p>
			<p class="lead" style="margin-top: 0px">The premiere site to play Bughouse.</p>
		</div>
		<div class="form-group col-lg-8 offset-lg-2" style="margin-top: 2%">
			<form class="form-group" role="form" method="POST" action="/loginresults">
				<h1>Log In <small class="lead"> ${message}</small></h1>
				<hr class="colorgraph">
				<div class="form-group">
					<input type="text" name="username" placeholder="username" class="form-control input-lg"></input>
				</div>
				<div class="form-group">
					<input name="password" type="password" placeholder="password" class="form-control input-lg"></input>
				</div>
				<hr class="colorgraph">
				<div class="row">
					<div class="col-xs-6 col-sm-6 col-md-6">
						<input type="submit" class="btn btn-lg btn-success btn-block" value="Sign In">
					</div>
					<div class="col-xs-6 col-sm-6 col-md-6">
						<a href="/newaccount" class="btn btn-lg btn-primary btn-block">Create Account</a>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>
<!-- <a href="" class="btn btn-link pull-left">Forgot Password?</a> -->

</#assign>
<#include "main.ftl">
