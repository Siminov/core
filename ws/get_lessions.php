<?php

	header("Content-Type:application/xml");
	header("HTTP/1.1", 200, "SUCCESS");
	
	$brandName = $_GET['name'];
	$response;
	
	if($brandName == "C") {	
		$response = "<lessions><lession><name>C First Lession</name><description>C First Lession Description</description><link>C First Lession Link</link></lession><lession><name>C Second Lession</name><description>C Second Lession Description</description><link>C Second Lession Link</link></lession></lessions>";
	} else if($brandName == "C++") {
		$response = "<lessions><lession><name>C++ First Lession</name><description>C++ First Lession Description</description><link>C++ First Lession Link</link></lession><lession><name>C++ Second Lession</name><description>C++ Second Lession Description</description><link>C++ Second Lession Link</link></lession></lessions>";
	} else if($brandName == "C#") {	
		$response = "<lessions><lession><name>C# First Lession</name><description>C# First Lession Description</description><link>C# First Lession Link</link></lession><lession><name>C# Second Lession</name><description>C# Second Lession Description</description><link>C# Second Lession Link</link></lession></lessions>";
	} else if($brandName == "Java") {
		$response = "<lessions><lession><name>Java First Lession</name><description>Java First Lession Description</description><link>Java First Lession Link</link></lession><lession><name>Java Second Lession</name><description>Java Second Lession Description</description><link>Java Second Lession Link</link></lession></lessions>";
	} else if($brandName == "JavaScript") {
		$response = "<lessions><lession><name>JavaScript First Lession</name><description>JavaScript First Lession Description</description><link>JavaScript First Lession Link</link></lession><lession><name>JavaScript Second Lession</name><description>JavaScript Second Lession Description</description><link>JavaScript Second Lession Link</link></lession></lessions>";
	} else if($brandName == "Objective C") {
		$response = "<lessions><lession><name>Objective C First Lession</name><description>Objective C First Lession Description</description><link>Objective C First Lession Link</link></lession><lession><name>Objective C Second Lession</name><description>Objective C Second Lession Description</description><link>Objective C Second Lession Link</link></lession></lessions>";
	} else if($brandName == "Swift") {
		$response = "<lessions><lession><name>Swift First Lession</name><description>Swift First Lession Description</description><link>Swift First Lession Link</link></lession><lession><name>Swift Second Lession</name><description>Swift Second Lession Description</description><link>Swift Second Lession Link</link></lession></lessions>";
	}

	echo $response;
?>