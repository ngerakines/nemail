<#setting url_escaping_charset='utf-8'>
<#macro admin_nav>
<div class="row">
	<div class="span12">
		<p>Admin page navigation.</p>
		<ul class="nav nav-pills">
			<li><a href="/admin/">Focus List</a></li>
			<li><a href="/admin/definitions">Definitions List</a></li>
			<li><a href="/admin/widgets">Widgets List</a></li>
		</ul>
	</div>
</div>
</#macro>
<#macro basic title="">
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<title>${title}</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta name="description" content="">
	<meta name="author" content="">

	<!-- Le styles -->
	<link href="/static/bootstrap/css/bootstrap.css" rel="stylesheet">
	<style type="text/css">
		body {
			padding-top: 20px;
			padding-bottom: 40px;
		}

			/* Custom container */
		.container-narrow {
			margin: 0 auto;
			max-width: 700px;
		}

		.container-narrow > hr {
			margin: 30px 0;
		}

			/* Main marketing message and sign up button */
		.jumbotron {
			margin: 60px 0;
			text-align: center;
		}

		.jumbotron h1 {
			font-size: 72px;
			line-height: 1;
		}

		.jumbotron .btn {
			font-size: 21px;
			padding: 14px 24px;
		}

			/* Supporting marketing content */
		.marketing {
			margin: 60px 0;
		}

		.marketing p + h4 {
			margin-top: 28px;
		}
	</style>
	<link href="/static/bootstrap/css/bootstrap-responsive.css" rel="stylesheet">

	<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
	<!--[if lt IE 9]>
	<script src="/static/bootstrap/js/html5shiv.js"></script>
	<![endif]-->
</head>

<body>

<div class="container-narrow">

	<div class="masthead">
		<ul class="nav nav-pills pull-right">
			<li class="active"><a href="/">Home</a></li>
			<li><a href="/about">About</a></li>
		</ul>
		<h3 class="muted">nemail</h3>
	</div>

	<hr>

	<#nested>

	<hr>

	<div class="footer">
		<p>&copy; Socklabs 2013</p>
	</div>

</div>
<!-- /container -->

<!-- Le javascript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="/static/js/jquery.js"></script>
<script src="/static/bootstrap/js/bootstrap.js"></script>
</body>
</html>
</#macro>
