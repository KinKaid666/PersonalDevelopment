#include <cstdlib>
#include <iomanip>
#include <iostream>
#include <vector>

using namespace std ;
void __sumset( vector<int> &a,
               int total,
               int index,
               int sumSoFar,
               vector<int> &setSoFar,
               vector< vector<int> > &setsSoFar )
{
    for( ; index < a.size() ; ++index )
    {
        setSoFar.push_back(a[index]) ;
        sumSoFar += a[index] ;
        total    -= a[index] ;

        /* Found one, add it */
        if( total == 0 )
        {
            setsSoFar.push_back( setSoFar ) ;
        }
        else
        {
            __sumset( a, total, index + 1, sumSoFar, setSoFar, setsSoFar ) ;
        }

        /* Pretend like it never happened and keep going */
        sumSoFar -= a[index] ;
        total    += a[index] ;
        setSoFar.pop_back() ;
    }
}

vector< vector<int> > sumset( vector<int> &a, int x )
{
    vector< vector<int> > sets ;
    vector<int> temp ;
    __sumset( a, x, 0, 0, temp, sets ) ;
    return sets ;
}

int main( int ac, char **av )
{
    if( ac < 3 )
    {
        cerr << "usage: " << av[0] << " <n> <val1> [<val2> [<val3>...] ] " 
             << endl ;
        exit(EXIT_FAILURE) ;
    }

    vector<int> a ;
    for( int i = 2 ; i < ac ; ++i )
    {
        a.push_back( atoi(av[i]) ) ;
    }

    vector< vector<int> > res = sumset( a, atoi(av[1]) ) ;

    for( int i = 0 ; i < res.size() ; ++i )
    {
        clog << setw(3) << i+1 << " = { " ;
        for( int j = 0 ; j < res[i].size() ; ++j )
        {
            if( j != 0 )
                clog << ", " ;
            clog << res[i][j] ;
        }
        clog << " }" << endl ;
    }
    return EXIT_SUCCESS ;
}
