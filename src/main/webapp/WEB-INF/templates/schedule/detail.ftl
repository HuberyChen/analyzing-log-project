<!DOCTYPE HTML>
<html lang="en-US">
<head>
    <title>Quidsi, Inc. |Log Analyzing</title>
    <meta charset="utf-8"/>
    <@css href="home.css" rel="stylesheet" type="text/css"/>
    <@js src="jquery.min.js"/>
</head>
<body>

<nav class="top-bar">
    <ul class="title-area">
        <li class="name">
            <h1><a href="<@url value='/home'/>/"><span style="color:White;">Analyzing Log</span></a></h1>
        </li>
    </ul>
    <ul class="right">
        <li class="has-form"><a class="small radius button" href="<@url value='/signOut'/>">Logout</a></li>
    </ul>
</nav>

<div class="row">
    <div class="large-12 columns">
        <a class="small radius button" style="margin-left: 100px;" id="scan" href="<@url value='/schedule/details'/>">Back</a>
        <fieldset style="width: 700px; margin-left: 100px;margin-top: 0px;">
            <textarea readonly style="width: 700px; height: 350px;">${messages}</textarea>
        </fieldset>
    </div>
</div>
</body>
</html>