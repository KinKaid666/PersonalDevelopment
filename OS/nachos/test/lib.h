#ifndef LIB_H__
#define LIB_H__

int N_strlen(char *string)
{
  int i;

  for(i = 0; string[i] != '\0'; i++)
    ;
  
  return i; 
}

#endif
