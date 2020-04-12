#! /usr/bin/perl -w

use warnings ;
use strict ;
use DB_File ;
our (%h, $k, $v) ;

tie %h, "DB_File", "fruit", O_RDONLY, 0666, $DB_HASH
    or die "Cannot open file 'fruit': $!\n";

# print the contents of the file
while (($k, $v) = each %h)
  { print "$k -> $v\n" }

untie %h ;
