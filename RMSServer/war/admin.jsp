<!doctype html>
<!-- The DOCTYPE declaration above will set the     -->
<!-- browser's rendering engine into                -->
<!-- "Standards Mode". Replacing this declaration   -->
<!-- with a "Quirks Mode" doctype is not supported. -->

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.googlecode.objectify.Objectify"%>
<%@ page import="com.googlecode.objectify.ObjectifyService" %>
<%@ page import="com.gmail.utexas.rmsystem.models.Biometrics" %>
<%@ page import="com.gmail.utexas.rmsystem.models.Device" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">

<!--                                                               -->
<!-- Consider inlining CSS to reduce the number of requested files -->
<!--                                                               -->
<link type="text/css" rel="stylesheet" href="RMSServer.css">

<!--                                           -->
<!-- Any title is fine                         -->
<!--                                           -->
<title>RMS Admin Console</title>

<!--                                           -->
<!-- This script loads your compiled module.   -->
<!-- If you add any GWT meta tags, they must   -->
<!-- be added before this line.                -->
<!--                                           -->
<script type="text/javascript" language="javascript"
	src="rmsserver/rmsserver.nocache.js"></script>
</head>

<!--                                           -->
<!-- The body can have arbitrary html, or      -->
<!-- you can leave the body empty if you want  -->
<!-- to create a completely dynamic UI.        -->
<!--                                           -->
<body>

	<!-- OPTIONAL: include this if you want history support -->
	<iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1'
		style="position: absolute; width: 0; height: 0; border: 0"></iframe>

	<!-- RECOMMENDED if your web app will not function without JavaScript enabled -->
	<noscript>
		<div
			style="width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif">
			Your web browser must have JavaScript enabled in order for this
			application to display correctly.</div>
	</noscript>

	<script src="http://code.jquery.com/jquery-1.10.1.min.js"></script>
	<script src="js/bootstrap.js"></script>


	<div class="container" align="center">	
			<h1>RMS Server Admin Console</h1>
			
			<h2>Alerts</h2>
			<a href="/alert?type=sleepwalking&app=g3" class="btn btn-lg btn-default">Sleepwalking</a>
			<a href="/alert?type=roaming&app=g3" class="btn btn-lg btn-default">Roaming</a>
			<a href="/alert?type=lowbattery&app=g3" class="btn btn-lg btn-default">Low Battery</a>
			<a href="/alert?type=headset&app=g3" class="btn btn-lg btn-default">Poor Headset Connection</a>
		

		<% 
	    ObjectifyService.register(Biometrics.class);
		Biometrics bio = ObjectifyService.ofy().load().type(Biometrics.class).id("RMShardware").get();
		
		String status = bio.getStatus();
		pageContext.setAttribute("asleep", "");
		pageContext.setAttribute("awake", "");
		pageContext.setAttribute("badinput", "");
		pageContext.setAttribute("falsepositive", "");
	    pageContext.setAttribute(status, "btn-success");
		%>
		
		<h2>Biometrics Inputs</h2>
		<table align="center">
			<tr>
				<td style="font-weight: bold; padding: 0 15px 0 15px"><a
					href="/biometrics?type=asleep" class="btn btn-lg btn-default ${fn:escapeXml(asleep)}">Asleep</a></td>
				<td style="font-weight: bold; padding: 0 15px 0 15px"><a
					href="/biometrics?type=awake" class="btn btn-lg btn-default ${fn:escapeXml(awake)}">Awake</a></td>
				<td style="font-weight: bold; padding: 0 15px 0 15px"><a
					href="/biometrics?type=badinput" class="btn btn-lg btn-default ${fn:escapeXml(badinput)}">Bad Input</a></td>
				<td style="font-weight: bold; padding: 0 15px 0 15px"><a
					href="/biometrics?type=falsepositive" class="btn btn-lg btn-default ${fn:escapeXml(falsepositive)}">False Positive</a></td>
			</tr>
		</table>
		
		
		<% 
	    ObjectifyService.register(Device.class);
		Device device = ObjectifyService.ofy().load().type(Device.class).id("RMShardware").get();
		boolean deviceStatus = device.getStatus();
		boolean bioStatus = device.getBioStatus();
		if(deviceStatus){
			pageContext.setAttribute("devicetext", "Device Active");
			pageContext.setAttribute("devicestyle", "btn-success");
		} else {
			pageContext.setAttribute("devicetext", "Device Inactive");
			pageContext.setAttribute("devicestyle", "btn-failure");
		}
		

		if(bioStatus){
			pageContext.setAttribute("biotext", "Biometrics Active");
			pageContext.setAttribute("biostyle", "btn-success");
			pageContext.setAttribute("biourl", "false");
		} else {
			pageContext.setAttribute("biotext", "Biometrics Inactive");
			pageContext.setAttribute("biostyle", "btn-failure");
			pageContext.setAttribute("biourl", "true");
		}
		%>
		
		<h2>Device Status</h2>
		<a href="/manual_status" class="btn btn-lg btn-default ${fn:escapeXml(devicestyle)}">${fn:escapeXml(devicetext)}</a>
		<a href="/test?status=${fn:escapeXml(biourl)}" class="btn btn-lg btn-default ${fn:escapeXml(biostyle)}">${fn:escapeXml(biotext)}</a>
		
		
	</div>
	
	

</body>
</html>
