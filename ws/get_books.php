<?php

	header("Content-Type:application/xml");
	header("HTTP/1.1", 200, "SUCCESS");
	
	$response = "<books><book><name>C</name><description>C Description</description><author>C Author</author><link>C Link</link></book><book><name>C Plus</name><description>C Plus Description</description><author>C Plus Author</author><link>C Plus Link</link></book><book><name>C Sharp</name><description>C Sharp Description</description><author>C Sharp Author</author><link>C Sharp Link</link></book><book><name>Java</name><description>Java Description</description><author>Java Author</author><link>Java Link</link></book><book><name>JavaScript</name><description>JavaScript Description</description><author>JavaScript Author</author><link>JavaScript Link</link></book><book><name>Swift</name><description>Swift Description</description><author>Swift Author</author><link>Swift Link</link></book><book><name>Objective C</name><description>Objective C Description</description><author>Objective C Author</author><link>Objective C Link</link></book></books>";


	echo $response;
?>