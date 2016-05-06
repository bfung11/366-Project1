# 366-Project1
A full stack program that schedules doctors


## How to connect to the database

**In terminal:**
- ssh to <unix server>.csc.calpoly.edu
- type “psql -h cslvm74.csc.calpoly.edu -p 5432 -U <username>”

**In NetBeans:**
- download glassfish 
- go to “Services” window
- right-click “Servers” 
- click “Add server”
- accept all license agreements and click next until finished
- go back to project and click “Run”


If using the professor’s account:
- username: lubo
- password: secret

## TODO
1. Delete old weeks so that min(date) will get the starting date of the schedule
2. Add secondary technician shifts in the scheduler
~~3. Update calendar with schedule - assigned to Justin Zaman ~~
~~4. Push schedule to database - assigned to Brian Fung ~~
5. Adding query to view schedule - assigned to Kevin Yang
6. Add query to update request with preferred shifts, or time off
7. Add requestedDay to DB
8. Remove requestedDay from set of requests if not valid
9. Add toString() to Shift
