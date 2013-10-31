<!DOCTYPE HTML>
<html lang="en-US">
<head>
    <title>Analyzing Log</title>
    <meta charset="utf-8"/>
    <link rel="shortcut icon" href="<@url value='/dstatic/images/favicon.ico'/>" type="image/vnd.microsoft.icon"/>
    <!-- Le HTML5 shim, for IE6-8 support of HTML elements -->
    <!--[if lt IE 9]>
    <@js src="html5.js"/>
    <![endif]-->
    <!--[if lt IE 9]>
    <@js src="json2.js"/>
    <![endif]-->
   	<@css href="foundation.min.css" rel="stylesheet" type="text/css"/> 
   	<@js src="jquery.min.js,common.js"/>
    <script type="text/javascript">
        golbalRootUrl = "<@url value='/' />";
        $(document).ready(function(){
        	if($("#getMsgType").val() == "error"){
        		$("<li><label class=\"error\" for=\"name\" generated=\"true\" style=\"display: block;\">Invalid Username or Password. Please Try Again.</label></li>").appendTo($("#info"))
        	}
        	$("#site-list > a").mouseover(function(){ 
				$("#site-list ul").show(); 
			}) 
			$("#site-list ul li a").click(function(){ 
				$("#site-list ul").hide(); 
			});
        });
    </script>
</head>
<body>

<!-- Header and Nav -->
<nav class="top-bar">
	<ul class="title-area">
		<!-- Title Area -->
		<li class="name">
			<h1><span style="color:White;">Analyzing Log</span></h1>
		</li>
	</ul>
	<#if msgType??>
        <input type="hidden" id="getMsgType" value="${msgType}"/>
    </#if>
</nav>
<!-- End Header and Nav --> 

<!-- Main Section -->
<section class="main">

    <div class="row">
            
        <div class="large-4 large-centered column">
			<form id="loginForm" action="<@url value='/login'/>" class="form-signin" method="post">
				<div>
					<h2 class="form-signin-heading">Please sign in</h2>
				</div>
				<div id="info">
				</div>
				<input type="text" name="userName" id="userName" class="input-block-level" placeholder="User name" autofocus>
				<input type="password" name="password" id="password" class="input-block-level" placeholder="Password">
				<button id="submitId" class="btn btn-large btn-primary btn-block" type="submit">Sign in</button>
			</form>
		</div>
        
	</div>	
	
</section>
<!-- End Main Section -->
</body>
</html>