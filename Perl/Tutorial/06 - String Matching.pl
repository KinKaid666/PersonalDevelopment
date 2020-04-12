#! /usr/bin/perl

$file = 'electricity.txt' ;
open( FILE, $file ) ;
$a = 0 ;
while( <FILE> )
{
    if( /x/ || /the/ || /[Tt]he/ || /\b[Tt]he\b/ ) 
    {
	$a++ ;
	printf "%03d %s", $a, $_ ;
    }
    else
    {
	printf "    %s", $_ ;
    }
}
close( FILE ) ;
