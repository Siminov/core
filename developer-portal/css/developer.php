@import url('../../css/home.php');



#developer-header {
	background: #4CA5B5;
	height: 80px;
	overflow: hidden;
	position: relative;
	text-align: left;
	width: 100%;
	z-index: 20;
}

.boxed {
	margin: 0 auto;
	padding: 0 1em;
}

.seprator {
	border-bottom: 1px solid #eee;
	margin-bottom: .8em;
	padding-top: 1.1em;
}

.para {
	border-top: 1px solid #eee;
	font-size: 2rem;
	margin-bottom: .8em;
	margin-top: .625em;
	padding-top: 1.1em;
}

a {
    text-decoration: none !important;
}

.image {
	margin-left: auto;
	margin-right: auto;
	display: block;
	padding: 20px;
}

#tagline {
	float: left;
	margin: 0 auto;
	padding-top: 22px;
	position: relative;
	z-index: 25;
}

#tagline h1 {
	color: #fff;
	font-weight: 400!important;
	font-size: 32px;
	margin-bottom: 5px;
	text-transform: uppercase;
	text-shadow: 0 1px 1px rgba(0,0,0,.2);
}

#version {
	float: right;
	padding-top: 33px;
	position: relative;
	z-index: 999;
}

#version h1 {
	font-size: 1.125em;
	color: #fff;
	opacity: .6;
}

#version h1:hover {
	opacity: 1;
}

.product_name span {
	border: none
}

.primary-nav-ul {
	float: right;
	margin-right: 12px;
	list-style: none;
}

#global-footer .primary-nav-ul {
	margin-right: 10px;
}

.primary-nav-ul>li {
	padding: 25px 0;
	width: auto;
	float: left;
	position: relative;
	line-height: 1.5;
}

.primary-nav-ul>li>a {
	border-bottom: 0;
	font-size: 12px;
	padding: 0 14px 0 20px;
	width: auto;
	color: #aaa;
	display: block;
	font-weight: 600;
	letter-spacing: 1px;
	border-right: 1px solid #ccc;	
}

.primary-nav-ul>li:last-child a{
	border-right: none;
}

.primary-nav-ul li ul {
	position: absolute;
	border: 1px solid #eaeced;
	margin-top: 12px;
	width: 225px;
	left: -76px;
	overflow: visible !important;
	box-shadow: 4px 4px 0 rgba(0, 0, 0, 0.1);
	-webkit-box-shadow: 4px 4px 0 rgba(0, 0, 0, 0.1);
	-moz-box-shadow: 4px 4px 0 rgba(0, 0, 0, 0.1);
	background: #ffffff;
	background: rgba(255,255,255,.95);
	opacity: 0;
	display: none;
	border-radius: 3px;
	z-index: 9999;
	-moz-transition: opacity .2s ease-in-out;
	-webkit-transition: opacity .2s ease-in-out;
	-o-transition: opacity .2s ease-in-out;
	transition: opacity .2s ease-in-out;
	overflow: visible !important;
	list-style: none;
}

.primary-nav-ul li:hover ul {
	opacity: 1;
	display: block;
}

.primary-nav-ul li ul>li:first-child {
	padding-top: 15px;
}

.primary-nav-ul li ul>li {
	position: relative;
	padding-bottom: 5px;
	text-align: -webkit-match-parent;
}

.primary-nav-ul li ul>li a:hover {
	background: #29beef !important;
}

.primary-nav-ul li ul>li:first-child:before {
	content: "";
	width: 0;
	height: 0;
	border-left: 10px solid transparent;
	border-right: 10px solid transparent;
	border-bottom: 7px solid #eaeced;
	position: absolute;
	top: -8px;
	left: 50%;
	margin-left: -11px;
}

.primary-nav-ul li ul>li:first-child:after {
	content: "";
	width: 0;
	height: 0;
	border-left: 9px solid transparent;
	border-right: 9px solid transparent;
	border-bottom: 7px solid #ffffff;
	position: absolute;
	top: -7px;
	left: 50%;
	margin-left: -10px;
}

.primary-nav-ul li ul>li a  {
	display: block;
	padding: 10px 10px 10px 35px;
	text-transform: none;
	font-size: 14px;
	color: #7b7b7b;
	padding: 8px 10px 8px 35px;
	font-size: 14px !important;
}

 
.product_header {
	width: 100% !important;
}


.sticky .primary-nav-ul {
	margin-top: -10px;
}

