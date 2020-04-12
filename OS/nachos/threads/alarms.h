//***************************************************************************
// alarms.h
//  Class definitions for managing alarms set by threads.
//
//  The alarms are maintained by an alarm manager.  This manager creates a
//  timer that calls the AlarmInterrupHandler.  In this handler a count of
//  timer ticks is maintained from when the manager was started.  This count
//  is used to determin if a pending alarm time has been reached.  The
//  interrupt handler will put any thread whose alarm has expired onto the
//  ready to run list.
//
//  A thread requests that an alarm be set using the Thread::GoToSleepFor()
//  function.
//***************************************************************************

// $Id: alarms.h,v 3.0 2001/11/04 19:46:54 trc2876 Exp $

// $Log: alarms.h,v $
// Revision 3.0  2001/11/04 19:46:54  trc2876
// force to 3.0
//
// Revision 2.0  2001/10/11 02:53:22  trc2876
// force update to 2.0 tree
//
// Revision 1.1  2001/09/23 20:19:12  etf2954
// Initial revision
//

#ifndef ALARM_H
#define ALARM_H

#include "list.h"
#include "timer.h"
#include "thread.h"

#define AlarmTicks	100

// The following class defines the alarm clock.
class AlarmClock
{
    List* AlarmList;                     /* sorted list of pending alarms */
    int CurrentTick;                         /* time since manager started */
    
    // Process the alarm interrupts here.  It's static so the you can
    // access it via a simple pointer to a function
    static void AlarmInterruptHandler(int arg);

public:
    AlarmClock();
    ~AlarmClock();

    int TimeNow() {return CurrentTick;}      /* return current time */
                                             /* increment time and return it */
    int IncrementTick() {return CurrentTick += AlarmTicks;}
    
    void SetAlarm(int howLong,Thread* Requester); // set an alarm
    void CheckAlarms();                     // handle expired alarms
};


// this is the information maintained in the alarm manager's sorted pending
//  alarm list for a single alarm
class Alarm
{
    int AlarmTime;                           /* time for the alarm */

public:
    Alarm(Thread* RequestingThread, int WakeUpTick);

    Thread* SleepingThread;                  /* thread that is asleep */
};

#endif
