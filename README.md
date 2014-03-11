Miru
=============

This is my Final Year Project, developed during my final year at Teesside University.

The original aim of the project was to provide a means of locating and discovering various assets on a process industry plant. This was done through Augmented Reality on an Android device.

The Application
-------

Currently, the project is very much in alpha stage, having mainly been built to satisfy my thesis. 

The application comes in two main parts:

* 2D Aerial Map
* Detail View
* Augmented Reality

### 2D Aerial Map

The map is provided using the GoogleMap API's, and hooks into the main data processing part of the application. 

### Detail View

Using the Master/Detail template provided by Eclipse, asset details can be viewed. This activity is accessed via the  main map activity.

### AR

A late addition (and still teething) is the augmented reality activity. Currently assets are drawn onto a canvas, which has yet to be overlayed above the custom camera class.


Bugs
------------

Again, the application is very much in alpha, and many cases not fully complete. For the most part, the AR part of the application requires some serious TLC.

I will not list bugs at this point, as the application is very much still being built.



Future
-----------

Miru has been built to satisfy my Uni thesis, but I would very much like to continue work. Firstly I very much want a data connection implemented. Secondly my intention is to have at least a stable system, and iron out the critical issues in the system.

The long term plan is to thoroughly rip out the data part of the system, and convert Miru into a generalised application. The concept will  mean that any client be more than able to create there own assets and import them into the system.


Blog
------------

Feel free to keep up with progress on Miru on [my site](http://ste-pam.com.blog/Topic/2).
