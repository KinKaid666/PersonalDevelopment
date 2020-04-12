#! /usr/bin/perl

# @food = ("apples", "oranges", "pears" ) ;
# foreach $morsel (@food)               # Visit each item in turn
                                # # and call it $morsel
# {
        # print "$morsel\n";    # Print the item
        # print "Yum yum\n";    # That was nice
# }
# 
# for ($i = 0; $i < 10; ++$i)   # Start with $i = 1
                                # # Do it while $i < 10
                                # # Increment $i before repeating
# {
        # print "$i\n";
# }
# 
# 
# print "Password? ";           # Ask for input
# $a = <STDIN>;                 # Get input
# chop $a;                      # Remove the newline at end
# while ($a ne "fred")          # While input is wrong...
# {
    # print "sorry. Again? ";   # Ask again
    # $a = <STDIN>;             # Get input again
    # chop $a;                  # Chop off newline again
# }
# 
# # same ...
# do
# {
        # "Password? ";         # Ask for input
        # $a = <STDIN>;         # Get input
        # chop $a;              # Chop off newline
# }
# while ($a ne "fred")          # Redo while wrong input

# Exercise

$file = '/etc/passwd' ;
open( INFO, $file ) ;
while( $lines = <INFO> ) 
{
    printf "%03d $lines", $. ;
}
close( INFO ) ;
