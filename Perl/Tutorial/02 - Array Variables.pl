#! /usr/bin/perl

@food  = ("apples", "pears", "eels") ;
@music = ("whistle", "flute") ;

print @food;    # By itself
print "\n" ;
print "@food";  # Embedded in double quotes
print "\n" ;
print @food.""; # In a scalar context
print "\n" ;
