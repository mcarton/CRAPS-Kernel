Software Development Plan
=========================

Introduction
------------

This project, suggested by Mr Hagimont, is based on the CRAPS processor developped by Mr Buisson and used in the first-year CPU architecture courses.
The goal would be to develop an operating system that would run on top of that processor. 

The reasons for that project are that before, it was only possible to do a little of assembly directly on the processor to see it work, but nothing more.
After our project, it should be possible for students to really see the layer that goes on top of the CPU in modern computers : the operating system. So that
the students can really make the link between the processor they just built and the computer and underlying operating systems they use everyday.


Directions concerning the solution
--------------------------------------------------------------------------------

Identities of the main stakeholders
--------------------------------------------------------------------------------

In-scope and out-of-scope items
--------------------------------------------------------------------------------


Project Overview
----------------

The objective of the project will be to create an operating system, with a scheduler running a few tasks. It will also provide functions
to display text to the user, do input/output to a permanent storage, ...

For now, having the OS loading programs dynamically is out-of-scope : the goal is to have a very simple functionnal os.
We will also have to improve the CPU to make it support our OS. Specifically we know we will have to support the RAM chips that are on the FPGA : currently,
we have 2 Ko of RAM that are built in, and that's clearly not enough.
Another important and time-consuming element will be to reuse the compiler we made last year during a project, and adapt it to generate the CRAPS assembly. We will surely
need to make other modifications to the compiler.

Development Approach and Logic
------------------------------

Agile methods ?

Method, Tools and Test Means
----------------------------

Test Means : it is quite a though point, as our only mean of real test is to put our code on the board and try. That's why we want to proceed iteratively and start with very basic things.

At the end, we want to be able to run a few processes that will communicate to the user, and store data in permanent memory.

Software Team Organisation and Responsabilities
-----------------------------------------------

Martin Carton : project leader
Korantin Auguste : 
Maxime Arthaud : 
Etienne Lebrun : 
Pierre-Louis Michel : 

Project Monitoring and Controls
-------------------------------

Gantt diagram, « hits » that we need to achieve.

Controls : meetings ?

Meetings and reporting
----------------------

See the « Réunions » folder.

Management of actions
---------------------

What is that ?

Management of risks
-------------------

Identify the risks :
-> Cause | Consequence | Probabilidad | Impact | Exposition | Threshold | Response plan

1) A FPGA can be damaged by us, making any test impossible. | 0.5 (likely) * 0.75 (really damageable) | 0.375 | Have more than one FPGA, and have the possibility to have more.
2) We may not be able to integrate the RAM, leading to huge memory limitations that may make the project impossible | 0.2 * 0.9 | 0.18 | Put as much ram as we can in the FPGA, in VHDL (but we will have a lot less anyway)

Management of Change Request
----------------------------

Agile methods ?

Quality and Configuration management
------------------------------------

Use of a version control system : a Git repository on Github.

Documentation
-------------

 - User Manual
 - modified CRAPS
