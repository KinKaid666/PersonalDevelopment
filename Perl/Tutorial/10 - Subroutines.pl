#! /usr/bin/perl

sub mysubroutine
{
	print "Not a very interesting routine\n";
	print "This does the same thing every time\n";
}

&mysubroutine;		# Call the subroutine
&mysubroutine($_);	# Call it with a parameter
&mysubroutine(1+2, $_);	# Call it with two parameters

sub printargs
{
	print "@_\n";
}

&printargs("perly", "king");	# Example prints "perly king"
&printargs("frog", "and", "toad"); # Prints "frog and toad"

sub maximum
{
	if ($_[0] > $_[1])
	{
		$_[0];
	}
	else
	{
		$_[1];
	}
}

$biggest = &maximum(37, 24);	# Now $biggest is 37

sub inside
{
	local($a, $b);			# Make local variables
	($a, $b) = ($_[0], $_[1]);	# Assign values
	$a =~ s/ //g;			# Strip spaces from
	$b =~ s/ //g;			#   local variables
	($a =~ /$b/ || $b =~ /$a/);	# Is $b inside $a
					#   or $a inside $b?
}

&inside("lemon", "dole money");		# true

# In fact, it can even be tidied up by replacing the first two lines with 

local($a, $b) = ($_[0], $_[1]);

