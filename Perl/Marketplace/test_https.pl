#!/usr/bin/perl

use HTTP::Headers;
use LWP::UserAgent;
use MIME::Base64 ;
use Data::Dumper ;
my $ua = new LWP::UserAgent;
my $head = new HTTP::Headers;
$head->authorization_basic(encode_base64('etf2954@rit.edu:1mitbdf1m'));
my $req = new HTTP::Request POST => 'https://secure.amazon.com/exec/panama/seller-admin/manual-reports/get-report-status', $head ;
$head->push_header('Content-type' => 'text/xml') ;

my $res = $ua->request($req) ;
print Dumper($res) ;
