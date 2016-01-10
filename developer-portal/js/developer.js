

function onVersionChanged(version) {

	var core1 = 'core-1.0';
	var core2 = 'core-2.0';
	
	var connect1 = 'connect-1.0';
	var connect2 = 'connect-2.0';
	
	var hybrid1 = 'hybrid-1.0';
	var hybrid2 = 'hybrid-2.0';
	
	var selectedVersion = version.value;
	
	if(selectedVersion == core1) {
		window.location = '../core/home-v1.0.html';
	} else if(selectedVersion == core2) {
		window.location = '../core/home.html';
	}
	
	else if(selectedVersion == connect1) {
		window.location = '../core/home-v1.0.html';
	} else if(selectedVersion == connect2) {
		window.location = '../core/home.html';
	}
	
	else if(selectedVersion == hybrid1) {
		window.location = '../core/home-v1.0.html';
	} else if(selectedVersion == hybrid2) {
		window.location = '../core/home.html';
	}
}


function loadWiki(title, subMenus, links) {
	
	var docsContent = document.getElementById('docs-content');
	docsContent.innerHTML = '';
	docsContent.innerHTML += '<h1>' + title + '</h1>';
	docsContent.innerHTML += '<ul>';
	
	if(subMenus != undefined) {
		
		for(var i = 0;i < subMenus.length;i++) {
			docsContent.innerHTML += "<li><a href='#" + subMenus[i] + "'>" + subMenus[i] + "</a></li>";
		}
	}

	docsContent.innerHTML += '</ul>';
	docsContent.innerHTML += '<p class=seprator>';
	
	$("#docs-content").LoadingOverlay("show");
	
	if(links != undefined && links != null && links.length > 0) {
		getWiki(links[0], 0);
	}
	
	function getWiki(link, index) {
	
		var xmlhttp = new XMLHttpRequest();
		xmlhttp.onreadystatechange = function() {
		  
		  	if (xmlhttp.readyState==4 && xmlhttp.status==200) {
		  		$("#docs-content").LoadingOverlay("hide");
				docsContent.innerHTML += Markdown.toHTML(xmlhttp.responseText);
				
				if(links.length > ++index) {
					getWiki(links[index], index++);
				} 
			}
		}  
		
		xmlhttp.open('GET', link, true);
		xmlhttp.send();
	}
}

function showDocsMenu() {
	
	var documentation = document.getElementById('documentation');
	if ($('#documentation').hasClass('nav-expanded')) {
		
		$('#documentation').removeClass('nav-expanded');
    	$('.docs-show').html('Navigate');
		
    } else {
    	$('#documentation').addClass('nav-expanded');
		$('.docs-show').html('Close');
    }

    return false;  
}

function showDocumentation(docs) {
	
	hideSample();
	hideApi();
	
	$(docs).addClass('tab-background');
	
	var docTab = document.getElementById('docTab');
	docTab.style.display = 'block';
	
}

function hideDocumentation() {
	$('#docs>ul li>a').removeClass('tab-background');
	
	var docTab = document.getElementById('docTab');
	docTab.style.display = 'none';
}

function showSample(sample) {
	
	hideDocumentation();
	hideApi();
	
	$(sample).addClass('tab-background');
	
	var sampleTab = document.getElementById('sampleTab');
	sampleTab.style.display = 'block';
}

function hideSample() {
	
	$('#docs>ul li>a').removeClass('tab-background');
	
	var sampleTab = document.getElementById('sampleTab');
	sampleTab.style.display = 'none';
}

function showApi(api) {
	
	hideDocumentation();
	hideSample();
	
	$(api).addClass('tab-background');
	
	var apiTab = document.getElementById('apiTab');
	apiTab.style.display = 'block';
}

function hideApi() {
	
	$('#docs>ul li>a').removeClass('tab-background');
	
	var apiTab = document.getElementById('apiTab');
	apiTab.style.display = 'none';
}