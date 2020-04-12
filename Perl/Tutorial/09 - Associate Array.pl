#! /usr/bin/perl

%ages = ("Michael Caine", 39,
         "Dirty Den", 34,
         "Angie", 27,
         "Willy", "21 in dog years",
         "The Queen Mother", 108);

foreach $person (keys %ages)
{
	print "I know the age of $person\n";
}
foreach $age (values %ages)
{
	print "Somebody is $age\n";
}

while (($person, $age) = each(%ages))
{
	print "$person is $age\n";
}

print "You are called $ENV{'USER'} and you are ";
print "using display $ENV{'DISPLAY'}\n";

