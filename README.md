This is a simple demonstration project that incorporates several core technologies: HTML5, CSS, jQuery, WebSockets, Atmosphere/Comet, Scala, Scalagtra, Jade, MySQL, Slick, MongoDB and Casbah.

The main page of the application shows groupings of meters with values that can vary from 0 to 100. The user can choose to increase, decrease, or hold the value of a meter. The meter value will change according to the setting.

The application requires MySQL and MongoDB instances to be running. Initialization scripts for these data stores can be found in the mysql and mongodb folders.

Use sbt to build and run. Browse to localhost:8080.