.sticky .primary-nav-ul li ul {
	top: 60px;
}


#connect-menu>ul>li::before {
	left:55% !important;
}

#connect-menu>ul>li::after {
	left:55% !important;
}


#web-menu>ul>li::before {
	left:88% !important;
}

#web-menu>ul>li::after {
	left:88% !important;
}


/*developer body */
#content {
	background: #f5f5f5;
	//overflow: hidden;
	//position: relative;
	width: 100%;
	z-index: 99;
	border-top: 1px solid #e5e5e5;
	float: left;
}

.version-picker--mobile {
	display: none;
}

.version-picker--mobile {
	margin-top: .2em;
}

#version, .version-picker--mobile {
	float: right;
	font-size: .7rem;
	position: relative;
}

.nolist {
	list-style: none;
	margin: 0;
	padding: 0;
}

.version-picker--mobile li {
	font-size: 1.25em;
	line-height: 1.5;
}

#version li, .version-picker--mobile li {
	display: inline;
	font-size: inherit;
	margin-left: .25em;
}

.version-picker--mobile li a {
	background: #fff;
	box-shadow: 1px 2px 0 rgba(0,0,0,.05);
	opacity: .6;
	padding: 4px 8px;
}

#version li a, .version-picker--mobile li a {
	border-radius: 2px;
	font-weight: 600;
}

.version-picker--mobile li.current a {
	opacity: 1;
}

#documentation {
	overflow: hidden;
	position: relative;
}

#documentation.nav-expanded {
	overflow-y: auto;
}

#documentation .docs-show {
	background: #fff;
	box-shadow: 1px 2px 0 rgba(0,0,0,.05);
	display: block;
	font-size: .9rem;
	margin-bottom: .5em;
	margin-top: .9em;
	padding: .4em .6em;
	width: 5.25rem;
	text-align: center;
	text-transform: uppercase;
	-ms-touch-action: none;
	margin-left: 8px;
}

#documentation nav#docs {
	position: absolute;
	//float: left;
	-webkit-transform: translateX(-300px);
	transform: translateX(-300px);
	width: 275px;
	padding-left:18px;
	z-index: 10;
}

#documentation nav#docs #sampleTab, #documentation nav#docs #apiTab {
	display: none;
}

#documentation nav#docs .primary-nav-ul {
	padding-right: 0;
}

#documentation nav#docs .primary-nav-ul li {
	border: none;
}

#documentation .tab-background {
	background-color: lightgray;
	color: #fff;
}

#documentation #docs-content h3.sideline-heading {
	text-transform: none; 
	text-align: right;
}	

#documentation #docs-content h3.sideline-heading:before {
	content: '';
	height: 0;
}

#documentation #docs-content, #documentation nav#docs {
	box-sizing: border-box;
	-webkit-transition: -webkit-transform .5s ease;
	transition: transform .5s ease;
}

#documentation.nav-expanded nav#docs {
	-webkit-transform: translateX(0px);
	transform: translateX(0px);
}

#documentation nav#docs ul li {
	border-bottom: 1px solid #e9e9e9;
	color: #f4645f;
	font-weight: 600;
	font-size: 16px;
	margin-bottom: 5px;
	padding: 5px 0;
	-webkit-transition: 250ms linear all;
	transition: 250ms linear all;
	line-height: 1.5em;
}

#documentation nav#docs ul li ul {
	margin-bottom: 25px;
	list-style: circle;
	margin-left: 22px;
}

#documentation nav#docs ul li ul li {
	border-bottom: none;
	margin-bottom: 0;
	padding: 0;
}

#documentation nav#docs ul li ul li a {
	font-weight: 600;
	font-size: 14px;
}

#documentation #docs-content {
	background: #fffefe;
	box-sizing: border-box;
	padding: 20px;
	//position: absolute;
}

#documentation #docs-content h1 {
	font-size: 32px;
	margin-bottom: .4em;
	margin-top: 0;
}

#documentation #docs-content h1 {
	margin-bottom: .8em;
	margin-top: .5em;
}

#documentation #docs-content h1+ul {
	padding-top: 0;
}

#documentation #docs-content p {
	font-size: 15px;
	line-height: 1.4em;
	margin: 0 0 0.8em;
	color: #444;
}


#documentation #docs-content ul {
	list-style: none;
	margin: 0;
	padding: 15px 0 20px;
}

#documentation #docs-content ul li:before {
	color: #97c6da;
	content: "# ";
	padding-right: 5px;
	opacity: .4;
}

