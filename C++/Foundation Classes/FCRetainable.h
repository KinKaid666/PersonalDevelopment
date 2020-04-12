#ifndef __FCRETAINABLE_H__
#define __FCRETAINABLE_H__

class FCRetainable
{
public:
    FCRetainable() : ptrs_(0) { ; }
    void addReference() { ++ptrs_ ; }
    unsigned int removeReference() { return --ptrs_ ; }
private:
    unsigned int ptrs_ ;
} ;

#endif /* __FCRETAINABLE_H__ */
