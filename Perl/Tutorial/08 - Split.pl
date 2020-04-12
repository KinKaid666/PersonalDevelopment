#! /usr/bin/perl
use POSIX ;

die "usage: <filename> <concordance>\n" if( $#ARGV != 1 );

$file   = $ARGV[0] ;
$target = $ARGV[1] ;

open( FILE, $file ) or die "$file: $!" ;
@lines = <FILE> ;
close( FILE ) ;

chomp( @lines ) ;

$" = '' ;
$text = "@lines" ;

$text =~ s/\t/        /g ;                      # Changed tabs to spaces for
                                                # spacing issues
@split = split(/$target/, $text ) ;

$justification = 0 ;
$index_str_len = 0 ;
for( $x = 0 ; $x <= $#split - 1 ; $x++ )
{
    $index_str_len = length($split[$x]) ;
    $justification = $index_str_len if( $index_str_len > $justification ) ;
}

$justification = ceil($justification / 2) ;
$justification = length( $split[0] ) if( length( $split[0] ) > $justification ) ;

$left_index  = length( $split[0] ) ;
$right_index = 0 ;
for( $x = 0 ; $x <= $#split - 1 ; $x++ )
{
    $padding = " " x ($justification - $left_index) ;
    if( $x == $#split - 1 )
    {
        $right_index = length( $split[$x+1] ) ;
    }
    else
    {
        $right_index = floor( length( $split[$x+1] ) / 2 ) ;
    }
    print $padding,
          substr( $split[$x], 0 - $left_index ),
          $target,
          substr( $split[$x + 1], 0, $right_index ),
          "\n" ;
    $left_index = length( $split[$x+1] ) - $right_index ;
}
