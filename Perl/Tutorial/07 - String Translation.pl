#! /usr/bin/perl

$file = 'electricity.txt' ;
open( FILE, $file ) ;
$a = 0 ;
while( <FILE> )
{
    if( /([a-zA-Z])\1/ )
    {
	s/([a-zA-Z])\1/(\1\1)/g ;
	$a++ ;
	printf "%03d %s", $a, $_ ;
    }
    else
    {
	printf "    %s", $_ ;
    }
}
close( FILE ) ;
