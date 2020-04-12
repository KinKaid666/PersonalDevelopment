//
// Name:        shape (base class)
// Desc:        This is an abstract class (contains pure virtual functions)
//              and sets up 2 required functions for any class that inherits it.
//
class shape
{
public:
    //
    // Pure virtual function.  Insures that there is no instances of this class
    // are created. 
    //
    virtual void draw()     = 0 ;
    virtual void identify() = 0 ;
} ;

class diamond
  : public shape
{
public:
    //
    // These function are kept virtual incase someone inherits them
    // and wants to overwrite these functions.
    //
    virtual void identify() { std::cout << "I am a diamond." << std::endl ; }
    virtual void draw()
    {
        std::cout << "   *   " << std::endl
                  << "  * *  " << std::endl
                  << " *   * " << std::endl
                  << "  * *  " << std::endl
                  << "   *   " << std::endl ;
    }
} ;

class square
  : public shape
{
public:
    virtual void identify() { std::cout << "I am a square." << std::endl ; }
    virtual void draw()
    {
        std::cout << " ****** " << std::endl
                  << " *    * " << std::endl
                  << " *    * " << std::endl
                  << " *    * " << std::endl
                  << " ****** " << std::endl ;

    }
} ;

class rectangle
  : public shape
{
public:
    virtual void identify() { std::cout << "I am a rectangle." << std::endl ; }
    virtual void draw()
    {
        std::cout << " *********** " << std::endl
                  << " *         * " << std::endl
                  << " *         * " << std::endl
                  << " *         * " << std::endl
                  << " *********** " << std::endl ;

    }
} ;
