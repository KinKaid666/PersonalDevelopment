#! /usr/bin/perl

$file = '/etc/passwd' ;
open( INFO, $file ) ;
@lines = <INFO> ;
$" = '# ' ;                     # tells what to print as the delimiter between
                                # array values
close( INFO ) ;
print "@lines" ;
