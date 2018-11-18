<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.lama.polyshare.ServletYo" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="To upload files" content="Upload files">
  <meta name="LAMA" content="Polyshare">
  <title>Upload your files to polyshare !</title>
  <style>
	form.form-example {
	    display: table;
	}

	div.form-example {
	    display: table-row;
	}

	label, input {
	    display: table-cell;
	    margin-bottom: 10px;
	}

	label {
	    padding-right: 10px;
	}
  </style>
</head>
<body>
	<h1>Enter your mail in order to upload your file to Polyshare !</h1>


<form method="POST" action="https://poly-share.appspot.com/Upload" enctype="multipart/form-data">
  <div class="form-example">
		<input type="file" id="file" name="file"/>​
    <label for="email">Enter your email: </label>
    <input type="mail" name="mail" id="mail" required>
  </div>
   <div class="form-example">
    <input type="submit" value="Upload!">
  </div> 
</form>

</body>
</html>