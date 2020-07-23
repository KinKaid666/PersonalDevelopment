#ifndef __FCSMARTPOINTER_H__
#define __FCSMARTPOINTER_H__

template< class T >
class P
{
public:
    /*
    ** Default Contructor & Destructor
    */
    P() : ptr_(0) { ; }
    ~P() { Assign(0) ; } 

    /*
    ** Other Constructors
    */
    P(T *p) : ptr_(0) { Assign(p) ; }
    P( const P<T> &p ) : ptr_(0) { Assign(p.ptr_) ; }

    /*
    ** Assigning a 'new' pointer
    */
    void NEW( T *p ) { Assign(p) ; }

    /*
    ** Various operators to mimic a real pointer
    */
    T*    operator ->() { return  ptr_ ; }
    T&    operator  *() { return *ptr_ ; }
          operator T*() { return  ptr_ ; }

    P<T>& operator=( const P<T> &p )
    {
        Assign(p.ptr_) ;
        return *this ;
    }

protected:
    void Assign(T *p)
    {
        if( p )
            p->addReference() ;
        if( ptr_ && 0 == ptr_->removeReference() )
        {
            delete ptr_ ;
            ptr_ = NULL ;
        }
        ptr_ = p ;
    }

private:
    T *ptr_ ;

} ;

#endif /* __FCSMARTPOINTER_H__ */
