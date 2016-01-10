

function showMenu() {

	if ($('#show-menu-button').hasClass('active')) {
		var menu = document.getElementById('menu');
	
		$('#header').addClass('fake_header');
        $('#header').slideDown('fast');

        $('#show-menu-button').removeClass('active');
    } else {
		var menu = document.getElementById('menu');

    	$('#header').removeClass('fake_header');
        $('#header').slideUp('fast');

        $('#show-menu-button').addClass('active');
    }

    return false;  
}

function showDevelopMenuItemsInMobile() {

	if($('#header li.has-submenu>.menu-item-wrap.develop').next().is('ul')) {
    	$('#header li.has-submenu>.menu-item-wrap.develop').find('a').removeAttr('href');
    }
    
    if($('#show-menu-button').is(':visible')) {
    	if ($('#header li.has-submenu>.menu-item-wrap.develop').next().is('ul')) {
    	
        	if ($('#header li.has-submenu>.menu-item-wrap.develop').next().is(':visible')) {
        	
               $('#header li.has-submenu>.menu-item-wrap.develop').next().removeClass('drop_active');
           	} else {
                $('#header li.has-submenu>.menu-item-wrap').next().removeClass('drop_active');
                $('#header li.has-submenu>.menu-item-wrap.develop').next().addClass('drop_active');
            }

        }
    }
}

function showCommunityMenuItemsInMobile() {

	 if ($('#header li.has-submenu>.menu-item-wrap.community').next().is('ul')) {
        $('#header li.has-submenu>.menu-item-wrap.community').find('a').removeAttr('href');
     }
    
    if ($('#show-menu-button').is(':visible')) {
    	if ($('#header li.has-submenu>.menu-item-wrap.community').next().is('ul')) {
        	if ($('#header li.has-submenu>.menu-item-wrap.community').next().is(':visible')) {
        	
               $('#header li.has-submenu>.menu-item-wrap.community').next().removeClass('drop_active');
           	} else {
                $('#header li.has-submenu>.menu-item-wrap').next().removeClass('drop_active');
                $('#header li.has-submenu>.menu-item-wrap.community').next().addClass('drop_active');
            }

        }
    }
}

function showAboutMenuItemsInMobile() {

	 if ($('#header li.has-submenu>.menu-item-wrap.about').next().is('ul')) {
        $('#header li.has-submenu>.menu-item-wrap.about').find('a').removeAttr('href');
     }
    
    if ($('#show-menu-button').is(':visible')) {
    	if ($('#header li.has-submenu>.menu-item-wrap.about').next().is('ul')) {
        	if ($('#header li.has-submenu>.menu-item-wrap.about').next().is(':visible')) {
        	
               $('#header li.has-submenu>.menu-item-wrap.about').next().removeClass('drop_active');
           	} else {
                $('#header li.has-submenu>.menu-item-wrap').next().removeClass('drop_active');
                $('#header li.has-submenu>.menu-item-wrap.about').next().addClass('drop_active');
            }

        }
    }
}

function showTopLoginMenu() {

	if ($('#open-top-panel').hasClass('active')) {
        $('.top-panel-inner').slideUp('slow');

        $('#open-top-panel').removeClass('active');
    } else {
        $('.top-panel-inner').slideDown('slow');

        $('#open-top-panel').addClass('active');
    }

    return false;  
}

function showCoreProduct() {
	
	hideConnectProduct();
	hideWebProduct();
	hideStudioProduct();

	var coreProduct = document.getElementById('product-1');
	coreProduct.style.backgroundColor = "#f6f6f6";
	
	var coreProductDesc = document.getElementById('product-1-desc');
	coreProductDesc.style.display = "block";
}

function hideCoreProduct() {
	
	hideConnectProduct();
	hideWebProduct();
	hideStudioProduct();
	
	var coreProduct = document.getElementById('product-1');
	coreProduct.style.backgroundColor = "#FFF";
	
	var coreProduct = document.getElementById('product-1-desc');
	coreProduct.style.display = "none";
}

function showConnectProduct() {

	hideCoreProduct();
	hideWebProduct();
	hideStudioProduct();
		
	var connectProduct = document.getElementById('product-2');
	connectProduct.style.backgroundColor = "#f6f6f6";
}

function hideConnectProduct() {
	
	var connectProduct = document.getElementById('product-2');
	connectProduct.style.backgroundColor = "#FFF";
}

function showWebProduct() {
	
	hideCoreProduct();
	hideConnectProduct();
	hideStudioProduct();
	
	var webProduct = document.getElementById('product-3');
	webProduct.style.backgroundColor = "#f6f6f6";
}

function hideWebProduct() {
	
	var webProduct = document.getElementById('product-3');
	webProduct.style.backgroundColor = "#FFF";
}

function showStudioProduct() {
	
	hideCoreProduct();
	hideConnectProduct();
	hideWebProduct();

	var studioProduct = document.getElementById('product-4');
	studioProduct.style.backgroundColor = "#f6f6f6";
}

function hideStudioProduct() {
	
	var studioProduct = document.getElementById('product-4');
	studioProduct.style.backgroundColor = "#FFF";
}


  