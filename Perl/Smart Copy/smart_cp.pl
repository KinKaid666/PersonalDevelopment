#! /usr/bin/perl

use strict ;
use Getopt::Long ;

my $DEFAULT_SIZE_BREAK = 100 ;
my $DEFAULT_SLEEP_TIME = 30 ;

my $size_break = $DEFAULT_SIZE_BREAK ;
my $sleep_time = $DEFAULT_SLEEP_TIME ;

GetOptions( 
    'help|usage|?' => \&usage,
    'size=s'       => \$size_break,
    'sleep=s'      => \$sleep_time
) || &usage() ;

sub usage
{
    print <<EOM ;
Usage: smart_cp [OPTION]... SOURCE DEST
  or:  smart_cp [OPTION]... SOURCE... DIRECTORY
  or:  smart_cp [OPTION]... --target-directory=DIRECTORY SOURCE...
Copy SOURCE to DEST, or multiple SOURCE(s) to DIRECTORY.

A wrapper around the classic command cp, but with the option of having
    the program sleep for a certain amount of time, every time a certain amount
    of space has been copied.

Options
--size		Amount (in Megabytes) of space to copy before sleeping 
		    (default: 100)
--sleep		Amount (in seconds) of time to sleep between copying
		    (default: 30)

Note: The size is just an estimate.  Size is check after each file, so if the
      limit is breached during a 1 gigabyte file, it will sleep after that file
      even if size is less than that.
EOM
}
