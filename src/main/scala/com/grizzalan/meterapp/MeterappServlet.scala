package com.grizzalan.meterapp

import org.scalatra._
import scalate.ScalateSupport

class MeterappServlet extends MeterappscalaStack {

	get("/") {
		contentType="text/html"
		layoutTemplate("/WEB-INF/templates/views/index.jade")
	}
	
	get("/admin") {
		contentType="text/html"
		layoutTemplate("/WEB-INF/templates/views/admin.jade")
	}
	
	get("/meters") {
		contentType="text/html"
		layoutTemplate("/WEB-INF/templates/views/meters.jade")
	}
	
}
