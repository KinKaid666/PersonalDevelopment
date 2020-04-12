// File:	$Id: reference.h,v 1.1 2002/08/01 15:40:19 etf2954 Exp $
// Author:	Eric Ferguson
// Description:	A reference class used for counted reference implementations

#ifndef __REFERENCE_H__
#define __REFERENCE_H__

class Reference
{
public:
    enum ReferenceFlag { STATIC_INIT };

    Reference( unsigned long initRef = 0 ) : refs_(initRef) { ; }
    Reference( ReferenceFlag ) { ; }

    unsigned long references() { return refs_ ; }

    void references( unsigned long refs ) { refs_ = refs ; }

    unsigned long    addReference() { return ++refs_ ; }
    unsigned long removeReference() { return --refs_ ; }

private:

    unsigned long refs_ ;

} ;

#endif /* __REFERENCE_H__ */

