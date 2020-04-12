//***************************************************************************
// alarms.cc
//  Routines to manage alarms.
//
//***************************************************************************

// $Id: alarms.cc,v 3.0 2001/11/04 19:46:54 trc2876 Exp $

// $Log: alarms.cc,v $
// Revision 3.0  2001/11/04 19:46:54  trc2876
// force to 3.0
//
// Revision 2.0  2001/10/11 02:53:22  trc2876
// force update to 2.0 tree
//
// Revision 1.1  2001/09/23 20:19:01  etf2954
// Initial revision
//

#include "system.h"
#include "alarms.h"

//***************************************************************************
//  AlarmClock::AlarmInterruptHandler
//
//  Called by the timer started in the alarm manager.  It is called every
//  AlarmTicks tick intervals.  The function maintains the alarm tick count
//  and then checks for expired alarms.
//
//***************************************************************************
void AlarmClock::AlarmInterruptHandler(int arg)
{
    // schedule the next alarm interrupt
    interrupt->Schedule(AlarmInterruptHandler,arg,AlarmTicks,AlarmInt); 

    AlarmClock* theAlarmClock = (AlarmClock*) arg;
    (void) theAlarmClock->IncrementTick();   // increment the tick count
    
    // check for any alarm that has expired
    theAlarmClock->CheckAlarms();
}


//***************************************************************************
//  AlarmClock::AlarmClock
//
//  The constructor creates and empty alarm list and initializes the tick
//  counter to 0.  The timer used to call the interrupt handler is also
//  constructed.
//
//***************************************************************************
AlarmClock::AlarmClock()
{
    AlarmList = new List;                    // initialize an empty alarm list
    CurrentTick = 0;                                 // start counting ticks
    
    // schedule the first interrupt from the timer device
    interrupt->Schedule(AlarmInterruptHandler,(int) this,AlarmTicks,
		AlarmInt); 
}


//***************************************************************************
//  AlarmClock::~AlarmClock
//
//  Only need to destroy the timer and list of pending alarms.  This should
//  only be done when the system is coming down.  Any thread on the alarm
//  list will never be started and the memory for those alarm entries is not
//  reclaimed.
//
//***************************************************************************
AlarmClock::~AlarmClock()
{
    delete AlarmList;            // waiting processes will be blocked forever
}


//***************************************************************************
// AlarmClock::SetAlarm
//
//  Called from the thread requesting to go to sleep.  A new alarm is
//  entered into the alarm manager's sorted alarm list.  The requesting
//  thread is then put to sleep.  It will be moved back to the ready to
//  run list in the alarm interrupt handler when the alarm has expired.
//
//***************************************************************************
void AlarmClock::SetAlarm(int howLong,Thread* Requester)
{
    if(howLong > (AlarmTicks/2))
    {            // more than half a timer interval so set up for the alarm
        // protect these operations
        IntStatus OldLevel = interrupt->SetLevel(IntOff);
        
        // time for alarm is current time + period requested
        Alarm* NewAlarm = new Alarm(Requester,CurrentTick + howLong);
        AlarmList->SortedInsert(NewAlarm,CurrentTick + howLong);

        // go to sleep till the alarm hits
        currentThread->Sleep();

        // end of critical section
        (void) interrupt->SetLevel(OldLevel);
    }
}


//***************************************************************************
// AlarmClock::CheckAlarms
//
//  This function will remove from the front of the pending alarm list any
//  alarm whose time has expired.  The thread that set the alarm is moved
//  to the ready to run list.
//
//***************************************************************************
void AlarmClock::CheckAlarms()
{
    int NextAlarm;
    
    while(AlarmList->PeekSorted(&NextAlarm) && (NextAlarm <= CurrentTick))
    {
        int key;

        // remove the alarm from the alarm list
        Alarm* ExpiredAlarm = (Alarm*) AlarmList->SortedRemove(&key);
        DEBUG('A',"Putting %s ready to run at %d ticks\n",
              ExpiredAlarm->SleepingThread->getName(),CurrentTick);
        
        // the thread is now ready to run
        scheduler->ReadyToRun(ExpiredAlarm->SleepingThread);

        // toss the alarm
        delete ExpiredAlarm;
    }
}




//***************************************************************************
//  Alarm::Alarm
//
//  Construct a new alarm to be put on the pending alarm list.
//
//***************************************************************************
Alarm::Alarm(Thread* RequestingThread,int WakeUpTick)
{
    SleepingThread = RequestingThread;
    AlarmTime = WakeUpTick;
}
