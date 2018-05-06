<#assign content>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
	<a class="navbar-brand" href="/home">Chess32</a>
	<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarTogglerDemo02" aria-controls="navbarTogglerDemo02" aria-expanded="false" aria-label="Toggle navigation">
		<span class="navbar-toggler-icon"></span>
	</button>
	<div class="collapse navbar-collapse" id="navbarTogglerDemo02">
		<ul class="navbar-nav mr-auto mt-2 mt-lg-0">
			<li class="nav-item">
				<a class="nav-link active" href="/newaccount">Sign Up<span class="sr-only">(current)</span></a>
			</li>
			<li class="nav-item">
				<a class="nav-link" href="/login">Login</a>
			</li>
		</ul>
	</div>
</nav>

<div class="container">
	<div class="container">
		<div class="row">
			<div class="col-lg-12 text-center" style="margin-top: 4%">
				<p class="display-4" style="margin-bottom: 0px;margin-top: 10px">Welcome to Chess32!</p>
				<p class="lead" style="margin-top: 0px">The premiere site to play Bughouse.</p>
			</div>
			<div class="form-group col-lg-8 offset-lg-2" style="margin-top:2%">
				<form role="form" method="POST" action="/newaccountresults">
					<h1>Create an Account <small> ${message}</small></h1>
					<hr class="colorgraph">
					<div class="form-group">
						<input type="text" name="username" placeholder="username" class="form-control input-lg" tabindex="3">
					</div>
					<div class="row">
						<div class="col-xs-6 col-sm-6 col-md-6">
							<div class="form-group">
								<input type="password" name="password" id="password" class="form-control input-lg" placeholder="password" tabindex="5">
							</div>
						</div>
						<div class="col-xs-6 col-sm-6 col-md-6">
							<div class="form-group">
								<input type="password" name="password2" placeholder="confirm password" class="form-control input-lg" tabindex="6">
							</div>
						</div>
					</div>
					<hr class="colorgraph">
					<div class="row">
						<div class="col-xs-6 col-sm-6 col-md-6">
							<input id="submit" type="submit" value="Register" class="btn btn-success btn-block btn-lg">
						</div>
						<div class="col-xs-6 col-sm-6 col-md-6">
							<a href="/login" class="btn btn-primary btn-block btn-lg">Already have an account?</a>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>

<canvas id="canvas"></canvas>
</#assign>
<#include "main.ftl">
