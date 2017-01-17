Introduction to Reo
===================

Why do you need Reo
-------------------

Suppose we ask you to design a small program that consists of tree components: two producers that output a string "Hello, " and "world!", respectively, and a consumer that **alternately** 
prints the produced strings "Hello, " and "world!", starting with "Hello, ". 

.. code-block:: text
	
	while(true) {
	sleep(4000);
	