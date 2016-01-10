$(function () {
  
  $('.header').stickyNavbar({
    activeClass: "active",
    sectionSelector: "scrollto",
    navOffset: 0,
    animDuration: 300,
    startAt: 0, /* Stick the menu at XXXpx from the top*/
    easing: "easeInQuad",
    bottomAnimation: true,
    jqueryEffects: false,
    animateCSS: true,
    animateCSSRepeat: false,
    selector: "a",
    jqueryAnim: "fadeInDown", /* jQuery effects: fadeIn, show, slideDown*/
    mobile: false
  });
  
  $('.product_header').stickyNavbar({
    activeClass: "active",
    sectionSelector: "scrollto",
    navOffset: 0,
    animDuration: 300,
    startAt: 70, /* Stick the menu at XXXpx from the top*/
    easing: "easeInQuad",
    bottomAnimation: true,
    jqueryEffects: false,
    animateCSS: true,
    animateCSSRepeat: false,
    selector: "a",
    jqueryAnim: "fadeInDown", /* jQuery effects: fadeIn, show, slideDown*/
    mobile: false
  });
  
  $.scrollUp({
    scrollName: 'scrollUp', /* Element ID*/
    topDistance: '300', /* Distance from top before showing element (px)*/
    topSpeed: 300, /* Speed back to top (ms)*/
    animation: 'fade', /* Fade, slide, none*/
    animationInSpeed: 200, /* Animation in speed (ms)*/
    animationOutSpeed: 200, /* Animation out speed (ms)*/
    activeOverlay: true, /* Set CSS color to display scrollUp active point, e.g '#00FFFF'*/
  });


	$('#layerslider_29').layerSlider({
	 
		startInViewport: false, 
		pauseOnHover: false, 
		forceLoopNum: false, 
		autoPlayVideos: false, 
		skinsPath: 'layer-slider/', 
		skin: 'v5', 
		thumbnailNavigation: 'hover'
	 
	});
	
	$('.button-codebox').click(function() {
      var clickedButton = $(this).data('name');
      $(this).parent().find('.button-codebox').removeClass('selected'); 
      $(this).addClass('selected'); 
      
      var parent = $(this).parents('.codebox');
      var child = parent.find('.prettyprinted');
       
      $(child).each(function() {
		  if($(this).data('name') == clickedButton) {
		  	this.style.display = 'block';	
		  } else {
		  	this.style.display = 'none';
		  }
	  }); 
	   
	});
		
	$('.flexslider').flexslider({
        animation: "slide",
        animationLoop: false,
        itemWidth: 210,
        itemMargin: 5,
        pausePlay: false,
        start: function(slider){
          $('body').removeClass('loading');
        }
  	});  
});

/*! WOW - v0.1.5 - 2014-03-05
* Copyright (c) 2014 Matthieu Aussaguel; Licensed MIT */(function(){var a,b=function(a,b){return function(){return a.apply(b,arguments)}};a=function(){function a(){}return a.prototype.extend=function(a,b){var c,d;for(c in a)d=a[c],null!=d&&(b[c]=d);return b},a.prototype.isMobile=function(a){return/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(a)},a}(),this.WOW=function(){function c(a){null==a&&(a={}),this.scrollCallback=b(this.scrollCallback,this),this.scrollHandler=b(this.scrollHandler,this),this.start=b(this.start,this),this.scrolled=!0,this.config=this.util().extend(a,this.defaults)}return c.prototype.defaults={boxClass:"wow",animateClass:"animated",offset:0,mobile:!0},c.prototype.init=function(){var a;return this.element=window.document.documentElement,"interactive"===(a=document.readyState)||"complete"===a?this.start():document.addEventListener("DOMContentLoaded",this.start)},c.prototype.start=function(){var a,b,c,d;if(this.boxes=this.element.getElementsByClassName(this.config.boxClass),this.boxes.length){if(this.disabled())return this.resetStyle();for(d=this.boxes,b=0,c=d.length;c>b;b++)a=d[b],this.applyStyle(a,!0);return window.addEventListener("scroll",this.scrollHandler,!1),window.addEventListener("resize",this.scrollHandler,!1),this.interval=setInterval(this.scrollCallback,50)}},c.prototype.stop=function(){return window.removeEventListener("scroll",this.scrollHandler,!1),window.removeEventListener("resize",this.scrollHandler,!1),null!=this.interval?clearInterval(this.interval):void 0},c.prototype.show=function(a){return this.applyStyle(a),a.className=""+a.className+" "+this.config.animateClass},c.prototype.applyStyle=function(a,b){var c,d,e;return d=a.getAttribute("data-wow-duration"),c=a.getAttribute("data-wow-delay"),e=a.getAttribute("data-wow-iteration"),a.setAttribute("style",this.customStyle(b,d,c,e))},c.prototype.resetStyle=function(){var a,b,c,d,e;for(d=this.boxes,e=[],b=0,c=d.length;c>b;b++)a=d[b],e.push(a.setAttribute("style","visibility: visible;"));return e},c.prototype.customStyle=function(a,b,c,d){var e;return e=a?"visibility: hidden; -webkit-animation-name: none; -moz-animation-name: none; animation-name: none;":"visibility: visible;",b&&(e+="-webkit-animation-duration: "+b+"; -moz-animation-duration: "+b+"; animation-duration: "+b+";"),c&&(e+="-webkit-animation-delay: "+c+"; -moz-animation-delay: "+c+"; animation-delay: "+c+";"),d&&(e+="-webkit-animation-iteration-count: "+d+"; -moz-animation-iteration-count: "+d+"; animation-iteration-count: "+d+";"),e},c.prototype.scrollHandler=function(){return this.scrolled=!0},c.prototype.scrollCallback=function(){var a;return this.scrolled&&(this.scrolled=!1,this.boxes=function(){var b,c,d,e;for(d=this.boxes,e=[],b=0,c=d.length;c>b;b++)a=d[b],a&&(this.isVisible(a)?this.show(a):e.push(a));return e}.call(this),!this.boxes.length)?this.stop():void 0},c.prototype.offsetTop=function(a){var b;for(b=a.offsetTop;a=a.offsetParent;)b+=a.offsetTop;return b},c.prototype.isVisible=function(a){var b,c,d,e,f;return c=a.getAttribute("data-wow-offset")||this.config.offset,f=window.pageYOffset,e=f+this.element.clientHeight-c,d=this.offsetTop(a),b=d+a.clientHeight,e>=d&&b>=f},c.prototype.util=function(){return this._util||(this._util=new a)},c.prototype.disabled=function(){return!this.config.mobile&&this.util().isMobile(navigator.userAgent)},c}()}).call(this);

new WOW().init();