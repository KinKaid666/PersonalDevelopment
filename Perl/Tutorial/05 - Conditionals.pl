#! /usr/bin/perl

$file = 'electricity.txt' ;
open( FILE, $file ) ;
$a = 0 ;
while( <FILE> )
{
    print ;
    chomp ;
    $a++ if $_ ;

}
printf "#\n# of non-empty lines were %d\n#\n", $a  ;
close( FILE ) ;
