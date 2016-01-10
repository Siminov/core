/*"Edit in JS Bin" button setup*/
function jsbinShowEdit(options) {
  'use strict';
  if (window.location.hash === '#noedit') {return;}

  var moveTimer, over,
  doc = document,
  aEL = 'addEventListener',
  path = options.root + window.location.pathname,
  style = doc.createElement('link'),
  btn = doc.createElement('a');


  /* show / hide button:*/
  btn.onmouseover = btn.onmouseout = function() {
    over = !over;
    (over ? show : hide)();
  };

  function show() {
    clearTimeout(moveTimer);
    btn.style.top = '0';
    moveTimer = setTimeout(hide, 2000);
  }

  function hide() {
    if (!over) { btn.style.top = '-60px'; }
  }

  show();
  if (aEL in doc) {doc[aEL]('mousemove', show, false);}
  else {doc.attachEvent('mousemove', show);}
}



jsbinShowEdit && jsbinShowEdit({"static":"","root":""});
