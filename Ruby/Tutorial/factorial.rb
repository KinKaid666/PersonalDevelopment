#! /usr/bin/ruby -w

def factorial(n)
    return (n == 1 ? 1 : n * factorial(n - 1))
end

if( ARGV.length != 1 )
    STDERR.puts "usage: factorial <number>"
else
    puts factorial(ARGV[0].to_i)
end

