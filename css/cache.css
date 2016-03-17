<?php 
	header('Content-type: text/css');
	
	
	$lastModified = filemtime(__FILE__);
	
	$etagFile = md5_file(__FILE__);
	
	$ifModifiedSince = (isset($_SERVER['HTTP_IF_MODIFIED_SINCE']) ? $_SERVER['HTTP_IF_MODIFIED_SINCE'] : false);
	
	$etagHeader = (isset($_SERVER['HTTP_IF_NONE_MATCH']) ? trim($_SERVER['HTTP_IF_NONE_MATCH']) : false);
	
	header("Last-Modified: ".gmdate("D, d M Y H:i:s", $lastModified)." GMT");
	
	header("Etag: $etagFile");
	
	header('Cache-Control: public');
	
	if(@strtotime($ifModifiedSince) == $lastModified || $etagHeader == $etagFile){
	   header("HTTP/1.1 304 Not Modified");
	   exit;
	}
?>