#documentation #docs-content ul li a {
	font-weight: 600;
}

#content p a {
	text-decoration: underline;
}

.sub-menu {
	padding: 20px !important;
}	

blockquote {
	color: white;
}

#documentation #docs-content h2 {
	border-top: 1px solid #eee;
	font-size: 1.65rem;
	margin-bottom: .4em;
	margin-top: .25em;
	padding-top: .75em;
}

#documentation #docs-content h2 {
	font-size: 1.8em;
	margin-bottom: .8em;
	margin-top: .625em;
	padding-top: 1.1em;
}

#documentation #docs-content h2 a {
	color: #666;
	width: 100%;
}

#documentation #docs-content h2 a:before {
	color: #97c6da;
	content: "# ";
	opacity: .4;
}

pre.prettyprint {
	background-color: #333;
	border: 1px solid #888;
}

pre {
	background: #F2F0EE;
	-webkit-border-radius: 11px;
	-moz-border-radius: 11px;
	border-radius: 11px;
	display: block;
	line-height: 100%;
	padding: 0 20px;
}

pre {
	margin: 1em 0px;
}	

.prettyprint code {
	font-family: Monaco,Consolas,"Lucida Console",monospace;
	font-size: 11px;
}

pre code {
	background: none;
	border: none;
	font-size: 11px;
	padding: 0px;
}

code {
	font-family: "DejaVu", "Monaco", "Courier New", "Courier";
	font-size: 90%;
	padding: 2px 4px;
	white-space: pre-wrap;
}

.pln {
	color: #fffefe!important;
}

.prettyprint .pun {
	color: #fff;
}

#documentation #docs-content pre {
	margin-left: inherit;
	margin-right: inherit;
}

#documentation #docs-content blockquote {
	background: #f4726d;
	border-radius: 2px;
	box-sizing: border-box;
	margin-bottom: 20px;
	max-width: 100%;
	padding: 18px 20px;
}

blockquote {
	position: relative;
	z-index: 5;
	margin: 0 0 15px;
	padding: 0;
}

#documentation #docs-content blockquote a, #documentation #docs-content blockquote p {
	color: #fff;
}

#documentation #docs-content blockquote p {
	margin: 0;
	padding: 0;
	font-size: 14px;
	line-height: 1.4em;
}

/* footer */
#global-footer {
	padding-top: 0;
	width: 100%;
	float: left;
	bottom: 0;
}

#global-footer .product_logo {
	height: 60px;
	width: 60px;
	float: left;
}

#global-footer .product_name span {
	font-size: 13px;
}

.navigation-panel {
	margin-right: 0px;
}

/*welcome page */

#developer-content {
	padding: 25px 0 25px 0;
	float: left;
	width: 100%;
	border-top: 1px solid #e5e5e5;
	background: -webkit-linear-gradient(#ecf0f1,#F9FAFA 80px);
	background: -moz-linear-gradient(#ecf0f1,#F9FAFA 80px);
	background: -ms-linear-gradient(#ecf0f1,#F9FAFA 80px);
	background: linear-gradient(#ecf0f1,#F9FAFA 80px);
}

#developer-content .content.with-gradient {
	border-left: 1px solid #ccc;
	padding: 0;
	background: none;
	//box-shadow: -5px 0 5px -5px rgba(0, 0, 0, .3);
}

.welcome-content {
	float: left;
	width: 50%;
}

#call-to-actions.build-content {
	float: right;
	width: 50%;
}

.action-button.featured {
	font-size: 15px !important;
	width: 135px !important;
}

#product-started-guide {
	float: left;
	padding-left: 60px;
	padding-right: 60px;
	overflow: hidden;
	margin: 2em auto;
}

#product-started-guide h3.sideline-heading {
	text-transform: none;
}

#product-started-guide .block-padding-half p {
	margin-top: 7px;
}

.top-gap {
	margin-top: 20px!important;
	width: 100%;
}

.product-download-box {
    width: 56%;
	padding: 20px;
	border: 1px solid #eee;
	border-radius: 26px;
	margin: 0 auto;
	background: #ecf0f1;
}

hr.subtle {
	margin: 1em 0;
}

.columns.bordered .cell {
	border-left: 1px solid #ccc;
}

.columns.bordered .cell.first {
	border-left: none;
}

.product-download-box .action-button.featured {
	width: 100% !important;
}

li>p {
	display: inline;
}
