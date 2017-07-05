<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Data Factory (htc)</title>
<link rel="shortcut icon" href="../css/images/favicon.ico" />
<script type="text/javascript" src="../js/common.js"></script>
<script type="text/javascript" src="../js/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="../js/sweetalert.min.js"></script>
<link rel="stylesheet" type="text/css" href="../css/style.css">
<link rel="stylesheet" type="text/css" href="../css/sweetalert.css">
<link rel="stylesheet" href="../css/style.css">
<script type="text/javascript" src="../js/dataGenerator.js"></script>
<style type="text/css">
.table tr td:first-child {
	text-align: right;
	padding-right: 2%;
}
.table tr td:last-child {
	text-align: left;
	padding-left: 2%;
}
</style>
</head>
<body>
<div style="float: left;width: 44%;margin-left: 5%;">
	<div  class="table">
	<span style="font-family: sans-serif;"><b><u>Data Factory</u></b><i>(Enter weight for the following Holon Object Types in percentage)</i></span>
			<table border="1">
				<tbody id="holonObjectTypeListFactory"></tbody>
			</table>
			<input type="hidden" id="totalHolonObjectTypes" value="" />
			<input type="hidden" id="holonObjectTypesIDs" value="" />
			<input type="hidden" id="holonObjectTypesPriorities" value="" />
			<table border="2">
				<tr>
					<td colspan="2" style="text-align: center;"><input class="button" type="button"
						style="cursor: pointer;" value="Send to data generator"
						onclick="factoryDataGenerator()" /></td>
				</tr>
			</table>
	</div>
</div>
<div style="float: right;width: 44%;margin-right: 5%;">
	<div class="table">
	<span style="font-family: sans-serif;"><i>(Each Holon Object will have a maximum of 10 randomly selected Holon Elements)</i></span>
			<table border="1">
				<tbody id="holonElementTypeListFactory"></tbody>
			</table>
	</div>
</div>
<div>
	<table>
		<tbody id="dataFactoryOutput"></tbody>
	</table>
</div>
<div id="spinner" class="spinner" style="display:none;">
   	<img id="img-spinner" src="../css/images/spinner.gif" alt="Loading"/>
</div>
</body>
</html>